package com.cmit.mvne.billing.settle.controller;

import com.cmit.mvne.billing.settle.util.DateTimeUtil;
import com.cmit.mvne.billing.settle.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SettleSiminnSumControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getSettleSiminnSumFee() throws Exception {
        String result = mockMvc.perform(get("/settle/siminnSumFee")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startMonth", "202001")
                .param("endMonth", "202005")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        log.info(result);
    }

    @Test
    public void getSettleSiminnSumAmount() throws Exception {
        String result = mockMvc.perform(get("/settle/siminnSumAmount")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startMonth", "202005")
                .param("endMonth", "202006")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        log.info(GsonUtil.toPrettyFormat(result));
    }

//    @Test
//    public void getSettleSiminnSumDayAmount() throws Exception {
//        String result = mockMvc.perform(get("/settle/siminnSumDayAmount")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("startTime", String.valueOf(DateTimeUtil.strToDate("20200410120000").toInstant().toEpochMilli()))
//                .param("endTime", String.valueOf(DateTimeUtil.strToDate("20200415120000").toInstant().toEpochMilli()))
//        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
//                .andReturn().getResponse().getContentAsString();
//        log.info(result);
//    }

    @Test
    public void getSettleSiminnSumDayAmount() throws Exception {
        String result = mockMvc.perform(get("/settle/siminnSumDayAmount")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startTime", String.valueOf(1591632000000L))
                .param("endTime", String.valueOf(1591804800000L))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        log.info(GsonUtil.toPrettyFormat(result));
    }

    @Test
    public void sumByDay() throws Exception {

        String result = mockMvc.perform(get("/settle/sumByDay")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("day", "20200607")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

        log.info(GsonUtil.toPrettyFormat(result));


    }

    @Test
    public void sumByMonth() throws Exception {

        String result = mockMvc.perform(get("/settle/sumByMonth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("month", "20200607")
        ).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

        log.info(GsonUtil.toPrettyFormat(result));
    }
}