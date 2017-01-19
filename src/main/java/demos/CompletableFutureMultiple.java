package demos;

import util.Futures;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Examples of how to wrangle multiple
 */
public class CompletableFutureMultiple
{
    public static void main(String [] args) throws Exception
    {
        demoAll();
    }

    private static void demoAll() throws Exception
    {
        // (1) Have no way to get at all 5 of the resulting values; just whether or not they finished
        // (2) Can't pass a collection of futures, only has a varargs version
        CompletableFuture<Void> all = CompletableFuture.allOf(
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)));

        System.out.println("> demoAllBetter: All threads dispatched. Waiting for everything.");
        all.join();
        System.out.println("> demoAllBetter: All threads complete!");
    }

    private static void demoAllBetter() throws Exception
    {
        // (1) Have no way to get at all 5 of the resulting values; just whether or not they finished
        // (2) Can't pass a collection of futures, only has a varargs version
        CompletableFuture<List<Integer>> all = Futures.all(
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3)));

        System.out.println("> demoAllBetter: All threads dispatched. Waiting for everything.");
        all.join().forEach(num -> System.out.println("> demoAllBetter: Value = " + num));
        System.out.println("> demoAllBetter: All threads complete!");
    }

    private static void demoAllBest() throws Exception
    {
        CompletableFuture<List<Integer>> all = Futures.allAsync(
            () -> Demo.randomIntIntensive(1, 3),
            () -> Demo.randomIntIntensive(1, 3),
            () -> Demo.randomIntIntensive(1, 3),
            () -> Demo.randomIntIntensive(1, 3),
            () -> Demo.randomIntIntensive(1, 3));

        System.out.println("> demoAllBest: All threads dispatched. Waiting for everything.");
        all.join().forEach(num -> System.out.println("> demoAllBest: Value = " + num));
        System.out.println("> demoAllBest: All threads complete!");
    }

    private static void demoAny() throws Exception
    {
        CompletableFuture<Object> any = CompletableFuture.anyOf(
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5)),
            CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5)));

        System.out.println("> demoAny: All threads dispatched. Waiting for everything.");
        Integer value = (Integer)any.join();
        System.out.println("> demoAny: All threads complete! Value = " + value);
    }

    private static void demoAnyCleaner() throws Exception
    {
        CompletableFuture<Integer> any = Futures.anyAsync(
            () -> Demo.randomIntIntensive(1, 5),
            () -> Demo.randomIntIntensive(1, 5),
            () -> Demo.randomIntIntensive(1, 5),
            () -> Demo.randomIntIntensive(1, 5),
            () -> Demo.randomIntIntensive(1, 5));

        System.out.println("> demoAnyCleaner: All threads dispatched. Waiting for everything.");
        Integer value = any.join();
        System.out.println("> demoAnyCleaner: All threads complete! Value = " + value);
    }
}
