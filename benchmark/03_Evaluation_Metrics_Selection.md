# Task 1.1c: Evaluation Metrics Selection for Movie Search Benchmark

## Use Case Scenario Recap

**Persona**: Alex, a film student researching classic cinema
**Goal**: Find movies by specific directors, time periods, and cinematic techniques
**Query Types**: Keywords, questions, recommendations, and descriptive queries
**Difficulty Levels**: Easy (40%), Medium (40%), Hard (20%)

## Analysis of Evaluation Metrics

### 1. Precision and Recall

**Precision**: Proportion of retrieved movies that are relevant
- Formula: P = (Relevant Retrieved) / (Total Retrieved)
- Strengths: Simple, interpretable, measures result quality
- Limitations: Doesn't consider ranking order, can be misleading with few results

**Recall**: Proportion of relevant movies that were retrieved
- Formula: R = (Relevant Retrieved) / (Total Relevant in Collection)
- Strengths: Measures completeness of retrieval
- Limitations: Requires knowing all relevant items (expensive to compute), doesn't consider ranking

**F1-Score**: Harmonic mean of Precision and Recall
- Formula: F1 = 2 * (P * R) / (P + R)
- Strengths: Balanced view of both metrics
- Limitations: Assumes equal importance of precision and recall

**Appropriateness for Use Case**: 
- ✅ Good for measuring overall retrieval quality
- ⚠️ Limited for recommendation scenarios where ranking matters more
- ⚠️ Recall is expensive to compute (requires full relevance assessment)

### 2. Mean Average Precision (MAP)

**Definition**: Average of Average Precision scores across all queries
- AP = Average of precision values at each relevant document position
- MAP = Mean of AP across all queries

**Strengths**: 
- Considers ranking order (highly relevant items should appear first)
- Single metric combining precision and ranking quality
- Standard in information retrieval research

**Limitations**: 
- Requires binary relevance judgments (relevant/not relevant)
- Doesn't distinguish between highly relevant vs. partially relevant

**Appropriateness for Use Case**: 
- ✅ Excellent for research scenario where finding the best matches first is important
- ✅ Works well with our query types (keywords, questions, recommendations)
- ✅ Standard metric allows comparison with other systems

### 3. Normalized Discounted Cumulative Gain (NDCG)

**Definition**: Measures ranking quality using graded relevance (0-2 scale)
- DCG = Sum of (relevance_score / log2(position + 1)) for all results
- NDCG = DCG / Ideal DCG (normalized to 0-1)

**Strengths**: 
- Handles graded relevance (highly relevant, partially relevant, not relevant)
- Discounts later positions (top results matter more)
- Industry standard for ranking evaluation

**Limitations**: 
- More complex to compute and explain
- Requires graded relevance judgments

**Appropriateness for Use Case**: 
- ✅✅ **IDEAL** - Our relevance assessment uses 3-level scale (0, 1, 2)
- ✅ Perfect for recommendation queries where ranking quality is crucial
- ✅ Matches our use case where film student needs best matches first

### 4. Mean Reciprocal Rank (MRR)

**Definition**: Average of reciprocal rank of first relevant result
- RR = 1 / rank_of_first_relevant
- MRR = Mean of RR across all queries

**Strengths**: 
- Simple and interpretable
- Focuses on finding at least one relevant result quickly
- Good for "finding the answer" scenarios

**Limitations**: 
- Only considers first relevant result
- Doesn't measure overall result quality

**Appropriateness for Use Case**: 
- ⚠️ Less suitable - film student needs multiple relevant results, not just one
- ⚠️ Doesn't capture the research nature of the use case

### 5. Precision@K (P@K)

**Definition**: Precision at top K results (e.g., P@5, P@10)

**Strengths**: 
- Practical metric (users typically only view top results)
- Easy to compute and understand
- Common in industry evaluations

**Limitations**: 
- Doesn't consider ranking within top K
- Fixed K may not suit all query types

**Appropriateness for Use Case**: 
- ✅ Good supplementary metric
- ✅ Practical for measuring what user actually sees
- ⚠️ Should be combined with ranking-aware metrics

## Selected Metrics and Justification

### Primary Metric: **NDCG@10**

**Justification**:
1. **Graded Relevance**: Our relevance assessment uses 3-level scale (0=not relevant, 1=partially relevant, 2=highly relevant), which NDCG handles naturally
2. **Ranking Quality**: Film student needs best matches first - NDCG discounts later positions appropriately
3. **Query Type Compatibility**: Works well for all query types:
   - Keywords: Top results should be exact matches
   - Questions: Best answers should appear first
   - Recommendations: Most similar movies should rank highest
   - Descriptive: Best thematic matches should be prioritized
4. **Research Context**: Film student is doing research - finding the most relevant movies quickly is critical
5. **Industry Standard**: Allows comparison with other retrieval systems

### Secondary Metrics: **MAP** and **P@10**

**MAP Justification**:
- Provides binary relevance perspective (complementary to NDCG)
- Standard in IR research for comparison
- Good for measuring overall retrieval effectiveness

**P@10 Justification**:
- Practical metric measuring what user actually sees
- Easy to interpret and communicate
- Useful for understanding system performance at typical result set size

### Metric Calculation Framework

```
For each query:
  1. Retrieve results using retrieval system
  2. Assess relevance using AI (0, 1, 2 scale)
  3. Calculate:
     - NDCG@10: Using graded relevance scores
     - MAP: Using binary relevance (2=relevant, 0-1=not relevant)
     - P@10: Precision of top 10 results (using binary relevance)

Aggregate across all queries:
  - Mean NDCG@10
  - Mean MAP
  - Mean P@10
```

## Comparison with Alternative Metrics

**Why not Recall?**
- Expensive to compute (requires full collection assessment)
- Less important for research scenario (finding best matches > finding all matches)
- Difficult to assess for large collections

**Why not MRR?**
- Only considers first relevant result
- Film student needs multiple relevant movies, not just one
- Doesn't capture ranking quality of full result set

**Why not F1 alone?**
- Doesn't consider ranking order
- Less suitable for recommendation and ranking scenarios
- Can be misleading with varying result set sizes

## Conclusion

For the film student research use case, **NDCG@10** is the primary metric because it:
- Handles our graded relevance assessments naturally
- Prioritizes ranking quality (critical for research)
- Works across all query types
- Is an industry standard for ranking evaluation

**MAP** and **P@10** serve as complementary metrics providing different perspectives on retrieval quality.

