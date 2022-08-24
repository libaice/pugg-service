package io.pugg.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class TokenUtil {
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private Web3jSdkUtil web3jSdkUtil;


    public BigInteger getBNBBalance(String address) throws IOException {
        Web3j web3jSingleton = web3jSdkUtil.getWeb3jSingleton();
        EthGetBalance send = web3jSingleton.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        BigInteger balance = send.getBalance();
        return balance;
    }

    public BigInteger getTokenBalanceOf(String userAddress, String tokenAddress) throws Exception {
        List<Type> params = Arrays.asList(new Address(userAddress));
        TypeReference<Uint256> currentBalance = new TypeReference<Uint256>() {
        };
        List<TypeReference<?>> outputParameters = Arrays.asList(currentBalance);
        List<Type> result = web3jSdkUtil.sendPreTransactionAndDecode(tokenAddress, "balanceOf", Constant.ETH_PRE_ADDRESS, params, outputParameters);
        BigInteger tokenBalance = (BigInteger) result.get(0).getValue();
        return tokenBalance;
    }

}
