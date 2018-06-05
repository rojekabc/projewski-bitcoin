package pl.projewski.bitcoin.explorer.neoscan;

import lombok.Data;

import java.util.List;

@Data
class NeoscanBalance {
    private List<NeoscanAsset> balance;
    private String address;
}
