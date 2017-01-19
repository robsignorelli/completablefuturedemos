package services;

import model.Datastore;
import model.Order;
import model.Product;
import model.User;
import util.Futures;
import util.OrderStatus;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * A super simple service that places orders and looks up their status.
 */
public class OrderService
{
    private Datastore datastore;

    public OrderService(Datastore datastore)
    {
        this.datastore = datastore;
    }

    /**
     * Retrieves the order w/ the given id
     * @param id The order id
     * @return A future that completes w/ the matching order
     */
    public CompletableFuture<Order> order(String id)
    {
        return datastore.get(Order.class, id);
    }

    /**
     * Places a brand new order for the user
     * @param user The user whose order we're going to place
     * @param product The product they're ordering
     * @return A future that completes with the newly placed/created order details
     */
    public CompletableFuture<Order> placeOrder(User user, Product product)
    {
        return Futures
            .supply(() -> {
                Order order = new Order();
                order.setUser(user);
                order.setProduct(product);
                order.setDateTime(Instant.now());
                order.setStatus(OrderStatus.PLACED);
                return order;
            })
            .thenCompose(this::applySalesTax)
            .thenCompose(datastore::save);
    }

    /**
     * Calculates the required sales tax for the given order and updates it. This will modify the input order. If you
     * wanted to be "purely" functional, you'd make a copy of the order but w/ sales tax, but we don't care here...
     * @param order The order whose tax we're calculating
     * @return A future that resolves with the required sales tax
     */
    private CompletableFuture<Order> applySalesTax(Order order)
    {
        // Yes, I know that 7% is not a universal sales tax but you get the idea...
        return Futures.supply(() -> {
            order.setSalesTax((long)(order.getProduct().getPrice() * 0.07));
            return order;
        });
    }

    /**
     * Moves the status of the order from PICKED to PACKED or something like that.
     * @param id The order to update
     * @param status The new status for the order
     * @return The updated order data
     */
    public CompletableFuture<Order> updateOrderStatus(String id, OrderStatus status)
    {
        return order(id)
            .thenApply(order -> order.status(status))
            .thenCompose(datastore::save);
    }

    /**
     * Retrieves all of the orders placed by the given user
     * @param user The user whose order history you want
     * @return A future that completes with all of the target orders
     */
    public CompletableFuture<List<Order>> orderHistory(User user)
    {
        return datastore.get(Order.class)
            .thenApply(orders -> orders.stream()
                .filter(o -> Objects.equals(o.getUser(), user))
                .sorted(Comparator.comparing(Order::getDateTime))
                .collect(Collectors.toList()));
    }
}
