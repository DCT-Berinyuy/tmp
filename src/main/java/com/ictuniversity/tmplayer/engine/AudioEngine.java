package com.ictuniversity.tmplayer.engine;

import com.ictuniversity.tmplayer.exception.PlaybackException;
import com.ictuniversity.tmplayer.model.Track;

/**
 * Strategy interface for decoding and rendering audio.
 *
 * <p>Contract owner: Dev A. {@code LocalFileEngine} is the Phase 1 implementation;
 * swapping in another engine must require no change anywhere else in the program.
 *
 * <p>Threading: implementations decode on their own background thread. Every method
 * here is called from the control thread and must return promptly — never block on audio.
 */
public interface AudioEngine extends AutoCloseable {

    /**
     * Prepares a track for playback, releasing any previously loaded one.
     * Does not start audio; call {@link #play()} after.
     *
     * @throws com.ictuniversity.tmplayer.exception.TrackNotFoundException if the file is missing or unreadable
     * @throws com.ictuniversity.tmplayer.exception.UnsupportedFormatException if no decoder handles the format
     */
    void load(Track track) throws PlaybackException;

    /** Starts or resumes the loaded track. No-op if already playing. */
    void play() throws PlaybackException;

    /** Pauses without discarding position. No-op if not playing. */
    void pause();

    /** Stops and resets position to zero. */
    void stop();

    /** Jumps to a position. Clamped to the track's bounds by the implementation. */
    void seek(long positionMillis) throws PlaybackException;

    boolean isPlaying();

    /** Elapsed position of the loaded track in milliseconds, or 0 when nothing is loaded. */
    long getPositionMillis();

    /** Total length in ms, or {@link Track#UNKNOWN_DURATION} when the decoder cannot report it. */
    long getDurationMillis();

    /** True when this engine can decode the given track — checked before {@link #load(Track)}. */
    boolean supports(Track track);

    /**
     * Called by the engine's own thread when a track reaches its end.
     * The Player registers itself here to trigger auto-advance.
     */
    void setOnTrackFinished(Runnable callback);

    /** Releases audio device resources. Safe to call more than once. */
    @Override
    void close();
}
