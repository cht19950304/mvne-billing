/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSmsTemp;
import com.cmit.mvne.billing.creditcontrol.entity.MvneCrmResponse;
import com.cmit.mvne.billing.creditcontrol.entity.SmsGatewayDto;
import com.cmit.mvne.billing.creditcontrol.job.service.impl.JobServiceImpl;
import com.cmit.mvne.billing.creditcontrol.remote.entity.StopAndStartDTO;
import com.cmit.mvne.billing.creditcontrol.service.ICreditControlSmsService;
import com.cmit.mvne.billing.creditcontrol.service.ICreditControlSmsTempService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangxm02
 *
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/creditControl")
public class CreditControlController {
	
	@Autowired
	private ICreditControlSmsService creditControlSmsServcieImpl;
	@Autowired
	private ICreditControlSmsTempService creditControlSmsTempServiceImpl;
	
	@Autowired
	private JobServiceImpl jobServiceImpl;
	@Value("${jobIds}")
	String jobIds;

	@PostMapping("smsList")
	@ResponseBody
	public MvneCrmResponse receiveSmsList(@RequestBody @Valid List<SmsGatewayDto> smsGatewayDtoList) {
		for (SmsGatewayDto smsGatewayDto : smsGatewayDtoList) {
			try {
				dealSmsGatewayDto(smsGatewayDto);
			}catch(Exception e) {
				log.error("信控接收号码:"+smsGatewayDto.getMsisdn()+"的短信提醒请求失败",e);
				return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
						+smsGatewayDto.getMsisdn()+"has been exception:"+e.getMessage());
			}
		}
		return new MvneCrmResponse().success();
	}
	
	@PostMapping("sms")
	@ResponseBody
	public MvneCrmResponse receiveSms(@RequestBody @Valid SmsGatewayDto smsGatewayDto) {
		return dealSmsGatewayDto(smsGatewayDto);
	}

	private MvneCrmResponse dealSmsGatewayDto(SmsGatewayDto smsGatewayDto) {
		CreditControlSms creditControlSms = new CreditControlSms();
		creditControlSms.setMsisdn(smsGatewayDto.getMsisdn());
		creditControlSms.setText(smsGatewayDto.getText());
		creditControlSms.setOperation(smsGatewayDto.getOperation());
		creditControlSms.setReason(smsGatewayDto.getReason());
		creditControlSms.setStatus("0");
		creditControlSms.setCreateDate(new Date());
		try {
			String reason = creditControlSms.getReason();
			String msisdn = creditControlSms.getMsisdn();
			String operation = creditControlSms.getOperation();
			if("1".equals(reason) || "11".equals(reason) || "12".equals(reason) || "13".equals(reason)) {
				if(!"0".equals(operation)) {
					return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
							+smsGatewayDto.getMsisdn()+"'s operation is wrong");
				}
				CreditControlSmsTemp creditControlSmsTemp = creditControlSmsTempServiceImpl.
						findCrediControlSmsTemp(msisdn, reason);
				if(null == creditControlSmsTemp) {
					creditControlSmsTempServiceImpl.createCreditControlSmsTemp(creditControlSms);
					creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
				}
				else {
					return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
							+smsGatewayDto.getMsisdn()+" has been repeated");
				}
			}
			else if("2".equals(reason) || "3".equals(reason)){
				if(!"1".equals(operation)) {
					return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
							+smsGatewayDto.getMsisdn()+"'s operation is wrong");
				}
				List<String> reasonList = new ArrayList<String>();
				reasonList.add("2");
				reasonList.add("3");
				CreditControlSmsTemp creditControlSmsTemp = creditControlSmsTempServiceImpl.
						findCreditControlSmsTempByList(msisdn, reasonList);
				if(null == creditControlSmsTemp) {
					creditControlSmsTempServiceImpl.createCreditControlSmsTemp(creditControlSms);
					creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
				}
				else {
					return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
							+smsGatewayDto.getMsisdn()+" has been repeated");
				}
			}else if("21".equals(reason)) {
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "1");
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "2");
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "3");
				creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
			}
			else if("31".equals(reason)) {
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "11");
				creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
			}
			else if("32".equals(reason)) {
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "12");
				creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
			}
			else if("33".equals(reason)) {
				creditControlSmsTempServiceImpl.deleteCreditControlSmsTemp(creditControlSms.getMsisdn(), "13");
				creditControlSmsServcieImpl.createCreditControlSms(creditControlSms);
			}
			else {
				return new MvneCrmResponse().fail().message("No such parameter");
			}
		}catch(Exception e) {
			log.error("信控接收号码:"+smsGatewayDto.getMsisdn()+"的短信提醒请求失败",e);
			return new MvneCrmResponse().fail().message("Credit control receive the sms request of:"
					+smsGatewayDto.getMsisdn()+"has been exception:"+e.getMessage());
		}
		return new MvneCrmResponse().success();
	}
	
	@GetMapping("smsTest")
	@ResponseBody
	public MvneCrmResponse sendSmsTest() {
		try {
			creditControlSmsServcieImpl.dealCreditControlSms(0);
			return new MvneCrmResponse().success();
		} catch (Exception e) {
			log.error("发送短信测试出现异常：",e);
			return new MvneCrmResponse().fail().message("调用短信网关发送短信出现异常"+e.getMessage());
		}
	}
	
	@GetMapping("pauseJob")
	@ResponseBody
	public MvneCrmResponse pauseQrtzJob() {
		try {
//			String jobIds = "15,16,17,18";
			jobServiceImpl.pause(jobIds);
			return new MvneCrmResponse().success();
		}catch(Exception e) {
			log.error("暂停定时任务出现异常:",e);
			return new MvneCrmResponse().fail().message("暂停定时任务出现异常:"+e.getMessage());
		}
	}
	
	@GetMapping("resumeJob")
	@ResponseBody
	public MvneCrmResponse resumeQrtzJob() {
		try {
//			String jobIds = "15,16,17,18";
			jobServiceImpl.resume(jobIds);
			return new MvneCrmResponse().success();
		}catch(Exception e) {
			log.error("恢复定时任务出现异常:",e);
			return new MvneCrmResponse().fail().message("恢复定时任务出现异常:"+e.getMessage());
		}
	}

	@GetMapping("test")
	@ResponseBody
	public String myTest() throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).setConnectTimeout(10000).build();

		HttpPost httpPost = new HttpPost("http://vasp.siminn.is/smap/push?L=yellowbv&P=1yellow2&msisdn=00354385200004&T=text");
		httpPost.setConfig(requestConfig);

		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		return "yes";
	}

	@GetMapping("post")
	@ResponseBody
	public String myPost() throws IOException {
		return "yes";
	}

	@GetMapping("ok")
	@ResponseBody
	public String ok() throws IOException {
		final OkHttpClient client = new OkHttpClient();

		HttpUrl url = HttpUrl.parse("http://vasp.siminn.is/smap/push?L=yellowbv&P=1yellow2&msisdn=00354385200004&T=text").newBuilder()
				.build();
		Request request = new Request.Builder()
				.url(url)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				log.info("Request:", "Is NOT sent " + request.toString() + " METHOD: " + request.method());
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				log.error("Request:", "Is sent " + response.toString());
			}


		});

		return "yes";
	}

	@GetMapping("yes")
	@ResponseBody
	public String yes() throws IOException, ExecutionException, InterruptedException {
		final CompletableFuture future = new CompletableFuture();

		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		HttpUrl url = HttpUrl.parse("http://vasp.siminn.is/smap/push?L=yellowbv&P=1yellow2&msisdn=00354385200004&T=text").newBuilder()
				.addQueryParameter("password", "123456")
				.addQueryParameter("username", "123456")
				.build();
		Request request = new Request.Builder()
				.url(url)
				.post(okhttp3.RequestBody.create(null, new byte[]{}))
				.build();
		client.newCall(request).enqueue(new Callback() {
											@Override
											public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
												System.out.println("Request:Is sent " + response.body().toString());
												future.complete("Hello World!");
											}

											@Override
											public void onFailure(@NotNull Call call, @NotNull IOException e) {
												e.printStackTrace();
											}
										}
		);
		future.get();

		return "yes";
	}
	
//	@PostMapping("stopAndStart")
//	@ResponseBody
//	public MvneCrmResponse stopAndStart(@RequestBody StopAndStartDTO stopAndStartDto) throws Exception {
//		MvneCrmResponse mvneCrmResponse = creditControlSmsServcieImpl.stopAndStart(stopAndStartDto);
//		return mvneCrmResponse;
//	}
}
