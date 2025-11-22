package edu.multimedia.lucene.search;

import edu.multimedia.lucene.index.MovieIndexer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides search functionality over the Lucene index.
 * Supports basic keyword search and enhanced search with filters.
 */
public class MovieSearcher {
    private final Path indexDirectory;
    private final StandardAnalyzer analyzer;
    private DirectoryReader reader;
    private IndexSearcher searcher;

    public MovieSearcher(Path indexDirectory) throws IOException {
        this.indexDirectory = indexDirectory;
        this.analyzer = new StandardAnalyzer();
        openIndex();
    }

    /**
     * Opens the index for searching.
     */
    private void openIndex() throws IOException {
        Directory directory = FSDirectory.open(indexDirectory);
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    /**
     * Performs a basic keyword search across title, overview, and cast fields.
     * 
     * @param queryText Search query
     * @param maxResults Maximum number of results to return
     * @return List of search results
     * @throws ParseException If query parsing fails
     * @throws IOException If search fails
     */
    public List<SearchResult> basicSearch(String queryText, int maxResults) 
            throws ParseException, IOException {
        // Check searcher state
        if (searcher == null || reader == null) {
            openIndex();
        }
        
        // Search in multiple fields with different boosts
        String[] fields = {
            MovieIndexer.FIELD_TITLE,
            MovieIndexer.FIELD_CAST,
            MovieIndexer.FIELD_OVERVIEW,
            MovieIndexer.FIELD_TAGLINE,
            MovieIndexer.FIELD_GENRES
        };
        
        Map<String, Float> boosts = new HashMap<>();
        boosts.put(MovieIndexer.FIELD_TITLE, 2.0f);      // Title matches are most important
        boosts.put(MovieIndexer.FIELD_CAST, 1.5f);       // Cast matches are important
        boosts.put(MovieIndexer.FIELD_OVERVIEW, 1.0f);
        boosts.put(MovieIndexer.FIELD_TAGLINE, 1.0f);
        boosts.put(MovieIndexer.FIELD_GENRES, 1.0f);
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query query = parser.parse(queryText);
        
        return executeSearch(query, null, maxResults);
    }

    /**
     * Performs an enhanced search with filters for rating, year, etc.
     * 
     * @param queryText Search query
     * @param minRating Minimum rating (null = no filter)
     * @param maxRating Maximum rating (null = no filter)
     * @param minYear Minimum year (null = no filter)
     * @param maxYear Maximum year (null = no filter)
     * @param maxResults Maximum number of results to return
     * @return List of search results
     * @throws ParseException If query parsing fails
     * @throws IOException If search fails
     */
    public List<SearchResult> enhancedSearch(String queryText, 
                                           Double minRating, Double maxRating,
                                           Integer minYear, Integer maxYear,
                                           int maxResults) 
            throws ParseException, IOException {
        // Check searcher state
        if (searcher == null || reader == null) {
            openIndex();
        }
        
        // Build text query
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
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        Query textQuery = parser.parse(queryText);
        
        // Build filter query
        List<Query> filterQueries = new ArrayList<>();
        
        if (minRating != null || maxRating != null) {
            double min = minRating != null ? minRating : 0.0;
            double max = maxRating != null ? maxRating : 10.0;
            Query ratingQuery = DoublePoint.newRangeQuery(
                MovieIndexer.FIELD_RATING, min, max);
            filterQueries.add(ratingQuery);
        }
        
        if (minYear != null || maxYear != null) {
            int min = minYear != null ? minYear : 1900;
            int max = maxYear != null ? maxYear : 2100;
            Query yearQuery = IntPoint.newRangeQuery(
                MovieIndexer.FIELD_YEAR, min, max);
            filterQueries.add(yearQuery);
        }
        
        Query filterQuery = null;
        if (!filterQueries.isEmpty()) {
            if (filterQueries.size() == 1) {
                filterQuery = filterQueries.get(0);
            } else {
                // Combine filters with AND
                filterQuery = new BooleanQuery.Builder()
                    .add(filterQueries.get(0), BooleanClause.Occur.MUST)
                    .add(filterQueries.get(1), BooleanClause.Occur.MUST)
                    .build();
            }
        }
        
        return executeSearch(textQuery, filterQuery, maxResults);
    }

    /**
     * Executes a search query with optional filter.
     * 
     * @param query Main search query
     * @param filter Optional filter query
     * @param maxResults Maximum number of results
     * @return List of search results
     * @throws IOException If search fails
     */
    private List<SearchResult> executeSearch(Query query, Query filter, int maxResults) 
            throws IOException {
        List<SearchResult> results = new ArrayList<>();
        
        // Check if reader is open
        if (reader == null || searcher == null) {
            System.err.println("Warning: Index reader not initialized, reopening...");
            openIndex();
        }
        
        // Apply filter if provided
        Query finalQuery = query;
        if (filter != null) {
            finalQuery = new BooleanQuery.Builder()
                .add(query, BooleanClause.Occur.MUST)
                .add(filter, BooleanClause.Occur.FILTER)
                .build();
        }
        
        // Execute search
        TopDocs topDocs = searcher.search(finalQuery, maxResults);
        
        // Extract results
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            SearchResult result = new SearchResult(doc, scoreDoc.score);
            results.add(result);
        }
        
        return results;
    }

    /**
     * Refreshes the index reader (call after re-indexing).
     */
    public void refresh() throws IOException {
        if (reader != null) {
            reader.close();
        }
        // Reopen the index
        openIndex();
    }

    /**
     * Closes the searcher and releases resources.
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        analyzer.close();
    }
}

