package demos;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Helpers used by our examples to transform strings, generate random values, and all sorts of useless but fun things.
 */
public class Demo
{
    private static final String[] NAMES = { "Rob", "Doug", "John" };

    /**
     * Puts the current thread to sleep for the given amount of time, gobbling up any interrupt exceptions
     * @param millis The number of milliseconds to sleep for
     */
    public static void sleepQuietly(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            /* I said QUIET! */
        }

    }
    /**
     * Generates a random number between the two given bounds
     * @param lower The smallest allowed number (inclusive)
     * @param upper The largest allowed number (inclusive)
     * @return The randomly generated number
     */
    public static int randomInt(int lower, int upper)
    {
        // +1 to make upper inclusive
        return ThreadLocalRandom.current().nextInt(lower, upper + 1);
    }

    /**
     * Generates a random number between the two given bounds in an "intensive" way which really means that we'll
     * sleep for that many seconds in order to simulate taking a long time to generate the value.
     * @param lower The smallest allowed number (inclusive)
     * @param upper The largest allowed number (inclusive)
     * @return The randomly generated number
     */
    public static int randomIntIntensive(int lower, int upper)
    {
        int value = randomInt(lower, upper);
        System.out.println(String.format("(sleeping for %s seconds)", value));
        sleepQuietly(value * 1000);
        return value;
    }

    /**
     * Randomly returns one of the three names we have to choose from
     * @return The randomly selected name
     */
    public static String randomName()
    {
        return NAMES[randomInt(0, NAMES.length - 1)];
    }

    /**
     * Creates a string like "****" that contains as many stars as the input integer. So starify(3) => "***".
     * @param numStars The number of stars
     * @return The star string
     */
    public static String starify(Integer numStars)
    {
        StringBuilder stars = new StringBuilder(numStars);
        for (int i = 0; i < numStars; i++)
            stars.append("*");

        return stars.toString();
    }

    /**
     * Wraps the given string in double-bangs so "foo" becomes "!!foo!!"
     * @param text The text to transform
     * @return The double-banged text
     */
    public static String bangBang(String text)
    {
        return "!!" + text + "!!";
    }
}
