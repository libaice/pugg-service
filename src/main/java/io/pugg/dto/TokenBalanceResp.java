package io.pugg.dto;

import lombok.Data;

@Data
public class TokenBalanceResp {
    private String bnbBalance;
    private String usdtBalance;
}
