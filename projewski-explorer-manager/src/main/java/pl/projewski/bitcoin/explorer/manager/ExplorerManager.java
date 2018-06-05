package pl.projewski.bitcoin.explorer.manager;

import lombok.Getter;
import pl.projewski.bitcoin.explorer.api.IExplorer;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ExplorerManager {
    private static ExplorerManager instance;
    @Getter
    List<IExplorer> explorers = new ArrayList<>();

    public static ExplorerManager getInstance() {
        if (instance == null) {
            instance = new ExplorerManager();
        }
        return instance;
    }

    private ExplorerManager() {
        final ServiceLoader<IExplorer> serviceLoader = ServiceLoader.load(IExplorer.class);
        serviceLoader.forEach(explorers::add);
    }

    public IExplorer findBySymbol(String symbol) {
        return explorers.stream().filter(explorer -> explorer.getSymbol().equals(symbol)).findFirst().orElse(null);
    }

}
