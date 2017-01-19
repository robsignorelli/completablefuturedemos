package demos;

import java.util.concurrent.CompletableFuture;

/**
 * A slightly more complex example of the original Java Future in action that highlights some of its pain points: dealing
 * with multiple values and multiple dependencies on those future values.
 */
public class CompletableFutureExample2
{
    public static void main(String [] args) throws Exception
    {
        System.out.println("> Submitting jobs");
        CompletableFuture.allOf(
            executeUnitOfWork("A"),
            executeUnitOfWork("B"),
            executeUnitOfWork("C"))
            .join();

        System.out.println("> All 3 jobs complete. Bye bye!");
    }

    private static CompletableFuture<Void> executeUnitOfWork(final String threadId)
    {
        return CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5))
            .thenApply(Demo::starify)
            .thenApply(Demo::bangBang)
            .thenApply(text -> String.format("> Value %s = %s", threadId, text))
            .thenAccept(System.out::println);
    }
}
