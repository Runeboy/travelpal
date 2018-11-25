package rune.formatting;

import java.text.MessageFormat;

/**
 * String logic
 */
public class StringExpert {

    // Methods

    public static String format(String str, Object... args) {
        return MessageFormat.format(str, args);
    }
}
