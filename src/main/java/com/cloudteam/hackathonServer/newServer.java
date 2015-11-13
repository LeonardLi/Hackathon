/**
* 
* @author leonard
*
*/
package com.cloudteam.hackathonServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.SQLException;

import com.cloudteam.handler.Login;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class newServer  {
	public static int SuccessCode_LOGIN = 200;
	public static int ErrorCode_INVALID_ACCESS_TOKEN = 401;
	public static int ErrorCode_MALFORMED_JSON = 400;
	public static int ErrorCode_USER_AUTH_FAIL = 403;
	public static int ErrorCode_NOT_AUTHORIZED_TO_ACCESS_CART = 401;
	public static int ErrorCode_FOOD_OUT_OF_LIMIT = 403;
	public static int ErrorCode_FOOD_NOT_FOUND = 404;
	public static int ErrorCode_CART_NOT_FOUND = 404;
	public static int ErrorCode_FOOD_OUT_OF_STOCK = 403;
	public static int ErrorCode_ORDER_OUT_OF_LIMIT = 403;
	public static int ErrorCode_EMPTY_REQUEST = 400;
	
	public static String ErrorInfo_INVALID_ACCESS_TOKEN = "{\n\"code\":\"INVALID_ACCESS_TOKEN\",\n\"message\":\"��Ч������\"\n}";
	public static String ErrorInfo_MALFORMED_JSON = "{\n\"code\":\"MALFORMED_JSON\",\n\"message\":\"格式错误\"\n}";
	public static String ErrorInfo_USER_AUTH_FAIL = "{\n\"code\":\"USER_AUTH_FAIL\",\n\"message\":\"用户名或密码错误\"\n}";
	public static String ErrorInfo_NOT_AUTHORIZED_TO_ACCESS_CART = "{\"code\":\"NOT_AUTHORIZED_TO_ACCESS_CART\",\"meaasge\":��Ȩ�޷���ָ��������\"}";
	public static String ErrorInfo_FOOD_OUT_OF_LIMIT = "{\"code\":\"FOOD_OUT_OF_LIMIT\",\"message\":\"������ʳ����������������\"}";
	public static String ErrorInfo_FOOD_NOT_FOUND = "{\"code\":\"FOOD_NOT_FOUND\",\"message\":\"ʳ�ﲻ����\"}";
	public static String ErrorInfo_CART_NOT_FOUND = "{\"code\":\"CART_NOT_FOUND\",\"message\":\"篮子不存在\"}";
	public static String ErrorInfoFOOD_OUT_OF_STOCK = "{\"code\":\"FOOD_OUT_OF_STOCK\",\"message\":\"ʳ���治��\"}";
	public static String ErrorInfo_ORDER_OUT_OF_LIMIT = "{\"code\":\"ORDER_OUT_OF_LIMIT\",\"message\":\"ÿ���û�ֻ����һ��\"}";
	public static String ErrorInfo_EMPTY_REQUEST = "{\"code\":\"EMPTY_REQUEST\" \"message\": \"请求体为空\" }";
	
	public static void main(String[] args) throws Exception {
		
		start();
    }
	
	public static void  start() throws IOException{
		HttpServerProvider provider = HttpServerProvider.provider();
		
		//指定端口号和最大并发数
		HttpServer httpServer = provider.createHttpServer(new InetSocketAddress(8080),1000);
		
		//绑定处理器
		httpServer.createContext("/login", new loginHandler());
        httpServer.createContext("/carts", new cartsHandler()); 
        httpServer.createContext("/foods", new foodsHandler()); 
        httpServer.createContext("/food", new foodHandler()); 
        httpServer.createContext("/order", new orderHandler()); 
        httpServer.createContext("/orders", new ordersHandler()); 
        httpServer.createContext("/admin-orders", new admin_ordersHandler()); 
        httpServer.setExecutor(null);
        httpServer.start();
	}
	//继承myHandler,重写handle()接口，提供了
    static class loginHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException{
            String response = "This is the login";
            //获取请求体，
            URI uri = t.getRequestURI();
            System.out.println(uri.toString());
            
            //从URI中获得token
            String token = this.getToken(uri);
            
            //获取请求数据，非阻塞
            BufferedReader body = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String data = body.readLine();
            
            //获取请求方法 POST/GET
            System.out.println(t.getRequestMethod() + " " + data + "\n");
            
			try {
				Login lh = new Login();
				response = lh.LoginHand(data);
				System.out.println(response+ "\n");
				if(response.equals(ErrorInfo_EMPTY_REQUEST))  //空请求或格式错误400
				{
					t.sendResponseHeaders(ErrorCode_EMPTY_REQUEST, response.length());
				}
				else if(response.equals(ErrorInfo_MALFORMED_JSON))
				{
					t.sendResponseHeaders(ErrorCode_MALFORMED_JSON, response.length());
				}
				else if(response.equals(ErrorInfo_USER_AUTH_FAIL))   //用户名或密码错误403
				{
					t.sendResponseHeaders(ErrorCode_USER_AUTH_FAIL, response.length());
				}
				else
				{
					t.sendResponseHeaders(SuccessCode_LOGIN, response.length());
				}
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            //获取Header里任意属性
            Headers headers = t.getRequestHeaders();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class cartsHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the carts";
            System.out.println(t.getRequestMethod());
            //获取请求体，
            URI uri = t.getRequestURI();
            System.out.println(uri.toString());
            
            //从URI中获得token
            String token = this.getToken(uri);
            
            //获取请求数据，非阻塞
            BufferedReader body = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String data = body.readLine();
            
            //获取请求方法 POST/GET
            System.out.println(t.getRequestMethod() + " " + data + "\n");
            

            
        }
    }
    
    static class foodHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class foodsHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class orderHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class ordersHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class admin_ordersHandler extends myHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    
}

class myHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange t) throws IOException {
		// TODO Auto-generated method stub
		
	}
	public String getToken(URI uri){
		URI tokenUri = uri;
		tokenUri.getAuthority();
		
//解析URI结构
//		System.out.println("authority:"+tokenUri.getAuthority()+
//				"Userinfo:"+tokenUri.getUserInfo()+
//				"Path:"+tokenUri.getPath()+
//				"Query:"+tokenUri.getQuery()+//token所在的地方
//				"fragment:"+tokenUri.getFragment()+
//				"SchemeSpecificpart:"+tokenUri.getSchemeSpecificPart());
		
		
		String query = tokenUri.getQuery();
		if(query== null) return null;
		
		String token = query.substring(query.indexOf("=")+1,query.length());
		System.out.println(token);
		return token;
	}
}