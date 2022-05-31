package com.zllh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.UUID;

public class Server2 {

    public static void main(String[] args) throws IOException {
        File target;    //接收到的文件保存的位置
        FileOutputStream save = null;  //将接收到的文件写入电脑
        FileInputStream in = null;     //读取穿送过来的数据文件
        ServerSocket server = null;    //服务器
        Socket socket = null;          //套接字
        String path="";

        //处理客户端的请求
        try {
            path = "D:\\Desktop\\Clipper\\";
            server = new ServerSocket(3090);    //服务端口
            //等待客户端的呼叫
            System.out.println("正在等待客户端的呼叫........");

            while(true) {
                String name = UUID.randomUUID().toString();
                target = new File(path+name+".txt");
                save = new FileOutputStream(target);

                socket = server.accept();   //阻塞
                System.out.println("接收到了客户端的请求");
                in = (FileInputStream) socket.getInputStream();
                //接收数据
                byte[] b = new byte[64];
                int n = in.read(b);
                int start = (int) System.currentTimeMillis();
                while (n != -1) {
                    save.write(b, 0, n);    //写入指定地方
                    n = in.read(b);
                }
                int end = (int) System.currentTimeMillis();
                System.out.println("接收成功,耗时：" + (end - start) + "毫秒");
            }
        }

        catch (Exception e) {
            System.out.println(e);
        }
        finally{
            in.close();
            socket.close();
            save.close();
            server.close();
        }
    }
}