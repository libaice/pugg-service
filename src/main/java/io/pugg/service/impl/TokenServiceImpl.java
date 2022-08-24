package io.pugg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.pugg.dto.TokenBalanceResp;
import io.pugg.service.ITokenService;
import io.pugg.utils.ConfigParam;
import io.pugg.utils.Constant;
import io.pugg.utils.HelperUtil;
import io.pugg.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements ITokenService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ConfigParam configParam;

    @Override
    public TokenBalanceResp queryTokenBalance(String address) throws Exception {
        BigInteger bnbBalance = tokenUtil.getBNBBalance(address);
        BigInteger usdtBalanceOf = tokenUtil.getTokenBalanceOf(address, configParam.BSC_USDT_CONTRACT);
        BigDecimal bnbBalanceResp = new BigDecimal(bnbBalance).divide(Constant.EIGHTEEN_BIT_DECIMALS, 18, BigDecimal.ROUND_HALF_UP);
        BigDecimal usdtBalanceResp = new BigDecimal(usdtBalanceOf).divide(Constant.EIGHTEEN_BIT_DECIMALS, 18, BigDecimal.ROUND_HALF_UP);

        TokenBalanceResp tokenBalanceResp = new TokenBalanceResp();
        tokenBalanceResp.setBnbBalance(bnbBalanceResp.stripTrailingZeros().toPlainString());
        tokenBalanceResp.setUsdtBalance(usdtBalanceResp.stripTrailingZeros().toPlainString());
        return tokenBalanceResp;
    }

    @Override
    public void queryUserNFTBalance(String userAddress) {
        String ethereumAPIPreFix = "https://restapi.nftscan.com/api/v2/account/own/all/";
        String bnbChainAPIPreFix = "https://bnbapi.nftscan.com/api/v2/account/own/all/";
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        paramMap.put("erc_type", "erc721");
        headerMap.put("X-API-KEY", "");
        String ethNftScanResult = HelperUtil.httpClientGet(ethereumAPIPreFix + userAddress, paramMap, headerMap);
        JSONObject jsonObject = JSON.parseObject(ethereumAPIPreFix);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) {
            // if user dont't have ethereum nft
            String bscNftScanResult = HelperUtil.httpClientGet(bnbChainAPIPreFix + userAddress, paramMap, headerMap);
        } else {
            for (int i = 0; i < data.size(); i++) {
                JSONObject oneData = (JSONObject) data.get(i);

            }
        }

    }
}
