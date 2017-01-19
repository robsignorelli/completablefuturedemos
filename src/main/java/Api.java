import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Datastore;
import model.Product;
import model.User;
import services.OrderService;
import services.ProductService;
import services.UserService;
import spark.Service;
import util.OrderStatus;
import util.Utils;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * The entry point for our demo API that utilizes CompletableFutures to interact with "remote" services in an
 * asynchronous fashion.
 */
public class Api
{
    public static void main(String[] args) throws Exception
    {
        // Typically we'd set up a dependency injector to do this, but keep it simple for demo purposes
        Service webServer = Service.ignite().port(12345);
        addExceptionHandlers(webServer);

        Datastore datastore = new Datastore();
        UserService users = new UserService(datastore);
        OrderService orders = new OrderService(datastore);
        ProductService products = new ProductService(datastore);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        /*
         * Typically you wouldn't just inline your lambdas for all of your API logic. You'd bundle them out into
         * separate controllers, but I'm trying to minimize code navigation so here you go! Also, we'd generalize
         * the conversion from POJO to JSON elsewhere, but we'll include it manually for the sake of example.
         */

        // Retrieve a single user
        webServer.get("/user/:id", (req, res) -> users.user(req.params(":id"))
            .thenApply(gson::toJson)
            .join());

        // List all orders by the given user
        webServer.get("/user/:id/order", (req, res) -> users.user(req.params(":id"))
            .thenCompose(orders::orderHistory)
            .thenApply(gson::toJson)
            .join());

        // Place an order for the given product by the specified user
        webServer.post("/user/:id/order/product/:pid", (req, res) -> users.user(req.params(":id"))
            .thenCombine(
                products.product(req.params(":pid")),
                orders::placeOrder)
            .thenApply(CompletableFuture::join)
            .thenApply(gson::toJson)
            .join());

        // This is equivalent to the above route in that it generates the same value. It does, however, lose the
        // composition and error handling capabilities of the above case. You'd need try/catch blocks to properly
        // handle errors here whereas above you could add ".exceptionally()" to the chain of calls. But... to many
        // this may be more readable, so it's up to you!
        webServer.post("/user/:id/order/product/:pid/v2", (req, res) -> {
            CompletableFuture<User> user = users.user(req.params(":id"));
            CompletableFuture<Product> product = products.product(req.params(":pid"));

            return orders.placeOrder(user.join(), product.join())
                .thenApply(gson::toJson)
                .join();
        });

        // Look up a specific product
        webServer.get("/product/:id", (req, res) -> products.product(req.params(":id"))
            .thenApply(gson::toJson)
            .join());

        // List all products in the given category
        webServer.get("/product/category/:category", (req, res) -> products.productsByCategory(req.params(":category"))
            .thenApply(gson::toJson)
            .join());

        // Look up a specific order
        webServer.get("/order/:id", (req, res) -> orders.order(req.params(":id"))
            .thenApply(gson::toJson)
            .join());

        // Update the status on a specific order
        webServer.post("/order/:id/status/:status", (req, res) -> {
            OrderStatus status = OrderStatus.valueOf(req.params(":status"));
            return orders.updateOrderStatus(req.params(":id"), status)
                .thenApply(gson::toJson)
                .join();
        });

        System.out.println("[Api] Web server running. Press ENTER to quit.");
        System.in.read();
        System.out.println("[Api] Web server shut down. Bye bye!");
        webServer.stop();
    }

    /**
     * Rather than the HTML that Spark spits out by default, handle errors accordingly.
     * @param webServer The web server config to update
     */
    private static void addExceptionHandlers(Service webServer)
    {
        webServer.exception(Exception.class, (t, req, res) -> {
            Throwable root = Utils.unwrap(t);
            res.type("application/json");

            if (root instanceof NoSuchElementException)
            {
                res.status(404);
                res.body("{ status: 404, message: \"The specified resource ain't there...\"");
            }
            else
            {
                res.status(500);
                res.body("{ status: 500, message: \"Something very bad happened...\"");
            }
        });
    }
}
