import java.io.Serializable;

public class CartItem implements Serializable {
    private String cd;
    private int quantity;

    public CartItem(String cd, int quantity) {
        this.cd = cd;
        this.quantity = quantity;
    }

    public String getCd() { return cd; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
