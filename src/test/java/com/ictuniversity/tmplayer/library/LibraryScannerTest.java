package com.ictuniversity.tmplayer.library;

import com.ictuniversity.tmplayer.model.Track;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryScannerTest {

    private final LibraryScanner scanner = new LibraryScanner();

    @Test
    void scanningAMissingFolderThrows() {
        assertThrows(IOException.class, () -> scanner.scan(Path.of("no-such-folder-xyz")));
    }

    @Test
    void findsWavFilesRecursivelyAndReadsDuration(@TempDir Path root) throws IOException {
        Path nested = root.resolve("lofi");
        Files.createDirectory(nested);
        writeSilentWav(nested.resolve("beat.wav"), 500);
        Files.writeString(root.resolve("notes.txt"), "not audio");

        List<Track> tracks = scanner.scan(root);

        assertEquals(1, tracks.size());
        Track track = tracks.get(0);
        assertEquals("wav", track.getFormat());
        assertTrue(track.hasKnownDuration());
        assertEquals(500L, track.getDurationMillis(), 20L);
    }

    @Test
    void skipsFilesThatLookLikeAudioButAreNot(@TempDir Path root) throws IOException {
        Files.writeString(root.resolve("fake.mp3"), "this is not a real mp3 file");

        List<Track> tracks = scanner.scan(root);

        assertEquals(0, tracks.size());
    }

    private static void writeSilentWav(Path target, int durationMillis) throws IOException {
        AudioFormat format = new AudioFormat(44_100f, 16, 1, true, false);
        int frameCount = (int) (format.getFrameRate() * durationMillis / 1000);
        byte[] silence = new byte[frameCount * format.getFrameSize()];

        try (AudioInputStream stream = new AudioInputStream(
                new ByteArrayInputStream(silence), format, frameCount)) {
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, target.toFile());
        }
    }
}
