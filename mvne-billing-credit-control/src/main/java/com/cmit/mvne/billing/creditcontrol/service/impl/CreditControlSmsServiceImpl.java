/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.service.impl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;
import org.springframework.transaction.annotation.Propagation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.creditcontrol.entity.CmUserDetail;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlDelete;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSmsErr;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSmsH;
import com.cmit.mvne.billing.creditcontrol.entity.MvneCrmResponse;
import com.cmit.mvne.billing.creditcontrol.mapper.CmUserDetailMapper;
import com.cmit.mvne.billing.creditcontrol.mapper.CreditControlDeleteMapper;
import com.cmit.mvne.billing.creditcontrol.mapper.CreditControlSmsErrMapper;
import com.cmit.mvne.billing.creditcontrol.mapper.CreditControlSmsHMapper;
import com.cmit.mvne.billing.creditcontrol.mapper.CreditControlSmsMapper;
import com.cmit.mvne.billing.creditcontrol.remote.MvneCrmClient;
import com.cmit.mvne.billing.creditcontrol.remote.entity.DelUserRequestDTO;
import com.cmit.mvne.billing.creditcontrol.remote.entity.StopAndStartDTO;
import com.cmit.mvne.billing.creditcontrol.service.ICreditControlSmsService;
import com.cmit.mvne.billing.creditcontrol.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author jiangxm02
 *
 */
@Slf4j
@Service
@Component
@Configuration
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CreditControlSmsServiceImpl extends ServiceImpl<CreditControlSmsMapper, CreditControlSms> 
implements ICreditControlSmsService {
	@Autowired
	private RestOperations restOperations;

	@Autowired
	private MvneCrmClient mvneCrmClient;
	@Autowired
	private CreditControlSmsMapper creditControlSmsMapper;
	@Autowired
	private CreditControlSmsHMapper creditControlSmsHMapper;
	@Autowired
	private CreditControlSmsErrMapper creditControlSmsErrMapper;
	@Autowired
	private CreditControlDeleteMapper creditControlDeleteMapper;
	@Autowired
	private CmUserDetailMapper cmUserDetailMapper;
	
	@Value("${sms.username}")
	String username;
	@Value("${sms.password}")
    String password;
    @Value("${sms.get.url}")
    String url;
    @Value("${sms.post.url}")
    String pUrl;
    @Value("${sms.push}")
    String pushSms;
    @Value("${operator.basicOrgId}")
    String basicOrgId;
    @Value("${operator.mvnoId}")
    String mvnoId;
	
	@Override
	public void createCreditControlSms(CreditControlSms creditControlSms) throws Exception{
		creditControlSmsMapper.insert(creditControlSms);
	}

	@Override
	public void dealCreditControlSmsAndCrm(int params) throws Exception{
		LambdaQueryWrapper<CreditControlSms> queryWrapper = new LambdaQueryWrapper<>(); 
		queryWrapper.ne(CreditControlSms::getOperation, "0");
		queryWrapper.eq(CreditControlSms::getStatus, "0");
		queryWrapper.last("limit "+params);
		List<CreditControlSms> creditControlSmsList = this.baseMapper.selectList(queryWrapper);
		
		for(CreditControlSms creditControlSms : creditControlSmsList) {
			MvneCrmResponse mvneCrmResponse = null;
			boolean tag = false;
			String result = "";
			try {
				creditControlSms.setStatus("1");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				
				//TODO:???????????????????????????????????????????????????
				if("1".equals(pushSms)) {
					result = sendSmsByHttpPost(creditControlSms);
				}
				else {
					result = "SUCCESS";
				}
				log.info("????????????????????????:"+result);
				if("OK".equals(result) || "ok".equals(result)) {
					result = "SUCCESS";
				}
				if("1".equals(creditControlSms.getOperation())) {
					//TODO:??????CRM???????????????
					StopAndStartDTO stopAndStartDto = new StopAndStartDTO();
					stopAndStartDto.setBillId(creditControlSms.getMsisdn());
					stopAndStartDto.setFromType("7");
					stopAndStartDto.setBusiType("1004");
					stopAndStartDto.setBasicOrgId(basicOrgId);
					stopAndStartDto.setMvnoId(mvnoId);
					mvneCrmResponse = mvneCrmClient.stopAndStart(stopAndStartDto);
					log.info("Credit control stop user's response is:"+mvneCrmResponse.get("code"));
				}
				if("2".equals(creditControlSms.getOperation())) {
					//TODO:??????CRM???????????????
					StopAndStartDTO stopAndStartDto = new StopAndStartDTO();
					stopAndStartDto.setBillId(creditControlSms.getMsisdn());
					stopAndStartDto.setFromType("7");
					stopAndStartDto.setBusiType("1006");
					stopAndStartDto.setBasicOrgId(basicOrgId);
					stopAndStartDto.setMvnoId(mvnoId);
					mvneCrmResponse = mvneCrmClient.stopAndStart(stopAndStartDto);
					log.info("Credit control start user's response is:"+mvneCrmResponse.get("code"));
				}
			}catch(Exception e) {
				//???????????????????????????CRM???????????????????????????????????????????????????
				log.error("Send sms gateway or call mvne-crm has exception:"+e.getMessage(),e);
				tag = true;
				creditControlSms.setStatus("3");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				CreditControlSmsErr creditControlSmsError = new CreditControlSmsErr();
				creditControlSmsError.setMsisdn(creditControlSms.getMsisdn());
				creditControlSmsError.setOperation(creditControlSms.getOperation());
				creditControlSmsError.setReason(creditControlSms.getReason());
				creditControlSmsError.setText(creditControlSms.getText());
				creditControlSmsError.setError_info("??????CRM?????????????????????:"+
				String.valueOf(mvneCrmResponse.get("message"))+";?????????????????????????????????:"+result);
				creditControlSmsError.setCreateDate(new Date());
				creditControlSmsErrMapper.insert(creditControlSmsError);
			}
			
			/**
			????????????CRM????????????????????????????????????????????????STATUS?????????"2",??????CreditControlSmsH???????????????CREDIT_CONTROL_SMS_H???
			?????????CRM???????????????????????????????????????????????????STATUS???????????????3????????????CreditControlSmsError???????????????CREDIT_CONTROL_SMS_ERR???
			*/
			String crmCode = String.valueOf(mvneCrmResponse.get("code"));
			if("200".equals(crmCode) && "SUCCESS".equals(result)) {
				creditControlSms.setStatus("2");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				CreditControlSmsH creditControlSmsH = new CreditControlSmsH();
				creditControlSmsH.setMsisdn(creditControlSms.getMsisdn());
				creditControlSmsH.setOperation(creditControlSms.getOperation());
				creditControlSmsH.setReason(creditControlSms.getReason());
				creditControlSmsH.setStatus(creditControlSms.getStatus());
				creditControlSmsH.setText(creditControlSms.getText());
				creditControlSmsH.setCreateDate(creditControlSms.getCreateDate());
				creditControlSmsH.setModifyDate(creditControlSms.getModifyDate());
				creditControlSmsHMapper.insert(creditControlSmsH);
			}
			else {
				if(tag == false) {
					//?????????????????????????????????????????????????????????????????????????????????
					creditControlSms.setStatus("3");
					creditControlSms.setModifyDate(new Date());
					this.baseMapper.updateById(creditControlSms);
					CreditControlSmsErr creditControlSmsError = new CreditControlSmsErr();
					creditControlSmsError.setMsisdn(creditControlSms.getMsisdn());
					creditControlSmsError.setOperation(creditControlSms.getOperation());
					creditControlSmsError.setReason(creditControlSms.getReason());
					creditControlSmsError.setText(creditControlSms.getText());
					creditControlSmsError.setError_info("??????CRM?????????????????????:"+
					String.valueOf(mvneCrmResponse.get("message"))+";?????????????????????????????????:"+result);
					creditControlSmsError.setCreateDate(creditControlSms.getCreateDate());
					creditControlSmsErrMapper.insert(creditControlSmsError);
				}
			}
		}
	}

	@Override
	public void dealCreditControlSms(int params) throws Exception{
		LambdaQueryWrapper<CreditControlSms> queryWrapper = new LambdaQueryWrapper<>(); 
		queryWrapper.eq(CreditControlSms::getOperation, "0");
		queryWrapper.eq(CreditControlSms::getStatus, "0");
		queryWrapper.last("limit "+params);
		List<CreditControlSms> creditControlSmsList = this.baseMapper.selectList(queryWrapper);
		for(CreditControlSms creditControlSms : creditControlSmsList) {
			boolean tag = false;
			String result = "";
			try {
				creditControlSms.setStatus("1");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				//TODO:???????????????????????????????????????????????????
				if("1".equals(pushSms)) {
					result = sendSmsByHttpPost(creditControlSms);
				}
				else {
					result = "SUCCESS";
				}
				log.info("????????????????????????:"+result);
				if("OK".equals(result) || "ok".equals(result)) {
					result = "SUCCESS";
				}
			}catch(Exception e) {
				//?????????????????????????????????????????????????????????
				log.error("Send sms gateway has exception:"+e.getMessage(),e);
				tag = true;
				creditControlSms.setStatus("3");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				CreditControlSmsErr creditControlSmsError = new CreditControlSmsErr();
				creditControlSmsError.setMsisdn(creditControlSms.getMsisdn());
				creditControlSmsError.setOperation(creditControlSms.getOperation());
				creditControlSmsError.setReason(creditControlSms.getReason());
				creditControlSmsError.setText(creditControlSms.getText());
				creditControlSmsError.setError_info(result);
				creditControlSmsError.setCreateDate(new Date());
				creditControlSmsErrMapper.insert(creditControlSmsError);
			}
			
			
			/**
			?????????????????????????????????????????????????????????STATUS?????????"2"?????????CreditControlSmsH???????????????CREDIT_CONTROL_SMS_H???
			?????????????????????????????????STATUS????????????3????????????CreditControlSmsError???????????????CREDIT_CONTROL_SMS_ERR???
			*/
			//???????????????????????????????????????????????????????????????????????????
			if("SUCCESS".equals(result)) {
				creditControlSms.setStatus("2");
				creditControlSms.setModifyDate(new Date());
				this.baseMapper.updateById(creditControlSms);
				CreditControlSmsH creditControlSmsH = new CreditControlSmsH();
				creditControlSmsH.setMsisdn(creditControlSms.getMsisdn());
				creditControlSmsH.setOperation(creditControlSms.getOperation());
				creditControlSmsH.setReason(creditControlSms.getReason());
				creditControlSmsH.setStatus(creditControlSms.getStatus());
				creditControlSmsH.setText(creditControlSms.getText());
				creditControlSmsH.setCreateDate(creditControlSms.getCreateDate());
				creditControlSmsH.setModifyDate(creditControlSms.getModifyDate());
				creditControlSmsHMapper.insert(creditControlSmsH);
			}
			else {
				if(tag == false) {
					creditControlSms.setStatus("3");
					creditControlSms.setModifyDate(new Date());
					this.baseMapper.updateById(creditControlSms);
					CreditControlSmsErr creditControlSmsError = new CreditControlSmsErr();
					creditControlSmsError.setMsisdn(creditControlSms.getMsisdn());
					creditControlSmsError.setOperation(creditControlSms.getOperation());
					creditControlSmsError.setReason(creditControlSms.getReason());
					creditControlSmsError.setText(creditControlSms.getText());
					creditControlSmsError.setError_info(result);
					creditControlSmsError.setCreateDate(new Date());
					creditControlSmsErrMapper.insert(creditControlSmsError);
				}
			}
		}
	}

	@Override
	public void dataDelete() throws Exception{
		LambdaQueryWrapper<CreditControlSms> deleteQueryWrapper_1 = new LambdaQueryWrapper<>();
		deleteQueryWrapper_1.eq(CreditControlSms::getStatus, "3");
		creditControlSmsMapper.delete(deleteQueryWrapper_1);
		LambdaQueryWrapper<CreditControlSms> deleteQueryWrapper_2 = new LambdaQueryWrapper<>();
		deleteQueryWrapper_2.eq(CreditControlSms::getStatus, "2");
		creditControlSmsMapper.delete(deleteQueryWrapper_2);
	}
	
	/**
	 * ??????vasp.siminn.is???UnkownHostException?????????
	public Object[] sendSMS(CreditControlSms creditControlSms) {
		//?????????????????????
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://vasp.siminn.is/axis2/services/SMSWS?wsdl");
		// ???????????????????????????????????????????????????
//		 client.getOutInterceptors().add(new ClientLoginInterceptor(username, password));
		
		Object[] objects = new Object[5];
		try {
			// invoke("?????????",??????1,??????2,??????3....);
			//?????????????????????????????????????????????????????????????????????????????????
			objects = client.invoke("sendSMS", username,password,creditControlSms.getMsisdn(),
					creditControlSms.getText(),"Yellow Mobi","no",false,"");
			System.out.println(objects[0].getClass());
			System.out.println("????????????:" + objects[0]);
		} catch (Exception e) {
			log.error("??????????????????????????????:",e);
		}
		return objects;
	}
	*/
	
	/**
	public Object[] sendSMS(String msisdn,String text) {
		//?????????????????????
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://vasp.siminn.is/axis2/services/SMSWS?wsdl");
		// ???????????????????????????????????????????????????
//		 client.getOutInterceptors().add(new ClientLoginInterceptor(username, password));
		
		Object[] objects = new Object[5];
		try {
			// invoke("?????????",??????1,??????2,??????3....);
			//?????????????????????????????????????????????????????????????????????????????????
			objects = client.invoke("sendSMS", username,password,msisdn,text,"Yellow Mobi","no",false);
			System.out.println(objects[0].getClass());
			System.out.println("????????????:" + objects[0]);
		} catch (Exception e) {
			log.error("??????????????????????????????:",e);
//			e.printStackTrace();
		}
		return objects;
	}
	 * @throws UnsupportedEncodingException 
	*/
	
	public String sendSmsByHttpGet(CreditControlSms creditControlSms) throws UnsupportedEncodingException {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("username", username);
		uriVariables.put("password", password);
		uriVariables.put("number", "00"+creditControlSms.getMsisdn());
		String encodeInfo = URLEncoder.encode(creditControlSms.getText(), "ISO-8859-1");
		uriVariables.put("text", encodeInfo);
		String result = restOperations.getForObject(url, String.class, uriVariables);
		return result;
	}
	
	/**
	public String sendSmsByHttpGet(String number,String text) {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("username", username);
		uriVariables.put("password", password);
		uriVariables.put("number", number);
		uriVariables.put("text", text);
		String result = restOperations.getForObject(url, String.class, uriVariables);
		return result;
	}
	*/
	
	/**
	 *????????????post???????????????????????????????????????
	public String sendSmsByHttpPost(String number,String text) {
//		String postUrl = pUrl + "xxxx";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String,String> p = new LinkedMultiValueMap<>();
		p.add("L", username);
		p.add("P", password);
		p.add("msisdn", number);
		p.add("T", text);
		HttpEntity< MultiValueMap<String,String>> entity = 
		new HttpEntity< MultiValueMap<String,String>>(p,headers);
		String result  = restOperations.postForObject(pUrl, entity, String.class);
		return result;
	}
	 * @throws IOException 
	*/
	
	/**
	//????????????????????????OK???
	public String sendSmsByHttpPost(String number,String text) throws IOException {
		URL url = new URL("http://vasp.siminn.is/smap/push");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setConnectTimeout(5 * 1000);
		// ????????????????????????
		httpCon.setReadTimeout(30 * 1000);
		//??????????????????????????????????????????????????????
		httpCon.setDoOutput(true);
		httpCon.setDoInput(true);
		httpCon.setUseCaches(false);
		httpCon.setRequestMethod("POST");
		httpCon.setInstanceFollowRedirects(true);
		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		String encodeInfo = URLEncoder.encode(text, "ISO-8859-1");
		//??????????????????????????????????????????????????? 
		String content = "L="+username+"&"+"P="+password+"&"+"msisdn="+number+"&"+"T="+encodeInfo;
		byte[] postData = content.getBytes("ISO-8859-1");
		httpCon.setRequestProperty("Content-Length", String.valueOf(postData.length));
		httpCon.connect();
		DataOutputStream out = new DataOutputStream(httpCon.getOutputStream());
		out.write(postData, 0, postData.length);
		out.flush();
		out.close();
		int responseCode = httpCon.getResponseCode();
		log.info("?????????:"+number+"???????????????HTTP??????????????????:"+responseCode);
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		reader.close();
		String sRet = httpCon.getResponseMessage();
		log.info("?????????:"+number+"???????????????????????????:"+sRet);
		httpCon.disconnect();
		return sRet;
	}
	*/
	
	//????????????????????????OK???
	public String sendSmsByHttpPost(CreditControlSms creditControlSms) throws IOException {
		//??????http??????
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
				.setSocketTimeout(3000).setConnectTimeout(3000).build();
		String encodeInfo = URLEncoder.encode(creditControlSms.getText(), "ISO-8859-1");
		String url = pUrl + "?L=" + username + "&P=" + password + "&msisdn=00" + creditControlSms.getMsisdn() + "&T=" + encodeInfo;

		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		CloseableHttpResponse response2 = httpclient.execute(httpPost);
		log.info("?????????:"+creditControlSms.getMsisdn()+"???????????????????????????:"+response2.getStatusLine());
		return "OK";
	}

	//????????????????????????OK???
	public String sendSmsByHttpsPost(CreditControlSms creditControlSms) throws IOException {
		//??????https??????
		String encodeInfo = URLEncoder.encode(creditControlSms.getText(), "ISO-8859-1");
		String url = pUrl + "?L=" + username + "&P=" + password + "&msisdn=00" + creditControlSms.getMsisdn() + "&T=" + encodeInfo;
//		String url = "http://vasp.siminn.is/smap/push?L=yellowbv&P=1yellow2&msisdn=001700000678&T=test";
		URL reqURL = new URL(null, url, new sun.net.www.protocol.https.Handler());//??????URL??????
		HttpsURLConnection httpsConn = (HttpsURLConnection) reqURL.openConnection();

		httpsConn.setDoOutput(true);
		httpsConn.setRequestMethod("POST");
		OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream());
		out.write("HTTPS Request");
		out.flush();
		out.close();

		//?????????????????????????????????????????????????????
		InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());

		//???????????????????????????????????????
		int respInt = insr.read();
		StringBuffer sb = new StringBuffer();
		while (respInt != -1) {
//			System.out.print((char) respInt);
			sb.append((char)respInt);
			respInt = insr.read();
		}
		log.info("?????????:"+creditControlSms.getMsisdn()+"???????????????????????????:"+sb.toString());
		return "OK";
	}

	@Override
	public void deleteUser(int params) throws Exception{
		String deleteUserMonth = DateUtil.getDeleteUserMonth();
		LambdaQueryWrapper<CmUserDetail> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CmUserDetail::getUserStatus, "02");
		queryWrapper.lt(CmUserDetail::getValidDate, deleteUserMonth);
		queryWrapper.gt(CmUserDetail::getExpireDate, DateUtil.getYYYYMMDDHHmmssDate());
		queryWrapper.select(CmUserDetail::getMsisdn);
		queryWrapper.last("limit "+params);
		List<CmUserDetail> cmUserDetailList = cmUserDetailMapper.selectList(queryWrapper);
		log.info("This credit control delete "+cmUserDetailList.size()+" user");
		for(CmUserDetail cmUserDetail : cmUserDetailList) {
			log.info("The current cm_user is:"+cmUserDetail.getMsisdn());
			//?????????????????????????????????CRM????????????
			DelUserRequestDTO delUserRequestDTO = new DelUserRequestDTO();
			delUserRequestDTO.setBillId(cmUserDetail.getMsisdn());
			delUserRequestDTO.setBasicOrgId(basicOrgId);
			delUserRequestDTO.setFromType("7");
			delUserRequestDTO.setMvnoId(mvnoId);
			
			MvneCrmResponse mvneCrmResponse = null;
			boolean tag = false;
			try {
				mvneCrmResponse = mvneCrmClient.deleteUser(delUserRequestDTO);
			}catch(Exception e) {
				log.error("call mvne crm delete user has exception:"+e.getMessage(),e);
				tag = true;
				CreditControlDelete creditControlDelete = new CreditControlDelete();
				creditControlDelete.setMsisdn(cmUserDetail.getMsisdn());
				creditControlDelete.setResult("The result code is:"+String.valueOf(mvneCrmResponse.get("code"))
				+" and the result message is:"+String.valueOf(mvneCrmResponse.get("message")));
				creditControlDelete.setCreateDate(new Date());
				creditControlDeleteMapper.insert(creditControlDelete);
			}
			
			if(tag == false) {
				log.info("The current cm_user's delete request's response is:"+mvneCrmResponse.get("code")+"$$$$$$$$");
				CreditControlDelete creditControlDelete = new CreditControlDelete();
				creditControlDelete.setMsisdn(cmUserDetail.getMsisdn());
				creditControlDelete.setResult("The result code is:"+String.valueOf(mvneCrmResponse.get("code"))
				+" and the result message is:"+String.valueOf(mvneCrmResponse.get("message")));
				creditControlDelete.setCreateDate(new Date());
				creditControlDeleteMapper.insert(creditControlDelete);
			}
		}
	}
	
//	public MvneCrmResponse stopAndStart(StopAndStartDTO stopAndStartDto) {
//		MvneCrmResponse mvneCrmResponse = mvneCrmClient.stopAndStart(stopAndStartDto);
//		return mvneCrmResponse;
//	}
}
