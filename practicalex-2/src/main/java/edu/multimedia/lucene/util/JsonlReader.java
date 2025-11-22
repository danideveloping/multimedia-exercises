package edu.multimedia.lucene.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.multimedia.lucene.model.Movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading JSONL (JSON Lines) files containing movie data.
 * Each line in the file is a separate JSON object.
 */
public class JsonlReader {
    private final Gson gson;

    public JsonlReader() {
        this.gson = new GsonBuilder().create();
    }

    /**
     * Reads movies from a JSONL file.
     * 
     * @param filePath Path to the JSONL file
     * @param maxRecords Maximum number of records to read (0 = read all)
     * @return List of Movie objects
     * @throws IOException If file cannot be read
     */
    public List<Movie> readMovies(Path filePath, int maxRecords) throws IOException {
        List<Movie> movies = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Movie movie = gson.fromJson(line, Movie.class);
                    movies.add(movie);
                    count++;
                    
                    if (maxRecords > 0 && count >= maxRecords) {
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        
        return movies;
    }

    /**
     * Reads all movies from a JSONL file.
     * 
     * @param filePath Path to the JSONL file
     * @return List of Movie objects
     * @throws IOException If file cannot be read
     */
    public List<Movie> readMovies(Path filePath) throws IOException {
        return readMovies(filePath, 0);
    }
}

