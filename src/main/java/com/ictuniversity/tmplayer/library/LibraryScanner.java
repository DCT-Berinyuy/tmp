package com.ictuniversity.tmplayer.library;

import com.ictuniversity.tmplayer.model.Track;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Scans a folder for playable audio files and builds one {@link Track} per file found.
 *
 * <p>Contract owner: Dev B. Walks the tree exclusively through {@link Path}, never a raw
 * string, so behavior is identical on Windows and Linux.
 *
 * <p>Duration comes from {@link AudioSystem#getAudioFileFormat(java.io.File)}. WAV support is
 * built into the JDK; MP3 support comes from mp3spi, which registers itself as an
 * {@code AudioFileReader} service provider the moment it is on the classpath — this class
 * never calls mp3spi directly, it just benefits from the provider being present.
 */
public final class LibraryScanner {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of("wav", "mp3");

    /**
     * Recursively scans {@code folder} and returns one {@link Track} per playable file.
     * A file that exists but fails to parse (corrupted, truncated, unreadable) is skipped
     * rather than aborting the whole scan.
     *
     * @throws IOException if {@code folder} itself cannot be walked (missing, not a directory, permissions)
     */
    public List<Track> scan(Path folder) throws IOException {
        if (!Files.isDirectory(folder)) {
            throw new IOException("Not a directory: " + folder);
        }

        List<Track> tracks = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(this::isSupported)
                    .forEach(path -> readTrack(path).ifPresent(tracks::add));
        }
        return tracks;
    }

    private boolean isSupported(Path path) {
        return SUPPORTED_EXTENSIONS.contains(extensionOf(path));
    }

    private String extensionOf(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private Optional<Track> readTrack(Path path) {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(path.toFile());
            long durationMillis = durationOf(fileFormat);
            return Optional.of(new Track(null, null, durationMillis, path));
        } catch (UnsupportedAudioFileException | IOException e) {
            return Optional.empty();
        }
    }

    private long durationOf(AudioFileFormat fileFormat) {
        long frameLength = fileFormat.getFrameLength();
        float frameRate = fileFormat.getFormat().getFrameRate();
        if (frameLength <= 0 || frameRate <= 0) {
            return Track.UNKNOWN_DURATION;
        }
        return Math.round((frameLength / frameRate) * 1000);
    }
}
