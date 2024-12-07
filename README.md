# adventofcode 2024

To run all the solutions, execute the following command:

```shell
gradlew run
```
This gives output:
```shell
> Task :run
Running all days
  AdventResults(day=Day01, part1Test=11, part1=1506483, part2Test=31, part2=23126924)
  Execution time1: 4 ms
  Execution time2: 10 ms
  AdventResults(day=Day02, part1Test=2, part1=516, part2Test=4, part2=561)
  Execution time1: 7 ms
  Execution time2: 7 ms
  AdventResults(day=Day03, part1Test=161, part1=163931492, part2Test=48, part2=76911921)
  Execution time1: 12 ms
  Execution time2: 10 ms
  AdventResults(day=Day04, part1Test=18, part1=2378, part2Test=9, part2=1796)
  Execution time1: 69 ms
  Execution time2: 27 ms
  AdventResults(day=Day05, part1Test=143, part1=5948, part2Test=123, part2=3062)
  Execution time1: 32 ms
  Execution time2: 203 ms
  AdventResults(day=Day06, part1Test=41, part1=4696, part2Test=6, part2=1443)
  Execution time1: 2 ms
  Execution time2: 229 ms
  AdventResults(day=Day07, part1Test=3749, part1=2314935962622, part2Test=11387, part2=401477450831495)
  Execution time1: 62 ms
  Execution time2: 329 ms
Done

```

We can see time including gradle, and java startup like this (in windows powershell):
```shell
## For 7 first days
Measure-Command { & gradle run}).TotalSeconds
1.9083536

```


Alternatively, you can run a specific day's solution from the src folder.
The file _DayRunAll.kt_ will run all the solutions for you.

''''

The project is configured to use latest version of Kotlin. You can change it in the _build.gradle.kts_ file.
