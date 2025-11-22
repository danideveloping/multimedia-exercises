"""
Main application for Whoosh-based movie search.
Provides menu-driven interface similar to Lucene implementation.
"""
import sys
from pathlib import Path
from whoosh import index
from movie_indexer import MovieIndexer, read_jsonl
from movie_searcher import MovieSearcher, display_results


def print_menu():
    """Print main menu."""
    print("\n--- Main Menu ---")
    print("1. Build/Re-build Index")
    print("2. Basic Keyword Search")
    print("3. Enhanced Search (with filters)")
    print("4. Exit")
    print()


def build_index(data_path, index_path, max_records=1000):
    """Build or rebuild the index."""
    print("\n--- Building Index ---")
    
    max_records_input = input(f"Enter number of records to index (0 for all, default {max_records}): ").strip()
    if max_records_input:
        try:
            max_records = int(max_records_input)
            if max_records == 0:
                max_records = 1000
        except ValueError:
            pass
    
    print(f"Reading movies from: {data_path}")
    movies = read_jsonl(data_path, max_records)
    print(f"Found {len(movies)} movies.")
    
    print(f"Creating index at: {index_path}")
    # Always rebuild when user selects option 1
    # Check if index exists and delete it first
    if index.exists_in(str(index_path)):
        import shutil
        from pathlib import Path
        # Close any open index first, then delete
        shutil.rmtree(str(index_path))
    
    # Create fresh index
    indexer = MovieIndexer(index_path, create_if_missing=True)
    indexer.create_index(movies)
    
    print("Index built successfully!")


def basic_search(index_path):
    """Perform basic keyword search."""
    print("\n--- Basic Keyword Search ---")
    query = input("Enter search query: ").strip()
    max_results_input = input("Maximum number of results (default 10): ").strip()
    max_results = int(max_results_input) if max_results_input else 10
    
    searcher = MovieSearcher(index_path)
    results, total_hits = searcher.basic_search(query, max_results)
    display_results(results, total_hits)


def enhanced_search(index_path):
    """Perform enhanced search with filters."""
    print("\n--- Enhanced Search with Filters ---")
    query = input("Enter search query: ").strip()
    
    print("\nFilter Options (press Enter to skip):")
    min_rating_input = input("Minimum rating (0.0-10.0): ").strip()
    min_rating = float(min_rating_input) if min_rating_input else None
    
    max_rating_input = input("Maximum rating (0.0-10.0): ").strip()
    max_rating = float(max_rating_input) if max_rating_input else None
    
    min_year_input = input("Minimum year: ").strip()
    min_year = int(min_year_input) if min_year_input else None
    
    max_year_input = input("Maximum year: ").strip()
    max_year = int(max_year_input) if max_year_input else None
    
    max_results_input = input("Maximum number of results (default 10): ").strip()
    max_results = int(max_results_input) if max_results_input else 10
    
    print("\nSearching with:")
    print(f"  Query: \"{query}\"")
    if min_rating is not None:
        print(f"  Min Rating: {min_rating}")
    if max_rating is not None:
        print(f"  Max Rating: {max_rating}")
    if min_year is not None:
        print(f"  Min Year: {min_year}")
    if max_year is not None:
        print(f"  Max Year: {max_year}")
    print(f"  Max Results: {max_results}")
    print()
    
    searcher = MovieSearcher(index_path)
    results, total_hits = searcher.enhanced_search(
        query, min_rating, max_rating, min_year, max_year, max_results
    )
    display_results(results, total_hits)


def main():
    """Main application loop."""
    # Default paths
    data_path = "../data/movie_dataset.jsonl"
    index_path = "whoosh_index"
    
    # Allow command-line arguments
    if len(sys.argv) > 1:
        data_path = sys.argv[1]
    if len(sys.argv) > 2:
        index_path = sys.argv[2]
    
    print("==========================================")
    print("   Whoosh Movie Search System")
    print("==========================================")
    print()
    
    running = True
    while running:
        print_menu()
        choice = input("Enter your choice: ").strip()
        
        try:
            if choice == "1":
                build_index(data_path, index_path)
            elif choice == "2":
                basic_search(index_path)
            elif choice == "3":
                enhanced_search(index_path)
            elif choice == "4":
                running = False
                print("Goodbye!")
            else:
                print("Invalid choice. Please try again.")
        except Exception as e:
            print(f"Error: {e}")
            import traceback
            traceback.print_exc()
        
        if running:
            input("\nPress Enter to continue...")


if __name__ == "__main__":
    main()

