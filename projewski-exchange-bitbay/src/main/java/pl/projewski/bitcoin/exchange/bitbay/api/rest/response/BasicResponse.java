package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Error;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Status;

import java.util.List;

@Accessors(chain = true)
@Data
public class BasicResponse {
    private Status status;
    private List<Error> errors;
}
