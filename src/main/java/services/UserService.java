package services;

import model.Datastore;
import model.User;

import java.util.concurrent.CompletableFuture;

/**
 * A super simple service that interacts with User data
 */
public class UserService
{
    private Datastore datastore;

    public UserService(Datastore datastore)
    {
        this.datastore = datastore;
    }

    /**
     * Retrieves the user info w/ the given id
     * @param id The target user id
     * @return A future that completes w/ the user
     */
    public CompletableFuture<User> user(String id)
    {
        return datastore.get(User.class, id);
    }
}
