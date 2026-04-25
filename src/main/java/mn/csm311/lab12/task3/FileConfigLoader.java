package mn.csm311.lab12.task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Энгийн key=value файлын уншигч.
 */
public class FileConfigLoader {

    public AppConfig load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }

        Map<String, String> props = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, props);
            }
        } catch (NoSuchFileException e) {
            throw new ConfigLoadException("config file not found: " + path, e);
        } catch (IOException e) {
            throw new ConfigLoadException("failed to read config: " + path, e);
        }

        return new AppConfig(props);
    }

    private void parseLine(String line, Map<String, String> props) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) return;
        int eq = line.indexOf('=');
        if (eq <= 0) return;
        String key = line.substring(0, eq).trim();
        String value = line.substring(eq + 1).trim();
        props.put(key, value);
    }
}
