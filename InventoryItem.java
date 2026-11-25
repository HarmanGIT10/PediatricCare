package pediatriccare;

public class InventoryItem {
    private final String medication;
    private final String category;
    private int stockLeft;

    public InventoryItem(String medication, String category, int stockLeft) {
        this.medication = medication;
        this.category = category;
        this.stockLeft = stockLeft;
    }

    public String medication() { return medication; }
    public String category() { return category; }
    public int stockLeft() { return stockLeft; }
    public void setStockLeft(int stockLeft) { this.stockLeft = stockLeft; }
}

