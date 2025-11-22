"""
Whoosh-based movie indexer.
Implements the same functionality as Task 2.1a using Whoosh instead of Lucene.
"""
import json
import os
import shutil
from whoosh import index
from whoosh.fields import Schema, TEXT, KEYWORD, NUMERIC, ID
from whoosh.analysis import StandardAnalyzer
from pathlib import Path


class MovieIndexer:
    """Creates and manages a Whoosh index for movie data."""
    
    # Field names
    FIELD_IMDB_ID = "imdb_id"
    FIELD_TITLE = "title"
    FIELD_OVERVIEW = "overview"
    FIELD_TAGLINE = "tagline"
    FIELD_CAST = "cast"
    FIELD_GENRES = "genres"
    FIELD_YEAR = "year"
    FIELD_RATING = "rating"
    FIELD_RUNTIME = "runtime"
    
    def __init__(self, index_dir, create_if_missing=True):
        """
        Initialize the indexer with index directory.
        
        Args:
            index_dir: Directory path for the index
            create_if_missing: If True, create index if it doesn't exist
        """
        self.index_dir = Path(index_dir)
        self.index_dir.mkdir(exist_ok=True)
        
        # Define schema
        # Note: Field boosting is done at query time in Whoosh, not in schema
        schema = Schema(
            imdb_id=ID(stored=True),
            title=TEXT(stored=True, analyzer=StandardAnalyzer()),
            overview=TEXT(stored=True, analyzer=StandardAnalyzer()),
            tagline=TEXT(stored=True, analyzer=StandardAnalyzer()),
            cast=TEXT(stored=True, analyzer=StandardAnalyzer()),
            genres=TEXT(stored=True, analyzer=StandardAnalyzer()),
            year=NUMERIC(stored=True),
            rating=NUMERIC(stored=True),
            runtime=NUMERIC(stored=True)
        )
        
        # Open existing index or create new one
        if index.exists_in(str(self.index_dir)):
            self.ix = index.open_dir(str(self.index_dir))
        elif create_if_missing:
            self.ix = index.create_in(str(self.index_dir), schema)
        else:
            raise ValueError(f"Index does not exist at {self.index_dir}")
    
    def rebuild_index(self):
        """Delete and recreate the index (for rebuilding)."""
        # Close the index if it's open
        if hasattr(self, 'ix') and self.ix is not None:
            self.ix.close()
        
        if index.exists_in(str(self.index_dir)):
            shutil.rmtree(str(self.index_dir))
        
        # Recreate schema
        schema = Schema(
            imdb_id=ID(stored=True),
            title=TEXT(stored=True, analyzer=StandardAnalyzer()),
            overview=TEXT(stored=True, analyzer=StandardAnalyzer()),
            tagline=TEXT(stored=True, analyzer=StandardAnalyzer()),
            cast=TEXT(stored=True, analyzer=StandardAnalyzer()),
            genres=TEXT(stored=True, analyzer=StandardAnalyzer()),
            year=NUMERIC(stored=True),
            rating=NUMERIC(stored=True),
            runtime=NUMERIC(stored=True)
        )
        
        self.ix = index.create_in(str(self.index_dir), schema)
    
    def create_index(self, movies):
        """Create index from list of movies."""
        writer = self.ix.writer()
        
        print(f"Indexing {len(movies)} movies...")
        
        for movie in movies:
            doc = {
                'imdb_id': movie.get('imdb_id', ''),
                'title': movie.get('title', '') or '',
                'overview': movie.get('overview', '') or '',
                'tagline': movie.get('tagline', '') or '',
                'cast': movie.get('cast', '') or '',
                'genres': ' '.join(movie.get('genres', [])) if movie.get('genres') else '',
                'year': movie.get('year', 0) or 0,
                'rating': movie.get('rating', 0.0) or 0.0,
                'runtime': movie.get('runtime', 0) or 0
            }
            
            # Only add if has required fields
            if doc['imdb_id'] and doc['title']:
                writer.add_document(**doc)
        
        writer.commit()
        print(f"Index created successfully with {self.ix.doc_count()} documents.")


def read_jsonl(file_path, max_records=0):
    """Read movies from JSONL file."""
    movies = []
    with open(file_path, 'r', encoding='utf-8') as f:
        for i, line in enumerate(f):
            if line.strip():
                try:
                    movie = json.loads(line)
                    movies.append(movie)
                    if max_records > 0 and len(movies) >= max_records:
                        break
                except json.JSONDecodeError as e:
                    print(f"Error parsing line {i}: {e}")
                    continue
    return movies


if __name__ == "__main__":
    # Example usage
    data_path = "../data/movie_dataset.jsonl"
    index_path = "whoosh_index"
    
    movies = read_jsonl(data_path, max_records=1000)
    print(f"Read {len(movies)} movies from {data_path}")
    
    indexer = MovieIndexer(index_path)
    indexer.create_index(movies)

