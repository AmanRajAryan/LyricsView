# LyricsView

[![Maven Central](https://img.shields.io/maven-central/v/io.github.amanrajaryan/LyricsView.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.amanrajaryan/LyricsView)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A powerful, highly customizable Android library to display synchronized lyrics. It goes beyond standard scrolling by supporting **Word-Level Sync (Karaoke)**, **Dual Vocals (Duets)**, and **Background Vocals** with visual effects.

## ✨ Features

* **Word-Level Synchronization:** Highlights words exactly when they are sung (Karaoke style).
* **Dual Vocals Support:** Distinguish between singers using `v1:` and `v2:` tags with different colors.
* **Background Vocals:** Special rendering (blur/dimming) for `[bg:]` tags.
* **Smooth Animations:** Fluid scrolling and text scaling.
* **Async Loading:** Parses large lyric files on a background thread to prevent UI stutter.
* **Highly Customizable:** Control text size, colors, alignment, vertical bias, and empty states via XML.
* **Touch Gestures:** Scroll manually, fling, and tap lines to seek.

## 📦 Installation

This library is available on **Maven Central**.

Add the dependency to your app-level `build.gradle` file:

**Kotlin DSL (`build.gradle.kts`):**
```kotlin
dependencies {
    implementation("io.github.amanrajaryan:LyricsView:1.0.2")
}
```

**Groovy DSL (`build.gradle`):**
```groovy
dependencies {
    implementation 'io.github.amanrajaryan:LyricsView:1.0.2'
}
```

*(Note: Ensure `mavenCentral()` is included in your repositories block, which is standard in modern Android projects.)*

## 🚀 Usage

### 1. Add to Layout (XML)

```xml
<aman.lyricsview.LyricsView
    android:id="@+id/lyrics_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:lyricsTextSize="22sp"
    app:lyricsActiveTextScale="1.2"
    app:lyricsColorActive="#FFFFFF"
    app:lyricsColorInactive="#80FFFFFF"
    app:lyricsColorSecondaryVocal="#00E5FF"
    app:lyricsGravity="center"
    app:lyricsBiasY="0.5"
    app:lyricsEmptyMessage="No Lyrics Available"
    />
```

### 2. Setup in Code (Java/Kotlin)

```java
LyricsView lyricsView = findViewById(R.id.lyrics_view);

// 1. Load Lyrics (Asynchronous recommended)
// You can pass a String content or an InputStream
String lrcContent = "... load your .lrc file string here ...";
lyricsView.setLyricsAsync(lrcContent);

// 2. Update Time (Call this continuously, e.g., in a Runnable or Handler)
// Pass the current player position in milliseconds
lyricsView.updateTime(mediaPlayer.getCurrentPosition());

// 3. Handle Seek Events (User taps a lyric line)
lyricsView.setSeekListener(timeMs -> {
    mediaPlayer.seekTo((int) timeMs);
});
```

## 🎨 Customization (XML Attributes)

You can customize almost every aspect of the view directly in XML:

| Attribute | Format | Description | Default |
|-----------|--------|-------------|---------|
| **Appearance** | | | |
| `lyricsTextSize` | dimension | Base size of the lyrics text. | 32dp |
| `lyricsActiveTextScale` | float | Scale factor for the current active line (e.g. 1.1 = 10% larger). | 1.1 |
| `lyricsGravity` | enum | `center` (default) or `start` (left-aligned). | center |
| **Colors** | | | |
| `lyricsColorActive` | color | Color of the active singing line. | White |
| `lyricsColorInactive` | color | Color of upcoming lines. | Translucent White |
| `lyricsColorSyncedPast` | color | Color of lines/words already sung. | Faded White |
| `lyricsColorSecondaryVocal` | color | Color for 2nd singer (`v2:` tags). | Cyan |
| **Layout** | | | |
| `lyricsBiasY` | float | Vertical position of active line. 0.5 = Center, 0.2 = Top. | 0.5 |
| `lyricsLineSpacing` | dimension | Space between distinct lyric lines. | 60dp |
| `lyricsWordWrapSpacing` | dimension | Space between wrapped lines of the same lyric. | 10dp |
| `lyricsLayoutPadding` | dimension | Horizontal padding. | 48dp |
| **Effects & Logic** | | | |
| `lyricsBackgroundVocalBlurRadius` | dimension | Blur strength for `[bg:]` lines. | 5dp |
| `lyricsEnableScroll` | boolean | Enable/Disable user touch scrolling. | true |
| `lyricsEmptyMessage` | string | Text to show when no lyrics are loaded. | "No Lyrics" |
| `lyricsEmptyColor` | color | Color of the empty state message. | Inactive Color |

## 📝 Supported LRC Formats

The parser is robust and handles various LRC features:

### 1. Standard Line
```
[00:12.50] This is a standard lyric line
```

### 2. Word-Level Sync (Karaoke)
Use `<mm:ss.xx>` tags for precise word highlighting.
```
[00:15.00] <00:15.10>Hello <00:15.50>World <00:16.00>!
```

### 3. Dual Vocals (Duets)
Use `v1:` and `v2:` prefixes to color lines differently (Primary vs Secondary singer).
```
[00:20.00] v1: Singer One (Uses Active Color)
[00:25.00] v2: Singer Two (Uses Secondary Color)
```

### 4. Background Vocals
Use `[bg:]` to apply blur/dimming effects to backing vocals.
```
[00:30.00] [bg:] (Backing vocals here...)
```

*(Or separate strictly bg lines)*
```
[bg:] <00:30.50> Ooh <00:31.00> Aah
```

## 🤝 Contributing

Pull requests are welcome! Feel free to open issues for bugs or feature requests.

## 📄 License

```
Copyright 2025 Aman Raj Aryan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```