package com.ictuniversity.tmplayer.player;

import com.ictuniversity.tmplayer.exception.PlaybackException;
import com.ictuniversity.tmplayer.model.Track;

/**
 * Observer contract: how the Player tells the UI that something changed.
 *
 * <p>Contract owner: Dev C consumes it, Dev A fires it. The Player never knows a UI exists.
 *
 * <p>Threading: callbacks may fire on the audio thread. Implementations must be quick
 * and must not block — do rendering work off this call if it is expensive.
 */
public interface PlaybackListener {

    void onStateChanged(PlaybackState newState);

    void onTrackChanged(Track track);

    /** Progress tick for the now-playing bar. {@code durationMillis} may be {@link Track#UNKNOWN_DURATION}. */
    void onProgress(long positionMillis, long durationMillis);

    /** A failure the UI should surface to the user rather than crash on. */
    void onError(PlaybackException error);
}
