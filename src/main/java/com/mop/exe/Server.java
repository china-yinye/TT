package com.mop.exe;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8003;

    public void server(){
        while (true) {
            try {
                ServerSocket server = new ServerSocket(PORT);
                System.out.println("等待客户端指令");
                Socket socket = server.accept();
                BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String instruction = is.readLine();
                System.out.println("收到客户端指令：" + instruction);
                is.close();
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.server();
    }

}
