package model;

import util.OrderStatus;

import java.time.Instant;
import java.util.Objects;

/**
 * A single order from the status
 */
public class Order implements BusinessObject
{
    private String id;
    private User user;
    private Product product;
    private long salesTax;
    private Instant dateTime;
    private Instant dateTimeDelivered;
    private OrderStatus status;

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public long getSalesTax()
    {
        return salesTax;
    }

    public void setSalesTax(long salesTax)
    {
        this.salesTax = salesTax;
    }

    public Instant getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(Instant dateTime)
    {
        this.dateTime = dateTime;
    }

    public Instant getDateTimeDelivered()
    {
        return dateTimeDelivered;
    }

    public void setDateTimeDelivered(Instant dateTimeDelivered)
    {
        this.dateTimeDelivered = dateTimeDelivered;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(OrderStatus status)
    {
        this.status = status;
    }

    public Order status(OrderStatus status)
    {
        setStatus(status);
        if (status == OrderStatus.DELIVERED)
        {
            setDateTimeDelivered(Instant.now());
        }
        return this;
    }

    @Override
    public int hashCode()
    {
        return (getClass().getSimpleName() + ":" + getId()).hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other != null && other.getClass() == getClass())
        {
            BusinessObject businessObject = (BusinessObject) other;
            return Objects.equals(getId(), businessObject.getId());
        }
        return false;
    }
}
