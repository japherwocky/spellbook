# Spellbook

![Build](https://github.com/japherwocky/spellbook/workflows/Build/badge.svg)

Modern vanilla-style Minecraft enchantment plugin for Paper servers.

- [Enchantment list](docs/ENCHANTMENTS.md)
- <a href="https://modrinth.com/plugin/spellbook" target="_blank">Download</a>

## Building from Source

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven

### Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/japherwocky/spellbook.git
   cd spellbook
   ```

2. Build with Maven:
   ```bash
   mvn clean package
   ```

3. The compiled JAR file will be located in the `target` directory:
   ```bash
   target/Spellbook-{version}.jar
   ```

### Development Setup

1. This plugin is designed for Paper servers (Minecraft 1.21.5+)
2. Place the compiled JAR in your Paper server's `plugins` folder
3. Restart your server to load the plugin

## Contributing

1. Fork the repository
2. Create a new branch for your feature
3. Submit a pull request

## License

See the [LICENSE](LICENSE) file for details.
