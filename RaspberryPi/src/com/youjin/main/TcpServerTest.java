package com.youjin.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpServerTest {
	private static ServerSocket serverSocket = null;
	private static Data data = Data.getInstance();
	public static void main(String[] args) {
		Serial s = new Serial();
		s.start();
		try {
			serverSocket = new ServerSocket(5000);
			System.out.println(getTime() + "서버가 준비되었습니다.");
		}catch(IOException e) {
			e.printStackTrace();
		}
		String op;
		while(true) {
			try {
				System.out.println(getTime() + "연결요청을 기다립니다.");
				Socket socket = serverSocket.accept();
				System.out.println(getTime() + socket.getInetAddress() + "로부터 연결요청이 들어왔습니다.");
				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);
				dos.writeInt(data.getTemperature());
				dos.writeInt(data.getWaterLine());
				dos.writeBoolean(data.isThief());
				dos.writeBoolean(data.isBtn());
				dos.writeBoolean(data.isEntry());
				System.out.println(getTime() + "데이터를 전송했습니다.");
				InputStream in = socket.getInputStream();
				DataInputStream dis = new DataInputStream(in);
				op = dis.readUTF();
				System.out.println(getTime() + op);
				switch(op) {
				case "o":
				case "f":
				case "q":
				case "w":
					Serial.out.write(op.charAt(0));
				}
				dis.close();
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date());
	}
}
