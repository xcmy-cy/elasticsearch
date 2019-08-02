package com.xcmy.elasticsearch.common;

import org.apache.http.HttpHost;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class BasicElasticSearchModel{

    private String host;

    private int port;

    private String scheme;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port == 0 ? 9200 : port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme == null ? "http" : scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
