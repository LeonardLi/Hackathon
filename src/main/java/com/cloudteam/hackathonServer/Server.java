package com.cloudteam.hackathonServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.cloudteam.handler.Login;
import com.cloudteam.utils.TokenGenerator;

public class Server {

	public static String charsetString = "utf-8";
	public static String APP_HOST = "0.0.0.0";
	public static int APP_PORT = 8081;
	public static String DB_HOST = "localhost";
	public static int DB_PORT = 3306;
	public static String DB_NAME = "eleme";
	public static String DB_USER = "root";
	public static String DB_PASS = "toor";
	public static String REDIS_HOST = "localhost";
	public static int REDIS_PORT = 6379;

	private ServerSocket socket = null;
	private Socket clientSocket = null;

	public static void main(String[] agrs) {
		TokenGenerator.getInstance();
		// TokenGenerator.getInstance().TokenMap
		new Server();
	}

	public Server() {
		try {
			socket = new ServerSocket(Server.APP_PORT);
			boolean isGO = true;
			while (isGO) {
				this.clientSocket = this.socket.accept();
				new Handler(this.clientSocket).start();
			}
		} catch (IOException e) {

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

				e.printStackTrace();
			}
		}
	}
}

class Handler extends Thread {

	public static String ErrorCode_EMPTY_REQUEST = "400 Bad Request\r\n"
			+ "{\r\n\"code\":\"EMPTY_REQUEST\",\r\n \"message\": \"请求体为空\" \r\n}";

	private float requestDelay = (float) 0.5;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream printer;
	public static String ErrorCode_INVALID_ACCESS_TOKEN = "401 Unauthorized\n{\n\"code\":\"INVALID_ACCESS_TOKEN\",\n\"message\":\"��Ч������\"\n}";

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
				// System.out.println("Max Memory Available");
				// System.out.println(Runtime.getRuntime().maxMemory() / 1024
				// / 1024 + "M");
				// System.out.println("Memory in use");
				// System.out.println(Runtime.getRuntime().totalMemory() / 1024
				// / 1024 + "M");
				// this.printer.println("HTTP/1.1 200 ok");

				// this.printer.println("Date:" + new Date());
				// this.printer.println("Server:JServer");
				// this.printer.println("Access-Control-Allow-Origin:*");
				// this.printer.println("Content-Type: text/html; charset=UTF-8");
				// this.printer.println();
				String response = this.getValidRequest();
				System.out.println(response);
				this.printer.println(response);
				this.printer.flush();
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
				String request_type = this.reader.readLine(); // ��ȡ��һ��
				String readline = null;
				String access_token = null;
				String cart_id = null;
				while ((readline = this.reader.readLine()) != null) {
					if (readline.equals("")) {
						break;
					}
				}
				char[] buf = new char[1024];
				String data = "";
				String request_info = null;
				int n = 0;
				//若请求体为空，这里会阻塞
				n = this.reader.read(buf);
				data = data.concat(new String(buf));
				if(n != -1){
				request_info = data.substring(0, n);
				
				System.out.println("request_type:" + request_type + "\n"
						+ "request_info:" + request_info);
				}
//				if (request_info == "")
//					return ErrorCode_EMPTY_REQUEST;

				if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/login")) { // Login
					// System.out.println("11111111111111111");
					Login lh = new Login();
					// System.out.println("2222222222");
					response = lh.LoginHand(request_info);
				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/foods")) {
					System.out.println("11111111111111111");
					response = "200 OK\r\n"
							+ "[\r\n{\"id\": 1, \"price\": 12, \"stock\": 99}\r\n,{\"id\": 2, \"price\": 10, \"stock\": 89}\r\n,{\"id\": 3, \"price\": 22, \"stock\": 91}\r\n]";
				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/carts")) {

				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/food")) {

				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/order")) {

				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/orders")) {

				} else if (request_type.contains("POST")
						|| request_type.contains("GET")
						&& request_type.contains("/admin-orders")) {

				}
				

			} else {
				response = "无对应的应答";
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (isGo == true) {
				if (response.equals(null))
					response = "返回为空";
				return response;
			} else {
				return "返回为空";
			}
		}
	}

	private String getLastLineOfReader(BufferedReader in) {

		String request_info_temp = null;
		String request_info = null;
		try {
			while ((request_info_temp = in.readLine()) != null) {
				request_info = request_info_temp;
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

}
