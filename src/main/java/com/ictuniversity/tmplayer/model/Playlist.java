package com.ictuniversity.tmplayer.model;

import java.util.List;
import java.util.Optional;

/**
 * An ordered, navigable collection of {@link Track}s.
 *
 * <p>Contract owner: Dev B (Phase 1 implements the bodies).
 * Signatures are frozen by Phase 0 — Player and TerminalUI are written against them.
 *
 * <p>Navigation is cursor-based: the playlist knows which track is current, and
 * {@link #next()} / {@link #previous()} move that cursor. Shuffle changes the
 * traversal order, never the caller's view of "what is playing now".
 */
public class Playlist {

    private final String name;

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Track track) {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    public boolean remove(Track track) {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    /** The track at the cursor, or empty when the playlist has no tracks. */
    public Optional<Track> current() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    /** Advances the cursor and returns the new current track, or empty at the end. */
    public Optional<Track> next() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    /** Moves the cursor back and returns the new current track, or empty at the start. */
    public Optional<Track> previous() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    /** Randomises traversal order. The currently playing track stays current. */
    public void shuffle() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    public int size() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }

    /** Unmodifiable snapshot in current traversal order — callers must not mutate the playlist through it. */
    public List<Track> getTracks() {
        throw new UnsupportedOperationException("Phase 1 — Dev B");
    }
}
