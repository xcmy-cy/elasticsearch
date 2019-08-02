package com.xcmy.elasticsearch;

import com.xcmy.elasticsearch.low.LowRestClientUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Java 通过 restful api 方式操作elasticsearch
 * <p>
 * 官方介绍:https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.2/index.html
 * 使用的是官方依赖 :org.elasticsearch.client:elasticsearch-rest-client:7.2.0
 * <p>
 * 低级客户端的功能包括：
 * 最小的依赖
 * 跨所有可用节点的负载平衡
 * 节点故障和特定响应代码时的故障转移
 * 连接惩罚失败（是否重试失败的节点取决于它失败的连续次数;失败次数越多，客户端在再次尝试同一节点之前等待的时间越长）
 * 持久连接
 * 跟踪请求和响应的日志记录
 * 可选的集群节点自动发现(需要依赖:org.elasticsearch.client:elasticsearch-rest-client-sniffer:7.2.0)
 */
public class LowRestClient {

    @Autowired
    LowRestClientUtils lowRestClientUtils;

    public static void main(String[] args) throws IOException {
        main1(args);
    }

    //写的一个简单工具
    public void test() throws Exception {
        RestClient client = lowRestClientUtils.createSimpleClient();
        Request request = new Request("GET", "user/emp/_search");
        //在上面的url后面加上?name=value
        request.addParameter("q", "last_name:Smith");

        Response response = client.performRequest(request);

        System.out.println(EntityUtils.toString(response.getEntity()));
        //最后根据情况选择关闭或回到池里.这是简单写法,确保在异常 服务关闭等状态下能正常执行
        client.close();

    }

    public static void main1(String[] args) throws IOException {

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("0.0.0.0", 9200, "http")
        );

        //设置节点选择器，用于过滤客户端将请求发送到客户端本身的节点。防止在启用嗅探时向专用主节点发送请求.
//        builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);

        //设置一个侦听器，每次节点出现故障时都会收到通知，以防需要执行操作
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                System.out.println(node.getName() + "节点挂了=============");
            }
        });

        //设置需要随每个请求一起发送的默认标头，以防止必须为每个请求指定它们
        Header[] headers = new Header[]{new BasicHeader("Content-type", "application/json")};
        builder.setDefaultHeaders(headers);

        //设置允许修改默认请求配置的回调例如，请求超时，身份验证或
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectionRequestTimeout(3 * 1000);
                return requestConfigBuilder;
            }
        });

        //设置允许修改http客户端配置的回调例如，通过ssl进行加密通信 线程数
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                //线程数
                httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
                //基本认证
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("username", "password"));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder;
            }
        });

        RestClient client = builder.build();

        //创建请求 请求方式为: GET POST PUT DELETE HEAD endpoint表示请求路径结尾拼接信息
        //与client组合起来请求的url为 scheme://hostname:port/endpoint/
        Request request = new Request("GET", "user/emp/_search");
        //在上面的url后面加上?name=value
        request.addParameter("q", "last_name:Smith");

        Response response = client.performRequest(request);

        System.out.println(EntityUtils.toString(response.getEntity()));
        //最后根据情况选择关闭或回到池里.这是简单写法,确保在异常 服务关闭等状态下能正常执行
        client.close();
    }

}
