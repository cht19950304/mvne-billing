package com.cmit.mvne.billing.creditcontrol.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.service.impl.CreditControlSmsServiceImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MvneCrmClientTest {
    @Autowired
    private CreditControlSmsServiceImpl creditControlSmsService;
	@Autowired
    private WebApplicationContext webApplicationContext;
//    @Autowired
//    private LoginHelper loginHelper;

    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        loginHelper.login("mrbird", "1234qwer");
    }
    
    @Test
    public void mvneCrmClienttest() throws UnsupportedEncodingException, Exception {
    	String content ="{\n" +
                "  \"billId\": \"17050000000\",\n" +
                "  \"fromType\": \"7\",\n" +
                "  \"busiType\": \"1004\",\n" +
                "  \"basicOrgId\": \"1\",\n" +
                "  \"mvnoId\": \"yellow mobile\"\n" +
                "}\n";
    	
    	System.out.println(content);
        String result = mockMvc.perform(post("/creditControl/stopAndStart")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }

    @Test
    public void sendSmsByHttpPostTest() throws IOException {
        CreditControlSms creditControlSms = new CreditControlSms();
        creditControlSms.setId(Long.valueOf(8));
        creditControlSms.setMsisdn("1700000678");
        creditControlSms.setOperation("1");
        creditControlSms.setReason("5");
        creditControlSms.setStatus("0");
        creditControlSms.setText("test");
        creditControlSms.setCreateDate(new Date());
        creditControlSms.setModifyDate(null);

        String s = creditControlSmsService.sendSmsByHttpsPost(creditControlSms);
        System.out.println(s);

//        String s = sendSmsByHttpPost(creditControlSms);
//        System.out.println(s);



//        String encodeInfo = URLEncoder.encode(creditControlSms.getText(), "ISO-8859-1");
//        String url = pUrl + "?L=" + username + "&P=" + password + "&msisdn=00" + creditControlSms.getMsisdn() + "&T=" + encodeInfo;
////        String url = "http://baidu.com";
////        URL reqURL = new URL(url); //??????URL??????
//        URL reqURL = new URL(null, url, new sun.net.www.protocol.https.Handler());//??????URL??????
//        HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL.openConnection();
//
//        httpsConn.setDoOutput(true);
//        httpsConn.setRequestMethod("POST");
//        OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream());
//        out.write("aaaaaaaaaaaaaaaaaaaa");
//        out.flush();
//        out.close();
//
//        //?????????????????????????????????????????????????????
//        InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
//
//        //???????????????????????????????????????
//        int respInt = insr.read();
//        while (respInt != -1) {
//            System.out.print((char) respInt);
//            respInt = insr.read();
//        }






////        SSLContext sslcontext = SSLContext.getInstance("SSL","SunJSSE");
////        sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
//        String url = "http://baidu.com";
////        URL serverUrl = new URL(null, url, new sun.net.www.protocol.https.Handler());//??????URL??????
//        URL serverUrl = new URL(url);//??????URL??????
//        HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
////        conn.setSSLSocketFactory(sslcontext.getSocketFactory());
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-type", "application/json");
//        //????????????false??????????????????redirect????????????????????????
//        conn.setInstanceFollowRedirects(false);
//        conn.connect();
////        String result = getReturn(conn);
//
//        //?????????????????????????????????????????????????????
//        InputStreamReader insr = new InputStreamReader(conn.getInputStream());
//
//        //???????????????????????????????????????
//        int respInt = insr.read();
//        while (respInt != -1) {
//            System.out.print((char) respInt);
//            respInt = insr.read();
//        }





    }

    
}
