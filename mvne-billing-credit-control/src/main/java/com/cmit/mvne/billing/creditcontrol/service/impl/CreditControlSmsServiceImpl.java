/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

/**
 * @author jiangxm02
 *
 */
@Slf4j
@Service
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
				
				//TODO:调用短信网关对相关号码进行短信发送
				if("1".equals(pushSms)) {
					result = sendSmsByHttpPost(creditControlSms);
				}
				else {
					result = "SUCCESS";
				}
				log.info("发送短信网关结果:"+result);
				if("OK".equals(result) || "ok".equals(result)) {
					result = "SUCCESS";
				}
				if("1".equals(creditControlSms.getOperation())) {
					//TODO:调用CRM的停机接口
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
					//TODO:调用CRM的复机接口
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
				//调用短信网关或调用CRM的接口出现异常，将相关数据入错误表
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
				creditControlSmsError.setError_info("调用CRM接口结果信息为:"+
				String.valueOf(mvneCrmResponse.get("message"))+";调用短信网关结果信息为:"+result);
				creditControlSmsError.setCreateDate(new Date());
				creditControlSmsErrMapper.insert(creditControlSmsError);
			}
			
			/**
			根据调用CRM接口的结果再更新状态，成功则更新STATUS字段为"2",封装CreditControlSmsH对象插入到CREDIT_CONTROL_SMS_H表
			对调用CRM失败或调用短信网关失败的接口，更新STATUS字段值为“3”，封装CreditControlSmsError对象插入到CREDIT_CONTROL_SMS_ERR表
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
					//正常执行业务逻辑得不到正确的应答，就将相关信息入错误表
					creditControlSms.setStatus("3");
					creditControlSms.setModifyDate(new Date());
					this.baseMapper.updateById(creditControlSms);
					CreditControlSmsErr creditControlSmsError = new CreditControlSmsErr();
					creditControlSmsError.setMsisdn(creditControlSms.getMsisdn());
					creditControlSmsError.setOperation(creditControlSms.getOperation());
					creditControlSmsError.setReason(creditControlSms.getReason());
					creditControlSmsError.setText(creditControlSms.getText());
					creditControlSmsError.setError_info("调用CRM接口结果信息为:"+
					String.valueOf(mvneCrmResponse.get("message"))+";调用短信网关结果信息为:"+result);
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
				//TODO:调用短信网关对相关号码进行短信发送
				if("1".equals(pushSms)) {
					result = sendSmsByHttpPost(creditControlSms);
				}
				else {
					result = "SUCCESS";
				}
				log.info("发送短信网关结果:"+result);
				if("OK".equals(result) || "ok".equals(result)) {
					result = "SUCCESS";
				}
			}catch(Exception e) {
				//调用短信网关出现异常将相关数据入错误表
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
			根据调用短信网关接口结果，成功的就更新STATUS字段为"2"，封装CreditControlSmsH对象插入到CREDIT_CONTROL_SMS_H表
			对调用接口失败的，更新STATUS字段为“3”，封装CreditControlSmsError对象插入到CREDIT_CONTROL_SMS_ERR表
			*/
			//这里的判断条件需要知道他们返回值是什么才能进行判断
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
	 * 出现vasp.siminn.is的UnkownHostException的异常
	public Object[] sendSMS(CreditControlSms creditControlSms) {
		//创建动态客户端
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://vasp.siminn.is/axis2/services/SMSWS?wsdl");
		// 需要密码的情况需要加上用户名和密码
//		 client.getOutInterceptors().add(new ClientLoginInterceptor(username, password));
		
		Object[] objects = new Object[5];
		try {
			// invoke("方法名",参数1,参数2,参数3....);
			//全大写的参数都需要后续进行补充，根据冰岛电信给出的结果
			objects = client.invoke("sendSMS", username,password,creditControlSms.getMsisdn(),
					creditControlSms.getText(),"Yellow Mobi","no",false,"");
			System.out.println(objects[0].getClass());
			System.out.println("返回数据:" + objects[0]);
		} catch (Exception e) {
			log.error("调用短信网关出现异常:",e);
		}
		return objects;
	}
	*/
	
	/**
	public Object[] sendSMS(String msisdn,String text) {
		//创建动态客户端
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://vasp.siminn.is/axis2/services/SMSWS?wsdl");
		// 需要密码的情况需要加上用户名和密码
//		 client.getOutInterceptors().add(new ClientLoginInterceptor(username, password));
		
		Object[] objects = new Object[5];
		try {
			// invoke("方法名",参数1,参数2,参数3....);
			//全大写的参数都需要后续进行补充，根据冰岛电信给出的结果
			objects = client.invoke("sendSMS", username,password,msisdn,text,"Yellow Mobi","no",false);
			System.out.println(objects[0].getClass());
			System.out.println("返回数据:" + objects[0]);
		} catch (Exception e) {
			log.error("调用短信网关出现异常:",e);
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
	 *暂时因为post请求可能参数不正确无法使用
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
	//成功的返回值为“OK”
	public String sendSmsByHttpPost(String number,String text) throws IOException {
		URL url = new URL("http://vasp.siminn.is/smap/push");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setConnectTimeout(5 * 1000);
		// 设置连接超时时间
		httpCon.setReadTimeout(30 * 1000);
		//设置从主机读取数据超时（单位：毫秒）
		httpCon.setDoOutput(true);
		httpCon.setDoInput(true);
		httpCon.setUseCaches(false);
		httpCon.setRequestMethod("POST");
		httpCon.setInstanceFollowRedirects(true);
		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		String encodeInfo = URLEncoder.encode(text, "ISO-8859-1");
		//请求的参数，根据网关开发文档来组装 
		String content = "L="+username+"&"+"P="+password+"&"+"msisdn="+number+"&"+"T="+encodeInfo;
		byte[] postData = content.getBytes("ISO-8859-1");
		httpCon.setRequestProperty("Content-Length", String.valueOf(postData.length));
		httpCon.connect();
		DataOutputStream out = new DataOutputStream(httpCon.getOutputStream());
		out.write(postData, 0, postData.length);
		out.flush();
		out.close();
		int responseCode = httpCon.getResponseCode();
		log.info("给号码:"+number+"发送短信的HTTP连接返回码为:"+responseCode);
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		reader.close();
		String sRet = httpCon.getResponseMessage();
		log.info("给号码:"+number+"的短信的发送状态为:"+sRet);
		httpCon.disconnect();
		return sRet;
	}
	*/
	
	//成功的返回值为“OK”
		public String sendSmsByHttpPost(CreditControlSms creditControlSms) throws IOException {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
					.setSocketTimeout(3000).setConnectTimeout(3000).build();
			String encodeInfo = URLEncoder.encode(creditControlSms.getText(), "ISO-8859-1");
			String url = pUrl + "?L=" + username + "&P=" + password + "&msisdn=00" + creditControlSms.getMsisdn() + "&T=" + encodeInfo;

			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);

			CloseableHttpResponse response2 = httpclient.execute(httpPost);
			log.info("给号码:"+creditControlSms.getMsisdn()+"的短信的发送状态为:"+response2.getStatusLine());
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
			//构造销户接口对象，调用CRM销户接口
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
