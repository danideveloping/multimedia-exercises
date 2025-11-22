# Whoosh Implementation (Task 2.1c)

This is an alternative implementation of Task 2.1a using **Whoosh** (Python) instead of Lucene (Java).

## Setup

1. **Install Python** (3.7 or higher)

2. **Install Whoosh**:
   ```bash
   pip install -r requirements.txt
   ```

3. **Run the application**:
   ```bash
   python main.py
   ```

## Usage

Same interface as Lucene implementation:
1. Build/Re-build Index
2. Basic Keyword Search
3. Enhanced Search (with filters)
4. Exit

## Features

- ✅ Import JSONL data into Whoosh index
- ✅ Basic keyword search with field boosting
- ✅ Enhanced search with rating/year filters
- ✅ Same functionality as Lucene implementation

## Comparison

See `COMPARISON.md` for detailed comparison with Lucene.

