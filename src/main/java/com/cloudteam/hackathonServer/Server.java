package com.cloudteam.hackathonServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.cloudteam.handler.LoginHandler;
import com.cloudteam.utils.RedisUtils;

public class Server {
	private static int PORT = 8888;
	public static String charsetString = "utf-8";
	private ServerSocket socket = null;
	private Socket clientSocket = null;

	public static void main(String[] agrs) {
		new RedisUtils();
		new Server();
		
	}

	public Server() {
		try {
			socket = new ServerSocket(Server.PORT);
			boolean isGO = true;
			while (isGO) {
				this.clientSocket = this.socket.accept();
				new Handler(this.clientSocket).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					this.socket.close();
					this.socket = null;
				}
				if (clientSocket != null) {
					this.clientSocket.close();
					this.clientSocket = null;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Handler extends Thread {
	private float requestDelay = (float) 0.5;

	private Socket socket;
	private BufferedReader reader;
	private PrintStream printer;
	public static String ErrorCode_EMPTY_REQUEST = "400 Bad Request\n" + "{\"code\":\"EMPTY_REQUEST\"\n \"message\": \"������Ϊ��\" \n}";
	Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			this.reader = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			this.printer = new PrintStream(this.socket.getOutputStream());

			if (!this.reader.equals("")) {
				System.out.println("Max Memory Available");
				System.out.println(Runtime.getRuntime().maxMemory() / 1024
						/ 1024 + "M");
				System.out.println("Memory in use");
				System.out.println(Runtime.getRuntime().totalMemory() / 1024
						/ 1024 + "M");
				this.printer.println("HTTP/1.1 200 ok");

				this.printer.println("Date:" + new Date());
				this.printer.println("Server:JServer");
				this.printer.println("Access-Control-Allow-Origin:*");
				this.printer.println("Content-Type: text/html; charset=UTF-8");
				this.printer.println();
				String response = this.getValidRequest();  //��ʼ�����ݴ������ݵõ�����
				System.out.println(response);
				this.printer.println(response);//��������д�ػ�����
				this.printer.flush();
				// IndeMatchInfo imi = new
				// IndeMatchInfo(22,22,2,2,22,(double)0.0,"S7-200");
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			this.close();
		}
	}
	@SuppressWarnings("finally")
	private String getValidRequest() {
		float second = (float) 0.0;
		boolean isGo = true;
		String response = null;
		try {
			while (!this.reader.ready()) {
				second += 0.01;
				if (second > this.requestDelay) {
					System.out.println("One Client Delayed" + this.socket);
					isGo = false;
					break;
				}
				Thread.sleep(10);
			}
			if (isGo == true) {
				String request_type = this.reader.readLine();  //��ȡ��һ��
				String readline = null;
				while((readline = this.reader.readLine()) != null)
				{
					if (readline.equals("")) {
						break;
					}
				}
				char[] buf = new char[1024];
				String data = "";
				int n = 0;
				while((n=this.reader.read(buf))!= -1){
					data = data.concat(new String(buf));
					break;
					
				}
				String request_info = data.substring(0, n);  //��ȡ���һ��
				
				System.out.println("request_type:" + request_type + "\n" + "request_info:" +request_info);
				
				if(request_info == "")  //������һ��Ϊ���򷵻�
					return ErrorCode_EMPTY_REQUEST;
				//��ʼ����
				if (request_type.contains("POST") && request_type.contains("/login")) { //Login
					System.out.println("11111111111111111");
					LoginHandler lh = new LoginHandler();
					System.out.println("2222222222");
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

	private void close() {
		try {
			if (this.reader != null) {
				this.reader.close();
				this.reader = null;
			}
			if (this.printer != null) {
				this.printer.close();
				this.printer = null;
			}
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}
			System.out.println("One Client Disconnected...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public static String ConvertStreamToString(BufferedReader in) { 
        StringBuilder sb = new StringBuilder();   
        String line = null;   
        try {   
            while ((line = in.readLine()) != null) {   
                sb.append(line + "/n");   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {
                in.close();   
            } catch (IOException e) {   
                e.printStackTrace(); 
            }   
        }   
        return sb.toString();   
	}*/
}

