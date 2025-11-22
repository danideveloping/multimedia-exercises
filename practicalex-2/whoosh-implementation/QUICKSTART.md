# Quick Start Guide - Whoosh Implementation

## Prerequisites

- Python 3.7 or higher
- pip (Python package manager)

## Installation

1. **Navigate to the whoosh-implementation directory:**
   ```bash
   cd whoosh-implementation
   ```

2. **Install Whoosh:**
   ```bash
   pip install -r requirements.txt
   ```

## Running the Application

1. **Start the application:**
   ```bash
   python main.py
   ```

2. **Build the index first:**
   - Select option `1` from the menu
   - Enter number of movies to index (or press Enter for default 1000)
   - Wait for indexing to complete

3. **Perform searches:**
   - Option `2`: Basic keyword search
   - Option `3`: Enhanced search with filters

## Example Usage

### Basic Search
```
Enter your choice: 2
Enter search query: star wars
Maximum number of results (default 10): 10
```

### Enhanced Search with Filters
```
Enter your choice: 3
Enter search query: action
Minimum rating (0.0-10.0): 7.0
Maximum rating (0.0-10.0): 
Minimum year: 1990
Maximum year: 
Maximum number of results (default 10): 10
```

## Data Path

The application looks for data at: `../data/movie_dataset.jsonl`

You can specify a different path by running:
```bash
python main.py /path/to/movie_dataset.jsonl
```

## Index Location

The Whoosh index is created in: `whoosh_index/` directory

To rebuild the index, simply select option `1` again (it will delete and recreate).

