"""
Standalone script that replicates the Jupyter notebook functionality
Run this to execute the same steps as precision-recall.ipynb
"""
from movies import MovieCollection
from features import FeatureExtractor
from boolean import BooleanRetriever
from tqdm import tqdm

def print_md(text):
    """Print markdown-style text"""
    print(text)

def display_vocabulary(vocabulary_df, vocabulary_idf, n_samples=20):
    """Display vocabulary table"""
    samples = sorted(vocabulary_df.items(), key=lambda x: x[1], reverse=True)[::int(len(vocabulary_df)/n_samples)]  
    print('token\tdf\tidf')
    for (token, _) in samples:
        print(f'{token}\t{vocabulary_df[token]}\t{vocabulary_idf[token]}')

def main():
    # Configuration (same as notebook widgets)
    use_stemming = True
    use_stopwords = False
    number_of_records = 3000
    
    # Step 1: Prepare Collection
    print("---\n")
    print("# Prepare Collection\n")
    
    # Step 2: Create Movies Collection
    print("### Creating Movies Collection")
    
    collection = MovieCollection(number_of_records)
    pipeline = FeatureExtractor(stemming=use_stemming, stopwords=use_stopwords)
    
    print("- extracting 'bag of words' features")
    features_bag = {movie.imdb_id: pipeline.bag_of_words(movie.to_text()) 
                    for movie in tqdm(collection.movies, desc="Processing")}
    
    print("- extracting 'set of words' features")
    features_set = {id: set(bag_of_words.keys()) 
                    for (id, bag_of_words) in features_bag.items()}
    
    print("- building the vocabulary")
    vocabulary_df = pipeline.df(features_set.values())
    vocabulary_idf = pipeline.idf(features_set.values())
    
    print("- extracting 'tfidf' features")
    features_tfidf = {id: pipeline.tfidf(bag_of_words, vocabulary_idf) 
                      for (id, bag_of_words) in features_bag.items()}
    
    # Display vocabulary
    display_vocabulary(vocabulary_df, vocabulary_idf, n_samples=20)
    
    # Step 3: Building the index
    print("\n---\n")
    print("# Building the index\n")
    
    index = BooleanRetriever(features_set)
    print(f"{index.n_docs}")
    
    # Step 4: Test Query
    print("\n**Test Query**\n")
    
    query = "star wars"
    
    # Use AND search (same as notebook)
    result = index.query_and(pipeline.set_of_words(query))
    
    # Display results in markdown table format
    print("|id|title|overview|cast")
    print("|-|-|-|-|")
    
    for movie_id in result:
        movie = collection.get(movie_id)
        tagline = f"**{movie.tagline}**<br/>" if len(movie.tagline) > 0 else ""
        overview = f"{tagline}[{', '.join(movie.genres)}]<br/><br/>{movie.overview}"
        print(f"|{movie_id}|**{movie.title}** ({movie.year})|{overview}|{movie.cast}")
    
    # Step 5: AI Assessment Prompt
    print("\n")
    prompt = f"""
You are a movie relevance classifier. Rate each movie's relevance to the query.

Query: "{query}"

Movies:
{collection.prompt_context(result)}

Rate each movie (0-2):
- 2 = Highly relevant (directly matches query intent)
- 1 = Partially relevant (somewhat related)
- 0 = Not relevant (no meaningful match)

RESPOND ONLY WITH:
MovieID:Score

Example:
tt0076759:2
tt0113107:0

Your ratings:
"""
    
    print(prompt)
    
    # Step 6: Example prompt_context for specific movies
    print(collection.prompt_context(["tt0076759", "tt0120915"]))

if __name__ == "__main__":
    main()

