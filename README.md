# Terminal Music Player

A cross-platform, ad-free console music player in Java. Plays audio from local storage,
identically on Windows and Linux. No ads, no tracking, no network calls, no account.

Java Programming final exam project — ICT University, Yaoundé.

## Status

**Phase 0 — contracts frozen.** Interfaces and signatures are merged; module implementations
are in progress on feature branches. `main` always compiles.

## Requirements

- JDK 17 or newer
- Maven 3.9+ (bundled with IntelliJ IDEA and the VS Code Java extension pack)

## Build and run

```bash
mvn clean package
java -jar target/terminal-music-player.jar
```

Run tests only:

```bash
mvn test
```

## Architecture

| Type | Responsibility | Owner |
|---|---|---|
| `Track` | Immutable metadata for one audio file | Dev B |
| `Playlist` | Ordered, cursor-based collection of tracks | Dev B |
| `AudioEngine` | Strategy interface — decode and render audio | Dev A |
| `Player` | State machine + the public API the UI may call | Dev A |
| `PlaybackListener` | Observer contract — Player notifies the UI | Dev A fires, Dev C consumes |
| `PlaybackException` | Checked exception hierarchy for playback failures | Dev A |
| `TerminalUI` | Now-playing view, progress bar, controls | Dev C |

**Design patterns:** Strategy (`AudioEngine` implementations are swappable) and
Observer (`PlaybackListener` — the `Player` never knows a UI exists).

**Cross-platform:** file paths are `java.nio.file.Path` everywhere — never hardcoded
path strings. Audio decoding runs on its own thread, separate from terminal redraw.

## Contributing

1. Never commit directly to `main`. One feature branch per module.
2. Every PR needs at least one teammate approval before merge.
3. Keep PRs small — one module or feature at a time.
4. Commit messages are short and imperative: `Add WAV playback to LocalFileEngine`.
5. **Shared interfaces are frozen.** Changing `AudioEngine`, `Player`, `PlaybackListener`,
   `Playlist` or the exception hierarchy requires flagging the other two first — it is
   never a unilateral change.

## Tools

| Tool | Purpose | License |
|---|---|---|
| Java (OpenJDK) | Core language and runtime | Free, open-source |
| `javax.sound.sampled` | WAV playback | Included with the JDK |
| mp3spi + JLayer | MP3 decoding, pure Java | Free, LGPL |
| `java.nio.file` | Cross-platform file scanning | Included with the JDK |
| JUnit 5 | Testing | Free, EPL 2.0 |
