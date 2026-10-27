# Advent of Code solutions in Java

Run solutions, check built-in examples, and generate new solution classes.

Requires **JDK 25** and **Bash** (if you wanna use shortcut commands). Run the shortcuts from the repository root; they build the application automatically.

For personal inputs, answer validation, and scaffolding, set your Advent of Code `session` cookie from your logged-in browser:

```bash
export SESSION_TOKEN="your-session-cookie"
```

You can also set `SESSION_TOKEN` in a `.env` file at the repository root.

```bash
./solve 2015 1       # Run a day with your puzzle input
./solve 2015 1 -t    # Run its built-in examples (no session needed)
./solve 2015         # Run an entire year
./solve --help      # Show all options
```

Omit both year and day to run all supported years. Inputs are cached automatically.

| Option | Purpose |
| --- | --- |
| `-v`, `--validate` | Check results against your previously submitted answers |
| `-j`, `--json-output` | Print results as JSON |
| `--fresh-data` | Download fresh inputs and answers, ignoring cached values |
| `--no-cache` | Skip cache reads and writes |

Provide your own input for a single day without a session cookie:

```bash
./solve 2015 1 -i < input.txt
./solve 2015 1 -i --input '()())'
```

Generate a solution template, including the puzzle description:

```bash
./scaffold 2025 1
```

This creates `event2025/Day1.java` under `app/src/main/java/com/vuryss/aoc/solutions/`.

For debugging, use `./debug 2015 1 -t` with the same arguments as `solve`. It waits for your IDE's remote JVM debugger to connect to `127.0.0.1:5005`.

Advent of Code is created by **Eric Wastl (Topaz)**. Please respect his [participation and sharing guidance](https://adventofcode.com/2025/about) and the [r/adventofcode automation rules](https://www.reddit.com/r/adventofcode/wiki/faqs/automation/):

- Be respectful, clearly label spoilers, and follow your private leaderboard's rules. Eric discourages using AI to solve the puzzles.
- Share your solution code, but do not publish puzzle text or personal inputs. Scaffolding embeds puzzle descriptions in generated source; remove those descriptions before publishing. Keep your session cookie and `.env` private.
- Cache downloads after the first request. This project caches inputs and known answers; use `--fresh-data` or `--no-cache` only when necessary, such as to fix a corrupted cache.
- Throttle automated requests. This client's requests are spaced five seconds apart within each run. Avoid polling unreleased puzzles or running repeated downloads in parallel.
- Identify automated traffic with a repository URL and the **tool maintainer's** contact details, as [Eric requests](https://www.reddit.com/r/adventofcode/comments/z9dhtd/). This client sends `github.com/vuryss/aoc-java by vuryss@gmail.com` in its `User-Agent`; update that identity if you maintain a fork.
