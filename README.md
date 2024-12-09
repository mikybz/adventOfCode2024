# adventofcode 2024
To run all directly from intellij, you can run the file _src/DayRunAll.kt_.

# Multi-threaded execution
To run all the solutions from command line multi thread, execute the following command:
```shell
gradlew run --args="mt"
```

This gives output:
```shell
Settings: multiThreadEnabled=true
Running all days
  Day01
    Results: part1Test=11, part1=1506483, part2Test=31, part2=23126924
    Runtime: part1=5ms  part2=9ms
  Day02
    Results: part1Test=2, part1=516, part2Test=4, part2=561
    Runtime: part1=8ms  part2=7ms
  Day03
    Results: part1Test=161, part1=163931492, part2Test=48, part2=76911921
    Runtime: part1=12ms  part2=12ms
  Day04
    Results: part1Test=18, part1=2378, part2Test=9, part2=1796
    Runtime: part1=21ms  part2=26ms
  Day05
    Results: part1Test=143, part1=5948, part2Test=123, part2=3062
    Runtime: part1=34ms  part2=202ms
  Day06
    Results: part1Test=41, part1=4696, part2Test=6, part2=1443
    Runtime: part1=3ms  part2=138ms
  Day07
    Results: part1Test=3749, part1=2314935962622, part2Test=11387, part2=401477450831495
    Runtime: part1=53ms  part2=329ms
  Day08
    Results: part1Test=14, part1=320, part2Test=34, part2=1157
    Runtime: part1=1ms  part2=2ms
  Day09
    Results: part1Test=1928, part1=6301895872542, part2Test=2858, part2=6323761685944
    Runtime: part1=8ms  part2=307ms

Success: The output from all days matched the expected output
Total time: 1236 ms
```


# Single thread execution
To run all the solutions in a single thread, execute the following command:

```shell
gradlew run --args="st"
```
This gives output:
```shell
All args: st
Settings: multiThreadEnabled=false
Running all days
  Day01
    Results: part1Test=11, part1=1506483, part2Test=31, part2=23126924
    Runtime: part1=5ms  part2=10ms
  Day02
    Results: part1Test=2, part1=516, part2Test=4, part2=561
    Runtime: part1=9ms  part2=8ms
  Day03
    Results: part1Test=161, part1=163931492, part2Test=48, part2=76911921
    Runtime: part1=11ms  part2=9ms
  Day04
    Results: part1Test=18, part1=2378, part2Test=9, part2=1796
    Runtime: part1=21ms  part2=26ms
  Day05
    Results: part1Test=143, part1=5948, part2Test=123, part2=3062
    Runtime: part1=33ms  part2=190ms
  Day06
    Results: part1Test=41, part1=4696, part2Test=6, part2=1443
    Runtime: part1=3ms  part2=436ms
  Day07
    Results: part1Test=3749, part1=2314935962622, part2Test=11387, part2=401477450831495
    Runtime: part1=60ms  part2=1436ms
  Day08
    Results: part1Test=14, part1=320, part2Test=34, part2=1157
    Runtime: part1=1ms  part2=1ms
  Day09
    Results: part1Test=1928, part1=6301895872542, part2Test=2858, part2=6323761685944
    Runtime: part1=7ms  part2=300ms

Success: The output from all days matched the expected output
Total time: 2604 ms
```


# Running timed from console

If we time the gradle run command, we also time a lot of gradle overhead.
To avoid this, and not have to create a java line of many thousand characters, we can build the whole application including its dependencies to one file and run it.
Here is how to create one jar without dependencies:
```shell
gradlew fatjar
```
Then  it can be run with:
```shell
 java -jar .\build\libs\adventofcode.jar --args "mt" 
```


Here is how to run it with a timer in windows powershell:
```shell
(Measure-Command { java -jar .\build\libs\adventofcode.jar --args "mt" } ).TotalSeconds
```
On my computer the first 9 days run in 1.5 seconds with multi-threading, and 2.8 seconds with single thread.

## Running single day from console
Here is how to run day 7 with multi-threading:
```shell
java -jar .\build\libs\adventofcode.jar "day07" "mt"
```

## Misc
Alternatively, you can run a specific day's solution from the src folder from Intellij.

The project is configured to use latest version of Kotlin. You can change it in the _build.gradle.kts_ file.
