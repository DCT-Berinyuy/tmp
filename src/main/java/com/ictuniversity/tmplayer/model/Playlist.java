package com.ictuniversity.tmplayer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An ordered, navigable collection of {@link Track}s.
 *
 * <p>Contract owner: Dev B.
 * Signatures are frozen by Phase 0 — Player and TerminalUI are written against them.
 *
 * <p>Navigation is cursor-based: the playlist knows which track is current, and
 * {@link #next()} / {@link #previous()} move that cursor. Shuffle changes the
 * traversal order, never the caller's view of "what is playing now".
 */
public class Playlist {

    private final String name;
    private final List<Track> tracks = new ArrayList<>();

    /** -1 means "no current track" — either the playlist is empty, or the cursor has run past an end. */
    private int cursor = -1;

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Track track) {
        Objects.requireNonNull(track, "track");
        boolean wasEmpty = tracks.isEmpty();
        tracks.add(track);
        if (wasEmpty) {
            cursor = 0;
        }
    }

    public boolean remove(Track track) {
        int index = tracks.indexOf(track);
        if (index < 0) {
            return false;
        }
        tracks.remove(index);
        if (tracks.isEmpty()) {
            cursor = -1;
        } else if (index < cursor) {
            cursor--;
        } else if (cursor >= tracks.size()) {
            cursor = tracks.size() - 1;
        }
        return true;
    }

    /** The track at the cursor, or empty when the playlist has no tracks. */
    public Optional<Track> current() {
        return isValidCursor() ? Optional.of(tracks.get(cursor)) : Optional.empty();
    }

    /** Advances the cursor and returns the new current track, or empty at the end. */
    public Optional<Track> next() {
        if (tracks.isEmpty()) {
            return Optional.empty();
        }
        if (cursor < tracks.size() - 1) {
            cursor++;
            return Optional.of(tracks.get(cursor));
        }
        cursor = tracks.size();
        return Optional.empty();
    }

    /** Moves the cursor back and returns the new current track, or empty at the start. */
    public Optional<Track> previous() {
        if (tracks.isEmpty()) {
            return Optional.empty();
        }
        if (cursor > 0) {
            cursor--;
            return Optional.of(tracks.get(cursor));
        }
        cursor = -1;
        return Optional.empty();
    }

    /** Randomises traversal order. The currently playing track stays current. */
    public void shuffle() {
        Track currentTrack = current().orElse(null);
        Collections.shuffle(tracks);
        if (currentTrack != null) {
            cursor = tracks.indexOf(currentTrack);
        }
    }

    public int size() {
        return tracks.size();
    }

    public boolean isEmpty() {
        return tracks.isEmpty();
    }

    /** Unmodifiable snapshot in current traversal order — callers must not mutate the playlist through it. */
    public List<Track> getTracks() {
        return List.copyOf(tracks);
    }

    private boolean isValidCursor() {
        return cursor >= 0 && cursor < tracks.size();
    }
}
