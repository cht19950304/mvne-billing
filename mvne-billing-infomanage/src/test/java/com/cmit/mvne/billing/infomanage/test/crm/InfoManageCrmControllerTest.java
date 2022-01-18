
package com.cmit.mvne.billing.infomanage.test.crm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import com.cmit.mvne.billing.infomanage.service.QueryInterfaceService;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsService;
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
import com.cmit.mvne.billing.user.analysis.entity.IProdDto;
import com.cmit.mvne.billing.user.analysis.entity.IUserDto;
import com.cmit.mvne.billing.infomanage.entity.InfoManageCrmEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoManageCrmControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private QueryInterfaceService queryInterfaceService;
    @Autowired
    private CdrGprsService cdrGprsService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test(){
        /*String s = "{\n" +
                " \"\"IOrdOrder\"\":\"\"{\n" +
                "             \"\"orderId\"\":\"\"1000009\"\",\n" +
                "             \"\"orderType\"\":\"\"3\"\",\n" +
                "             \"\"busiOperCode\"\":\"\"1001\"\",\n" +
                "             \"\"custId\"\":\"\"10000000\"\",\n" +
                "             \"\"userId\"\":\"\"100000000\"\",\n" +
                "             \"\"msisdn\"\":\"\"17050000000\"\",\n" +
                "             \"\"factMoney\"\":\"\"50.00\"\",\n" +
                "             \"\"createDate\"\":\"\"2020-03-03 23:35:36\"\",\n" +
                "             \"\"doneDate\"\":\"\"2020-03-03 23:35:36\"\",\n" +
                "             \"\"xferDate\"\":\"\"2020-03-03 23:35:36\"\"\n" +
                "            }\"\",\n" +
                " \"\"IProd\"\":\"\"{\n" +
                "             \"\"orderId\"\":\"\"1000009\"\",\n" +
                "             \"\"userId\"\":\"\"100000000\"\",\n" +
                "             \"\"rectype\"\":\"\"开户\"\",\n" +
                "             \"\"productFee\"\":\"\"100.00\"\",\n" +
                "             \"\"productId\"\":\"\"101700004\"\",\n" +
                "             \"\"productInsId\"\":\"\"1001\"\",\n" +
                "             \"\"validDate\"\":\"\"2020-03-03 23:35:36\"\",\n" +
                "             \"\"expireDate\"\":\"\"2099-12-31 23:59:59\"\"\n" +
                "            }\"\",\n" +
                " \"\"IUser\"\":\"\"{\n" +
                "             \"\"orderId\"\":\"\"1000009\"\",\n" +
                "             \"\"userId\"\":\"\"100000000\"\",\n" +
                "             \"\"acctId\"\":\"\"100000001\"\",\n" +
                "             \"\"custId\"\":\"\"10000000\"\",\n" +
                "             \"\"msisdn\"\":\"\"17050000000\"\",\n" +
                "             \"\"imsi\"\":\"\"400000000000000\"\",\n" +
                "             \"\"channelCode\"\":\"\"channel20191219154300\"\",\n" +
                "             \"\"validDate\"\":\"\"2020-03-03 23:35:36\"\",\n" +
                "             \"\"expireDate\"\":\"\"2099-12-31 23:59:59\"\",\n" +
                "             \"\"userStatus\"\":\"\"03\"\"\n" +
                "            }\"\"\n" +
                " }\n" +
                "\n" +
                "\n";*/

        String s = "{\"iOrdOrder\":{\"orderId\":1000009,\"orderType\":\"3\",\"busiOperCode\":\"1001\",\"custId\":10000016,\"userId\":100000016,\"msisdn\":\"1700000136\",\"factMoney\":100,\"createDate\":1614752457320,\"doneDate\":1614752457320,\"xferDate\":1614752457320},\"iProd\":{\"orderId\":1000036,\"userId\":100000016,\"rectype\":\"开户\",\"productFee\":50,\"productId\":101700004,\"productInsId\":1001,\"validDate\":1614752457320,\"expireDate\":4102415999000},\"iUser\":{\"orderId\":1000036,\"userId\":100000016,\"acctId\":100000016,\"custId\":10000016,\"msisdn\":\"1700000136\",\"imsi\":\"400000000000000\",\"channelCode\":\"channel20191219154300\",\"validDate\":1614752457321,\"expireDate\":4102415999000,\"userStatus\":\"03\"}}";
        System.out.println("=========");
        System.out.println(s);
        System.out.println("=========");
        InfoManageCrmEntity infoManageCrmEntity = JSON.parseObject(s, InfoManageCrmEntity.class);
//        InfoManageCrmEntity parse = (InfoManageCrmEntity) JSON.parse(s);
        System.out.println(infoManageCrmEntity.toString());
    }

    @Test
    public void selectCdrInfo() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateString = "2020-03-25 00:00:00";
        Date startDate = sdf.parse(startDateString);
        String endDateString = "2020-03-25 23:59:59";
        Date endDate = sdf.parse(endDateString);
        List<QueryUserCdrInfoDto> rgp = cdrGprsService.selectCdrInfo("20200505", "17050000000", startDateString, endDateString, "RGP");
        System.out.println(rgp.get(0).getDownloadVol().add(rgp.get(0).getUploadVol()));
    }
    @Test
    public void mulSelectCdrInfo() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateString = "2020-03-25 00:00:00";
        Date startDate = sdf.parse(startDateString);
        String endDateString = "2020-03-25 23:59:59";
        Date endDate = sdf.parse(endDateString);
        List<QueryUserCdrInfoDto> rgp = cdrGprsService.selectCdrInfo("20200505", "17050000000", startDateString, endDateString, "RGP");
        System.out.println(rgp.get(0).getDownloadVol().add(rgp.get(0).getUploadVol()));
    }

    @Test
    public void createUserTest() throws Exception {
        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000009L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1001");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(100.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");
        IProdDto iProd = new IProdDto();
        iProd.setOrderId(1000036L);
        iProd.setUserId(100000016L);
        iProd.setRectype("开户");
        iProd.setProductFee(new BigDecimal(50.00));
        //iProd.setProductId(100l);
        iProd.setProductId(101700004L);
        iProd.setProductInsId(1001L);
        iProd.setValidDate(new Date().getTime());
        iProd.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        IUserDto iUser = new IUserDto();
        iUser.setOrderId(1000036L);
        iUser.setUserId(100000016L);
        iUser.setAcctId(100000016L);
        iUser.setCustId(10000016L);
        iUser.setMsisdn("1700000136");
        iUser.setImsi("400000000000000");
        iUser.setChannelCode("channel20191219154300");
        iUser.setValidDate(new Date().getTime());
        iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        iUser.setUserStatus("03");

        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIProd(iProd);
        infoManageCrmEntity.setIUser(iUser);
        String content = JSON.toJSONString(infoManageCrmEntity);

//		String content ="{\n" +
		content ="{\n" +
                "  \"ord_order\": {\n" +
                "  \"orderId\": 1000000,\n" +
                "  \"orderType\": \"3\",\n" +
                "  \"busiOperCode\": \"1001\",\n" +
                "  \"custId\": \"10000000\",\n" +
                "  \"factMoney\": 0.00,\n" +
                "  \"createDate\": "+ new Date().getTime() + ",\n"+
                "  \"doneDate\": "+ new Date().getTime() + ",\n" +
                "  \"xferDate\": "+ new Date().getTime() + ",\n" +
                "  \"updateFlag\": \"10\"\n" +
                "  },\n"+
                "  \"prod\":{\n"+
                "  \"orderId\": \"1000000\",\n" +
                "  \"userId\": \"100000000\",\n" +
                "  \"rectype\": \"开户\",\n" +
                "  \"productId\": 100,\n" +
                "  \"productInsId\": 1001,\n" +
                "  \"validDate\": "+ new Date().getTime() + ",\n"+
                "  \"expireDate\":\"2099-12-31 23:59:59\"\n" +
                "  },\n"+
                "  \"user\":{\n"+
                "  \"orderId\": \"1000000\",\n" +
                "  \"userId\": \"100000000\",\n" +
                "  \"acctId\": \"100000001\",\n" +
                "  \"custId\": \"10000000\",\n" +
                "  \"msisdn\": \"17050000000\",\n" +
                "  \"imsi\": \"400000000000000\",\n" +
                "  \"channelCode\": \"channel20191219154300\",\n" +
                "  \"validDate\": "+ new Date().getTime() + ",\n"+
                "  \"expireDate\":\"2099-12-31 23:59:59\",\n" +
                "  \"userStatu\": \"1\"\n" +
                "  }\n"+
                "}\n";
        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/createUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println(result);
    }

    @Test
    public void changeOfferTest() throws Exception {

        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000010L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1010");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(0.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");
        IProdDto iProd = new IProdDto();
        iProd.setOrderId(1000010L);
        iProd.setUserId(100000016L);
        iProd.setRectype("开户");
        iProd.setProductFee(new BigDecimal(50.00));
        //iProd.setProductId(101l);
        iProd.setProductId(101700005L);
        iProd.setProductInsId(1002L);
        iProd.setValidDate(new Date().getTime());
        iProd.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIProd(iProd);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/changeOffer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

    @Test
    public void stopTest() throws Exception {
        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000011L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1004");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(0.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");

        IUserDto iUser = new IUserDto();
        iUser.setOrderId(1000011L);
        iUser.setUserId(100000016L);
        iUser.setAcctId(100000016L);
        iUser.setCustId(10000016L);
        iUser.setMsisdn("1700000136");
        iUser.setImsi("400000000000000");
        iUser.setChannelCode("channel20191219154300");
        iUser.setValidDate(new Date().getTime());
        iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        iUser.setUserStatus("02");

        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIUser(iUser);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/stop")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

    @Test
    public void startTest() throws Exception {
        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000012L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1006");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(0.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");

        IUserDto iUser = new IUserDto();
        iUser.setOrderId(1000012L);
        iUser.setUserId(100000016L);
        iUser.setAcctId(100000016L);
        iUser.setCustId(10000016L);
        iUser.setMsisdn("1700000136");
        iUser.setImsi("400000000000000");
        iUser.setChannelCode("channel20191219154300");
        iUser.setValidDate(new Date().getTime());
        iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        iUser.setUserStatus("03");

        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIUser(iUser);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/start")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

    @Test
    public void reChargeTest() throws Exception {

        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000014L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1020");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(100.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");


        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/reCharge")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

    @Test
    public void changeCardTest() throws Exception {

        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000015L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1002");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(0.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");

        IUserDto iUser = new IUserDto();
        iUser.setOrderId(1000015L);
        iUser.setUserId(100000016L);
        iUser.setAcctId(100000016L);
        iUser.setCustId(10000016L);
        iUser.setMsisdn("1700000136");
        iUser.setImsi("400000000000001");
        iUser.setChannelCode("channel20191219154300");
        iUser.setValidDate(new Date().getTime());
        iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        iUser.setUserStatus("03");

        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIUser(iUser);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/changeCard")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

    @Test
    public void deleteUserTest() throws Exception {

        IOrdOrderDto iOrdOrder = new IOrdOrderDto();
        iOrdOrder.setOrderId(1000016L);
        iOrdOrder.setOrderType("3");
        iOrdOrder.setBusiOperCode("1008");
        iOrdOrder.setCustId(10000016L);
        iOrdOrder.setUserId(100000016L);
        iOrdOrder.setMsisdn("1700000136");
        iOrdOrder.setFactMoney(new BigDecimal(0.00));
        iOrdOrder.setCreateDate(new Date().getTime());
        iOrdOrder.setDoneDate(new Date().getTime());
        iOrdOrder.setXferDate(new Date().getTime());
        //iOrdOrder.setUpdateFlag("10");

        IUserDto iUser = new IUserDto();
        iUser.setOrderId(1000016L);
        iUser.setUserId(100000016L);
        iUser.setAcctId(100000016L);
        iUser.setCustId(10000016L);
        iUser.setMsisdn("1700000136");
        iUser.setImsi("400000000000001");
        iUser.setChannelCode("channel20191219154300");
        iUser.setValidDate(new Date().getTime());
        iUser.setExpireDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2099-12-31 23:59:59").getTime());
        iUser.setUserStatus("04");

        InfoManageCrmEntity infoManageCrmEntity = new InfoManageCrmEntity();
        infoManageCrmEntity.setIOrdOrder(iOrdOrder);
        infoManageCrmEntity.setIUser(iUser);
        String content = JSON.toJSONString(infoManageCrmEntity);

        System.out.println(content);

        String result = mockMvc.perform(post("/infoManage/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

//        log.info(GsonUtil.toPrettyFormat(result));
        System.out.println("############################" + result);
    }

}

