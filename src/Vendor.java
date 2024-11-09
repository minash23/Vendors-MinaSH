import java.util.*;

class Vending {
    private String vendorName;
    HashMap<String, Item> stock;
    private HashMap<String, Integer> purchaseHistory;
    private HashMap<String, String> itemDescriptions;
    private HashMap<String, Double> itemDiscounts;
    private Set<String> bestsellers;
    private double balance;

    // Constructor now takes a vendor name
    public Vending(String vendorName, int numCandy, int numGum) {
        this.vendorName = vendorName;
        this.stock = new HashMap<>();
        this.purchaseHistory = new HashMap<>();
        this.itemDescriptions = new HashMap<>();
        this.itemDiscounts = new HashMap<>();
        this.bestsellers = new HashSet<>();
        this.balance = 0;

        // Initialize default items
        stock.put("Candy", new Item(1.25, numCandy));
        stock.put("Gum", new Item(0.5, numGum));

        // Initialize purchase history
        purchaseHistory.put("Candy", 0);
        purchaseHistory.put("Gum", 0);

        // Set default descriptions
        itemDescriptions.put("Candy", "Sweet candy treat");
        itemDescriptions.put("Gum", "Refreshing chewing gum");
    }

    // Getter for vendor name
    public String getVendorName() {
        return vendorName;
    }

    // Restock an item
    public void restock(String itemName, int quantity, double price) {
        if (stock.containsKey(itemName)) {
            Item currentItem = stock.get(itemName);
            stock.put(itemName, new Item(currentItem.price, currentItem.stock + quantity));
        } else {
            stock.put(itemName, new Item(price, quantity));
            purchaseHistory.put(itemName, 0);
        }
    }

    // Rename an item
    public void renameItem(String oldName, String newName) {
        if (stock.containsKey(oldName)) {
            Item item = stock.remove(oldName);
            stock.put(newName, item);

            // Update related mappings
            Integer purchases = purchaseHistory.remove(oldName);
            purchaseHistory.put(newName, purchases);

            String description = itemDescriptions.remove(oldName);
            itemDescriptions.put(newName, description);

            Double discount = itemDiscounts.remove(oldName);
            if (discount != null) {
                itemDiscounts.put(newName, discount);
            }

            if (bestsellers.contains(oldName)) {
                bestsellers.remove(oldName);
                bestsellers.add(newName);
            }
        }
    }

    // Remove an item
    public void removeItem(String itemName) {
        stock.remove(itemName);
        purchaseHistory.remove(itemName);
        itemDescriptions.remove(itemName);
        itemDiscounts.remove(itemName);
        bestsellers.remove(itemName);
    }

    // Set item description
    public void setItemDescription(String itemName, String description) {
        if (stock.containsKey(itemName)) {
            itemDescriptions.put(itemName, description);
        }
    }

    // Get item description
    public String getItemDescription(String itemName) {
        return itemDescriptions.getOrDefault(itemName, "No description available");
    }

    // Set discount for an item (0.0 to 1.0 representing percentage off)
    public void setDiscount(String itemName, double discountPercentage) {
        if (stock.containsKey(itemName) && discountPercentage >= 0.0 && discountPercentage <= 1.0) {
            itemDiscounts.put(itemName, discountPercentage);
        }
    }

    // Mark item as bestseller
    public void markAsBestseller(String itemName) {
        if (stock.containsKey(itemName)) {
            bestsellers.add(itemName);
        }
    }

    // Unmark item as bestseller
    public void unmarkAsBestseller(String itemName) {
        bestsellers.remove(itemName);
    }

    // Get purchase count for an item
    public int getPurchaseCount(String itemName) {
        return purchaseHistory.getOrDefault(itemName, 0);
    }

    // Print inventory
    public void printInventory() {
        System.out.println("Vendor: " + vendorName);
        System.out.println("Current Inventory:");

        for (Map.Entry<String, Item> entry : stock.entrySet()) {
            String itemName = entry.getKey();
            Item item = entry.getValue();
            double actualPrice = getDiscountedPrice(itemName);

            System.out.printf("%s%s - Price: $%.2f%s, Stock: %d, Purchases: %d%n",
                    itemName,
                    bestsellers.contains(itemName) ? " ⭐" : "",
                    actualPrice,
                    itemDiscounts.containsKey(itemName) ? " (On Sale!)" : "",
                    item.stock,
                    purchaseHistory.get(itemName)
            );
        }
    }

    // Helper method to get discounted price
    private double getDiscountedPrice(String itemName) {
        Item item = stock.get(itemName);
        double discount = itemDiscounts.getOrDefault(itemName, 0.0);
        return item.price * (1 - discount);
    }
    public boolean isBestseller(String itemName) {
        return bestsellers.contains(itemName);
    }

    public void resetBalance() {
        this.balance = 0;
    }

    public double getBalance() {
        return this.balance;
    }

    public void addMoney(double amt) {
        this.balance = this.balance + amt;
    }

    public void select(String name) {
        if (stock.containsKey(name)) {
            Item item = stock.get(name);
            double actualPrice = getDiscountedPrice(name);

            if (balance >= actualPrice) {
                item.purchase(1);
                this.balance = this.balance - actualPrice;

                // Update purchase history
                purchaseHistory.put(name, purchaseHistory.get(name) + 1);
            } else {
                System.out.println("Insufficient funds. Required: $" + String.format("%.2f", actualPrice));
            }
        } else {
            System.out.println("Item not available");
        }
    }
}