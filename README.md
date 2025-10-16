# Advent of Code solutions in Java

## Connect your AoC account

Copy `.env.dist` to `.env` and set your Advent of Code session cookie value in `SESSION_TOKEN` (after logging in to AoC). This enables the CLI to download inputs and validate answers. Test runs and stdin mode do not require network access.

## CLI usage

Two scripts are provided at the repository root:
- `./solve` — runs the CLI normally
- `./debug` — runs the CLI in Quarkus dev mode with the JVM suspended, waiting for a debugger to attach (port 5005)

Both scripts accept the same parameters shown below; `debug` only changes how the app starts (for debugging).

### Common examples

```bash
# 1) Run a single day with your AoC input
./solve -y 2023 -d 1

# 2) Run a single day using built‑in test data (no AoC account needed)
./solve -y 2022 -d 13 -t

# 3) Run an entire year with your AoC input
./solve -y 2021
```

Tip: You can also provide input via stdin for a single day:
```bash
echo "<your input>" | ./solve -y 2020 -d 10 -i
```

### Parameters for `solve` and `debug`

Below are the supported parameters. All are optional; omit `--year` and `--day` to run all released events and all days (1–25).

- Year (`-y`, `--year`)
  - Type: integer
  - Required: no
  - Default: all released years (2015..current year; excludes current year if it’s not December)
  - Purpose: Select which event year(s) to run
  - Example: `./solve -y 2023`

- Day (`-d`, `--day`)
  - Type: integer (1..25)
  - Required: no
  - Default: all days (1..25)
  - Purpose: Select which day(s) to run; if year is omitted, runs that day across all released years
  - Example: `./solve -d 5`

- Test mode (`-t`, `--test`)
  - Type: flag (boolean)
  - Required: no
  - Default: false
  - Purpose: Run built-in test cases instead of your AoC input
  - Example: `./solve -y 2022 -d 13 -t`

- Validate (`-v`, `--validate`)
  - Type: flag (boolean)
  - Required: no
  - Default: false
  - Purpose: Validate answers of already completed puzzles using AoC
  - Example: `./solve -y 2021 -d 12 -v`

- Overwrite cache (`-c`, `--overwrite-cache`)
  - Type: flag (boolean)
  - Required: no
  - Default: false
  - Purpose: Ignore local cache and re-download AoC input and answers
  - Example: `./solve -y 2019 -c`

- Read input from stdin (`-i`, `--input-from-stdin`)
  - Type: flag (boolean)
  - Required: no (only valid when a single year and single day are specified)
  - Default: false
  - Purpose: Provide puzzle input via stdin; useful for ad-hoc runs or CI
  - Example: `echo "..." | ./solve -y 2020 -d 10 -i`

### Debugging

Use the `./debug` script with the same parameters as `./solve`:

```bash
./debug -y 2023 -d 1
```

Then attach your IDE’s Remote JVM Debug to localhost:5005.

### Running tests on the API

```bash
./gradlew test --rerun
```

## Scaffolding a solution

Generate a solution class skeleton for a specific year and day:

```bash
./scaffold 2023 1
```

---
This repo follows the automation guidelines on the /r/adventofcode community wiki: https://www.reddit.com/r/adventofcode/wiki/faqs/automation

- Once inputs are downloaded, they are cached locally
- If you suspect your input is corrupted, you can execute the solution without a cache by adding `-c`
- The User-Agent header in `com.vuryss.aoc.client.NativeJavaAdventOfCodeClient` is set to the repository maintainer