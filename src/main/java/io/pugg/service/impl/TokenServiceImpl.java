package io.pugg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.pugg.dto.NFTDataResp;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public List<NFTDataResp> queryUserNFTBalance(String userAddress) {
        String ethereumAPIPreFix = "https://restapi.nftscan.com/api/v2/account/own/all/";
        String bnbChainAPIPreFix = "https://bnbapi.nftscan.com/api/v2/account/own/all/";
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        paramMap.put("erc_type", "erc721");
        headerMap.put("X-API-KEY", configParam.NFTSCAN_API_KEY);
        String ethNftScanResult = HelperUtil.httpClientGet(ethereumAPIPreFix + userAddress, paramMap, headerMap);
        JSONObject jsonObject = JSON.parseObject(ethNftScanResult);
        JSONArray data = jsonObject.getJSONArray("data");

        List<NFTDataResp> nftDataRespList = new ArrayList<>();
        if (data.size() == 0) {
            // if user dont't have ethereum nft
            String bscNftScanResult = HelperUtil.httpClientGet(bnbChainAPIPreFix + userAddress, paramMap, headerMap);
        } else {
            for (int i = 0; i < data.size(); i++) {
                JSONObject oneData = (JSONObject) data.get(i);
                String logoUrl = oneData.getString("logo_url");
                String ownAmount = oneData.getString("owns_total");
                String contractName = oneData.getString("contract_name");
                String tradingPrice = ((JSONObject) oneData.getJSONArray("assets").get(0)).getString("latest_trade_price");
                tradingPrice = tradingPrice == null ? "0" : tradingPrice;
                NFTDataResp nftDataResp = NFTDataResp.builder().contractName(contractName).logoUrl(logoUrl).ownAmount(ownAmount).tradingPrice(tradingPrice).build();
                nftDataRespList.add(nftDataResp);
            }
        }
        return nftDataRespList;
    }
}
