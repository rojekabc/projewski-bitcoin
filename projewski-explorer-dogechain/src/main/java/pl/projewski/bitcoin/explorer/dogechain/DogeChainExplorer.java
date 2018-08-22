package pl.projewski.bitcoin.explorer.dogechain;

import pl.projewski.bitcoin.explorer.api.ExplorerBalance;
import pl.projewski.bitcoin.explorer.api.IExplorer;

import java.util.Collections;
import java.util.List;

public class DogeChainExplorer implements IExplorer {
    private final DogeChainEndpoint endpoint = new DogeChainEndpoint();
    private final static String ASSET_NAME = "DOGE";

    @Override
    public String getSymbol() {
        return "dogechain";
    }

    @Override
    public List<ExplorerBalance> getBalance(final String address) {
        final DogeChainBalance balance = endpoint.getBalance(address);
        return Collections.singletonList(new ExplorerBalance(ASSET_NAME, balance.getBalance()));
    }
}
