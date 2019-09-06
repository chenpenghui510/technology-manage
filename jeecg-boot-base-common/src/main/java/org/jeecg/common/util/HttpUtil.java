package org.jeecg.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 为了响应景安公司接口调取restful风格，特此封装一个工具类。
 * 
 * 先简单介绍一下我在百度上搜到的关于http请求类型的信息和个人见解，
 * restful风格的HTTP请求类型：
 * 	OPTIONS：返回服务器针对特定资源所支持的HTTP请求方法。也可以利用向Web服务器发送'*'的请求来测试服务器的功能性。 
	HEAD：向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。这一方法可以在不必传输整个响应内容的情况下，
			就可以获取包含在响应消息头中的元信息。 
	TRACE：回显服务器收到的请求，主要用于测试或诊断。
	
	GET：向特定的资源发出请求。 （从服务器取出资源（一项或多项））。
	POST：向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。
			（POST请求可能会导致新的资源的创建和/或已有资源的修改。） 
	PUT：向指定资源位置上传其最新内容。在服务器更新资源（客户端提供改变后的完整资源）。 
	DELETE：请求服务器删除Request-URI所标识的资源。 （从服务器删除资源。）
	
	常用：GET、POST、PUT、DELETE
	分别对应:select、create、update、delete
	
 *	按照常理来说（参考黄老大写的请求工具类），我们至少需要写出来4种常用请求方式，至少对应5个方法（post含两种，一种类似表单提交，一种json请求）
 *	我TM写了3个方法，我就烦了，一直是复制粘贴，方法大体类似，没一点意思。于是，决定，再战commons-httpclient API
 * 也算是不负我的心意，让我研究出来一点通性。
 * 
 * 封装工具类如下：适用于restful风格请求，亦适用于我们一般风格的请求
 * 
 */


/**
 * @author along
 * @author 作者 E-mail:
 * @version 1.0 创建时间：2018年7月9日 下午9:43:07
 * @description 类说明:Http请求工具类
 */
public class HttpUtil {
	private Logger log=Logger.getLogger("HttpUtil.log:");

	/*------------------HTTP请求类型-----------------------*/
	public static final String POST_METHOD="POST";
	public static final String PUT_METHOD="PUT";
	public static final String GET_METHOD="GET";
	public static final String DELETE_METHOD="DELETE";

	/*--------------------常见请求头----------------------*/
	public static final String CHARSET=";charset=";
	public static final String FORM_CONTEN_TYPE="application/x-www-form-urlencoded";
	public static final String JSON_CONTENT_TYPE="application/json";

	// 连接超时时间限制
	private static final int CONNECTIONTIMEOUT=10000;//ali源码中该值设置的为10000，可能跟性能有关吧，不管它（单位:毫秒）
	// 数据读取超时限制
	private static final int SOTIMEOUT=10000;//ali源码中该值设置的为10000，可能跟性能有关吧，不管它（单位:毫秒）

	private HttpMethod method;
	private Map<String,String> requestHeader;

	public HttpMethod getMethod() {
		return method;
	}
	public void setMethod(HttpMethod method) {
		this.method = method;
	}
	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}
	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}
	// 添加请求头
	public void addRequestHeader(String headerName,String headerValue){
		if(this.requestHeader!=null&&!this.requestHeader.isEmpty()){
			this.requestHeader.put(headerName, headerValue);
		}else{
			this.requestHeader=new Hashtable<String,String>();
			requestHeader.put(headerName, headerValue);
		}
	}
	@SuppressWarnings("unchecked")
	public HttpUtil(String methodType, String url){
		if(url!=null&&!"".equals(url.trim())){
			this.method =  (HttpMethod)getHttpMethod(getMethodClass(methodType),url);
			if(this.method==null){
				throw new RuntimeException("SB你HTTP请求类型传错了！！！！");
			}
			String name = this.method.getName();
			if(POST_METHOD.equalsIgnoreCase(name)||PUT_METHOD.equalsIgnoreCase(name)){
				this.method=(EntityEnclosingMethod)method;
			}else{
				this.method=(HttpMethod)method;
			}
		}else{
			throw new RuntimeException("SB你没传url，你让我访问哪嘞安？？！！！");
		}
	}

	/**
	 * 适用restful风格的请求，适用一般get请求url参数拼接
	 * 我们只需要在创建HttpUtil这个对象的时候，指定http请求类型，url即可
	 * @return String:请求响应结果
	 */
	public String doRestful(){
		String str = "";
		HttpClient client = null;
		try{
			client=new HttpClient();
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(CONNECTIONTIMEOUT);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(SOTIMEOUT);
			if(method instanceof HttpMethod){
				HttpMethod httpMethod=this.method;
				// 设置请求头
				if(this.requestHeader!=null&&!this.requestHeader.isEmpty()){
					Set<String> set=this.requestHeader.keySet();
					for(String headerName:set){
						String headerValue = requestHeader.get(headerName);
						if(headerValue!=null&&!"".equals(headerValue.trim())){
							httpMethod.addRequestHeader(headerName, headerValue);
						}
					}
				}
				int statuCode = client.executeMethod(httpMethod);
				this.log.info("-----请求状态码:"+statuCode);
				if(statuCode==HttpStatus.SC_OK){
					InputStream in = httpMethod.getResponseBodyAsStream();
					str=getResult(in,"UTF-8");
				}
			}else{
				this.log.info("这是一个假的HttpClient请求方法:"+this.method);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(client!=null){
				SimpleHttpConnectionManager simpleManager = (SimpleHttpConnectionManager)client.getHttpConnectionManager();
				simpleManager.closeIdleConnections(0);//释放链接资源
			}
		}
		return str;
	}

	/**
	 * 适用我们平时一般格式 json请求
	 * @param object:能被转换为json的Object对象
	 * @return String:请求响应结果
	 */
	public String doJSON(Object object){
		String str = "";
		HttpClient client = null;
		try{
			client=new HttpClient();
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(CONNECTIONTIMEOUT);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(SOTIMEOUT);
			if(method instanceof EntityEnclosingMethod){
				EntityEnclosingMethod entityEnclosingMethod=(EntityEnclosingMethod)this.method;
				// 设置请求头
				if(requestHeader!=null&&!requestHeader.isEmpty()){
					Set<String> set=requestHeader.keySet();
					for(String headerName:set){
						String headerValue = requestHeader.get(headerName);
						if(headerValue!=null&&!"".equals(headerValue.trim())){
							entityEnclosingMethod.addRequestHeader(headerName, headerValue);
						}
					}
				}
				// 填充请求体
//				entityEnclosingMethod.setRequestBody(JSON.toJSONString(object));
				entityEnclosingMethod.setRequestEntity(new StringRequestEntity(JSON.toJSONString(object), "application/json", "UTF-8"));
				this.log.info("----HttpUtil.doJSON()---请求体:"+entityEnclosingMethod.getRequestEntity());
				int statuCode = client.executeMethod(entityEnclosingMethod);
				this.log.info("----请求状态码:"+statuCode);
				if(statuCode==HttpStatus.SC_OK){
					InputStream in = entityEnclosingMethod.getResponseBodyAsStream();
					str=getResult(in,"UTF-8");
				}
			}else{
				this.log.info("----这是一个假的HttpClient请求方法:"+this.method);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(client!=null){
				SimpleHttpConnectionManager simpleManager = (SimpleHttpConnectionManager)client.getHttpConnectionManager();
				simpleManager.closeIdleConnections(0);//释放链接资源
			}
		}
		return str;
	}

	/**
	 * 适用我们平时form表单提交数据格式请求
	 * @param paramMap:提交的表单数据
	 * @return String:请求响应结果
	 */
	public String doForm(Map<String,Object> paramMap){
		this.log.info("----form表单数据为:"+paramMap);
		String str = "";
		HttpClient client = null;
		try{
			client=new HttpClient();
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			// 设置连接超时时间(单位毫秒)
			managerParams.setConnectionTimeout(CONNECTIONTIMEOUT);
			// 设置读数据超时时间(单位毫秒)
			managerParams.setSoTimeout(SOTIMEOUT);
			if(this.method instanceof PostMethod){
				PostMethod post=(PostMethod)this.method;
				// 设置请求头
				if(requestHeader!=null&&!requestHeader.isEmpty()){
					Set<String> set=requestHeader.keySet();
					for(String headerName:set){
						String headerValue = requestHeader.get(headerName);
						if(headerValue!=null&&!"".equals(headerValue.trim())){
							post.addRequestHeader(headerName, headerValue);
						}
					}
				}
				post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
				// 填充请求参数
				Set<Entry<String, Object>> entrySet = paramMap.entrySet();
				for(Entry<String, Object> entry:entrySet){
					post.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
				}
				int statuCode = client.executeMethod(post);
				this.log.info("----请求状态码:"+statuCode);
				if(statuCode==HttpStatus.SC_OK){
					InputStream in = post.getResponseBodyAsStream();
					str=getResult(in,"UTF-8");
				}
			}else{
				this.log.info("----这是一个假的HTTP-POST请求:"+this.method);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(client!=null){
				SimpleHttpConnectionManager simpleManager = (SimpleHttpConnectionManager)client.getHttpConnectionManager();
				simpleManager.closeIdleConnections(0);//释放链接资源
			}
		}
		return str;
	}

	/**
	 * 其实这里我考虑可以封装成枚举，算了就这样吧
	 * @param methodType:<a>GET_METHOD</a>,<a>POST_METHOD</a>,<a>PUT_METHOD</a>,<a>DELETE_METHOD</a>
	 * @return Class
	 */
	@SuppressWarnings("rawtypes")
	//private Class getMethodClass(String methodType){
	// en,想来想去，感觉还是不将其私有化了，权限稍微放开一点，给后续使用者，可扩展的空间
	protected Class getMethodClass(String methodType){
		Class clz=null;
		try{
			switch(methodType){
				case GET_METHOD:{
					clz=GetMethod.class;
					break;
				}
				case POST_METHOD:{
					clz=PostMethod.class;
					break;
				}
				case PUT_METHOD:{
					clz=PutMethod.class;
					break;
				}
				case DELETE_METHOD:{
					clz=DeleteMethod.class;
					break;
				}
				default:{
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		this.log.info("----返回的请求方法类型class:"+clz);
		return clz;
	}
	/**
	 * 这里可以再次修改，把私有变共有，然后让使用者，自行封装方法请求方法，但是，那样我感觉还需要我再扩展工具类，太麻烦，没意思。
	 * 所以，我提供一个思路：我将此方法由私有，变成受保护权限，使用者，继承该类，继续后续扩展即可。
	 * @param cls
	 * @param url
	 * @return
	 */
	//private <T>T getHttpMethod(Class<T> cls,String url){
	protected <T>T getHttpMethod(Class<T> cls,String url){
		T t=null;
		try{
			if(cls!=null){
				if(url != null){
					// 一切的核心仅含在了这两行代码
					Constructor<T> declaredConstructor = cls.getDeclaredConstructor(String.class);
					t = declaredConstructor.newInstance(url);
				}else{
					Constructor<T> declaredConstructor = cls.getDeclaredConstructor();
					t = declaredConstructor.newInstance();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		this.log.info("----创建出来的对象为:"+t);
		return t;
	}
	/**
	 * 从输入流中读取数据
	 * @param in:输入流
	 * @param charset:字符编码
	 * @return
	 */
	private String getResult(InputStream in,String charset){
		String str="";
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new InputStreamReader(in,charset));
			StringBuilder sb=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			str=sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(reader!=null){
					reader.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return str;
	}
}
