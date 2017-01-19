package model;

import java.util.Objects;

/**
 * A user from the system
 */
public class User implements BusinessObject
{
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String postalCode;

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

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
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

    public static User create(
        String id,
        String firstName,
        String lastName,
        String address,
        String city,
        String state,
        String postalCode)
    {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setCity(city);
        user.setState(state);
        user.setPostalCode(postalCode);
        return user;
    }
}
