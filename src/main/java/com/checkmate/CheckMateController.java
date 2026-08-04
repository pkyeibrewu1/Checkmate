package com.checkmate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

            restaurant.addMenuItem(new MenuItem(101, "Mozzarella Sticks", 7.99));
            restaurant.addMenuItem(new MenuItem(102, "Buffalo Wings", 11.99));
            restaurant.addMenuItem(new MenuItem(103, "Caesar Salad", 8.99));
            restaurant.addMenuItem(new MenuItem(201, "Classic Burger", 12.99));
            restaurant.addMenuItem(new MenuItem(202, "Crispy Fries", 4.50));
            restaurant.addMenuItem(new MenuItem(203, "Ribeye Steak", 26.99));
            restaurant.addMenuItem(new MenuItem(204, "Grilled Salmon", 21.50));
            restaurant.addMenuItem(new MenuItem(301, "Fountain Soda", 2.50));
            restaurant.addMenuItem(new MenuItem(302, "Iced Tea", 3.00));
            restaurant.addMenuItem(new MenuItem(303, "Craft Beer", 6.50));
            restaurant.addMenuItem(new MenuItem(401, "New York Cheesecake", 6.99));
            restaurant.addMenuItem(new MenuItem(402, "Chocolate Lava Cake", 7.50));

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
            return Map.of(
                "success", true,
                "receiptId", receipt.getReceiptId(),
                "amount", receipt.getTotalAmount()
            );
        }
        return Map.of("success", false, "message", "Payment failed or no active order");
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