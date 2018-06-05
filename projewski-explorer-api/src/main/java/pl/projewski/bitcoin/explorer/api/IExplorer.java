package pl.projewski.bitcoin.explorer.api;

import java.util.List;

/**
 * The interface for explorer of wallet address.
 */
public interface IExplorer {
    /**
     * Get identifying symbol name for explorer.
     *
     * @return symbol name
     */
    String getSymbol();

    /**
     * Get balance from address.
     *
     * @param address address
     * @return the list of balances
     */
    List<ExplorerBalance> getBalance(String address);
}
