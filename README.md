[![MIT](https://img.shields.io/github/license/m1ra9e/cli-commander?color=blue)](./LICENSE)

# Cli-commander

Demo CLI application for working with the vehicle database.

## Build

Build requires Java (JDK) 21+ and Apache Maven 3.8+.

```sh
git clone https://github.com/m1ra9e/cli-commander.git cli-commander
cd cli-commander
mvn clean package
```

## Run

```sh
git clone https://github.com/m1ra9e/cli-commander.git cli-commander
cd cli-commander
mvn clean package -DskipTests
cd target
java -jar vehicle-cli-2.3.0.jar -d car_red_d904df,truck_white_e765wer,motorcycle_black_x430cvb
```

For run [released version](https://github.com/m1ra9e/cli-commander/releases) unpack zip-archive and use 

- vehicle-cli.bat on Windows 

```bat
vehicle-cli.bat -d car_red_d904df,truck_white_e765wer,motorcycle_black_x430cvb
```

- vehicle-cli.sh on Linux.

```sh
vehicle-cli.sh -d car_red_d904df,truck_white_e765wer,motorcycle_black_x430cvb
```

> these arguments are taken as an example only : `-d car_red_d904df,truck_white_e765wer,motorcycle_black_x430cvb`

## Test

```sh
git clone https://github.com/m1ra9e/cli-commander.git cli-commander
cd cli-commander
mvn clean test
```

Also available are tools for quick application testing:
- for test run of the application, execute [run.bat](tools/run.bat) on Windows or [run.sh](tools/run.sh) on Linux;
- for run tests, execute [test.bat](tools/test.bat) on Windows or [test.sh](tools/test.sh) on Linux.

## For auto release

```sh
git tag -a v1.0.0 version_commit_hash -m "tag description message"
git tag
git show v1.0.0
git push origin v1.0.0
```

> this argument is taken only as an example : `v1.0.0`

## Changelog

[Changelog information](CHANGELOG.md)

<details>
  <summary>Short version description</summary>

  | version | description                                                                  |
  | ------- | ---------------------------------------------------------------------------- |
  | 2.3.0   | added scripts tools/for_run, improved build, auto release via github actions |
  | 2.2.0   | get name and version from pom, execution time logging, added model usage     |
  | 2.1.0   | added github actions (checks compilation, runs tests, checks build)          |
  | 2.0.0   | added usage of jcommander library, refactoring                               |
  | 1.0.0   | simple base logic of cli application                                         |

</details>

## Plans

- fix bugs
- add new functions
- refactoring
