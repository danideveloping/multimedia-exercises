# Task 1.1a: Automated Query Generation Process

## Overview
This task focuses on creating a balanced set of queries for the movie collection using generative AI. The goal is to automate query generation rather than crafting them manually.

## Use Case Scenario

**Persona**: Alex, a film student researching classic cinema
**Goal**: Find movies by specific directors, time periods, and cinematic techniques
**Context**: Research-focused use case requiring diverse query types

## Query Generation Prompt

### Prompt Used for LLM:

```
You are helping create realistic search queries for a movie database. 

PERSONA: Alex, a film student researching classic cinema. Wants to find movies by specific directors, time periods, and cinematic techniques. 

MOVIE COLLECTION SAMPLE: 
ID: tt0114709 Title: Toy Story (1995) Summary: . Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences. Genres: [Animation, Comedy, Family] Cast: Tom Hanks as Woody (voice), Wallace Shawn as Rex (voice), Laurie Metcalf as Mrs. Davis (voice), R. Lee Ermey as Sergeant (voice) 

ID: tt0113497 Title: Jumanji (1995) Summary: Roll the dice and unleash the excitement!. When siblings Judy and Peter discover an enchanted board game that opens the door to a magical world, they unwittingly invite Alan -- an adult who's been trapped inside the game for 26 years -- into their living room. Alan's only hope for freedom is to finish the game, which proves risky as all three find themselves running from giant rhinoceroses, evil monkeys and other terrifying creatures. Genres: [Adventure, Fantasy, Family] Cast: Robin Williams as Alan Parrish, Kirsten Dunst as Judy Sheperd, Bonnie Hunt as Sarah Whittle, Bebe Neuwirth as Nora Shepherd 

ID: tt0113277 Title: Heat (1995) Summary: A Los Angeles Crime Saga. Obsessive master thief, Neil McCauley leads a top-notch crew on various insane heists throughout Los Angeles while a mentally unstable detective, Vincent Hanna pursues him without rest. Each man recognizes and respects the ability and the dedication of the other even though they are aware their cat-and-mouse game may end in violence. Genres: [Action, Crime, Drama, Thriller] Cast: Al Pacino as Lt. Vincent Hanna, Robert De Niro as Neil McCauley, Val Kilmer as Chris Shiherlis, Jon Voight as Nate, Tom Sizemore as Michael Cheritto, Diane Venora as Justine Hanna, Ashley Judd as Charlene Shiherlis, Mykelti Williamson as Sergeant Drucker, Hank Azaria as Alan Marciano, Danny Trejo as Trejo, Jeremy Piven as Dr. Bob, Xander Berkeley as Ralph, Philip Ettington as Ellis (uncredited) 

ID: tt0112641 Title: Casino (1995) Summary: No one stays at the top forever.. The life of the gambling paradise – Las Vegas – and its dark mafia underbelly. Genres: [Drama, Crime] Cast: Robert De Niro as Sam 'Ace' Rothstein, Sharon Stone as Ginger McKenna, Joe Pesci as Nicky Santoro, James Woods as Lester Diamond, Kevin Pollak as Phillip Green, Richard Riehle as Charlie Clark 

ID: tt0114388 Title: Sense and Sensibility (1995) Summary: Lose your heart and come to your senses.. Rich Mr. Dashwood dies, leaving his second wife and her daughters poor by the rules of inheritance. Two daughters are the titular opposites. Genres: [Drama, Romance] Cast: Emma Thompson as Elinor Dashwood, Hugh Grant as Edward Ferrars, Tom Wilkinson as Mr. Dashwood 

ID: tt0076759 Title: Star Wars (1977) Summary: A long time ago in a galaxy far, far away.... Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire. Genres: [Adventure, Action, Science Fiction] Cast: Harrison Ford as Han Solo, Carrie Fisher as Princess Leia Organa, Kenny Baker as Artoo-Detoo (R2-D2), James Earl Jones as Voice of Darth Vader (voice), William Hootkins as Red Six (Porkins) 

ID: tt0109830 Title: Forrest Gump (1994) Summary: The world will never be the same, once you've seen it through the eyes of Forrest Gump.. A man with a low IQ has accomplished great things in his life and been present during significant historic events - in each case, far exceeding what anyone imagined he could do. Yet, despite all the things he has attained, his one true love eludes him. 'Forrest Gump' is the story of a man who rose above his challenges, and who proved that determination, courage, and love are more important than ability. Genres: [Comedy, Drama, Romance] Cast: Tom Hanks as Forrest Gump, Mykelti Williamson as Pvt. Benjamin Buford 'Bubba' Blue, Siobhan Fallon as School Bus Driver, Mary Ellen Trainor as Jenny's Babysitter (uncredited) 

ID: tt0120915 Title: Star Wars: Episode I - The Phantom Menace (1999) Summary: Every generation has a legend. Every journey has a first step. Every saga has a beginning.. Anakin Skywalker, a young slave strong with the Force, is discovered on Tatooine. Meanwhile, the evil Sith have returned, enacting their plot for revenge against the Jedi. Genres: [Adventure, Action, Science Fiction] Cast: Liam Neeson as Qui-Gon Jinn, Ewan McGregor as Obi Wan Kenobi, Kenny Baker as R2-D2, Frank Oz as Yoda (voice), Terence Stamp as Chacellor Valorum, Samuel L. Jackson as Mace Windu 

Generate 30 diverse search queries that this persona might use: 

QUERY TYPES (distribute evenly): 
- Keyword queries: 1-3 words (e.g., "heat pacino", "star wars") 
- Questions: Natural language (e.g., "What are classic crime dramas from the 90s?") 
- Recommendations: Similar movies (e.g., "movies like Heat") 
- Descriptive: Plot/theme (e.g., "heist films with ensemble cast") 

DIFFICULTY LEVELS: 
- Easy (40%): Popular movies, direct titles, famous actors 
- Medium (40%): Partial info, common themes, decade/genre 
- Hard (20%): Vague descriptions, obscure cinematography terms 

FREQUENCY (model realistic search patterns): 
- Popular A-list movies: 50% of queries (e.g., Star Wars, Forrest Gump) 
- Mid-tier movies: 30% 
- Niche/art-house films: 20% 

FORMAT each query as: [TYPE] | [DIFFICULTY] | [QUERY TEXT] 

Example: 
KEYWORD | EASY | star wars 
QUESTION | MEDIUM | what are the best crime films from 1995 
SIMILAR | MEDIUM | movies like heat with ensemble cast 
DESCRIPTIVE | HARD | slow-burn character study dramas 

Please generate 30 queries now.
```

## Generated Queries

### Results from LLM:

🎥 30 Diverse Movie Search Queries

**KEYWORD | EASY | star wars**

**KEYWORD | EASY | forrest gump tom hanks**

**KEYWORD | EASY | martin scorsese casino**

**KEYWORD | MEDIUM | classic 90s thrillers**

**KEYWORD | HARD | chiaroscuro lighting crime films**

**QUESTION | EASY | who directed heat starring al pacino**

**QUESTION | EASY | what disney movies were made in 1995**

**QUESTION | MEDIUM | what are the best drama films from the 1990s**

**QUESTION | MEDIUM | which movies feature tom hanks and animation together**

**QUESTION | HARD | which 90s crime films used handheld camera techniques**

**SIMILAR | EASY | movies like forrest gump**

**SIMILAR | EASY | movies like star wars**

**SIMILAR | MEDIUM | films similar to heat about cops and robbers**

**SIMILAR | MEDIUM | movies like sense and sensibility with period romance**

**SIMILAR | HARD | films inspired by scorsese's narrative style**

**DESCRIPTIVE | EASY | space adventure movies from the 70s**

**DESCRIPTIVE | EASY | 90s family adventure movies**

**DESCRIPTIVE | MEDIUM | crime dramas set in las vegas**

**DESCRIPTIVE | MEDIUM | romantic dramas based on classic novels**

**DESCRIPTIVE | HARD | neo-noir films exploring moral duality**

**KEYWORD | EASY | jumanji robin williams**

**QUESTION | MEDIUM | what movies feature rival characters who respect each other**

**SIMILAR | EASY | movies like toy story**

**DESCRIPTIVE | MEDIUM | films showing american history through one character's life**

**QUESTION | HARD | which films from the 90s used non-linear storytelling**

**SIMILAR | MEDIUM | movies like casino about gambling and greed**

**DESCRIPTIVE | EASY | romantic period films directed by ang lee**

**KEYWORD | MEDIUM | de niro pacino collaboration films**

**QUESTION | HARD | what are examples of ensemble heist films with realism**

**DESCRIPTIVE | MEDIUM | family adventure movies mixing fantasy and real world**

## Process Summary

1. **Define Persona and Use Case**: Created specific persona (film student) with clear research goals
2. **Provide Movie Samples**: Included diverse sample movies from the collection to give context
3. **Specify Query Requirements**: 
   - Query types (keyword, question, similar, descriptive)
   - Difficulty distribution (40% easy, 40% medium, 20% hard)
   - Frequency modeling (50% popular, 30% mid-tier, 20% niche)
4. **Generate Queries**: Used LLM to generate 30 diverse queries following the specifications
5. **Validate Approach**: Manually reviewed generated queries to ensure they match the persona and requirements

## Key Considerations

- **Query Diversity**: Ensures balanced representation across query types
- **Difficulty Variation**: Models realistic search patterns with varying complexity
- **Frequency Modeling**: Reflects that popular movies are searched more often
- **Persona Alignment**: All queries match the film student research context

## Next Steps

- Validate queries against the actual collection
- Refine prompt based on query quality
- Generate larger query set (100-300 queries) for full benchmark
- Test queries with retrieval system to ensure they produce results

