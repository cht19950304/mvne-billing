package com.cmit.mvne.billing.infomanage;


//import com.cmit.mvne.billing.infomanage.service.InfoManageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MvneInfoManageApplicationTests {

	@Test
	public void testCreateUserSync() {
	    System.out.println("Hello");
	}

	@Autowired
	private WebApplicationContext webApplicationContext;
/*	@Autowired
	private InfoManageService infoManageService;*/


	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void insertData() throws Exception{
		String result = mockMvc.perform(get("/test/insertData/{\"orderId\"}/{\"userId\"}/{\"msisdn\"}/{\"count\"}","1110","100","1703","5"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn().getResponse().getContentAsString();
		log.info("insertData test result:"+result);

	}

/*	@Test
	public void sync() throws Exception
	{
		String result = mockMvc.perform(get("/test/sync"))
				//.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn().getResponse().getContentAsString();
		log.info("test result:"+result);
	}*/


/*	//@RequestMapping(value = "/sync")
	@Test
	public void sync(){
		//同步数据
		infoManageService.syncInfo();
	}*/

	@Test
	public void deleteData() throws Exception{
		String result = mockMvc.perform(get("/test/deleteData/{\"deleteStatus\"}/{\"orderId\"}/{\"userId\"}/{\"msisdn\"}/{\"count\"}","all","1000009","100000000","17050000000","1"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn().getResponse().getContentAsString();
		log.info("deleteData test result:"+result);
	}

}
