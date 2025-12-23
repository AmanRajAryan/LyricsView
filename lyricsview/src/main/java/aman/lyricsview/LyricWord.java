package aman.lyricsview;

public class LyricWord {
    public long time;
    public String text;
    public float width; // OPTIMIZATION: Store width directly

    public LyricWord(long time, String text) {
        this.time = time;
        this.text = text;
    }
}
