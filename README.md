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

## System Architecture

### Component Overview

**Core Game Logic**

- **`Board`**, **`Tile`**, **`Word`**: Manage game state, including word placement and scoring.

**Search and Caching**

- **`BloomFilter`**: Quickly checks potential word presence using the bloom filter algorithem. it can determine with full confidence that a word does not exist, and with some confidence that it does.
- **`CacheManager`**, **`LRU`**, **`LFU`**: Optimizes word lookup with memory caching strategies.

**Networking**

- **`MyServer`**: Handles client connections and real-time game sessions.
- **`ClientHandler`**, **`BookScrabbleHandler`**: Processes requests and communicates game states.

**Dictionary Management**

- **`DictionaryManager`**, **`Dictionary`**: Manages dictionaries, providing word verification and legality checks.

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
