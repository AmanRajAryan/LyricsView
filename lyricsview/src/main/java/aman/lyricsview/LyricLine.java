package aman.lyricsview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LyricLine implements Serializable {
    public long startTime;
    public long endTime;
    public List<LyricWord> words = new ArrayList<>();
    public boolean isWordSynced = true;
    public int vocalType = 1;
    public boolean isBackground = false;

    public LyricLine(long startTime) {
        this.startTime = startTime;
    }

    public String getFullText() {
        StringBuilder sb = new StringBuilder();
        for (LyricWord word : words) {
            sb.append(word.text);
        }
        return sb.toString();
    }
}
