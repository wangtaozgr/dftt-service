package com.atao.dftt.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atao.base.util.StringUtils;

public class SmsUtils {
	static final String product = "Dysmsapi";
	static final String domain = "dysmsapi.aliyuncs.com";
	static final String accessKeyId = "LTAICOUbjVAjlWgP";
	static final String accessKeySecret = "nDhTBA6szhZUATIpaqdZEscxh6jbSQ";

	public static String sendSms(String mobilephone, String templateCode, String json) {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain(domain);
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", "汪涛");
		request.putQueryParameter("PhoneNumbers", mobilephone);
        request.putQueryParameter("TemplateCode", templateCode);
        if(StringUtils.isNotBlank(json)) {
            request.putQueryParameter("TemplateParam", json);
        }
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
			return response.getData();
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String sendMsg(String mobilephone, String templateCode, String username) {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain(domain);
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", "汪涛");
		request.putQueryParameter("PhoneNumbers", mobilephone);
        request.putQueryParameter("TemplateCode", templateCode);
		String json = "{\"username\":\""+username+"\"}";
        request.putQueryParameter("TemplateParam", json);
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
			return response.getData();
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ClientException, InterruptedException {
		String smsJson = "{\"username\":\"wangtaowinner\"}";
		String response =SmsUtils.sendSms("18714990981", "SMS_181210223", smsJson);
		System.out.println(response);
		//String response = sendSms("17755117870", "SMS_122280451", smsJson);
	}
}