public class Table {
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
    private Order currentOrder;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = false;
        this.currentOrder = null;
    }

    public int getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
    public Order getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }

    @Override
    public String toString() {
        String status = isOccupied ? "Occupied" : "Available";
        return "Table " + tableNumber + " (Seats " + capacity + ") - Status: " + status;
    }
}