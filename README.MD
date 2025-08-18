# Enchantio

Modern vanilla-style Minecraft enchantment plugin for Paper servers.

- [Enchantment list](docs/src/content/docs/documentation/enchant-list.md)
- [Documentation](docs/)
- <a href="https://modrinth.com/plugin/enchantio" target="_blank">Download</a>

## Building from Source

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven

### Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Enchantio.git
   cd Enchantio
   ```

2. Build with Maven:
   ```bash
   mvn clean package
   ```

3. The compiled JAR file will be located in the `target` directory:
   ```bash
   target/Enchantio-{version}.jar
   ```

### Development Setup

1. This plugin is designed for Paper servers (Minecraft 1.21.5+)
2. Place the compiled JAR in your Paper server's `plugins` folder
3. Restart your server to load the plugin

### Documentation

The documentation is built using [Astro](https://astro.build/) with the [Starlight](https://starlight.astro.build/) integration. To build and view the documentation locally:

1. Navigate to the docs directory:
   ```bash
   cd docs
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Open your browser and navigate to `http://localhost:4321` to view the documentation

## Contributing

1. Fork the repository
2. Create a new branch for your feature
3. Submit a pull request

## License

See the [LICENSE](LICENSE) file for details.
