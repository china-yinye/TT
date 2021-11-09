package com.mop.utils;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtil {

	private final static Logger log = Logger.getLogger(HttpUtil.class);

	public static void downloadPicture(String urlList,String path) {
		URL url = null;
		int imageNumber = 0;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			byte[] context=output.toByteArray();
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//okHttp params
	public static String okHttp(String decodeType,String params) throws IOException {
		int i = 1;
		if (decodeType.equals("code")){
			i = 2;
		}
		OkHttpClient client = new OkHttpClient().newBuilder()
				.followRedirects(false)
				.build();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart(decodeType, params)
				.build();
		Request request = new Request.Builder()
				.url("http://test-u-bmsrf.tt.cn/login/main_login/jiemi" + i)
				.method("POST", body)
				.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static String doPost(String httpUrl, String param) {
		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			URL url = new URL(httpUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);
			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
			os.write(param.getBytes());
			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {

				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置'''''''''''''''
				br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

				StringBuilder sbf = new StringBuilder();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			if (connection != null){
				connection.disconnect();
			}
		}
		return result;
	}

	public static String easyHttp_post(String url, Map<String,Object> param) throws ParseException, IOException{
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(35000)
				.setConnectionRequestTimeout(900000)
				.setSocketTimeout(900000)
				.build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
		if (param != null && param.size() > 0)
		{
			List<NameValuePair> params = new ArrayList<>();
			Set<Map.Entry<String,Object>> entrySet = param.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			result = EntityUtils.toString(httpEntity);
			response.close();;
			httpClient.close();
		}
		return result;
	}

	public static String easyHttp_get_noHeader(String url) throws ParseException, IOException
	{
		String string = "";
		//创建httpClient请求对象
		CloseableHttpClient  httpClient = HttpClients.createDefault();
		//声明get请求
		HttpGet httpGet = new HttpGet(url);
		//设置配置请求参数
		RequestConfig requestConfig = RequestConfig.custom()
				//连接服务器时间
				.setConnectTimeout(35000)
				//请求超时时间15分钟
				.setConnectionRequestTimeout(900000)
				//获取数据时间
				.setSocketTimeout(900000)
				.build();
		//为httpGet配置实例
		httpGet.setConfig(requestConfig);
		//发送请求
		CloseableHttpResponse response = httpClient.execute(httpGet);
		//判断状态码
		if(response.getStatusLine().getStatusCode()==200)
		{
			HttpEntity entity = response.getEntity();
			//使用工具类EntityUtils，从响应中取出实体表示的内容并转换成字符串
			string = EntityUtils.toString(entity, "utf-8");
		}
		//5.关闭资源
		response.close();
		httpClient.close();
		return string;
	}


	// 下载网络文件
	public static String downloadByNet(String link) throws MalformedURLException {
		int bytesum = 0;
		int byteread = 0;
		URL url = new URL(link);
		String apkName = "apk" + System.currentTimeMillis() + ".apk";
		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream("/data/apk_package/" + apkName);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apkName;
	}

	// 下载网络文件
	public static String downloadByNet(String link,String pathName) throws MalformedURLException {
		int bytesum = 0;
		int byteread = 0;
		URL url = new URL(link);
		//String apkName = "apk" + System.currentTimeMillis() + ".apk";
		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(pathName);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pathName;
	}

}