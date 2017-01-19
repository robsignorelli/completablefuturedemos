package util;

/**
 * General purpose helpers
 */
public class Utils
{
    /**
     * Determines whether or not the given text has any non-whitespace characters.
     * @param text The text to test
     * @return True when the trimmed string has at least 1 character in it
     */
    public static boolean hasValue(String text)
    {
        return (text != null) && (text.trim().length() > 0);
    }

    /**
     * Unwraps an exception back to its original, root cause.
     * @param t The exception to unwrap
     * @return the root cause
     */
    public static Throwable unwrap(Throwable t)
    {
        Throwable root = t;
        while (root.getCause() != null)
            root = root.getCause();

        return root;
    }
}
