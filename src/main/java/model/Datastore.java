package model;

import util.Futures;
import util.OrderStatus;
import util.Utils;

import java.time.Instant;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * A mock datastore that serves as our "database" of users, products, and orders. I realize this is terrible, but this
 * is not a talk about good data access patterns, so keep it simple :)
 */
public class Datastore
{
    // Pretend that these are the "tables" in our database
    private Set<User> users;
    private Set<Order> orders;
    private Set<Product> products;

    // Used to generate ids for new items.
    private long nextId;

    /**
     * Retrieves a record with the given id
     * @param modelType The type of model (i.e. table) that the record belongs to
     * @param id The id to look up
     * @return A future that resolves w/ the matching record
     */
    public <T extends BusinessObject> CompletableFuture<T> get(Class<T> modelType, String id)
    {
        return get(modelType)
            .thenApply(tableModels -> tableModels.stream()
                .filter(model -> Objects.equals(model.getId(), id))
                .findFirst()
                .get());
    }

    /**
     * Retrieves an iterable set of all instances of the given record type.
     * @param modelType The type of record whose instances you want
     * @return A future that completes w/ the matching records
     */
    @SuppressWarnings("unchecked")
    public <T extends BusinessObject> CompletableFuture<Set<T>> get(Class<T> modelType)
    {
        if (modelType == User.class) return Futures.of((Set<T>)users);
        if (modelType == Order.class) return Futures.of((Set<T>)orders);
        if (modelType == Product.class) return Futures.of((Set<T>)products);

        return Futures.error(new IllegalArgumentException(modelType + " is not a persistent type"));
    }

    /**
     * Saves a new or existing record to the database. If this is a new record, we'll assign a unique id to it before
     * saving it. When the future completes, you'll be able to see the generated id.
     * @param model The record to save
     * @return A future that completes w/ the now-saved record
     */
    @SuppressWarnings("unchecked")
    public <T extends BusinessObject> CompletableFuture<T> save(T model)
    {
        return get(model.getClass())
            .thenApply(tableModels -> {
                assignId(model);
                ((Set<T>)tableModels).add(model);
                return model;
            });
    }

    /**
     * Deletes the record from the datastore... as you would expect this to do.
     * @param modelType The type of record to delete
     * @param id The id of the record to smoke
     * @return A future that completes when the work is complete
     */
    public <T extends BusinessObject> CompletableFuture<Void> delete(Class<T> modelType, String id)
    {
        return get(modelType)
            .thenCompose(tableModels -> tableModels.removeIf(m -> Objects.equals(m.getId(), id))
                ? Futures.of(null)
                : Futures.error(new NoSuchElementException()));
    }

    /**
     * Assign the next incremental id to the given record if it doesn't already have an id.
     * @param model The record to update
     * @return The input model
     */
    private synchronized <T extends BusinessObject> T assignId(T model)
    {
        if (!Utils.hasValue(model.getId()))
        {
            model.setId(String.valueOf(nextId++));
        }
        return model;
    }

    /**
     * Fill our "database" with some testing data
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Datastore()
    {
        // This is higher than the ids we're hard-coding below, so all is cool.
        nextId = 802;

        users = new HashSet<>();
        orders = new HashSet<>();
        products = new HashSet<>();

        users.add(User.create("100", "Jeff", "Lebowski", "123 Main St", "Indianapolis", "IN", "46256"));
        users.add(User.create("101", "Walter", "Sobchak", "8080 Open Port", "Indianapolis", "IN", "46250"));
        users.add(User.create("102", "Jackie", "Treehorn", "94 Beachfront Way", "Malibu", "CA", "90264"));

        products.add(Product.create("500", "Half and Half", "Food", 79));
        products.add(Product.create("501", "Bowling Ball", "Stuff", 12999));
        products.add(Product.create("502", "White Russian", "Food", 749));
        products.add(Product.create("503", "Laundry, The Whites", "Stuff", 499));
        products.add(Product.create("504", "Folgers Coffee", "Food", 859));
        products.add(Product.create("505", "Creedence Clearwater Cassette Tape", "Stuff", 1399));

        Order orderA = new Order();
        orderA.setId("800");
        orderA.setUser(users.stream().filter(u -> u.getId().equals("100")).findFirst().get());
        orderA.setProduct(products.stream().filter(p -> p.getId().equals("500")).findFirst().get());
        orderA.setDateTime(Instant.now());
        orderA.setStatus(OrderStatus.PLACED);
        orders.add(orderA);

        Order orderB = new Order();
        orderB.setId("801");
        orderB.setUser(users.stream().filter(u -> u.getId().equals("100")).findFirst().get());
        orderB.setProduct(products.stream().filter(p -> p.getId().equals("501")).findFirst().get());
        orderB.setDateTime(Instant.now());
        orderB.setStatus(OrderStatus.PLACED);
        orders.add(orderB);
    }
}
