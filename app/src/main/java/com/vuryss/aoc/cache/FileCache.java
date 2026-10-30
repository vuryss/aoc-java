package com.vuryss.aoc.cache;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.Optional;
import java.util.stream.Stream;

public class FileCache {
    private final Path cacheRootPath;

    public FileCache() {
        try {
            this.cacheRootPath = CachePathResolver.resolveCacheDirectory();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to initialize cache directory", e);
        }
    }

    public void put(String key, String value) {
        Path cacheFile = getPathForKey(key);

        try {
            Files.writeString(cacheFile, value, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Log error or handle more gracefully
            System.err.println("Failed to write to cache file " + cacheFile + ": " + e.getMessage());
        }
    }

    public Optional<String> get(String key) {
        Path cacheFile = getPathForKey(key);

        try {
            return Optional.of(Files.readString(cacheFile, StandardCharsets.UTF_8));
        } catch (NoSuchFileException e) {
            return Optional.empty();
        } catch (IOException e) {
            System.err.println("Failed to read cache file " + cacheFile + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public void clear() {
        if (!Files.exists(cacheRootPath)) {
            return;
        }

        try (Stream<Path> walk = Files.walk(cacheRootPath)) {
            walk
                .filter(path -> !path.equals(cacheRootPath)) // Do not delete cache root directory itself
                .sorted(Comparator.reverseOrder())
                .forEach(this::deletePathSafely);
        } catch (IOException e) {
            System.err.println("Error walking cache directory for deletion: " + e.getMessage());
        }
    }

    private void deletePathSafely(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("Failed to delete path " + path + ": " + e.getMessage());
        }
    }

    private Path getPathForKey(String key) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hashBytes = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            return cacheRootPath.resolve(HexFormat.of().formatHex(hashBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}

