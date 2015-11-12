package com.cloudteam.hackathonServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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
		HttpServer httpServer = provider.createHttpServer(new InetSocketAddress(8000),1000);
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
	
    static class loginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the login";
            
            BufferedReader body = new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String data = body.readLine();
            System.out.println(data);
            System.out.println(t.getRequestMethod());
            
            String token = (String) t.getAttribute("token");
            Headers headers = t.getRequestHeaders();
            token= headers.getFirst("Content-type");
            System.out.println(token);
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class cartsHandler implements HttpHandler {
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
    
    static class foodHandler implements HttpHandler {
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
    
    static class foodsHandler implements HttpHandler {
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
    
    static class orderHandler implements HttpHandler {
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
    
    static class ordersHandler implements HttpHandler {
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
    
    static class admin_ordersHandler implements HttpHandler {
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