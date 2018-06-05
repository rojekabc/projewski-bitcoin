package pl.projewski.bitcoin.explorer.neoscan;

import lombok.NonNull;
import pl.projewski.bitcoin.explorer.api.ExplorerBalance;
import pl.projewski.bitcoin.explorer.api.IExplorer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NeoscanExplorer implements IExplorer {

    private final NeoscanEndpoint endpoint = new NeoscanEndpoint();

    @Override
    public String getSymbol() {
        return "neoscan";
    }

    @NonNull
    @Override
    public List<ExplorerBalance> getBalance(@NonNull final String address) {
        final List<ExplorerBalance> result = new ArrayList<>();
        final List<NeoscanAsset> balance = endpoint.getBalance(address).getBalance();
        for (final NeoscanAsset asset : balance) {
            final ExplorerBalance explorerBalance = new ExplorerBalance(asset.getAsset(), calculateAmount(asset));
            result.add(explorerBalance);
        }
        return result;
    }

    private BigDecimal calculateAmount(final NeoscanAsset asset) {
        switch (asset.getAsset()) {
        case "GAS":
        case "NEO":
            return asset.getAmount();
        default:
            return asset.getAmount().divide(new BigDecimal(100000000));
        }
    }
}
