# Practical Exercise 2: Text Retrieval with Lucene

This project implements a movie search system using Apache Lucene for text retrieval. It provides functionality to index movie data from a JSONL file and perform keyword-based searches with optional filters.

## Features

- **Import JSONL data into Lucene index**: Reads movie data from JSONL format and creates a searchable Lucene index
- **Basic keyword search**: Search across movie titles, descriptions, cast, and other fields
- **Enhanced search with filters**: Filter results by rating, release year, and other metadata
- **Field boosting**: Prioritizes matches in title and cast fields for better relevance

## Project Structure

```
practicalex-2/
├── pom.xml                                    # Maven configuration
├── build.gradle                               # Gradle configuration
├── gradlew.bat                                # Gradle wrapper (Windows)
├── README.md                                   # This file
├── QUICK_INSTALL.md                           # Quick setup guide
├── INSTALL.md                                  # Detailed installation guide
└── src/main/java/edu/multimedia/lucene/
    ├── MovieSearchApp.java                    # Main application with menu interface
    ├── model/
    │   └── Movie.java                         # Movie data model
    ├── util/
    │   └── JsonlReader.java                   # JSONL file reader
    ├── index/
    │   └── MovieIndexer.java                 # Lucene indexer
    └── search/
        ├── MovieSearcher.java                 # Search functionality
        └── SearchResult.java                  # Search result wrapper
```

## Prerequisites

- Java 11 or higher (✅ You have Java 17 installed)
- **Either** Maven 3.6+ **or** Gradle 7.0+ (see installation below)

## Quick Start (No Admin Rights Needed!)

### Option 1: Use Gradle Wrapper (Easiest)

If you have the `gradlew.bat` file, just run:
```powershell
cd practicalex-2
.\gradlew.bat build
.\gradlew.bat run
```

### Option 2: Auto-Setup Gradle (Recommended)

Run the setup script to download Gradle to your user directory:
```powershell
cd practicalex-2
powershell -ExecutionPolicy Bypass -File setup-gradle.ps1
```

Then follow the instructions it prints. You'll need to add Gradle to your PATH for the current session:
```powershell
$env:PATH = "$env:USERPROFILE\.gradle\gradle-8.5\bin;$env:PATH"
gradle build
gradle run
```

### Option 3: Manual Gradle Installation

See [QUICK_INSTALL.md](QUICK_INSTALL.md) for manual installation instructions.

## Usage

### Running the Application

**With Gradle:**
```powershell
gradle run
```

**With Maven (if installed):**
```powershell
mvn exec:java
```

**With custom arguments:**
```powershell
# Gradle
gradle run --args="../benchmark/data/movie_dataset.jsonl index"

# Maven
mvn exec:java -Dexec.args="../benchmark/data/movie_dataset.jsonl index"
```

### Application Menu

The application provides a menu-driven interface:

1. **Build/Re-build Index**: Creates a new Lucene index from the JSONL file
   - Prompts for number of records to index (default: 1000 for faster development)
   - Rebuilds the index from scratch each time

2. **Basic Keyword Search**: Performs a simple keyword search
   - Searches across: title, overview, cast, tagline, genres
   - Title matches are boosted (2x)
   - Cast matches are boosted (1.5x)

3. **Enhanced Search (with filters)**: Advanced search with metadata filters
   - Keyword search + optional filters:
     - Minimum/Maximum rating (0.0-10.0)
     - Minimum/Maximum year
   - All filters are optional (press Enter to skip)

4. **Exit**: Quit the application

### Example Usage

1. **First time setup**:
   - Run the application
   - Select option 1 to build the index
   - Enter 1000 (or desired number) for development, or 0 for all records

2. **Basic search**:
   - Select option 2
   - Enter query: "star wars"
   - View results

3. **Filtered search**:
   - Select option 3
   - Enter query: "action"
   - Set minimum rating: 7.0
   - Set minimum year: 2000
   - View filtered results

## Development Notes

### Using a Subset of Data

During development, it's recommended to use a small subset (e.g., 1000 records) to:
- Speed up index building
- Reduce memory usage
- Enable faster iteration

The index is rebuilt from scratch each time (option 1), so you can easily test with different dataset sizes.

### Index Location

The index is stored in the `index/` directory (relative to the project root). You can delete this folder to force a complete rebuild.

### Field Boosting

The system uses field boosting to improve search relevance:
- **Title**: 2.0x boost (most important)
- **Cast**: 1.5x boost (important)
- **Overview, Tagline, Genres**: 1.0x boost (standard)

This means matches in the title field will rank higher than matches in other fields.

## Dependencies

- **Apache Lucene 9.9.0**: Core search library
- **Gson 2.10.1**: JSON parsing for JSONL files
- **SLF4J**: Logging framework

All dependencies are managed by Maven/Gradle and will be downloaded automatically.

## Troubleshooting

### Index Not Found Error

If you see "Index not found", run option 1 (Build Index) first.

### File Not Found Error

Make sure the JSONL file path is correct. The default path is `../benchmark/data/movie_dataset.jsonl` relative to the project root.

### Out of Memory Error

If indexing fails with memory errors:
- Reduce the number of records (use 1000 instead of all)
- Increase Java heap size: `gradle run -Dorg.gradle.jvmargs="-Xmx2g"`

### Build Tool Not Found

If you don't have Maven or Gradle installed:
- **Easiest**: Run `setup-gradle.ps1` to download Gradle automatically
- **Alternative**: See [QUICK_INSTALL.md](QUICK_INSTALL.md) for manual installation

## Code References

**AI-Assisted Code Generation**: 
- The project structure and boilerplate code were generated with AI assistance
- Core Lucene indexing and search logic follows standard Lucene patterns
- Field boosting and filter implementation based on Lucene best practices

## Task 2.1c: Alternative Retrieval Engine

This project also includes a **Whoosh (Python)** implementation as an alternative to Lucene.

### Whoosh Implementation

Located in `whoosh-implementation/` directory:
- Same functionality as Lucene implementation (Task 2.1a)
- Pure Python implementation using Whoosh library
- See `whoosh-implementation/README.md` for setup and usage

### Comparison

See **[COMPARISON.md](COMPARISON.md)** for a detailed comparison between:
- **Apache Lucene** (Java) - Original implementation
- **Whoosh** (Python) - Alternative implementation

The comparison covers:
- Performance benchmarks
- Feature comparison
- Code complexity
- Use case recommendations
- Conclusion and recommendations

## Future Enhancements

Potential improvements:
- Fuzzy search for typo tolerance
- Phrase queries for exact phrase matching
- Faceted search for genre/cast filtering
- Query expansion and synonyms
- Performance optimizations for larger datasets

## License

This project is for educational purposes as part of the Multimedia Exercises course.
