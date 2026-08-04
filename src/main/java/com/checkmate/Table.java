package com.checkmate;
import java.io.Serializable;

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;

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
    public Order getCurrentOrder() { return currentOrder; }

    public void seatTable() {
        this.isOccupied = true;
    }

    public void clearTable() {
        this.isOccupied = false;
        this.currentOrder = null;
    }

    public void setCurrentOrder(Order currentOrder) { 
        this.currentOrder = currentOrder; 
        if (currentOrder != null) {
            this.isOccupied = true;
        }
    }

    @Override
    public String toString() {
        String status = isOccupied ? "Occupied" : "Available";
        return "Table " + tableNumber + " (" + status + ")";
    }
}