# Advent of Code solutions in Java

## Connect your AoC account

Copy `.env.dist` file into `.env` file and fill in the value of `session` cookie after you have authenticated in AoC.  

## Current ongoing event execution
`./gradlew run --args="--day=1"`

## Custom event - current or previous
`./gradlew run --args="--year=2015 --day=1"`

## Running tests
`./gradlew run --args="--day=1 --test"`

## Stupid mistakes/assumptions I did while solving puzzles

### Year 2023

- **Day 15:** Missed one crucial sentence in the puzzle description, but fix was trivial.
- **Day 14:** Repeating cycles do not necessary start from 0
- **Day 13:** Assume that only 2 lines or 2 columns are not enough for the pattern to be mirrored
- **Day 12:** Silent int overflow
- **Day 11:** Off by one error (1 + 1000000 does not equal 1000000 multiplication of the universe)
- **Day 10:** Did not replace the start position with the correct pipe type
- **Day 9:** Did not fully account for the negative numbers
- **Day 8:** Looked at which index we reach the end instead of how many steps we need to reach the end
- **Day 7:** Constructing too many nested if/else for a much simpler logic, missed lots of edge cases
- **Day 6:** 
- **Day 5:** 
- **Day 4:** 
- **Day 3:** 
- **Day 2:** 
- **Day 1:** 

---
This repo does follow the automation guidelines on the /r/adventofcode community wiki https://www.reddit.com/r/adventofcode/wiki/faqs/automation. Specifically:

- Once inputs are downloaded, they are cached locally
- If you suspect your input is corrupted, you can manually request a fresh copy by deleting the input file in `src/main/resources/inputs` and re-running the program.
- The User-Agent header in com.vuryss.aoc.InputResolver is set to me since I maintain this repo :)