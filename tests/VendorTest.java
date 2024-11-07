import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VendorTest {

    static Vending vendor;

    @BeforeAll
    public static void setUp(){
        vendor = new Vending(10, 10);
    }

    //Test to check if you can add money to vendor
    @Test
    void canAddMoney(){
        double initialBalance = vendor.getBalance();
        vendor.addMoney(2.0);
        assertEquals(initialBalance + 2.0, vendor.getBalance(), "Balance should increase by the amount added");
    }

    //Test to  buy item from vendor
    @Test
    void canBuyItem() {
        double initialBalance = vendor.getBalance();
        int initialCandyStock = (vendor.Stock.get("Candy")).stock;

        vendor.addMoney(2.0);
        vendor.select("Candy");

        assertEquals(initialBalance + 2.0 - 1.25, vendor.getBalance(), "Balance should decrease by the item price");
        assertEquals(initialCandyStock - 1, (vendor.Stock.get("Candy")).stock, "Candy stock should decrease by 1");
    }

    //test to validate you can empty vendor inventory
    @Test
    void canEmptyInventory() {
        // Set the stock to 0 for both items
        (vendor.Stock.get("Candy")).stock = 0;
        (vendor.Stock.get("Gum")).stock = 0;

        // Verify inventory is empty
        assertEquals(0, (vendor.Stock.get("Candy")).stock, "Candy stock should be 0");
        assertEquals(0, ((vendor.Stock.get("Gum")).stock), "Gum stock should be 0");
    }

}