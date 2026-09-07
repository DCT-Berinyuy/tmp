package com.ictuniversity.tmplayer.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Phase 0 smoke tests: proves the frozen Track contract behaves and the test harness runs. */
class TrackTest {

    @Test
    void blankTitleFallsBackToFileName() {
        Track track = new Track("  ", null, 1000L, Path.of("music", "lofi-beat.mp3"));

        assertEquals("lofi-beat.mp3", track.getTitle());
        assertEquals("Unknown Artist", track.getArtist());
    }

    @Test
    void negativeDurationIsReportedAsUnknown() {
        Track track = new Track("Song", "Artist", -42L, Path.of("song.wav"));

        assertEquals(Track.UNKNOWN_DURATION, track.getDurationMillis());
        assertFalse(track.hasKnownDuration());
    }

    @Test
    void formatIsLowercasedExtension() {
        assertEquals("mp3", new Track("t", "a", 0L, Path.of("Song.MP3")).getFormat());
        assertEquals("", new Track("t", "a", 0L, Path.of("noextension")).getFormat());
    }

    @Test
    void tracksAreEqualWhenTheyPointAtTheSameFile() {
        Path path = Path.of("music", "song.wav");

        assertTrue(new Track("A", "X", 1L, path).equals(new Track("B", "Y", 2L, path)));
        assertEquals(new Track("A", "X", 1L, path).hashCode(), new Track("B", "Y", 2L, path).hashCode());
    }
}
