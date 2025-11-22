# Task 2.1b: Advanced Search Features Guide

This document explains how to use the advanced search features implemented in Task 2.1b.

## Features Overview

1. **Fuzzy Search** - Handle typos and variations
2. **Automated Query Expansion** - Automatically expand queries with few results
3. **Spell Checking** - Correct misspelled queries
4. **Faceted Search** - Filter by categories (decade, genre)
5. **Pagination** - Navigate through results 10 at a time

## How to Use

### Accessing Advanced Search

From the main menu, select:
```
4. Advanced Search (fuzzy, expansion, facets, pagination)
```

### 1. Fuzzy Search

**Syntax:** Add `?` after a word to enable fuzzy matching

**Examples:**
- `leon?` - Finds "Leon", "Leonardo", "Leone", etc. (handles typos)
- `star wars?` - Regular search for "star", fuzzy for "wars"
- `action? movie` - Fuzzy search for "action", regular for "movie"

**Use Case:** 
- Handle typos: `leon?` will find "Leon" even if you meant "Leonardo"
- Find variations: `scifi?` might find "sci-fi", "science fiction"

### 2. Automated Query Expansion

**How it works:**
- If your search returns fewer than 3 results, the system automatically expands the query
- You'll see: "Few results found. Expanding query..."
- The system tries to find related terms and variations

**Example:**
- Query: `space opera` (might return 2 results)
- System expands and finds more related movies

### 3. Spell Checking

**How it works:**
- The system checks if query terms exist in the index
- If a term is not found, it suggests corrections
- You'll see: "Did you mean: [corrected query]? (using corrected query)"

**Example:**
- You type: `star wras`
- System suggests: "Did you mean: 'star wars'?"

### 4. Faceted Search

**How it works:**
- After displaying results, the system shows available filters
- Filters are extracted from the current result set
- You can filter by decade or genre

**Available Facets:**
- **Decades**: 1990s, 2000s, 2010s, etc.
- **Genres**: Action, Drama, Comedy, etc.

**How to filter:**
After seeing results, use the command:
```
filter decade 1990s
```
or
```
filter genre Action
```

### 5. Pagination

**How it works:**
- Results are displayed 10 per page
- Navigate using commands: `next`, `prev`, or `n`, `p`

**Commands:**
- `next` or `n` - Go to next page
- `prev` or `p` - Go to previous page
- `filter <type> <value>` - Apply facet filter
- `back` or `b` - Return to main menu

**Example Session:**
```
--- Page 1 of 3 (Showing 10 results) ---
[Results 1-10 displayed]

--- Navigation ---
Enter command: next
--- Page 2 of 3 (Showing 10 results) ---
[Results 11-20 displayed]

Enter command: filter decade 1990s
Filtered to 15 results.
[Shows filtered results]

Enter command: back
[Returns to main menu]
```

## Complete Example

```
Main Menu > 4 (Advanced Search)

Enter search query: action? movie
Found 45 total results.

--- Page 1 of 5 (Showing 10 results) ---
[10 action movies displayed]

--- Available Filters ---
Decades: 1990s, 2000s, 2010s
Genres: Action, Adventure, Thriller, Crime

--- Navigation ---
Enter command: filter decade 2000s
Filtered to 12 results.
[Shows only 2000s action movies]

Enter command: next
--- Page 2 of 2 (Showing 2 results) ---
[Remaining results]

Enter command: back
[Returns to menu]
```

## Technical Details

### Fuzzy Matching
- Uses Lucene's `FuzzyQuery`
- Default fuzziness: 0.8 (2 edit distance)
- Searches across all fields (title, cast, overview, etc.)

### Query Expansion
- Triggered when results < 3
- Expands query with related terms
- Uses Boolean OR to combine original and expanded terms

### Spell Checking
- Checks terms against index vocabulary
- Uses fuzzy matching to find corrections
- Suggests most likely correction

### Faceted Search
- Extracts facets from current result set
- Supports decade and genre filtering
- Filters are applied to already-retrieved results

### Pagination
- 10 results per page
- Maintains state across navigation
- Works with filtered results

## Tips

1. **Use fuzzy search for typos**: `leon?` instead of `leonardo`
2. **Combine features**: Use fuzzy + filters for best results
3. **Navigate efficiently**: Use `next`/`prev` to browse large result sets
4. **Refine with facets**: After seeing results, filter by decade/genre
5. **Let expansion help**: If you get few results, let the system expand automatically

