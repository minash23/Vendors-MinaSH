import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VendorTest {
    static Vending vendor1;
    static Vending vendor2;

    @BeforeEach
    public void setUp() {
        // Initialize two vendors for multiple vendor testing
        vendor1 = new Vending("Shop 1", 10, 10);
        vendor2 = new Vending("Shop 2", 5, 5);
    }

    //Test to check if you can add money to vendor
    @Test
    void canAddMoney(){
        double initialBalance = vendor1.getBalance();
        vendor1.addMoney(2.0);
        assertEquals(initialBalance + 2.0, vendor1.getBalance(), "Balance should increase by the amount added");
    }

    //Test to buy item from vendor
    @Test
    void canBuyItem() {
        double initialBalance = vendor1.getBalance();
        int initialCandyStock = (vendor1.stock.get("Candy")).stock;

        vendor1.addMoney(2.0);
        vendor1.select("Candy");

        assertEquals(initialBalance + 2.0 - 1.25, vendor1.getBalance(), "Balance should decrease by the item price");
        assertEquals(initialCandyStock - 1, (vendor1.stock.get("Candy")).stock, "Candy stock should decrease by 1");
    }

    //test to validate you can empty vendor inventory
    @Test
    void canEmptyInventory() {
        // Set the stock to 0 for both items
        (vendor1.stock.get("Candy")).stock = 0;
        (vendor1.stock.get("Gum")).stock = 0;

        // Verify inventory is empty
        assertEquals(0, (vendor1.stock.get("Candy")).stock, "Candy stock should be 0");
        assertEquals(0, ((vendor1.stock.get("Gum")).stock), "Gum stock should be 0");
    }

    // Test restocking functionality
    @Test
    void canRestockExistingItem() {
        vendor1.restock("Candy", 5, 1.25);
        assertEquals(15, vendor1.stock.get("Candy").stock, "Stock should increase by restock amount");
    }

    @Test
    void canAddNewItemThroughRestock() {
        vendor1.restock("Chips", 10, 2.00);
        assertTrue(vendor1.stock.containsKey("Chips"), "New item should be added to inventory");
        assertEquals(10, vendor1.stock.get("Chips").stock, "New item should have correct stock");
    }

    // Test item renaming
    @Test
    void canRenameItem() {
        vendor1.renameItem("Candy", "Sweet");
        assertFalse(vendor1.stock.containsKey("Candy"), "Old name should no longer exist");
        assertTrue(vendor1.stock.containsKey("Sweet"), "New name should exist in inventory");
    }

    // Test multiple vendor functionality
    @Test
    void canManageMultipleVendors() {
        assertNotEquals(vendor1.getVendorName(), vendor2.getVendorName(), "Vendors should have different names");

        vendor1.restock("Chips", 5, 2.00);
        assertFalse(vendor2.stock.containsKey("Chips"), "Item added to vendor1 should not appear in vendor2");
    }

    // Test item removal
    @Test
    void canRemoveItem() {
        vendor1.removeItem("Gum");
        assertFalse(vendor1.stock.containsKey("Gum"), "Removed item should not exist in inventory");
    }

    // Test purchase tracking
    @Test
    void tracksPurchaseHistory() {
        vendor1.addMoney(5.0);
        vendor1.select("Candy");
        vendor1.select("Candy");
        assertEquals(2, vendor1.getPurchaseCount("Candy"), "Purchase count should increment correctly");
    }

    // Test item description
    @Test
    void canManageItemDescriptions() {
        String description = "Delicious chocolate candy";
        vendor1.setItemDescription("Candy", description);
        assertEquals(description, vendor1.getItemDescription("Candy"), "Description should be set and retrieved correctly");
    }

    // Test discount functionality
    @Test
    void canApplyDiscounts() {
        vendor1.setDiscount("Candy", 0.2); // 20% off
        vendor1.addMoney(2.0);
        vendor1.select("Candy");

        // Original price is 1.25, with 20% off should be 1.00
        assertEquals(1.0, vendor1.getBalance(), "Balance should reflect discounted price");
    }

    // Test bestseller marking
    @Test
    void canMarkBestsellers() {
        vendor1.markAsBestseller("Candy");
        vendor1.addMoney(5.0);
        vendor1.select("Candy");
        vendor1.select("Candy");

        // Implementation specific - you might need to add a method to check bestseller status
        assertTrue(vendor1.isBestseller("Candy"), "Item should be marked as bestseller");
    }

    // Test invalid operations
    @Test
    void handleInvalidOperations() {
        // Test restocking invalid amount
        vendor1.restock("Candy", -5, 1.25);
        assertTrue(vendor1.stock.get("Candy").stock >= 0, "Stock should not become negative");

        // Test invalid discount
        vendor1.setDiscount("Candy", 1.5);
        vendor1.addMoney(2.0);
        vendor1.select("Candy");
        assertTrue(vendor1.getBalance() > 0, "Price should not become negative due to discount");
    }

    // Test combined operations
    @Test
    void handlesCombinedOperations() {
        // Add new item
        vendor1.restock("Chips", 5, 2.00);

        // Apply discount
        vendor1.setDiscount("Chips", 0.1);

        // Set description
        vendor1.setItemDescription("Chips", "Crispy potato chips");

        // Mark as bestseller
        vendor1.markAsBestseller("Chips");

        // Make purchase
        vendor1.addMoney(2.0);
        vendor1.select("Chips");

        // Verify all operations worked together
        assertTrue(vendor1.stock.containsKey("Chips"), "Item should exist");
        assertEquals(4, vendor1.stock.get("Chips").stock, "Stock should be decreased");
        assertEquals(1, vendor1.getPurchaseCount("Chips"), "Purchase should be tracked");
        assertEquals("Crispy potato chips", vendor1.getItemDescription("Chips"), "Description should be maintained");
        assertTrue(vendor1.isBestseller("Chips"), "Bestseller status should be maintained");
    }
}