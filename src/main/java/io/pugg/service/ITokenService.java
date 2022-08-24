package io.pugg.service;

import io.pugg.dto.TokenBalanceResp;

public interface ITokenService {
    TokenBalanceResp queryTokenBalance(String address) throws Exception;

    void queryUserNFTBalance(String userAddress);
}
