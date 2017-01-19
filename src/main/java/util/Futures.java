package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * My standard set of helper functions that cuts down the verbosity of interacting with CompletableFuture instances.
 */
public class Futures
{
    /**
     * A shorthand for creating an already-completed future
     * @param value The value to complete a future w/
     * @return The completed future
     */
    public static <T> CompletableFuture<T> of(T value)
    {
        return CompletableFuture.completedFuture(value);
    }

    /**
     * A safe way to execute a unit of work and capture the return value as a completable future. Any errors thrown
     * during the process are propagated as an "exceptionally" completed future.
     * @param work The work to execute
     * @return A future for the given unit of work
     */
    public static <T> CompletableFuture<T> supply(Supplier<T> work)
    {
        try
        {
            return of(work.get());
        }
        catch (Throwable t)
        {
            return error(t);
        }
    }

    /**
     * A shorthand for creating a future that has already been completed exceptionally.
     * @param t The error to propagate
     * @return The completed future
     */
    public static <T> CompletableFuture<T> error(Throwable t)
    {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(t);
        return future;
    }

    /**
     * The standard <code>CompletableFuture.allOf()</code> returns a future w/ a Void type so you can't do anything
     * with the resulting values from each input future. This "glue" lets you reassemble all of the inputs' results
     * into a single future that contains all of the input results once they're all back.
     * @param futures All of the units of work you want to wait for
     * @param <T> The return type of each input future
     * @return A single future that resolves w/ the results of all input operations when they're done.
     */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> all(CompletableFuture<T>... futures)
    {
        return all(Arrays.asList(futures));
    }

    /**
     * The standard <code>CompletableFuture.allOf()</code> returns a future w/ a Void type so you can't do anything
     * with the resulting values from each input future. This "glue" lets you reassemble all of the inputs' results
     * into a single future that contains all of the input results once they're all back.
     * @param futures All of the units of work you want to wait for
     * @param <T> The return type of each input future
     * @return A single future that resolves w/ the results of all input operations when they're done.
     */
    public static <T> CompletableFuture<List<T>> all(List<CompletableFuture<T>> futures)
    {
        // Thank you Tomasz - http://www.nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
        if (futures == null || futures.isEmpty())
            return CompletableFuture.completedFuture(Collections.emptyList());

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDone.thenApply(v -> futures
            .stream()
            .map(future -> future.join())
            .collect(Collectors.toList()));
    }

    /**
     * The standard <code>CompletableFuture.allOf()</code> returns a future w/ a Void type so you can't do anything
     * with the resulting values from each input future. This "glue" lets you reassemble all of the inputs' results
     * into a single future that contains all of the input results once they're all back.
     * @param futures All of the units of work you want to wait for
     * @param <T> The return type of each input future
     * @return A single future that resolves w/ the results of all input operations when they're done.
     */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> allAsync(Supplier<T>... futures)
    {
        return all(Stream.of(futures)
            .map(CompletableFuture::supplyAsync)
            .collect(Collectors.toList()));
    }

    /**
     * A shorthand workaround for casting the result of <code>CompletableFuture.anyOf()</code> to the original type shared
     * by all of the input futures. This doesn't magically give any type safety that was lost by converting to Object
     * in the first place so don't be an idiot when using this.
     * @param futures The input futures you want to race against each other
     * @return A future that completes when the first input future finishes.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> any(CompletableFuture<T>... futures)
    {
        return CompletableFuture.anyOf(futures)
            .thenApply(value -> (T)value);
    }

    /**
     * The standard <code>CompletableFuture.allOf()</code> returns a future w/ a Void type so you can't do anything
     * with the resulting values from each input future. This "glue" lets you reassemble all of the inputs' results
     * into a single future that contains all of the input results once they're all back.
     * @param futures All of the units of work you want to wait for
     * @param <T> The return type of each input future
     * @return A single future that resolves w/ the results of all input operations when they're done.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> anyAsync(Supplier<T>... futures)
    {
        return CompletableFuture.anyOf(Stream.of(futures)
            .map(CompletableFuture::supplyAsync)
            .collect(Collectors.toList())
            .toArray(new CompletableFuture[futures.length]))
            .thenApply(value -> (T)value);
    }
}
