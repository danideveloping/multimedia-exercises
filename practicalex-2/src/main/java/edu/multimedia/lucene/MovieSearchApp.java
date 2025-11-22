package edu.multimedia.lucene;

import edu.multimedia.lucene.index.MovieIndexer;
import edu.multimedia.lucene.model.Movie;
import edu.multimedia.lucene.search.MovieSearcher;
import edu.multimedia.lucene.search.SearchResult;
import edu.multimedia.lucene.search.EnhancedSearcher;
import edu.multimedia.lucene.search.SearchResults;
import edu.multimedia.lucene.util.JsonlReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main application class for the Lucene Movie Search system.
 * Provides a menu-driven interface for indexing and searching movies.
 */
public class MovieSearchApp {
    private static final String DEFAULT_DATA_PATH = "data/movie_dataset.jsonl";
    private static final String DEFAULT_INDEX_PATH = "index";
    private static final int DEFAULT_MAX_RECORDS = 1000; // Use 1000 for development
    
    private MovieSearcher searcher;
    private EnhancedSearcher enhancedSearcher;
    private final Path indexPath;
    private final Path dataPath;
    private final Scanner scanner;

    public MovieSearchApp(String dataPath, String indexPath) {
        this.dataPath = Paths.get(dataPath);
        this.indexPath = Paths.get(indexPath);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        String dataPath = args.length > 0 ? args[0] : DEFAULT_DATA_PATH;
        String indexPath = args.length > 1 ? args[1] : DEFAULT_INDEX_PATH;
        
        MovieSearchApp app = new MovieSearchApp(dataPath, indexPath);
        app.run();
    }

    /**
     * Main application loop with menu.
     */
    public void run() {
        System.out.println("==========================================");
        System.out.println("   Lucene Movie Search System");
        System.out.println("==========================================");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        buildIndex();
                        break;
                    case 2:
                        basicSearch();
                        break;
                    case 3:
                        enhancedSearch();
                        break;
                    case 4:
                        advancedSearch();
                        break;
                    case 5:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        close();
    }

    /**
     * Prints the main menu.
     */
    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Build/Re-build Index");
        System.out.println("2. Basic Keyword Search");
        System.out.println("3. Enhanced Search (with filters)");
        System.out.println("4. Advanced Search (fuzzy, expansion, facets, pagination)");
        System.out.println("5. Exit");
        System.out.println();
    }

    /**
     * Builds or rebuilds the Lucene index from the JSONL file.
     */
    private void buildIndex() throws IOException {
        System.out.println("\n--- Building Index ---");
        
        int maxRecords = getIntInput(
            "Enter number of records to index (0 for all, default " + DEFAULT_MAX_RECORDS + "): ");
        if (maxRecords == 0) {
            maxRecords = DEFAULT_MAX_RECORDS;
        }
        
        System.out.println("Reading movies from: " + dataPath);
        JsonlReader reader = new JsonlReader();
        List<Movie> movies = reader.readMovies(dataPath, maxRecords);
        
        System.out.println("Found " + movies.size() + " movies.");
        System.out.println("Creating index at: " + indexPath);
        
        MovieIndexer indexer = new MovieIndexer(indexPath);
        indexer.createIndex(movies);
        indexer.close();
        
        // Close existing searcher if it exists
        if (searcher != null) {
            try {
                searcher.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        if (enhancedSearcher != null) {
            try {
                enhancedSearcher.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        
        // Small delay to ensure file locks are released (Windows issue)
        try {
            Thread.sleep(500); // Increased delay for Windows
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Create new searcher with fresh index
        searcher = new MovieSearcher(indexPath);
        enhancedSearcher = new EnhancedSearcher(indexPath);
        
        System.out.println("Index built successfully!");
    }

    /**
     * Performs a basic keyword search.
     */
    private void basicSearch() throws Exception {
        if (searcher == null) {
            System.out.println("Index not found. Please build the index first (option 1).");
            return;
        }
        
        System.out.println("\n--- Basic Keyword Search ---");
        String query = getStringInput("Enter search query: ");
        System.out.println("(Press Enter to continue...)");
        int maxResults = getIntInput("Maximum number of results (default 10): ");
        if (maxResults <= 0) {
            maxResults = 10;
        }
        
        try {
            List<SearchResult> results = searcher.basicSearch(query, maxResults);
            displayResults(results);
        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs an enhanced search with filters.
     */
    private void enhancedSearch() throws Exception {
        if (searcher == null) {
            System.out.println("Index not found. Please build the index first (option 1).");
            return;
        }
        
        System.out.println("\n--- Enhanced Search with Filters ---");
        String query = getStringInput("Enter search query: ");
        System.out.println("(Press Enter to continue...)");
        
        System.out.println("\nFilter Options (press Enter to skip):");
        Double minRating = getDoubleInput("Minimum rating (0.0-10.0): ");
        Double maxRating = getDoubleInput("Maximum rating (0.0-10.0): ");
        Integer minYear = getIntInputOrNull("Minimum year: ");
        Integer maxYear = getIntInputOrNull("Maximum year: ");
        
        int maxResults = getIntInput("Maximum number of results (default 10): ");
        if (maxResults <= 0) {
            maxResults = 10;
        }
        
        System.out.println("\nSearching with:");
        System.out.println("  Query: \"" + query + "\"");
        if (minRating != null) System.out.println("  Min Rating: " + minRating);
        if (maxRating != null) System.out.println("  Max Rating: " + maxRating);
        if (minYear != null) System.out.println("  Min Year: " + minYear);
        if (maxYear != null) System.out.println("  Max Year: " + maxYear);
        System.out.println("  Max Results: " + maxResults);
        System.out.println();
        
        try {
            List<SearchResult> results = searcher.enhancedSearch(
                query, minRating, maxRating, minYear, maxYear, maxResults);
            displayResults(results);
        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs advanced search with fuzzy matching, query expansion, spell checking, 
     * faceted search, and pagination.
     */
    private void advancedSearch() throws Exception {
        if (enhancedSearcher == null) {
            System.out.println("Index not found. Please build the index first (option 1).");
            return;
        }
        
        System.out.println("\n--- Advanced Search ---");
        System.out.println("Features: Fuzzy search (use 'word?'), Auto-expansion, Spell-check, Facets, Pagination");
        System.out.println();
        String query = getStringInput("Enter search query (use 'word?' for fuzzy matching): ");
        
        int maxResults = 50; // Get more results for pagination
        SearchResults searchResults = enhancedSearcher.performSearch(query, maxResults);
        
        System.out.println("\nFound " + searchResults.getTotalHits() + " total results.");
        
        // Display first page
        displayPaginatedResults(searchResults, enhancedSearcher);
    }

    /**
     * Displays paginated results with faceted search options.
     */
    private void displayPaginatedResults(SearchResults searchResults, EnhancedSearcher searcher) 
            throws IOException {
        boolean browsing = true;
        
        while (browsing) {
            List<SearchResult> pageResults = searchResults.getPage(searchResults.getCurrentPage());
            
            System.out.println("\n--- Page " + searchResults.getCurrentPage() + 
                             " of " + searchResults.getTotalPages() + 
                             " (Showing " + pageResults.size() + " results) ---");
            
            // Display results
            for (int i = 0; i < pageResults.size(); i++) {
                SearchResult result = pageResults.get(i);
                int globalIndex = (searchResults.getCurrentPage() - 1) * SearchResults.getResultsPerPage() + i + 1;
                System.out.println("\n" + globalIndex + ". " + result);
            }
            
            // Show facets
            if (searchResults.getCurrentPage() == 1) {
                Map<String, List<String>> facets = searcher.getFacets(pageResults);
                if (!facets.isEmpty()) {
                    System.out.println("\n--- Available Filters ---");
                    if (!facets.get("decade").isEmpty()) {
                        System.out.println("Decades: " + String.join(", ", facets.get("decade")));
                    }
                    if (!facets.get("genres").isEmpty()) {
                        System.out.println("Genres: " + String.join(", ", facets.get("genres").subList(0, 
                            Math.min(10, facets.get("genres").size()))));
                    }
                }
            }
            
            // Navigation options
            System.out.println("\n--- Navigation ---");
            System.out.println("Commands: 'next' (next page), 'prev' (previous page), 'filter <type> <value>' (filter), 'back' (new search)");
            String command = getStringInput("Enter command: ").toLowerCase().trim();
            
            if (command.equals("next") || command.equals("n")) {
                if (searchResults.hasNextPage()) {
                    searchResults.nextPage();
                } else {
                    System.out.println("Already on last page.");
                }
            } else if (command.equals("prev") || command.equals("p")) {
                if (searchResults.hasPreviousPage()) {
                    searchResults.previousPage();
                } else {
                    System.out.println("Already on first page.");
                }
            } else if (command.startsWith("filter ")) {
                String[] parts = command.split(" ", 3);
                if (parts.length >= 3) {
                    String facetType = parts[1];
                    String facetValue = parts[2];
                    List<SearchResult> filtered = searcher.filterByFacet(
                        searchResults.getResults(), facetType, facetValue);
                    System.out.println("Filtered to " + filtered.size() + " results.");
                    // Create new SearchResults with filtered list
                    SearchResults filteredResults = new SearchResults(filtered, filtered.size());
                    displayPaginatedResults(filteredResults, searcher);
                    return;
                }
            } else if (command.equals("back") || command.equals("b")) {
                browsing = false;
            } else {
                System.out.println("Unknown command. Use 'next', 'prev', 'filter', or 'back'.");
            }
        }
    }

    /**
     * Displays search results.
     */
    private void displayResults(List<SearchResult> results) {
        System.out.println("\n--- Search Results (" + results.size() + " found) ---");
        
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            System.out.println("\n" + (i + 1) + ". " + result);
        }
    }

    /**
     * Gets integer input from user.
     */
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        System.out.flush(); // Ensure prompt is displayed
        String input = scanner.nextLine();
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Gets integer input from user, returns null if empty (for optional filters).
     */
    private Integer getIntInputOrNull(String prompt) {
        System.out.print(prompt);
        System.out.flush(); // Ensure prompt is displayed
        String input = scanner.nextLine();
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Gets double input from user.
     */
    private Double getDoubleInput(String prompt) {
        System.out.print(prompt);
        System.out.flush(); // Ensure prompt is displayed
        String input = scanner.nextLine();
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Gets string input from user.
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        System.out.flush(); // Ensure prompt is displayed
        String input = scanner.nextLine();
        return input != null ? input.trim() : "";
    }

    /**
     * Closes resources.
     */
    private void close() {
        if (searcher != null) {
            try {
                searcher.close();
            } catch (IOException e) {
                System.err.println("Error closing searcher: " + e.getMessage());
            }
        }
        if (enhancedSearcher != null) {
            try {
                enhancedSearcher.close();
            } catch (IOException e) {
                System.err.println("Error closing enhanced searcher: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
