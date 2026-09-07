package com.ictuniversity.tmplayer.player;

import com.ictuniversity.tmplayer.exception.PlaybackException;
import com.ictuniversity.tmplayer.model.Playlist;
import com.ictuniversity.tmplayer.model.Track;

import java.util.Optional;

/**
 * The complete public API the TerminalUI is allowed to call. Nothing else is fair game —
 * the UI must never reach past this into an AudioEngine.
 *
 * <p>Contract owner: Dev A. This is an interface so Dev C can build a {@code FakePlayer}
 * against it in Phase 1 and swap in the real one at integration without touching UI code.
 */
public interface Player extends AutoCloseable {

    /** Replaces the queue and stops playback. Does not auto-start. */
    void setPlaylist(Playlist playlist);

    /** Plays the playlist's current track, or resumes if paused. */
    void play() throws PlaybackException;

    void pause();

    void stop();

    /** Advances to the next track and plays it. No-op at the end of the playlist. */
    void next() throws PlaybackException;

    /** Goes back one track and plays it. No-op at the start of the playlist. */
    void previous() throws PlaybackException;

    void seek(long positionMillis) throws PlaybackException;

    PlaybackState getState();

    /** Empty when nothing is loaded. */
    Optional<Track> getCurrentTrack();

    long getPositionMillis();

    /** May be {@link Track#UNKNOWN_DURATION}. */
    long getDurationMillis();

    void addListener(PlaybackListener listener);

    void removeListener(PlaybackListener listener);

    /** Stops playback and releases the engine. Safe to call more than once. */
    @Override
    void close();
}
