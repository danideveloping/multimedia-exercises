"""
Script to generate movie context for ChatGPT/Claude
Run this and copy the output to your AI assistant
"""
from movies import MovieCollection

def main():
    print("Loading movie collection...")
    collection = MovieCollection(3000)
    
    # Get diverse sample movies from the actual collection
    # We'll pick from different years and genres
    print("Selecting diverse movie samples from collection...")
    
    sample_movies = []
    for movie in collection.movies:
        # Get a good mix
        if movie.imdb_id in ["tt0114709", "tt0113497", "tt0076759", "tt0120915", 
                            "tt0113277", "tt0114388", "tt0112641", "tt0109830"]:
            sample_movies.append(movie.imdb_id)
            if len(sample_movies) >= 8:
                break
    
    # Fallback: if we don't have enough, just take first 8
    if len(sample_movies) < 8:
        sample_movies = [movie.imdb_id for movie in collection.movies[:8]]
    
    print(f"Selected {len(sample_movies)} movies for context")
    
    print("\n" + "="*80)
    print("MOVIE CONTEXT FOR AI PROMPT")
    print("="*80)
    print("\nCopy everything below this line and paste into ChatGPT/Claude:\n")
    print("-"*80)
    
    # Get and print context (only for movies that exist)
    valid_ids = [id for id in sample_movies if collection.get(id) is not None]
    context = collection.prompt_context(valid_ids)
    print(context)
    
    print("-"*80)
    print("\n✓ Copy the text above (between the dashed lines)")
    print("✓ Paste into ChatGPT/Claude along with your query generation prompt")

if __name__ == "__main__":
    main()

