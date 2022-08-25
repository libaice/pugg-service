package io.pugg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NFTDataResp {
    private String logoUrl;
    private String ownAmount;
    private String contractName;
    private String tradingPrice;
}
