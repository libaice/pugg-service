package io.pugg.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.List;

@Slf4j
@Component
public class Web3jSdkUtil {

    @Autowired
    private ConfigParam configParam;

    private Web3j web3jSingleton;

    // Singleton
    public Web3j getWeb3jSingleton() {
        if (web3jSingleton == null) {
            synchronized (Web3jSdkUtil.class) {
                if (web3jSingleton == null) {
                    web3jSingleton = Web3j.build(new HttpService(configParam.BSC_WEB3J_URL));
                }
            }
        }
        return web3jSingleton;
    }

    public List<Type> sendPreTransactionAndDecode(String contract, String name, String address, List<Type> params, List<TypeReference<?>> outputParameters) throws Exception {
        Web3j web3j = getWeb3jSingleton();
        try {
            Function function = new Function(name, params, outputParameters);
            String transactionData = FunctionEncoder.encode(function);
            Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contract, transactionData);
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            web3j.shutdown();
            Response.Error error = ethCall.getError();
            if (error != null) {
                String errorMessage = error.getMessage();
                log.error("error when invoke contract:{},name:{},error:{}", contract, name, errorMessage);
//                throw new Exception(name, String.format("ErrorInfo.SEND_TX_FAIL.descCN()", name), String.format("ErrorInfo.SEND_TX_FAIL.descEN()", name), "ErrorInfo.SEND_TX_FAIL.code()");
            }
            List<Type> result = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return result;
        } catch (Exception e) {
            web3j.shutdown();
            throw e;
        } finally {
            web3j.shutdown();
        }
    }


}
