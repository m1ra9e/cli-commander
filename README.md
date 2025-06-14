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
java -jar vehicle-cli-2.0.0.jar -d car,truck,motorcycle
```

> these arguments are taken as an example only : `-d car,truck,motorcycle`

## Test

```sh
git clone https://github.com/m1ra9e/cli-commander.git cli-commander
cd cli-commander
mvn clean test
```

## Changelog

[Changelog information](CHANGELOG.md)

<details>
  <summary>Short version description</summary>

  | version | description                                    |
  | ------- | ---------------------------------------------- |
  | 2.1.0   | added github actions                           |  
  | 2.0.0   | added usage of jcommander library, refactoring |
  | 1.0.0   | simple base logic of cli application           |

</details>

## Plans

- fix bugs
- add new functions
- refactoring
