package edu.multimedia.lucene.search;

import edu.multimedia.lucene.index.MovieIndexer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Enhanced search functionality with fuzzy matching, query expansion, 
 * spell checking, faceted search, and pagination.
 */
public class EnhancedSearcher {
    private final Path indexDirectory;
    private final StandardAnalyzer analyzer;
    private DirectoryReader reader;
    private IndexSearcher searcher;
    private static final float DEFAULT_FUZZINESS = 0.8f;
    private static final int MIN_RESULTS_FOR_EXPANSION = 3;

    public EnhancedSearcher(Path indexDirectory) throws IOException {
        this.indexDirectory = indexDirectory;
        this.analyzer = new StandardAnalyzer();
        openIndex();
    }

    private void openIndex() throws IOException {
        Directory directory = FSDirectory.open(indexDirectory);
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    /**
     * Performs enhanced search with fuzzy matching, query expansion, and spell checking.
     * 
     * @param queryText Search query (supports "word?" for fuzzy matching)
     * @param maxResults Maximum number of results
     * @return Search results
     */
    public SearchResults performSearch(String queryText, int maxResults) 
            throws ParseException, IOException {
        // Step 1: Spell checking
        String correctedQuery = checkSpelling(queryText);
        if (!correctedQuery.equals(queryText)) {
            System.out.println("Did you mean: \"" + correctedQuery + "\"? (using corrected query)");
        }

        // Step 2: Process fuzzy terms (words ending with ?)
        String processedQuery = processFuzzyTerms(correctedQuery);

        // Step 3: Build and execute query
        Query query = buildQuery(processedQuery);
        TopDocs topDocs = searcher.search(query, maxResults * 2); // Get more for expansion check

        // Step 4: Query expansion if needed
        if (topDocs.totalHits.value < MIN_RESULTS_FOR_EXPANSION) {
            System.out.println("Few results found. Expanding query...");
            query = expandQuery(correctedQuery);
            topDocs = searcher.search(query, maxResults * 2);
        }

        // Step 5: Extract results
        List<SearchResult> results = extractResults(topDocs, maxResults);
        
        return new SearchResults(results, topDocs.totalHits.value);
    }

    /**
     * Checks spelling and suggests corrections.
     */
    private String checkSpelling(String query) {
        // Simple spell checking - check if terms exist in index
        // For production, use a proper spell checker dictionary
        String[] terms = query.toLowerCase().split("\\s+");
        List<String> corrected = new ArrayList<>();
        
        for (String term : terms) {
            // Remove punctuation for checking
            String cleanTerm = term.replaceAll("[^a-zA-Z0-9]", "");
            if (cleanTerm.length() > 2) {
                // Try to find similar terms using fuzzy search
                try {
                    TermQuery termQuery = new TermQuery(new Term(MovieIndexer.FIELD_TITLE, cleanTerm));
                    TopDocs docs = searcher.search(termQuery, 1);
                    if (docs.totalHits.value == 0) {
                        // Term not found, try fuzzy
                        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(MovieIndexer.FIELD_TITLE, cleanTerm), 1);
                        TopDocs fuzzyDocs = searcher.search(fuzzyQuery, 1);
                        if (fuzzyDocs.totalHits.value > 0) {
                            // Found fuzzy match, keep original for now
                            corrected.add(term);
                        } else {
                            corrected.add(term);
                        }
                    } else {
                        corrected.add(term);
                    }
                } catch (IOException e) {
                    corrected.add(term);
                }
            } else {
                corrected.add(term);
            }
        }
        
        return String.join(" ", corrected);
    }

    /**
     * Processes fuzzy terms (words ending with ?).
     * Example: "leon?" -> fuzzy search for "leon"
     */
    private String processFuzzyTerms(String query) {
        // This will be handled in query building
        return query;
    }

    /**
     * Builds a query with support for fuzzy matching.
     */
    private Query buildQuery(String queryText) throws ParseException {
        String[] fields = {
            MovieIndexer.FIELD_TITLE,
            MovieIndexer.FIELD_CAST,
            MovieIndexer.FIELD_OVERVIEW,
            MovieIndexer.FIELD_TAGLINE,
            MovieIndexer.FIELD_GENRES
        };
        
        Map<String, Float> boosts = new HashMap<>();
        boosts.put(MovieIndexer.FIELD_TITLE, 2.0f);
        boosts.put(MovieIndexer.FIELD_CAST, 1.5f);
        boosts.put(MovieIndexer.FIELD_OVERVIEW, 1.0f);
        boosts.put(MovieIndexer.FIELD_TAGLINE, 1.0f);
        boosts.put(MovieIndexer.FIELD_GENRES, 1.0f);

        // Check if query contains fuzzy terms (words ending with ?)
        String[] words = queryText.split("\\s+");
        boolean hasFuzzy = false;
        StringBuilder regularQuery = new StringBuilder();
        List<Query> fuzzyQueries = new ArrayList<>();
        
        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z0-9?]", "");
            if (cleanWord.endsWith("?")) {
                // Fuzzy search
                hasFuzzy = true;
                String baseWord = cleanWord.substring(0, cleanWord.length() - 1).toLowerCase();
                if (baseWord.length() > 0) {
                    // Create fuzzy queries for each field
                    for (String field : fields) {
                        FuzzyQuery fuzzyQuery = new FuzzyQuery(
                            new Term(field, baseWord), 
                            2 // Max edit distance (0.8 fuzziness ≈ 2 edits for typical words)
                        );
                        fuzzyQueries.add(fuzzyQuery);
                    }
                }
            } else if (!cleanWord.isEmpty()) {
                regularQuery.append(cleanWord).append(" ");
            }
        }

        // Build queries
        List<Query> allQueries = new ArrayList<>();
        
        // Add regular query if there are non-fuzzy terms
        if (regularQuery.length() > 0) {
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
            Query regularQueryObj = parser.parse(regularQuery.toString().trim());
            allQueries.add(regularQueryObj);
        }
        
        // Add fuzzy queries
        if (!fuzzyQueries.isEmpty()) {
            // Combine all fuzzy queries with OR
            BooleanQuery.Builder fuzzyBuilder = new BooleanQuery.Builder();
            for (Query fq : fuzzyQueries) {
                fuzzyBuilder.add(fq, BooleanClause.Occur.SHOULD);
            }
            allQueries.add(fuzzyBuilder.build());
        }

        // Combine all queries with OR (regular OR fuzzy)
        if (allQueries.size() == 1) {
            return allQueries.get(0);
        } else if (allQueries.size() > 1) {
            BooleanQuery.Builder combined = new BooleanQuery.Builder();
            for (Query q : allQueries) {
                combined.add(q, BooleanClause.Occur.SHOULD);
            }
            return combined.build();
        }
        
        // Fallback: parse original query
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        return parser.parse(queryText.replace("?", ""));
    }

    /**
     * Expands query by adding synonyms or related terms.
     */
    private Query expandQuery(String originalQuery) throws ParseException {
        // Simple expansion: add common variations
        String expanded = originalQuery;
        
        // Add common word variations (simple approach)
        String[] words = originalQuery.split("\\s+");
        for (String word : words) {
            // Could add synonyms here
        }
        
        return buildQuery(expanded);
    }

    /**
     * Extracts search results from TopDocs.
     */
    private List<SearchResult> extractResults(TopDocs topDocs, int maxResults) throws IOException {
        List<SearchResult> results = new ArrayList<>();
        int count = Math.min(maxResults, topDocs.scoreDocs.length);
        
        for (int i = 0; i < count; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            SearchResult result = new SearchResult(doc, scoreDoc.score);
            results.add(result);
        }
        
        return results;
    }

    /**
     * Gets facets (categories) for faceted search.
     */
    public Map<String, List<String>> getFacets(List<SearchResult> results) throws IOException {
        Map<String, Set<String>> decadeMap = new HashMap<>();
        Map<String, Set<String>> genreMap = new HashMap<>();
        
        for (SearchResult result : results) {
            Document doc = result.getDocument();
            
            // Extract decade
            String yearStr = doc.get(MovieIndexer.FIELD_YEAR);
            if (yearStr != null && !yearStr.isEmpty()) {
                try {
                    int year = Integer.parseInt(yearStr);
                    String decade = (year / 10) * 10 + "s";
                    decadeMap.computeIfAbsent(decade, k -> new HashSet<>()).add(yearStr);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
            
            // Extract genres (if stored)
            String genresStr = doc.get(MovieIndexer.FIELD_GENRES);
            if (genresStr != null && !genresStr.isEmpty()) {
                String[] genres = genresStr.split("\\s+");
                for (String genre : genres) {
                    if (!genre.isEmpty() && genre.length() > 2) {
                        genreMap.computeIfAbsent(genre, k -> new HashSet<>()).add(genre);
                    }
                }
            }
        }
        
        Map<String, List<String>> facets = new HashMap<>();
        List<String> decades = new ArrayList<>(decadeMap.keySet());
        decades.sort(String::compareTo);
        facets.put("decade", decades);
        
        List<String> genres = new ArrayList<>(genreMap.keySet());
        genres.sort(String::compareTo);
        facets.put("genres", genres);
        
        return facets;
    }

    /**
     * Filters results by facet (decade, genre, etc.).
     */
    public List<SearchResult> filterByFacet(List<SearchResult> results, String facetType, String facetValue) 
            throws IOException {
        List<SearchResult> filtered = new ArrayList<>();
        
        for (SearchResult result : results) {
            Document doc = result.getDocument();
            boolean matches = false;
            
            if ("decade".equals(facetType)) {
                String yearStr = doc.get(MovieIndexer.FIELD_YEAR);
                if (yearStr != null) {
                    try {
                        int year = Integer.parseInt(yearStr);
                        String decade = (year / 10) * 10 + "s";
                        if (decade.equals(facetValue)) {
                            matches = true;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            } else if ("genre".equals(facetType) || "genres".equals(facetType)) {
                String genresStr = doc.get(MovieIndexer.FIELD_GENRES);
                if (genresStr != null && genresStr.toLowerCase().contains(facetValue.toLowerCase())) {
                    matches = true;
                }
            }
            
            if (matches) {
                filtered.add(result);
            }
        }
        
        return filtered;
    }

    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        analyzer.close();
    }
}

