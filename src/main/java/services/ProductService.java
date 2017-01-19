package services;

import model.Datastore;
import model.Product;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Manages and interacts with the store's inventory of products to sell.
 */
public class ProductService
{
    private Datastore datastore;

    public ProductService(Datastore datastore)
    {
        this.datastore = datastore;
    }

    /**
     * Retrieves a product from the inventory
     * @param id The id of the product to look up
     * @return A future that completes w/ the matching product
     */
    public CompletableFuture<Product> product(String id)
    {
        return datastore.get(Product.class, id);
    }

    /**
     * Adds a new product to the inventory
     * @param name The name of the product
     * @param category The category that the product belongs to (e.g. "Stuff" or "Food")
     * @param price The price of the product (in cents)
     * @return The newly committed prodct info (with id)
     */
    public CompletableFuture<Product> createProduct(String name, String category, long price)
    {
        return datastore.save(Product.create("", name, category, price));
    }

    /**
     * Lists all products that belong to the given category
     * @param category The product category to scan for
     * @return A future that resolves w/ the matching products
     */
    public CompletableFuture<Set<Product>> productsByCategory(String category)
    {
        return datastore.get(Product.class)
            .thenApply(products -> products.stream()
                .filter(p -> Objects.equals(p.getCategory(), category))
                .collect(Collectors.toSet()));
    }
}
