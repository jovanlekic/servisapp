package servisapp;

public class TableViewData {
    private final String name;
    private final String value;
    private final String quantity;
    private final String price;

    public TableViewData(String name, String value, String quantity, String price) {
        this.name = name;
        this.value = value;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }
}