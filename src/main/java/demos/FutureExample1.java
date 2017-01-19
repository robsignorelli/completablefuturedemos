package demos;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A simple example that shows how the original Java 5 Future class worked.
 */
public class FutureExample1
{
    public static void main(String[] args) throws Exception
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        System.out.println("> Submitting work");
        Future<Integer> future = threadPool.submit(() -> Demo.randomIntIntensive(1, 5));
        System.out.println("> Future received");
        printResult(future);
        System.out.println("> Job complete. Bye bye!");

        threadPool.shutdown();
    }

    private static void printResult(Future<Integer> future)
    {
        Integer value;
        try
        {
            // This call to .get() blocks until the "intensive" work has completed.
            value = future.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            value = -1;
        }
        System.out.println(String.format("> Value = %s", value));
    }
}
