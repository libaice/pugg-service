package io.pugg.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ConfigParam")
public class ConfigParam {

    @Value("${bsc.web3j.url}")
    public String BSC_WEB3J_URL;

    @Value("${bsc.usdt.contract}")
    public String BSC_USDT_CONTRACT;

    @Value("${nftscan.api.key}")
    public String NFTSCAN_API_KEY;
}
