package com.cloudteam.hackathonServer;

import java.io.*;
import java.net.*;
import java.util.*;

import com.cloudteam.handler.LoginHandler;

public class PooledConnectionHandler{  // implements Runnable
protected Socket connection;
protected static LinkedList<Socket> pool=new LinkedList<Socket>();
private float requestDelay = (float) 0.5;
public static String ErrorCode_EMPTY_REQUEST = "400 Bad Request\n" + "{\"code\":\"EMPTY_REQUEST\"\n \"message\": \"������Ϊ��\" \n}";
		public PooledConnectionHandler(){
			
		}
}
		/*public void handleConnection(){
			try{
				PrintWriter streamWriter=new PrintWriter(connection.getOutputStream());
				BufferedReader streamReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String fileToRead=streamReader.readLine();
				BufferedReader fileReader=new BufferedReader(new FileReader(fileToRead));
				String line=null;
				while((line=fileReader.readLine())!=null)
				streamWriter.println(line);
				fileReader.close();
				streamWriter.close();
				streamReader.close();
				if (!streamReader.equals("")) {
					System.out.println("Max Memory Available");
					System.out.println(Runtime.getRuntime().maxMemory() / 1024
							/ 1024 + "M");
					System.out.println("Memory in use");
					System.out.println(Runtime.getRuntime().totalMemory() / 1024
							/ 1024 + "M");
					//System.out.println(reader.);
					
					streamWriter.println("HTTP/1.1 200 ok");
	
					streamWriter.println("Date:" + new Date());
					streamWriter.println("Server:JServer");
					streamWriter.println("Access-Control-Allow-Origin:*");
					streamWriter.println("Content-Type: text/html; charset=UTF-8");
					streamWriter.println();
					String response = this.getValidRequest(connection);  //��ʼ�����ݴ������ݵõ�����
					System.out.println("response : " +response);
					streamWriter.println(response);//��������д�ػ�����
					streamWriter.flush();
					streamWriter.close();
					streamReader.close();
				}
			}
			catch(IOException e){
				System.out.println(""+e);
			}
		}
		
		@SuppressWarnings("finally")
		private String getValidRequest(Socket socket) {
			float second = (float) 0.0;
			boolean isGo = true;
			String response = null;
			try {
				BufferedReader streamReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while (!streamReader.ready()) {
					second += 0.01;
					if (second > this.requestDelay) {
						System.out.println("One Client Delayed" + socket);
						isGo = false;
						break;
					}
					Thread.sleep(10);
				}
				if (isGo == true) {
					String request_type = streamReader.readLine();  //��ȡ��һ��
					String request_info = getLastLineOfReader(streamReader);  //��ȡ���һ��
					if(request_info == "")  //������һ��Ϊ���򷵻�
						return ErrorCode_EMPTY_REQUEST;
					//��ʼ����
					if ("POST".equals(request_type) || "GET".equals(request_type) && "/login".equals(request_type)) { //Login
							LoginHandler lh = new LoginHandler();
							response = lh.LoginHand(request_info);
						}else {
							isGo = false;
						}
				}
	
			} catch (InterruptedException e) {
				e.printStackTrace();
	
			} catch (IOException e) {
				e.printStackTrace();
	
			} finally {
				if (isGo == true) {
					return response;
				} else {
					return "null";
				}
			}
		}
	
		//��ȡһ��reader�����һ��
		private String getLastLineOfReader(BufferedReader in) {
					String request_info_temp = null;
					String request_info = null;
					try {
						while( (request_info_temp = in.readLine()) != null) {
							request_info = request_info_temp;  //��ȡ���һ��
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return request_info;
		}
		
		//�ر���
		public void close(BufferedReader streamReader,PrintWriter streamWriter,Socket socket) {
			try {
				if (streamReader != null) {
					streamReader.close();
					streamReader = null;
				}
				if (streamWriter != null) {
					streamWriter.close();
					streamWriter = null;
				}
				if (socket != null) {
					socket.close();
					socket = null;
				}
				System.out.println("One Client Disconnected...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static void processRequest(Socket requestToHandle){
			synchronized(pool){
				pool.add(pool.size(),requestToHandle);
				pool.notifyAll();
			}
		}
		
		public void run(){
			while(true){
				synchronized(pool){
					while(pool.isEmpty()){
						try{
							pool.wait();
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					connection=(Socket)pool.remove(0);
				}
				handleConnection();
			}
		}*/
