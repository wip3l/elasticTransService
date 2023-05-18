package com.jh.elastictransservice.utils;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liqijian
 */
@Data
@Component
public class ElasticClientUtils {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private String port;
    @Value("${elasticsearch.auth}")
    private Boolean auth;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;



    public RestHighLevelClient getElasticClient() {
        RestHighLevelClient client = null;
        if (auth) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));  //es账号密码
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(host, Integer.parseInt(port), "http")
                    ).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            httpClientBuilder.disableAuthCaching();
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        }
                    })
            );

        } else {
            client = new RestHighLevelClient(RestClient.builder(
                    new HttpHost(host, Integer.parseInt(port), "http")));
        }
        return client;
    }

    public void shutdown() throws Exception {
        getElasticClient().close();
    }

}
