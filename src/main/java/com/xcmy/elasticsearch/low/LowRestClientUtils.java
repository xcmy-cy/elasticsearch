package com.xcmy.elasticsearch.low;

import com.xcmy.elasticsearch.common.BasicElasticSearchConfig;
import com.xcmy.elasticsearch.common.RequestElasticSearchConfig;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LowRestClientUtils {

    @Autowired
    private BasicElasticSearchConfig basicElasticSearchConfig;

    @Autowired
    private RequestElasticSearchConfig RequestConfigInit;

    public BasicElasticSearchConfig getBasicElasticSearchConfig() {
        return basicElasticSearchConfig;
    }

    public void setBasicElasticSearchConfig(BasicElasticSearchConfig basicElasticSearchConfig) {
        this.basicElasticSearchConfig = basicElasticSearchConfig;
    }

    public RequestElasticSearchConfig getRequestConfigInit() {
        return RequestConfigInit;
    }

    public void setRequestConfigInit(RequestElasticSearchConfig requestConfigInit) {
        RequestConfigInit = requestConfigInit;
    }

    //创建一个最基本的连接
    public RestClient createSimpleClient() throws Exception {

        return RestClient.builder(basicElasticSearchConfig.get()).build();
    }

}
