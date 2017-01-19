package demos;

import java.util.concurrent.CompletableFuture;

/**
 * Shows how you can build a complex pipeline of operations by chaining completable futures and combining
 * additional operations that result in completable futures.
 */
public class CompletableFutureCombine
{
    public static void main(String [] args) throws Exception
    {
        // Although one of the "composed" operations is sync and the other is async this code is the exact same!
        CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5))
            .thenApply(Demo::starify)
            // thenCompose is equivalent to thenApply except that it will flatten the resulting CF down to the raw
            // String it generates before passing it along to the "bangify" stage. thenApply would have passed
            // the entire CF<String> to bangify which would result in a compile error.
            .thenCompose(CompletableFutureCombine::buildStarName)
            .thenCompose(CompletableFutureCombine::bangify)
            .thenAccept(text -> System.out.println(String.format("> Value %s", text)))
            .join();

        System.out.println("> Work complete!");
    }

    private static CompletableFuture<String> buildStarName(String stars)
    {
        return CompletableFuture.completedFuture(stars + Demo.randomName() + stars);
    }

    private static CompletableFuture<String> bangify(final String name)
    {
        return CompletableFuture.supplyAsync(() -> Demo.bangBang(name));
    }
}






