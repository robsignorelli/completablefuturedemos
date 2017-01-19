package demos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Shows how you can build a complex pipeline of operations by chaining completable futures.
 */
public class CompletableFutureThreads
{
    public static void main(String [] args) throws Exception
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        // Remember that any stage in the pipeline executes in the same thread as the previous stage UNLESS
        // you constructed that stage using one of the "Async" variants of the "then" methods. You can either supply
        // an executor to grab a thread from or by omitting that argument it will use ForkJoinPool.commonPool()
        System.out.println("> Starting thread: " + Thread.currentThread().getName());
        CompletableFuture
            .supplyAsync(() -> echoThread(1, "Hello World"), threadPool)
            .thenApply(value -> echoThread(2, value))
            .thenApplyAsync(value -> echoThread(3, value))
            .thenApply(value -> echoThread(4, value))
            .thenAcceptAsync(value -> echoThread(5, value), threadPool)
            .join();

        System.out.println("> Work complete!");
        threadPool.shutdown();
    }

    private static String echoThread(int step, String value)
    {
        System.out.println(String.format("> Step %s -> Thread = %s", step, Thread.currentThread().getName()));
        return value;
    }
}
