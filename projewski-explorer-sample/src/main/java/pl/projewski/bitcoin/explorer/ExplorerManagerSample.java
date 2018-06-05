package pl.projewski.bitcoin.explorer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pl.projewski.bitcoin.explorer.api.ExplorerBalance;
import pl.projewski.bitcoin.explorer.api.IExplorer;
import pl.projewski.bitcoin.explorer.manager.ExplorerManager;

import java.util.Arrays;
import java.util.List;

public class ExplorerManagerSample {

    private final static List<Pair<String, String>> ADDRESS_COLLECTION = Arrays.asList(
            new ImmutablePair("neoscan", "AYZfoMQZjX27z8E3aE8w4TMmcK4knjxt63")
    );

    public static void main(final String[] args) {
        final ExplorerManager manager = ExplorerManager.getInstance();
        for (final Pair<String, String> address : ADDRESS_COLLECTION) {
            final IExplorer explorer = manager.findBySymbol(address.getLeft());
            if (explorer == null) {
                System.out.println("Explorer not found: " + address.getLeft());
                System.out.println();
                continue;
            }
            System.out.println("Explorer: " + explorer.getSymbol());
            System.out.println("Address: " + address.getRight());
            System.out.println("Balances:");
            final List<ExplorerBalance> balances = explorer.getBalance(address.getRight());
            for (final ExplorerBalance balance : balances) {
                System.out.println(balance);
            }
            System.out.println();
        }

    }
}
