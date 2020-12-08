
package com.cmit.mvne.billing.infomanage.test.crm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.alibaba.fastjson.JSON;
import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IProdDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IUserDto;
import com.cmit.mvne.billing.infomanage.entity.InfoManageCrmEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoManageCrmControllerTest {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

	@Test
	public void createUserTest() throws Exception {
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000009l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1001");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");
		IProdDto iProd = new IProdDto();
		iProd.setOrderId(1000009l);
		iProd.setUserId(100000000l);
		iProd.setRectype("开户");
		//iProd.setProductId(100l);
		iProd.setProductId(101710004l);
		iProd.setProductInsId(1001l);
		iProd.setValidDate(new Date().getTime());
		iProd.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		IUserDto iUser = new IUserDto();
		iUser.setOrderId(1000009l);
		iUser.setUserId(100000000l);
		iUser.setAcctId(100000001l);
		iUser.setCustId(10000000l);
		iUser.setMsisdn("17050000000");
		iUser.setImsi("400000000000000");
		iUser.setChannelCode("channel20191219154300");
		iUser.setValidDate(new Date().getTime());
		iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		iUser.setUserStatus("1");

		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIprod(iProd);
		infoManageCrmEntity.setIuser(iUser);
		String content = JSON.toJSONString(infoManageCrmEntity);
		
//		String content ="{\n" +
//                "  \"ord_order\": {\n" +
//                "  \"orderId\": 1000000,\n" +
//                "  \"orderType\": \"3\",\n" +
//                "  \"busiOperCode\": \"1001\",\n" +
//                "  \"custId\": \"10000000\",\n" +
//                "  \"factMoney\": 0.00,\n" +
//                "  \"createDate\": "+ new Date().getTime() + ",\n"+
//                "  \"doneDate\": "+ new Date().getTime() + ",\n" +
//                "  \"xferDate\": "+ new Date().getTime() + ",\n" +
//                "  \"updateFlag\": \"10\"\n" +
//                "  },\n"+
//                "  \"prod\":{\n"+
//                "  \"orderId\": \"1000000\",\n" +
//                "  \"userId\": \"100000000\",\n" +
//                "  \"rectype\": \"开户\",\n" +
//                "  \"productId\": 100,\n" +
//                "  \"productInsId\": 1001,\n" +
//                "  \"validDate\": "+ new Date().getTime() + ",\n"+
//                "  \"expireDate\":\"2099-12-31 23:59:59\"\n" +
//                "  },\n"+
//                "  \"user\":{\n"+
//                "  \"orderId\": \"1000000\",\n" +
//                "  \"userId\": \"100000000\",\n" +
//                "  \"acctId\": \"100000001\",\n" +
//                "  \"custId\": \"10000000\",\n" +
//                "  \"msisdn\": \"17050000000\",\n" +
//                "  \"imsi\": \"400000000000000\",\n" +
//                "  \"channelCode\": \"channel20191219154300\",\n" +
//                "  \"validDate\": "+ new Date().getTime() + ",\n"+
//                "  \"expireDate\":\"2099-12-31 23:59:59\",\n" +
//                "  \"userStatu\": \"1\"\n" +
//                "  }\n"+
//                "}\n";
		System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/createUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println(result);
	}
	
	@Test
	public void changeOfferTest() throws Exception {
		
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000010l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1010");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");
		IProdDto iProd = new IProdDto();
		iProd.setOrderId(1000010l);
		iProd.setUserId(100000000l);
		iProd.setRectype("开户");
		iProd.setProductFee(new BigDecimal(50.00));
		//iProd.setProductId(101l);
		iProd.setProductId(101710004l);
		iProd.setProductInsId(1002l);
		iProd.setValidDate(new Date().getTime());
		iProd.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIprod(iProd);
		String content = JSON.toJSONString(infoManageCrmEntity);
		
		System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/changeOffer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################"+result);
	}

	@Test
	public void stopTest() throws Exception {
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000011l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1004");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");

		IUserDto iUser = new IUserDto();
		iUser.setOrderId(1000011l);
		iUser.setUserId(100000000l);
		iUser.setAcctId(100000001l);
		iUser.setCustId(10000000l);
		iUser.setMsisdn("17050000000");
		iUser.setImsi("400000000000000");
		iUser.setChannelCode("channel20191219154300");
		iUser.setValidDate(new Date().getTime());
		iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		iUser.setUserStatus("11");

		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIuser(iUser);
		String content = JSON.toJSONString(infoManageCrmEntity);

		System.out.println(content);

		String result = mockMvc.perform(post("/infoManage/stop")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
				.andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
		System.out.println("############################"+result);
	}

	@Test
	public void startTest() throws Exception {
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000012l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1006");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");
		
		IUserDto iUser = new IUserDto();
		iUser.setOrderId(1000012l);
		iUser.setUserId(100000000l);
		iUser.setAcctId(100000001l);
		iUser.setCustId(10000000l);
		iUser.setMsisdn("17050000000");
		iUser.setImsi("400000000000000");
		iUser.setChannelCode("channel20191219154300");
		iUser.setValidDate(new Date().getTime());
		iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		iUser.setUserStatus("1");
		
		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIuser(iUser);
		String content = JSON.toJSONString(infoManageCrmEntity);
		
		System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/start")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################"+result);
	}
	
	@Test
	public void reChargeTest() throws Exception {
		
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000014l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1020");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(100.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");


		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		String content = JSON.toJSONString(infoManageCrmEntity);
		
		System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/reCharge")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################"+result);
	}

	@Test
	public void changeCardTest() throws Exception {

		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000015l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1002");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");

		IUserDto iUser = new IUserDto();
		iUser.setOrderId(1000015l);
		iUser.setUserId(100000000l);
		iUser.setAcctId(100000001l);
		iUser.setCustId(10000000l);
		iUser.setMsisdn("17050000000");
		iUser.setImsi("400000000000001");
		iUser.setChannelCode("channel20191219154300");
		iUser.setValidDate(new Date().getTime());
		iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		iUser.setUserStatus("1");

		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIuser(iUser);
		String content = JSON.toJSONString(infoManageCrmEntity);

		System.out.println(content);

		String result = mockMvc.perform(post("/infoManage/changeCard")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
				.andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
		System.out.println("############################"+result);
	}
	
	@Test
	public void deleteUserTest() throws Exception {
		
		IOrdOrderDto iOrdOrder = new IOrdOrderDto();
		iOrdOrder.setOrderId(1000016l);
		iOrdOrder.setOrderType("3");
		iOrdOrder.setBusiOperCode("1008");
		iOrdOrder.setCustId(10000000l);
		iOrdOrder.setUserId(100000000l);
		iOrdOrder.setFactMoney(new BigDecimal(0.00));
		iOrdOrder.setCreateDate(new Date().getTime());
		iOrdOrder.setDoneDate(new Date().getTime());
		iOrdOrder.setXferDate(new Date().getTime());
		//iOrdOrder.setUpdateFlag("10");
		
		IUserDto iUser = new IUserDto();
		iUser.setOrderId(1000016l);
		iUser.setUserId(100000000l);
		iUser.setAcctId(100000001l);
		iUser.setCustId(10000000l);
		iUser.setMsisdn("17050000000");
		iUser.setImsi("400000000000001");
		iUser.setChannelCode("channel20191219154300");
		iUser.setValidDate(new Date().getTime());
		iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 00:00:00").getTime());
		iUser.setUserStatus("1");
		
		InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
		infoManageCrmEntity.setIordOrder(iOrdOrder);
		infoManageCrmEntity.setIuser(iUser);
		String content = JSON.toJSONString(infoManageCrmEntity);
		
		System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################"+result);
	}
	


}

