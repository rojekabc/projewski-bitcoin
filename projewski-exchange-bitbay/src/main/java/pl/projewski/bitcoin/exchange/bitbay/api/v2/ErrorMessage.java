package pl.projewski.bitcoin.exchange.bitbay.api.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
	int code;
	String message;
}
