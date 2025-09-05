# Changelog

All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

### Added

### Changed

### Fixed
 
## [3.0.0] - XXXX-XX-XX

### Added

- Added parameter ("-v", "--version").

### Changed

- Refactored.
- Removed unnecessary output in bat-script for run released jar-file.

### Fixed

- Fixed checking of parameter values.

## [2.3.0] - 2025-08-24

### Added

- Added tools-scripts (bat/sh).
- Added scripts (bat/sh) for run released jar-file.
- Added auto release via github actions (on push version tag).

### Changed

- Improved build: added copying files to the special directory and archiving it for release.

## [2.2.0] - 2025-07-31

### Added

- Added getting application name and version from pom-file.
- Added execution time logging.
- Added model usage.

## [2.1.0] - 2025-06-06

### Added

- Added github actions (checking compilation, runing tests, checking build).

## [2.0.0] - 2025-05-23

### Added

- Added usage of JCommander library.
- Added parameter ("-h", "--help").

### Changed

- Renamed parameter ("-s", "--scanner") to ("-i", "--interactive").
- Made refactoring.

## [1.0.0] - 2025-05-19

### Added

- Added simple base logic of cli application.
