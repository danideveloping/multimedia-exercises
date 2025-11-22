# Task 1.1b: Automated Relevance Assessment Process

## Overview
This task focuses on using generative AI to efficiently evaluate search results and determine which movies are relevant for a given query. The goal is to streamline the assessment process and make it automatable.

## Initial Prompt

### Basic Prompt Structure:

```python
prompt = f"""
You are assessing movies. Given the query below and the results, assess which movies are relevant for the query

Query: {query}

Results:

{collection.prompt_context(result)}
"""
```

## Optimized Prompt

### Improved Prompt for Better Parsing:

```python
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
```

## Key Improvements

1. **Structured Output Format**: Changed from verbose explanations to simple "MovieID:Score" format
2. **Clear Scoring Scale**: Defined 3-level relevance scale (0, 1, 2)
3. **Explicit Instructions**: Added "RESPOND ONLY WITH" to minimize verbose output
4. **Example Format**: Provided example to guide the AI's response format
5. **Faster Parsing**: Simple format allows easy extraction via regex or string parsing

## Example Assessment

### Query: "star wars"

### Assessment Results:

**Relevance Assessment:**

| ID | Title | Relevance | Reason |
|---|---|---|---|
| tt0113107 | Frankie Starlight | ❌ Not relevant (0) | Has the word "Star" but no relation to Star Wars. It's a drama about a boy and his mother — no sci-fi or franchise link. |
| tt0114663 | Three Wishes | ❌ Not relevant (0) | Fantasy/drama involving magic and family. "Star" appears in a character's name but unrelated to Star Wars. |
| tt0076759 | Star Wars (1977) | ✅✅ Highly relevant (2) | Exact title match — the original Star Wars film. |
| tt0082971 | Raiders of the Lost Ark | ⚠️ Partially relevant (1) | Not Star Wars, but shares creators (George Lucas, Harrison Ford, etc.). Could appear due to production link, but only tangentially related. |
| tt0119859 | Paradise Road | ❌ Not relevant (0) | War drama with no sci-fi or Star Wars relation. |
| tt0120915 | Star Wars: Episode I – The Phantom Menace | ✅✅ Highly relevant (2) | Part of the Star Wars franchise. |
| tt0143924 | Promise Her Anything | ❌ Not relevant (0) | Comedy-drama; unrelated thematically or by creators. |
| tt0054331 | Spartacus | ❌ Not relevant (0) | Historical epic by Kubrick, not Star Wars-related. |
| tt0048380 | Mister Roberts | ❌ Not relevant (0) | Military comedy-drama; no sci-fi or franchise connection. |

**Final Relevant Results:**
- tt0076759 — Star Wars (1977) [Score: 2]
- tt0120915 — Star Wars: Episode I – The Phantom Menace (1999) [Score: 2]
- tt0082971 — Raiders of the Lost Ark (1981) [Score: 1] (optional soft match)

## Parsing Strategy

### Simple Parsing Code:

```python
def parse_relevance_scores(response):
    """
    Parse AI response in format: MovieID:Score
    Returns: dict mapping movie_id to score (0, 1, or 2)
    """
    scores = {}
    for line in response.strip().split('\n'):
        line = line.strip()
        if ':' in line:
            movie_id, score = line.split(':', 1)
            movie_id = movie_id.strip()
            try:
                score = int(score.strip())
                if score in [0, 1, 2]:
                    scores[movie_id] = score
            except ValueError:
                continue
    return scores
```

## Testing Different Query Types

### Keyword Query: "star wars"
- **Result**: Correctly identified exact matches and franchise connections
- **Quality**: High - clear distinction between relevant and irrelevant

### Question Query: "what are classic crime dramas from the 90s"
- **Expected**: Should identify crime dramas from 1990s
- **Assessment**: Need to test with actual results

### Recommendation Query: "movies like heat"
- **Expected**: Should identify similar crime/heist films
- **Assessment**: Need to test with actual results

### Descriptive Query: "heist films with ensemble cast"
- **Expected**: Should identify heist movies with multiple main characters
- **Assessment**: Need to test with actual results

## Anecdotal Experience with Quality

### Strengths:
- ✅ **Accurate for Clear Cases**: Excellent at identifying exact matches and obvious relevance
- ✅ **Context Awareness**: Understands franchise connections and creator relationships
- ✅ **Consistent Scoring**: Uses the 0-2 scale appropriately

### Limitations:
- ⚠️ **Edge Cases**: Sometimes struggles with partially relevant items (e.g., Raiders of the Lost Ark)
- ⚠️ **Verbose Output**: Even with instructions, sometimes includes explanations (though parseable)
- ⚠️ **Subjectivity**: Different queries may have different interpretation of "partially relevant"

### Quality Observations:
- **High Relevance (Score 2)**: Very reliable - AI correctly identifies exact matches
- **Partial Relevance (Score 1)**: More variable - depends on query type and context
- **Not Relevant (Score 0)**: Generally accurate, but may miss subtle connections

## Debugging Prompt (Optional)

For understanding AI reasoning, use this extended prompt:

```python
debug_prompt = f"""
You are assessing movies. Given the query below and the results, assess which movies are relevant for the query.

Query: {query}

Results:
{collection.prompt_context(result)}

For each movie, provide:
1. Relevance score (0-2): 2=highly relevant, 1=partially relevant, 0=not relevant
2. Brief explanation of your reasoning

Format:
MovieID: Score - Explanation

Example:
tt0076759: 2 - Exact title match for the query
tt0113107: 0 - Contains word "star" but unrelated to Star Wars franchise
"""
```

## Process Summary

1. **Initial Prompt**: Basic prompt with verbose output
2. **Optimization**: Structured format for easier parsing
3. **Testing**: Validated with example query ("star wars")
4. **Refinement**: Added explicit format instructions and examples
5. **Parsing**: Created simple parser for extracting scores

## Next Steps

- Test with all query types (keyword, question, recommendation, descriptive)
- Validate consistency across multiple runs
- Compare AI assessments with human judgments
- Refine prompt based on edge cases
- Automate the assessment pipeline

## Automation Framework

```python
def assess_relevance(query, results, collection):
    """
    Automatically assess relevance of search results using AI
    """
    prompt = f"""
    You are a movie relevance classifier. Rate each movie's relevance to the query.
    
    Query: "{query}"
    
    Movies:
    {collection.prompt_context(results)}
    
    Rate each movie (0-2):
    - 2 = Highly relevant (directly matches query intent)
    - 1 = Partially relevant (somewhat related)
    - 0 = Not relevant (no meaningful match)
    
    RESPOND ONLY WITH:
    MovieID:Score
    
    Your ratings:
    """
    
    response = call_ai_api(prompt)
    scores = parse_relevance_scores(response)
    return scores
```

