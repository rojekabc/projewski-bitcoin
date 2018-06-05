package pl.projewski.bitcoin.explorer.neoscan;

import org.junit.Test;
import pl.projewski.bitcoin.explorer.api.ExplorerBalance;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class NeoscanExplorerTest {
    // put here an address
    private final static String ADDRESS = "AYZfoMQZjX27z8E3aE8w4TMmcK4knjxt63";

    @Test
    public void test_getBalance() {
        final NeoscanExplorer explorer = new NeoscanExplorer();
        final List<ExplorerBalance> balances =
                explorer.getBalance(ADDRESS);
        assertNotNull(balances);
        assertFalse(balances.isEmpty());
        for (final ExplorerBalance balance : balances) {
            assertNotNull(balance);
            assertNotNull(balance.getAsset());
            final BigDecimal amount = balance.getAmount();
            assertNotNull(amount);
            assertTrue(amount.compareTo(BigDecimal.ZERO) >= 0);
            assertEquals(-1, amount.compareTo(new BigDecimal(200)));
        }
    }
}
