package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public enum OfferType {
    buy, sell;

    @JsonCreator
    public static OfferType from(String string) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(string);
        }

        for (OfferType offerType : OfferType.values()) {
            if (offerType.name().equals(string.toLowerCase())) {
                return offerType;
            }
        }

        throw new IllegalArgumentException(string);
    }
}
