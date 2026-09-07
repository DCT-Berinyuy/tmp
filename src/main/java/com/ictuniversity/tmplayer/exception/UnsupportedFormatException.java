package com.ictuniversity.tmplayer.exception;

/** Thrown when a file exists but its audio format has no available decoder. */
public class UnsupportedFormatException extends PlaybackException {

    private final String format;

    public UnsupportedFormatException(String format) {
        super("Unsupported audio format: " + format);
        this.format = format;
    }

    public UnsupportedFormatException(String format, Throwable cause) {
        super("Unsupported audio format: " + format, cause);
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
