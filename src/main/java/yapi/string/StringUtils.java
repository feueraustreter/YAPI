package yapi.string;

import java.util.*;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String formatText(String s) {
        return StringFormatting.formatText(s);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String unformatText(String s) {
        return StringFormatting.unformatText(s);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static boolean contains(String s, char c) {
        return StringFormatting.contains(s, c);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static int occurrences(String s, char c) {
        return StringFormatting.occurrences(s, c);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static boolean contains(String s, char[] chars) {
        return StringFormatting.contains(s, chars);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static int occurrences(String s, char[] chars) {
        return StringFormatting.occurrences(s, chars);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String toHex(byte[] bytes) {
        return StringFormatting.toHex(bytes);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String toHex(byte[] bytes, boolean spaces) {
        return StringFormatting.toHex(bytes, spaces);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static byte[] toBytes(String string) {
        return StringFormatting.toBytes(string);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String toString(byte[] bytes) {
        return StringFormatting.toString(bytes);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String toString(char[] chars) {
        return StringFormatting.toString(chars);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String substring(String s, int i) {
        return StringFormatting.substring(s, i);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static String getLongestCommonPrefix(String s, String t) {
        return StringFormatting.getLongestCommonPrefix(s, t);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringFormatting'")
    public static List<Integer> getOccurrences(String s, String t) {
        return StringFormatting.getOccurrences(s, t);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringCrypto'")
    public static byte[] hash(String s) {
        return StringCrpyting.hash(s);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringCrypto'")
    public static byte[] hash(String s, String type) {
        return StringCrpyting.hash(s, type);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringSplitting'")
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted) {
        return StringSplitting.splitString(string, splitStrings, reviveSplitted, false, false);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringSplitting'")
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast) {
        return StringSplitting.splitString(string, splitStrings, reviveSplitted, addToLast, false);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringSplitting'")
    public static String[] splitString(String string, String[] splitStrings, boolean reviveSplitted, boolean addToLast, boolean splitInStrings) {
        return StringSplitting.splitString(string, splitStrings, reviveSplitted, addToLast, splitInStrings);
    }

    @Deprecated(since = "Version 1.2, now found in 'StringSplitting'")
    public static String merge(String[] strings, String s) {
        return StringSplitting.merge(strings, s);
    }

}