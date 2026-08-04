package com.checkmate;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CheckMateController {

    private final Restaurant restaurant;

    public CheckMateController() {
        Restaurant loaded = Restaurant.loadData();
        if (loaded != null) {
            this.restaurant = loaded;
        } else {
            this.restaurant = new Restaurant("CheckMate Bistro");
            for (int i = 1; i <= 9; i++) {
                restaurant.addTable(new Table(i));
            }
            restaurant.addMenuItem(new MenuItem(101, "Classic Burger", 12.99));
            restaurant.addMenuItem(new MenuItem(102, "Crispy Fries", 4.50));
            restaurant.addMenuItem(new MenuItem(103, "Fountain Soda", 2.50));
            restaurant.addMenuItem(new MenuItem(104, "Ribeye Steak", 26.99));
            restaurant.addMenuItem(new MenuItem(105, "Caesar Salad", 8.99));
            restaurant.saveData();
        }
    }

    @GetMapping("/tables")
    public List<Table> getTables() {
        return restaurant.getTables();
    }

    @GetMapping("/menu")
    public List<MenuItem> getMenu() {
        return restaurant.getMenu();
    }

    @PostMapping("/seat/{tableNumber}")
    public Map<String, Object> seatTable(@PathVariable int tableNumber) {
        boolean success = restaurant.seatTable(tableNumber);
        if (success) {
            restaurant.createOrder(tableNumber);
        }
        return Map.of("success", success);
    }

    @PostMapping("/order/add")
    public Map<String, Object> addItem(@RequestParam int tableNumber, @RequestParam int itemId) {
        MenuItem item = restaurant.findMenuItemById(itemId);
        if (item != null) {
            restaurant.addItemToOrder(tableNumber, item);
            return Map.of("success", true);
        }
        return Map.of("success", false, "message", "Item not found");
    }

    @PostMapping("/pay/{tableNumber}")
    public Map<String, Object> payBill(@PathVariable int tableNumber) {
        Receipt receipt = restaurant.recordPayment(tableNumber);
        if (receipt != null) {
            return Map.of("success", true, "receiptId", receipt.getReceiptId(), "amount", receipt.getTotalAmount());
        }
        return Map.of("success", false, "message", "Payment failed");
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyReceipt(@RequestParam String receiptId) {
        boolean approved = restaurant.verifyReceipt(receiptId);
        return Map.of("approved", approved);
    }

    @PostMapping("/clear/{tableNumber}")
    public Map<String, Object> clearTable(@PathVariable int tableNumber) {
        restaurant.clearTable(tableNumber);
        return Map.of("success", true);
    }
}