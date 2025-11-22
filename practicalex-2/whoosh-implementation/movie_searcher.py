"""
Whoosh-based movie searcher.
Implements basic and enhanced search functionality.
"""
from whoosh import index
from whoosh.qparser import MultifieldParser, AndGroup
from whoosh.query import And, Or, Term, NumericRange
from pathlib import Path


class MovieSearcher:
    """Provides search functionality over the Whoosh index."""
    
    def __init__(self, index_dir):
        """Initialize searcher with index directory."""
        self.index_dir = Path(index_dir)
        if not index.exists_in(str(self.index_dir)):
            raise ValueError(f"Index not found at {self.index_dir}")
        self.ix = index.open_dir(str(self.index_dir))
    
    def basic_search(self, query_text, max_results=10):
        """
        Performs basic keyword search across multiple fields.
        
        Args:
            query_text: Search query string
            max_results: Maximum number of results to return
            
        Returns:
            List of search results (dictionaries)
        """
        with self.ix.searcher() as searcher:
            # Define fields to search with boosts
            fields = ['title', 'cast', 'overview', 'tagline', 'genres']
            
            # Create multifield parser with boosts (fieldboosts is a dict parameter)
            fieldboosts = {
                'title': 2.0,   # Title matches are most important
                'cast': 1.5     # Cast matches are important
            }
            parser = MultifieldParser(fields, schema=self.ix.schema, fieldboosts=fieldboosts)
            
            # Parse query
            query = parser.parse(query_text)
            
            # Execute search
            results = searcher.search(query, limit=max_results)
            
            # Extract results
            search_results = []
            for hit in results:
                result = {
                    'imdb_id': hit['imdb_id'],
                    'title': hit['title'],
                    'year': hit.get('year', 0),
                    'rating': hit.get('rating', 0.0),
                    'overview': hit.get('overview', ''),
                    'cast': hit.get('cast', ''),
                    'score': hit.score
                }
                search_results.append(result)
            
            return search_results, results.scored_length()
    
    def enhanced_search(self, query_text, min_rating=None, max_rating=None,
                       min_year=None, max_year=None, max_results=10):
        """
        Performs enhanced search with filters.
        
        Args:
            query_text: Search query string
            min_rating: Minimum rating (optional)
            max_rating: Maximum rating (optional)
            min_year: Minimum year (optional)
            max_year: Maximum year (optional)
            max_results: Maximum number of results
            
        Returns:
            List of search results (dictionaries)
        """
        with self.ix.searcher() as searcher:
            # Build text query
            fields = ['title', 'cast', 'overview', 'tagline', 'genres']
            fieldboosts = {
                'title': 2.0,   # Title matches are most important
                'cast': 1.5      # Cast matches are important
            }
            parser = MultifieldParser(fields, schema=self.ix.schema, fieldboosts=fieldboosts)
            text_query = parser.parse(query_text)
            
            # Build filter queries
            filter_queries = []
            
            if min_rating is not None or max_rating is not None:
                min_r = min_rating if min_rating is not None else 0.0
                max_r = max_rating if max_rating is not None else 10.0
                rating_query = NumericRange('rating', min_r, max_r)
                filter_queries.append(rating_query)
            
            if min_year is not None or max_year is not None:
                min_y = min_year if min_year is not None else 1900
                max_y = max_year if max_year is not None else 2100
                year_query = NumericRange('year', min_y, max_y)
                filter_queries.append(year_query)
            
            # Combine queries
            if filter_queries:
                final_query = And([text_query] + filter_queries)
            else:
                final_query = text_query
            
            # Execute search
            results = searcher.search(final_query, limit=max_results)
            
            # Extract results
            search_results = []
            for hit in results:
                result = {
                    'imdb_id': hit['imdb_id'],
                    'title': hit['title'],
                    'year': hit.get('year', 0),
                    'rating': hit.get('rating', 0.0),
                    'overview': hit.get('overview', ''),
                    'cast': hit.get('cast', ''),
                    'score': hit.score
                }
                search_results.append(result)
            
            return search_results, results.scored_length()


def display_results(results, total_hits):
    """Display search results in a formatted way."""
    print(f"\n--- Search Results ({len(results)} of {total_hits} found) ---")
    
    if not results:
        print("No results found.")
        return
    
    for i, result in enumerate(results, 1):
        overview = result['overview']
        if len(overview) > 100:
            overview = overview[:100] + "..."
        
        print(f"\n{i}. Score: {result['score']:.3f} | {result['title']} ({result['year']}) | "
              f"Rating: {result['rating']:.1f} | {overview}")


if __name__ == "__main__":
    # Example usage
    index_path = "whoosh_index"
    
    searcher = MovieSearcher(index_path)
    
    # Basic search
    print("=== Basic Search ===")
    results, total = searcher.basic_search("star wars", max_results=10)
    display_results(results, total)
    
    # Enhanced search
    print("\n=== Enhanced Search ===")
    results, total = searcher.enhanced_search(
        "action", 
        min_rating=7.0,
        min_year=1990,
        max_results=10
    )
    display_results(results, total)

