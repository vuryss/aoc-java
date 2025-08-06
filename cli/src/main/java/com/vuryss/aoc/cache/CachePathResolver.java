package com.vuryss.aoc.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A utility class to resolve the appropriate cache directory based on OS conventions.
 * Follows Windows, macOS, and Linux XDG standards.
 */
public final class CachePathResolver {
    private static final String CACHE_DIRECTORY = "com.vuryss.aoc.cache";

    public static Path resolveCacheDirectory() throws IOException {
        Path baseCachePath = getBaseCachePath();
        Path appCachePath = baseCachePath.resolve(CACHE_DIRECTORY);

        // Ensure the directory exists
        return Files.createDirectories(appCachePath);
    }

    private static Path getBaseCachePath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows: %LOCALAPPDATA%
            String localAppData = System.getenv("LOCALAPPDATA");

            if (localAppData!= null &&!localAppData.isEmpty()) {
                return Paths.get(localAppData);
            }

            // Fallback for older systems or unusual configurations
            return Paths.get(System.getProperty("user.home"), "AppData", "Local");
        }

        if (os.contains("mac")) {
            // macOS: ~/Library/Caches
            return Paths.get(System.getProperty("user.home"), "Library", "Caches");
        }

        // Linux/Unix: XDG Base Directory Specification
        String xdgCacheHome = System.getenv("XDG_CACHE_HOME");

        if (xdgCacheHome!= null &&!xdgCacheHome.isEmpty()) {
            return Paths.get(xdgCacheHome);
        }

        // Default fallback: ~/.cache
        return Paths.get(System.getProperty("user.home"), ".cache");
    }
}