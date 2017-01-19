package demos;

import java.util.concurrent.CompletableFuture;

/**
 * Shows how you can build a complex pipeline of operations by chaining completable futures.
 */
public class CompletableFutureChaining
{
    public static void main(String [] args) throws Exception
    {
        String result = CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 3))
            .thenApply(Demo::starify)
            .thenApply(stars -> stars + Demo.randomName() + stars)
            .thenApply(Demo::bangBang)
            .thenApply(text -> {
                System.out.println("> Value = " + text);
                return text;
            })
            .exceptionally(t -> null)
            .join();

        if (result != null)
        {
            System.out.println("> Work complete!");
        }
    }
}
