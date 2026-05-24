package aman.lyricsview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser {

    public static List<LyricLine> parse(InputStream is) {
        List<LyricLine> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                LyricLine lyricLine = parseLine(line);
                if (lyricLine != null) {
                    lines.add(lyricLine);
                }
            }
            Collections.sort(lines, (l1, l2) -> {
                if (l1.startTime == l2.startTime) {
                    if (l1.isBackground != l2.isBackground) {
                        return l1.isBackground ? 1 : -1;
                    }
                }
                return Long.compare(l1.startTime, l2.startTime);
            });
            calculateEndTimes(lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static LyricLine parseLine(String lineContent) {
        if (lineContent == null || lineContent.trim().isEmpty()) return null;
        String trimmed = lineContent.trim();

        if (trimmed.startsWith("[bg:") && trimmed.endsWith("]")) {
            String content = trimmed.substring(4, trimmed.length() - 1);
            LyricLine bgLine = new LyricLine(-1);
            bgLine.isBackground = true;
            parseTextAndVocals(bgLine, content);
            if (!bgLine.words.isEmpty() && bgLine.words.get(0).time != -1) {
                bgLine.startTime = bgLine.words.get(0).time;
            } else {
                return null; 
            }
            return bgLine;
        }

        Pattern linePattern = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})\\](.*)");
        Matcher lineMatcher = linePattern.matcher(lineContent);

        if (lineMatcher.find()) {
            int min = Integer.parseInt(lineMatcher.group(1));
            int sec = Integer.parseInt(lineMatcher.group(2));
            String msStr = lineMatcher.group(3);
            int ms = Integer.parseInt(msStr) * (msStr.length() == 2 ? 10 : 1);
            long startTime = (min * 60L + sec) * 1000L + ms;
            String text = lineMatcher.group(4);

            LyricLine lyricLine = new LyricLine(startTime);
            if (text != null && text.trim().startsWith("[bg:") && text.trim().endsWith("]")) {
                 lyricLine.isBackground = true;
                 text = text.trim().substring(4, text.trim().length() - 1);
            }
            parseTextAndVocals(lyricLine, text);
            return lyricLine;
        } else {
            LyricLine lyricLine = new LyricLine(-1);
            lyricLine.isWordSynced = false;
            String[] words = lineContent.split(" ");
            for (String w : words) {
                 if(!w.isEmpty()) lyricLine.words.add(new LyricWord(-1, w + " "));
            }
            if (lyricLine.words.isEmpty()) lyricLine.words.add(new LyricWord(-1, lineContent));
            return lyricLine;
        }
    }

    private static void parseTextAndVocals(LyricLine lyricLine, String text) {
        if (text == null) return;
        String workingText = text;
        if (!lyricLine.isBackground) {
            workingText = workingText.replaceAll("(?i)\\[v\\d:\\]", "").trim();
        }
        Pattern wordPattern = Pattern.compile("<(\\d{2,10})>([^<]*)");
        Matcher wordMatcher = wordPattern.matcher(workingText);
        boolean foundWordSync = false;
        while (wordMatcher.find()) {
            foundWordSync = true;
            long time = Long.parseLong(wordMatcher.group(1));
            String wordText = wordMatcher.group(2);
            lyricLine.words.add(new LyricWord(time, wordText));
        }
        if (!foundWordSync) {
            lyricLine.isWordSynced = false;
            lyricLine.words.add(new LyricWord(-1, workingText));
        }
    }

    private static void calculateEndTimes(List<LyricLine> lines) {
        for (int i = 0; i < lines.size(); i++) {
            LyricLine lyricLine = lines.get(i);
            if (lyricLine.startTime == -1) continue;
            LyricLine nextSameType = null;
            for (int j = i + 1; j < lines.size(); j++) {
                if (lines.get(j).isBackground == lyricLine.isBackground && lines.get(j).startTime != -1) {
                    nextSameType = lines.get(j);
                    break;
                }
            }
            if (nextSameType != null) {
                lyricLine.endTime = nextSameType.startTime;
            } else {
                lyricLine.endTime = lyricLine.startTime + 5000;
            }
            if (lyricLine.isWordSynced && !lyricLine.words.isEmpty()) {
                LyricWord lastEntry = lyricLine.words.get(lyricLine.words.size() - 1);
                if (lastEntry.time != -1 && (lastEntry.text == null || lastEntry.text.trim().isEmpty())) {
                    lyricLine.endTime = lastEntry.time;
                    lyricLine.words.remove(lyricLine.words.size() - 1);
                }
            }
        }
    }
}
