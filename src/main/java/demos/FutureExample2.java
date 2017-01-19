package demos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A slightly more complex example of the original Java Future in action that highlights some of its pain points: dealing
 * with multiple values and multiple dependencies on those future values.
 */
public class FutureExample2
{
    public static void main(String [] args) throws Exception
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        System.out.println("> Submitting jobs");
        Future<Integer> a = threadPool.submit(() -> Demo.randomIntIntensive(1, 3));
        Future<Integer> b = threadPool.submit(() -> Demo.randomIntIntensive(1, 3));
        Future<Integer> c = threadPool.submit(() -> Demo.randomIntIntensive(1, 3));

        System.out.println("> Converting all 3 jobs to stars");
        String starsA = Demo.starify(a.get());
        String starsB = Demo.starify(b.get());
        String starsC = Demo.starify(c.get());

        System.out.println("> Printing stars");
        Future<?> printA = threadPool.submit(() -> printIntensiveResult("A", starsA));
        Future<?> printB = threadPool.submit(() -> printIntensiveResult("B", starsB));
        Future<?> printC = threadPool.submit(() -> printIntensiveResult("C", starsC));

        // Make sure all 3 print jobs finish
        printA.get();
        printB.get();
        printC.get();
        System.out.println("> All 3 jobs complete. Bye bye!");

        threadPool.shutdown();
    }

    private static void printIntensiveResult(String threadId, String stars)
    {
        // Pretend this says getStarService().save(threadId, stars)
        Demo.sleepQuietly(1000);
        System.out.println(String.format("> Value %s = %s", threadId, stars));
    }
}
