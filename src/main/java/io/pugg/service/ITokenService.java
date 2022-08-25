package io.pugg.service;

import io.pugg.dto.NFTDataResp;
import io.pugg.dto.TokenBalanceResp;

import java.util.List;

public interface ITokenService {
    TokenBalanceResp queryTokenBalance(String address) throws Exception;

    List<NFTDataResp> queryUserNFTBalance(String userAddress);
}
