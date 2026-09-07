package com.ictuniversity.tmplayer.exception;

/** Thrown when a decoder recognises the format but the audio data is damaged mid-stream. */
public class CorruptedAudioException extends PlaybackException {

    public CorruptedAudioException(String message) {
        super(message);
    }

    public CorruptedAudioException(String message, Throwable cause) {
        super(message, cause);
    }
}
