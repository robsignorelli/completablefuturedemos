package model;

import java.util.Objects;

/**
 * A single product from the store's inventory
 */
public class Product implements BusinessObject
{
    private String id;
    private String name;
    private String category;
    private long price;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public long getPrice()
    {
        return price;
    }

    public void setPrice(long price)
    {
        this.price = price;
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

    public static Product create(String id, String name, String category, long price)
    {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        return product;
    }
}
