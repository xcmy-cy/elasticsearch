package com.xcmy.elasticsearch;

import com.xcmy.elasticsearch.low.LowRestClientUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LowRestClientTest {

    @Autowired
    LowRestClientUtils lowRestClientUtils;


    @Test
    public void test1() throws Exception {
        RestClient client = lowRestClientUtils.createSimpleClient();
        Request request = new Request("GET", "user/emp/_search");
        //在上面的url后面加上?name=value
        request.addParameter("q", "last_name:Smith");

        Response response = client.performRequest(request);

        System.out.println(EntityUtils.toString(response.getEntity()));
        //最后根据情况选择关闭或回到池里.这是简单写法,确保在异常 服务关闭等状态下能正常执行
        client.close();
    }
}