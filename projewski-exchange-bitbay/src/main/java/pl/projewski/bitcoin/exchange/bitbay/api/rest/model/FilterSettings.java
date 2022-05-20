package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class FilterSettings {
    List<UUID> balancesId;
    List<String> balanceCurrencies;
    List<WalletType> balanceTypes;
    List<UUID> users;
    String engine;
    long fromTime;
    long toTime;
    boolean absValue;
    BigDecimal fromValue;
    BigDecimal toValue;
    List<String> sort;
    int limit;
    int offset;
    List<OperationType> types;
}
