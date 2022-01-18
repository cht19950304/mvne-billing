package com.cmit.mvne.billing.preparation.controller;

import com.cmit.mvne.billing.preparation.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/1
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReprocessRejectedControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    public void test() {
        Assert.assertEquals("a", "a");
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void rejectedCdrs() throws Exception {

        String result = mockMvc.perform(get("/preparation/rejected")
                        .param("errorType", "Fatal").param("page", "0").param("size", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
//        log.info(GsonUtil.toPrettyFormat(result));
    }

    @Test
    public void rejectedCdrsReprocess() throws Exception {
        List<Long> idList = new ArrayList<Long>();
        idList.add(1L);
        idList.add(2L);
        String context = GsonUtil.parseToJsonString(idList);
//        log.info(context);
        String result = mockMvc.perform(post("/preparation/rejected/reprocess")
                .content(context).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info("result : {}", result);
    }
}