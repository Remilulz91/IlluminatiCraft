package fr.illuminaticraft.util;

/**
 * Formatage du temps restant, en minutes et secondes.
 */
public class TimeFormat {

    /** 155 -> "2:35", 42 -> "0:42". */
    public static String minutesSeconds(int totalSeconds) {
        int seconds = Math.max(0, totalSeconds);
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}
