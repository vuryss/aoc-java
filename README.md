# Advent of Code solutions in Java

## Connect your AoC account

Copy `.env.dist` file into `.env` file and fill in the value of `session` cookie after you have authenticated in AoC.  

## Examples how to run the CLI

### Execute single solution with user input - Event 2023, Day 1

***requires configured AoC access in .env file***

```bash
./app -y 2023 -d 1
```

### Execute single solution with test data - Event 2022, Day 13

```bash
./app -y 2022 -d 13 -t
```

### Execute single solution and validate answers - Event 2021, Day 12

***requires configured AoC access in .env file***

```bash
./app -y 2021 -d 12 -v
```

### Execute single solution - invalidate cache (means download input and answers from AoC) - Event 2020, Day 10

***requires configured AoC access in .env file***

```bash
./app -y 2020 -d 10 -c
```

### Execute whole year - Event 2022, with user input

***requires configured AoC access in .env file***

```bash
./app -y 2022
```

### Execute whole year - Event 2021, with test data

```bash
./app -y 2021 -t
```

### Execute whole year - Event 2020, validate answers

***requires configured AoC access in .env file***

```bash
./app -y 2020 -v
```

### Execute whole year - Event 2019, invalidate cache

***requires configured AoC access in .env file***

```bash
./app -y 2019 -c
```

### Execute ALL events - with user input

***requires configured AoC access in .env file***

```bash
./app
```

---
This repo does follow the automation guidelines on the /r/adventofcode community wiki https://www.reddit.com/r/adventofcode/wiki/faqs/automation. Specifically:

- Once inputs are downloaded, they are cached locally
- If you suspect your input is corrupted, you can execute the solution without a cache by adding '-c' flag
- The User-Agent header in com.vuryss.aoc.client.NativeJavaAdventOfCodeClient is set to me since I maintain this repo :)