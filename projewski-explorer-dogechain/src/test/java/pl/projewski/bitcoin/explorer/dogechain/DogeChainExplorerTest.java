package pl.projewski.bitcoin.explorer.dogechain;

import org.junit.Test;
import pl.projewski.bitcoin.explorer.api.ExplorerBalance;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class DogeChainExplorerTest {
    private final static String SAMPLE_ADDRESS = "DNE83xLH4Meaf1fQixb9RfVJBxSLvGYMzg";
    private final DogeChainExplorer explorer = new DogeChainExplorer();

    @Test
    public void testGetBalance() {
        final List<ExplorerBalance> balance = explorer.getBalance(SAMPLE_ADDRESS);
        assertNotNull(balance);
        assertFalse(balance.isEmpty());
        assertEquals(1, balance.size());
        final ExplorerBalance explorerBalance = balance.get(0);
        assertEquals("DOGE", explorerBalance.getAsset());
        final BigDecimal amount = explorerBalance.getAmount();
        assertNotNull(amount);
        assertTrue(amount.compareTo(BigDecimal.ZERO) >= 0);
    }
}
