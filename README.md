# Advent of Code solutions in Java

## Connect your AoC account

Copy `.env.dist` file into `.env` file and fill in the value of `session` cookie after you have authenticated in AoC.  

## Current ongoing event execution
`./gradlew run --args="--day=1"`

## Custom event - current or previous
`./gradlew run --args="--year=2015 --day=1"`

## Running tests
`./gradlew run --args="--day=1 --test"`

---
This repo does follow the automation guidelines on the /r/adventofcode community wiki https://www.reddit.com/r/adventofcode/wiki/faqs/automation. Specifically:

- Once inputs are downloaded, they are cached locally
- If you suspect your input is corrupted, you can manually request a fresh copy by deleting the input file in `src/main/resources/inputs` and re-running the program.
- The User-Agent header in com.vuryss.aoc.InputResolver is set to me since I maintain this repo :)