package com.xcmy.elasticsearch.common;

import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.es")
public class BasicElasticSearchConfig {

    private List<BasicElasticSearchModel> basic;

    public List<BasicElasticSearchModel> getBasic() {
        return basic;
    }

    public void setBasic(List<BasicElasticSearchModel> basic) {
        this.basic = basic;
    }

    public HttpHost[] get() {
        if (basic != null && !basic.isEmpty()) {
            HttpHost[] httpHosts = new HttpHost[basic.size()];
            for (int i = 0; i < basic.size(); i++) {
                BasicElasticSearchModel b = basic.get(i);
                httpHosts[i] = new HttpHost(b.getHost(), b.getPort(), b.getScheme());
            }
            return httpHosts;
        }
        return null;
    }

}
