# Quick Start Guide

## First Time Setup

1. **Navigate to the project directory**:
   ```bash
   cd practicalex-2
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn exec:java
   ```

## Typical Workflow

### Step 1: Build the Index
- Select option **1** from the menu
- Enter **1000** (for development) or **0** (for all records)
- Wait for indexing to complete

### Step 2: Perform Searches
- Select option **2** for basic search
- Or select option **3** for filtered search

## Example Searches

### Basic Search Examples:
- `star wars` - Find Star Wars movies
- `tom hanks` - Find movies with Tom Hanks
- `crime drama` - Find crime dramas
- `1995` - Find movies from 1995

### Enhanced Search Examples:
- Query: `action`
  - Min Rating: `7.0`
  - Min Year: `2000`
  - Results: High-rated action movies from 2000+

- Query: `comedy`
  - Max Year: `1990`
  - Results: Classic comedies from before 1990

## Common Commands

```bash
# Clean and rebuild
mvn clean compile

# Run with custom data path
mvn exec:java -Dexec.args="../benchmark/data/movie_dataset.jsonl index"

# Package the application
mvn clean package

# Run with more memory (if needed)
mvn exec:java -Dexec.jvmArgs="-Xmx2g"
```

## Troubleshooting

**Problem**: "Index not found"  
**Solution**: Run option 1 (Build Index) first

**Problem**: "File not found"  
**Solution**: Check that `../benchmark/data/movie_dataset.jsonl` exists

**Problem**: Out of memory  
**Solution**: Use fewer records (1000 instead of all) or increase heap size

