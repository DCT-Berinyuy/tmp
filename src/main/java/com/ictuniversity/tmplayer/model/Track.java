package com.ictuniversity.tmplayer.model;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Immutable metadata for one audio file.
 *
 * <p>Contract owner: Dev B. Fully implemented in Phase 0 because it is a value object —
 * every other module reads these accessors, so freezing them now is the point.
 *
 * <p>Paths are always {@link Path}, never {@code String}, so Windows and Linux behave identically.
 */
public final class Track {

    /** Returned by {@link #getDurationMillis()} when duration could not be determined. */
    public static final long UNKNOWN_DURATION = -1L;

    private final String title;
    private final String artist;
    private final long durationMillis;
    private final Path path;

    public Track(String title, String artist, long durationMillis, Path path) {
        this.path = Objects.requireNonNull(path, "path");
        this.title = (title == null || title.isBlank()) ? fileNameOf(path) : title;
        this.artist = (artist == null || artist.isBlank()) ? "Unknown Artist" : artist;
        this.durationMillis = durationMillis < 0 ? UNKNOWN_DURATION : durationMillis;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public Path getPath() {
        return path;
    }

    public boolean hasKnownDuration() {
        return durationMillis != UNKNOWN_DURATION;
    }

    /** Lowercase extension without the dot, or an empty string when there is none. */
    public String getFormat() {
        String name = fileNameOf(path);
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(java.util.Locale.ROOT);
    }

    private static String fileNameOf(Path path) {
        Path name = path.getFileName();
        return name == null ? path.toString() : name.toString();
    }

    /** Identity is the file it points at — two entries for the same file are the same track. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Track track)) {
            return false;
        }
        return path.equals(track.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return artist + " - " + title;
    }
}
