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
        vendor.addMoney(5.0);
        assertEquals(initialBalance + 5.0, vendor.getBalance(), "Balance should increase by the amount added");
    }


}