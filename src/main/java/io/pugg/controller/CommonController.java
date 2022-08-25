package io.pugg.controller;


import io.pugg.dto.NFTDataResp;
import io.pugg.dto.TokenBalanceResp;
import io.pugg.service.ITokenService;
import io.pugg.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/token")
@CrossOrigin
public class CommonController {
    private static final String OK = "OK";

    @Autowired
    private ITokenService tokenService;

    @GetMapping("/check-health")
    public String checkHealth() {
        return OK;
    }

    @GetMapping("/balance/{address}")
    public Result queryTokenBalance(@PathVariable String address) throws Exception {
        String action = "queryUserTokenBalance";
        TokenBalanceResp tokenBalanceResp = tokenService.queryTokenBalance(address);
        return new Result(action, 0, "SUCCESS", tokenBalanceResp);
    }

    @GetMapping("/nft-balance/{address}")
    public Result queryUserNftBalance(@PathVariable String address) throws Exception {
        String action = "queryUserNftBalance";
        List<NFTDataResp> nftDataRespList = tokenService.queryUserNFTBalance(address);
        return new Result(action, 0, "SUCCESS", nftDataRespList);
    }


}
