package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.MarketConfiguration;

@Data
public class MarketConfigResponse extends BasicResponse {
    MarketConfiguration config;
}
