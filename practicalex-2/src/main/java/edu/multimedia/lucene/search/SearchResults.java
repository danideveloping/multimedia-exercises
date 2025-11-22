package edu.multimedia.lucene.search;

import java.util.List;

/**
 * Container for search results with pagination support.
 */
public class SearchResults {
    private final List<SearchResult> results;
    private final long totalHits;
    private int currentPage = 1;
    private static final int RESULTS_PER_PAGE = 10;

    public SearchResults(List<SearchResult> results, long totalHits) {
        this.results = results;
        this.totalHits = totalHits;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalHits / RESULTS_PER_PAGE);
    }

    public List<SearchResult> getPage(int page) {
        currentPage = page;
        int start = (page - 1) * RESULTS_PER_PAGE;
        int end = Math.min(start + RESULTS_PER_PAGE, results.size());
        
        if (start >= results.size()) {
            return List.of();
        }
        
        return results.subList(start, end);
    }

    public boolean hasNextPage() {
        return currentPage < getTotalPages();
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public int nextPage() {
        if (hasNextPage()) {
            currentPage++;
        }
        return currentPage;
    }

    public int previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
        }
        return currentPage;
    }

    public static int getResultsPerPage() {
        return RESULTS_PER_PAGE;
    }
}

