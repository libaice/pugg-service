1. 查询用户所有Token 余额
`/api/v1/token/balance/{address}`

>  GET 请求 返回结果示例, 可以根据需要, 查询其他的Token
 ```json
{
    "action": "queryUserTokenBalance",
    "error": 0,
    "desc": "SUCCESS",
    "result": {
        "bnbBalance": "15510.697968382069916255",
        "usdtBalance": "19753668.735561866227156558"
    },
    "version": "v1"
}
```

2. 查询用户的所有NFT 余额
   当前查询用户在Ethereum Chain 上的NFT【erc721】 余额, 种类, logo， 合约地址信息, 下一步拓展用户BSC上面的NFT余额list 
`api/v1/token/nft-balance/{adress}`


 ```json
{
"action": "queryUserNftBalance",
"error": 0,
"desc": "SUCCESS",
"result": [
{
"logoUrl": "https://logo.nftscan.com/logo/0x8a9ece9d8806eb0cde56ac89ccb23a36e2c718cf.png",
"ownAmount": "1",
"contractName": "Humans of the Metaverse",
"tradingPrice": "0.1",
"contractAddress": "0x8a9ece9d8806eb0cde56ac89ccb23a36e2c718cf"
} 
 ]
}



