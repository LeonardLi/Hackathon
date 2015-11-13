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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

public class newServer  {
	public static void main(String[] args) throws Exception {
		
		start();
    }
	
	public static void  start() throws IOException{
		HttpServerProvider provider = HttpServerProvider.provider();
		
		//指定端口号和最大并发数
		HttpServer httpServer = provider.createHttpServer(new InetSocketAddress(8000),1000);
		
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
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the login";
            //获取请求体，
            URI uri = t.getRequestURI();
            System.out.println(uri.toString());
            
            //从URI中获得token
            String token = this.getToken(uri);
            
            //获取请求数据，非阻塞
            BufferedReader body = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String data = body.readLine();
            System.out.println(data);
            
            //获取请求方法 POST/GET
            System.out.println(t.getRequestMethod());
            
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
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
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