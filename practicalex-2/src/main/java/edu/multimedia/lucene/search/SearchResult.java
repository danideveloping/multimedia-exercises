package edu.multimedia.lucene.search;

import edu.multimedia.lucene.index.MovieIndexer;
import org.apache.lucene.document.Document;

/**
 * Represents a single search result with the document and its relevance score.
 */
public class SearchResult {
    private final Document document;
    private final float score;

    public SearchResult(Document document, float score) {
        this.document = document;
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public float getScore() {
        return score;
    }

    /**
     * Gets the IMDB ID from the document.
     */
    public String getImdbId() {
        return document.get(MovieIndexer.FIELD_IMDB_ID);
    }

    /**
     * Gets the title from the document.
     */
    public String getTitle() {
        return document.get(MovieIndexer.FIELD_TITLE);
    }

    /**
     * Gets the year from the document.
     */
    public int getYear() {
        return Integer.parseInt(document.get(MovieIndexer.FIELD_YEAR));
    }

    /**
     * Gets the rating from the document.
     */
    public double getRating() {
        return Double.parseDouble(document.get(MovieIndexer.FIELD_RATING));
    }

    /**
     * Gets the overview from the document.
     */
    public String getOverview() {
        return document.get(MovieIndexer.FIELD_OVERVIEW);
    }

    /**
     * Gets the cast from the document.
     */
    public String getCast() {
        return document.get(MovieIndexer.FIELD_CAST);
    }

    /**
     * Formats the result as a string for display.
     */
    @Override
    public String toString() {
        return String.format(
            "Score: %.3f | %s (%d) | Rating: %.1f | %s",
            score,
            getTitle(),
            getYear(),
            getRating(),
            getOverview() != null && getOverview().length() > 100 
                ? getOverview().substring(0, 100) + "..." 
                : getOverview()
        );
    }
}

