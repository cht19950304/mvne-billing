package com.cmit.mvne.billing.creditcontrol.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MvneCrmClientTest {

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
    
}
