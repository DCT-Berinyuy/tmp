package com.ictuniversity.tmplayer.exception;

import java.nio.file.Path;

/** Thrown when a track's file no longer exists or cannot be read. */
public class TrackNotFoundException extends PlaybackException {

    private final transient Path path;

    public TrackNotFoundException(Path path) {
        super("Track file not found or unreadable: " + path);
        this.path = path;
    }

    public TrackNotFoundException(Path path, Throwable cause) {
        super("Track file not found or unreadable: " + path, cause);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
