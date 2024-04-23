# Scrabble Game Engine

## Overview

The Scrabble Game Engine is a comprehensive Java application designed to simulate and manage multiplayer Scrabble games. It incorporates networked communication, advanced caching strategies, and efficient search mechanisms to provide a seamless and interactive game experience.

## Table of Contents

- [Scrabble Game Engine](#scrabble-game-engine)
  - [Overview](#overview)
  - [Table of Contents](#table-of-contents)
  - [System Architecture](#system-architecture)
    - [Component Overview](#component-overview)
    - [Design Patterns](#design-patterns)
  - [Setup and Installation](#setup-and-installation)
  - [Usage](#usage)
  - [Best Practices](#best-practices)
  - [Contributing](#contributing)
  - [License](#license)

## System Architecture

### Component Overview

**Core Game Logic**

- **Board**, **Tile**, **Word**: Manage game state, including word placement and scoring.

**Search and Caching**

- **BloomFilter**: Quickly checks potential word presence.
- **CacheManager**, **LRU**, **LFU**: Optimizes word lookup with memory caching strategies.

**Networking**

- **MyServer**: Handles client connections and real-time game sessions.
- **ClientHandler**, **BookScrabbleHandler**: Processes requests and communicates game states.

**Dictionary Management**

- **DictionaryManager**, **Dictionary**: Manages dictionaries, providing word verification and legality checks.

### Design Patterns

- **Singleton**: Ensures a single instance manages dictionary operations.
- **Factory Method**: Creates cache objects dynamically in `CacheManager`.
- **Strategy**: Allows swapping caching algorithms at runtime via `CacheReplacementPolicy`.
- **Facade**: Simplifies dictionary and caching interactions in `DictionaryManager`.
- **Observer**: Updates components on changes like word placements.
- **Decorator**: Enhances functionalities for tiles or words optionally.

## Setup and Installation

1. **Install Java JDK 11+**: Ensure Java is installed on your machine.
2. **Compile the Code**: Use `javac` to compile the source files.
3. **Start the Server**: Execute `java MyServer` to run the server.
4. **Connect Clients**: Use Java network sockets to connect to the server.

## Usage

- **Starting a Game**: First, start the server and then connect through the client application.
- **Playing the Game**: Players place words on the board through their client interfaces. The game validates moves and updates the board accordingly.

## Best Practices

- **Code Style**: Follow Java coding standards for clarity and maintenance.
- **Documentation**: Comment extensively for better readability and maintenance.
- **Testing**: Regularly run tests to ensure stability and performance.

## Contributing

We encourage contributions! Please follow these steps:

1. **Fork the Repository**: Click on the 'Fork' button at the top right of the page.
2. **Create a Feature Branch**: `git checkout -b new-feature`
3. **Make Changes**: Implement your feature or bug fix.
4. **Commit Changes**: `git commit -am 'Add some feature'`
5. **Push to the Branch**: `git push origin new-feature`
6. **Submit a Pull Request**: Open a pull request to the `main` branch of the original repo.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details.
