package com.vuryss.aoc.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class CachePathResolver {
    private static final String APP_ID = "com.vuryss.aoc";

    private CachePathResolver() {}

    public static Path resolveCacheDirectory() throws IOException {
        var path = getBaseCachePath().resolve(APP_ID);

        return Files.createDirectories(path);
    }

    private static Path getBaseCachePath() {
        var os = System.getProperty("os.name", "").toLowerCase();
        var home = Path.of(System.getProperty("user.home"));

        if (os.contains("win")) {
            // Windows: %LOCALAPPDATA%
            var localAppData = System.getenv("LOCALAPPDATA");

            if (localAppData != null && !localAppData.isEmpty()) return Path.of(localAppData);

            // Fallback for older systems or unusual configurations
            return home.resolve("AppData").resolve("Local");
        }

        if (os.contains("mac")) {
            // macOS: ~/Library/Caches
            return home.resolve("Library").resolve("Caches");
        }

        // Linux/Unix: XDG Base Directory Specification
        var xdgCacheHome = System.getenv("XDG_CACHE_HOME");

        if (xdgCacheHome != null && !xdgCacheHome.isEmpty()) {
            var path = Path.of(xdgCacheHome);

            if (path.isAbsolute()) return path;
        }

        // Default fallback: ~/.cache
        return home.resolve(".cache");
    }
}
