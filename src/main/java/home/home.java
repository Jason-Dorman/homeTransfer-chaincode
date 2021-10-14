package home;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;

@DataType()
public final class home {

    // neccessary variables defined with property annotation - essentially the
    // neccessary components of the transaction
    @Property()
    private final String id;

    @Property()
    private final String name;

    @Property()
    private final String area;

    @Property()
    private final String owner;

    @Property()
    private final String value;

    // define the get method for each of the variables

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getOwner() {
        return owner;
    }

    public String getValue() {
        return value;
    }

    // define class constructor to initialize variables
    public home(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
            @JsonProperty("area") final String area, @JsonProperty("owner") final String owner,
            @JsonProperty("value") final String value) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.owner = owner;
        this.value = value;
    }

    // method to convert json messages to json format
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        home other = (home) obj;

        return Objects.deepEquals(new String[] { getId(), getName(), getArea(), getOwner(), getValue() },
                new String[] { other.getId(), other.getName(), other.getArea(), other.getOwner(), other.getValue() });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getArea(), getOwner(), getValue());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [id=" + id + ", name=" + name
                + ", area=" + area + ", owner=" + owner + ", value=" + value + "]";
    }

}
