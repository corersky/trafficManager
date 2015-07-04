package com.yuandu.erp.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * httpclient工具类
 * 使用get\post方法提交请求
 *
 */
public class HttpClientUtil {
	
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
	
	/** 连接超时,默认10秒 */
	private static int HTTP_CONNECT_TIMEOUT = 10000;
	/** 读取超时,默认10秒 */
	private static int HTTP_SOCKET_TIMEOUT = 10000;
	
	/**
	 * 使用get方法发送请求
	 * 不抛出异常   在方法内部拦截所有异常
	 * @throws IOException 
	 */
	@SuppressWarnings({ "unused" })
	public static String doGetURL(String reqURL) throws Exception{
		
		long responseLength = 0;       //响应长度
		String responseContent = null; //响应内容
		HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例

		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,HTTP_CONNECT_TIMEOUT);//连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,HTTP_SOCKET_TIMEOUT);//数据传输时间

		HttpGet httpGet = new HttpGet(reqURL);           //创建org.apache.http.client.methods.HttpGet
		try{
			HttpResponse response = httpClient.execute(httpGet); //执行GET请求
			HttpEntity entity = response.getEntity();            //获取响应实体
			if(null != entity){
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return responseContent;
	}
	
	/**
	 * 使用post方法发送请求
	 * @throws IOException 
	 */
	public static String doPostURL(String reqURL,JSONObject body) throws Exception{
		
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,HTTP_CONNECT_TIMEOUT);//连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,HTTP_SOCKET_TIMEOUT);//数据传输时间

		HttpPost httpPost = new HttpPost(reqURL);
		try{
			httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			setParams(params,body);
			logger.info("请求参数："+params.toString());
			httpPost.setEntity(new StringEntity(URLEncodedUtils.format(params, "UTF-8")));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return responseContent;
	}

	@SuppressWarnings("unchecked")
	private static void setParams(List<NameValuePair> params, JSONObject body) {
		if(body != null){
			Set<String> set = body.keySet();
			for(String key:set){
				Object value = body.get(key);
				if(value!=null&&!StringUtils.isEmpty(value.toString())){
					params.add(new BasicNameValuePair(key,value.toString()));
				}
			}
		}
	}
	
}
