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

1. This plugin is designed for Paper servers (Minecraft 1.21.8+)
2. Place the compiled JAR in your Paper server's `plugins` folder
3. Restart your server to load the plugin

## Releases

### Creating a Release

This project uses automated GitHub Actions to build and distribute plugin binaries:

1. **Create a GitHub Release**:
   - Go to the [Releases page](https://github.com/japherwocky/spellbook/releases)
   - Click "Create a new release"
   - Choose a tag version (e.g., `v1.2.3` or `1.2.3`)
   - Add release notes describing changes
   - Click "Publish release"

2. **Automatic Build Process**:
   - GitHub Actions will automatically trigger when the release is created
   - The plugin JAR will be built with the release version
   - The compiled binary will be uploaded to the release as an asset
   - Build artifacts are also available in the Actions tab

3. **Download**:
   - The plugin JAR will be available for download from the release page
   - File format: `Spellbook-{version}.jar`

### Version Format
- Use semantic versioning (e.g., `1.0.0`, `1.2.3`)
- Optionally prefix with `v` (e.g., `v1.0.0`)
- The build system will automatically handle version formatting

## Contributing

1. Fork the repository
2. Create a new branch for your feature
3. Submit a pull request

## License

See the [LICENSE](LICENSE) file for details.
