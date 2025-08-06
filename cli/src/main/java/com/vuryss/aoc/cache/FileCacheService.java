package com.vuryss.aoc.cache;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class FileCacheService {
    private final Path cacheRootPath;
    private final MessageDigest sha256Digest;

    public FileCacheService() {
        try {
            this.cacheRootPath = CachePathResolver.resolveCacheDirectory();
            this.sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to initialize cache directory", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
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

        if (Files.exists(cacheFile)) {
            try {
                return Optional.of(Files.readString(cacheFile, StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.err.println("Failed to read from cache file " + cacheFile + ": " + e.getMessage());
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    public boolean exists(String key) {
        return Files.exists(getPathForKey(key));
    }

    public void clear() {
        if (!Files.exists(cacheRootPath)) {
            return;
        }

        try (Stream<Path> walk = Files.walk(cacheRootPath)) {
            walk.sorted(Comparator.reverseOrder()).forEach(this::deletePathSafely);
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
        byte[] hashBytes = sha256Digest.digest(key.getBytes(StandardCharsets.UTF_8));
        String hashedKey = HexFormat.of().formatHex(hashBytes);

        return cacheRootPath.resolve(hashedKey);
    }
}