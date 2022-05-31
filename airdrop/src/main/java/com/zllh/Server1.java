package com.zllh;


import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Server1 {

    public static boolean flag = true;

    public static void main(String[] args){
        File target;    //接收到的文件保存的位置
        String dir = "normal\\";
        FileOutputStream save = null;  //将接收到的文件写入电脑
        FileInputStream in = null;     //读取穿送过来的数据文件
        Socket socket = null;          //套接字
        ServerSocket server = null;    //服务器
        String path="D:\\Desktop\\receive\\";
        byte bytes[] = new byte[1024];
        byte nameBytes[] = new byte[128];

        //处理客户端的请求
        try {

//            //接受前文件的准备
//            String s = "D:\\Desktop\\receive\\r.txt";
//            target = new File(s);
//            save = new FileOutputStream(target);
            server = new ServerSocket(2017);    //服务端口
            System.out.println("等待客户端呼叫");
            while(flag){
            socket = server.accept();
            in = (FileInputStream) socket.getInputStream();
            
            int n = in.read(nameBytes);
//            支持传输txt,png,jpg三种格式
            String format = new String(nameBytes,0,n);
            System.out.println(format);
            String[] info = format.split("#");
            System.out.println("传输过来的长度是"+info.length);
            for (String s : info) {
                System.out.println(s);
            }
            if(info[0].contains("txt")){
                if(Long.parseLong(info[1])>65536){
                    dir = "temp\\";
                }
                target = new File(path+dir+UUID.randomUUID().toString()+".txt");
                save = new FileOutputStream(target);
                while(n!=-1){
                    n = in.read(bytes);
                    if(n!=-1) {
                        save.write(bytes, 0, n);
                    }
                }
                System.out.println("接受成功!");
            }
            else if(info[0].contains("png")){
                if(Long.parseLong(info[1])>65536){
                    dir = "temp\\";
                }
                target = new File(path+dir+UUID.randomUUID().toString()+".png");
                save = new FileOutputStream(target);
                while(n!=-1){
                    n = in.read(bytes);
                    if(n!=-1) {
                        save.write(bytes, 0, n);
                    }
                }
                System.out.println("接受成功!");
            }
            else{
                if(Long.parseLong(info[1])>65536){
                    dir = "temp\\";
                }
                target = new File(path+ UUID.randomUUID().toString()+".jpg");
                save = new FileOutputStream(target);
                while(n!=-1){
                    n = in.read(bytes);
                    if(n!=-1) {
                        save.write(bytes, 0, n);
                    }
                }
                System.out.println("接受成功");
            }}

            //等待客户端的呼叫

//            //接收数据
//            byte[] b = new byte[64];
//            int n = in.read(b);
//            int start = (int)System.currentTimeMillis();
//            while (n != -1) {
//                save.write(b, 0, n);    //写入指定地方
//                n = in.read(b);
//            }
//            int end = (int)System.currentTimeMillis();
//            System.out.println("接收成功,耗时：" + (end-start)+"毫秒");
//            socket.close();
//            server.close();
//            in.close();
//            save.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        finally{
            try {
                save.close();
                in.close();
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



