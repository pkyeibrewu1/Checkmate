public class Table {
    private int tableNumber;
    private boolean isOccupied;
    private Order currentOrder;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.isOccupied = false;
        this.currentOrder = null;
    }

    public int getTableNumber() { return tableNumber; }
    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
    
    public Order getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(Order currentOrder) { 
        this.currentOrder = currentOrder; 
        this.isOccupied = (currentOrder != null);
    }

    public boolean isPaid() {
        return currentOrder != null && currentOrder.isPaid();
    }

    @Override
    public String toString() {
        String status = isOccupied ? "Occupied" : "Available";
        return "Table " + tableNumber + " (" + status + ")";
    }
}