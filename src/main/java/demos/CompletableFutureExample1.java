package demos;

import java.util.concurrent.CompletableFuture;

/**
 * The "Hello World" of completable future examples
 */
public class CompletableFutureExample1
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("> Submitting work");

        CompletableFuture.supplyAsync(() -> Demo.randomIntIntensive(1, 5))
            .thenAccept(num -> System.out.println("> Value = " + num))
            .join();

        System.out.println("> Job complete. Bye bye!");
    }
}
