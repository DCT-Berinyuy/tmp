package com.ictuniversity.tmplayer.exception;

/**
 * Base checked exception for every playback failure.
 *
 * <p>Checked on purpose: callers must decide what to do when audio fails,
 * rather than letting a track silently die.
 *
 * <p>Contract owner: Dev A. Frozen by Phase 0 — do not add subclasses without
 * flagging the team first.
 */
public class PlaybackException extends Exception {

    public PlaybackException(String message) {
        super(message);
    }

    public PlaybackException(String message, Throwable cause) {
        super(message, cause);
    }
}
