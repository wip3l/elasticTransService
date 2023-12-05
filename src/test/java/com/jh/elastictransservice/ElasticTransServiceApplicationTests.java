package com.jh.elastictransservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@SpringBootTest
class ElasticTransServiceApplicationTests {

    @Test
    void contextLoads() throws UnsupportedEncodingException {
       String encodedUrl = URLEncoder.encode("D:\\SHJH\\专利", "UTF-8");
       String encodedUrl2 = URLEncoder.encode("D:\\SHJH\\专利\\一种飞机性能变化趋势分析方法 - 摘要附图.vsdx", "UTF-8");
        String filesPath = URLDecoder.decode(encodedUrl, "UTF-8");

        System.out.println("encodedUrl = " + encodedUrl);
        System.out.println("encodedUrl2 = " + encodedUrl2);
        System.out.println("filesPath = " + filesPath);
    }

}
