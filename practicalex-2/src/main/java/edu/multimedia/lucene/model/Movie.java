package edu.multimedia.lucene.model;

import java.util.List;

/**
 * Data model representing a movie from the dataset.
 * This class maps to the JSON structure in the JSONL file.
 */
public class Movie {
    private int index;
    private String imdbId;
    private String title;
    private String overview;
    private String tagline;
    private String cast;
    private List<String> genres;
    private int runtime;
    private double rating;
    private int year;

    // Default constructor
    public Movie() {
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("Movie{imdbId='%s', title='%s', year=%d, rating=%.1f}", 
                imdbId, title, year, rating);
    }
}

