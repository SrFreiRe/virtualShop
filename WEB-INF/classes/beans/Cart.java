package beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CDItem> items = new ArrayList<>();

    public Cart() {}

    public List<CDItem> getItems() {
        return items;
    }
    public void setItems(List<CDItem> items) {
        this.items = items;
    }
    public void addItem(CDItem item) {
        // If CD with same name/price exists, increment quantity
        for (CDItem cd : items) {
            if (cd.getName().equals(item.getName()) && cd.getPrice() == item.getPrice()) {
                cd.setQuantity(cd.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) items.remove(index);
    }
    public void decrementItem(int index) {
        if (index >= 0 && index < items.size()) {
            CDItem cd = items.get(index);
            if (cd.getQuantity() > 1) {
                cd.setQuantity(cd.getQuantity() - 1);
            } else {
                items.remove(index);
            }
        }
    }
    public double getTotal() {
        double total = 0.0;
        for (CDItem cd : items) {
            total += cd.getSubtotal();
        }
        return total;
    }
    public int getSize() {
        return items.size();
    }
    public void clear() {
        items.clear();
    }
}
