package com.ictuniversity.tmplayer.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaylistTest {

    private static Track track(String name) {
        return new Track(name, "Artist", 1000L, Path.of(name + ".mp3"));
    }

    @Test
    void newPlaylistIsEmptyWithNoCurrentTrack() {
        Playlist playlist = new Playlist("Focus");

        assertTrue(playlist.isEmpty());
        assertEquals(0, playlist.size());
        assertEquals(Optional.empty(), playlist.current());
    }

    @Test
    void firstAddBecomesCurrent() {
        Playlist playlist = new Playlist("Focus");
        Track a = track("a");

        playlist.add(a);

        assertEquals(Optional.of(a), playlist.current());
        assertEquals(1, playlist.size());
    }

    @Test
    void nextAdvancesCursorAndStopsAtEnd() {
        Playlist playlist = new Playlist("Focus");
        Track a = track("a");
        Track b = track("b");
        playlist.add(a);
        playlist.add(b);

        assertEquals(Optional.of(b), playlist.next());
        assertEquals(Optional.empty(), playlist.next());
        assertEquals(Optional.empty(), playlist.current());
    }

    @Test
    void previousRetreatsCursorAndStopsAtStart() {
        Playlist playlist = new Playlist("Focus");
        Track a = track("a");
        Track b = track("b");
        playlist.add(a);
        playlist.add(b);
        playlist.next();

        assertEquals(Optional.of(a), playlist.previous());
        assertEquals(Optional.empty(), playlist.previous());
        assertEquals(Optional.empty(), playlist.current());
    }

    @Test
    void removeCurrentTrackShiftsCursorToNextTrack() {
        Playlist playlist = new Playlist("Focus");
        Track a = track("a");
        Track b = track("b");
        playlist.add(a);
        playlist.add(b);

        assertTrue(playlist.remove(a));

        assertEquals(Optional.of(b), playlist.current());
    }

    @Test
    void removeLastTrackWhileCurrentClampsCursor() {
        Playlist playlist = new Playlist("Focus");
        Track a = track("a");
        Track b = track("b");
        playlist.add(a);
        playlist.add(b);
        playlist.next();

        assertTrue(playlist.remove(b));

        assertEquals(Optional.of(a), playlist.current());
    }

    @Test
    void removeUnknownTrackReturnsFalse() {
        Playlist playlist = new Playlist("Focus");
        playlist.add(track("a"));

        assertFalse(playlist.remove(track("missing")));
    }

    @Test
    void shuffleKeepsCurrentTrackCurrent() {
        Playlist playlist = new Playlist("Focus");
        for (int i = 0; i < 20; i++) {
            playlist.add(track("t" + i));
        }
        playlist.next();
        playlist.next();
        playlist.next();
        Track currentBeforeShuffle = playlist.current().orElseThrow();

        playlist.shuffle();

        assertEquals(Optional.of(currentBeforeShuffle), playlist.current());
    }

    @Test
    void getTracksIsAnUnmodifiableSnapshot() {
        Playlist playlist = new Playlist("Focus");
        playlist.add(track("a"));

        List<Track> snapshot = playlist.getTracks();

        assertEquals(1, snapshot.size());
        assertThrowsUnsupported(() -> snapshot.add(track("b")));
    }

    private static void assertThrowsUnsupported(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // expected
        }
    }
}
