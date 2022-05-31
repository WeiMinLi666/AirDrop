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
        File target;    //���յ����ļ������λ��
        String dir = "normal\\";
        FileOutputStream save = null;  //�����յ����ļ�д�����
        FileInputStream in = null;     //��ȡ���͹����������ļ�
        Socket socket = null;          //�׽���
        ServerSocket server = null;    //������
        String path="D:\\Desktop\\receive\\";
        byte bytes[] = new byte[1024];
        byte nameBytes[] = new byte[128];

        //����ͻ��˵�����
        try {

//            //����ǰ�ļ���׼��
//            String s = "D:\\Desktop\\receive\\r.txt";
//            target = new File(s);
//            save = new FileOutputStream(target);
            server = new ServerSocket(2017);    //����˿�
            System.out.println("�ȴ��ͻ��˺���");
            while(flag){
            socket = server.accept();
            in = (FileInputStream) socket.getInputStream();
            
            int n = in.read(nameBytes);
//            ֧�ִ���txt,png,jpg���ָ�ʽ
            String format = new String(nameBytes,0,n);
            System.out.println(format);
            String[] info = format.split("#");
            System.out.println("��������ĳ�����"+info.length);
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
                System.out.println("���ܳɹ�!");
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
                System.out.println("���ܳɹ�!");
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
                System.out.println("���ܳɹ�");
            }}

            //�ȴ��ͻ��˵ĺ���

//            //��������
//            byte[] b = new byte[64];
//            int n = in.read(b);
//            int start = (int)System.currentTimeMillis();
//            while (n != -1) {
//                save.write(b, 0, n);    //д��ָ���ط�
//                n = in.read(b);
//            }
//            int end = (int)System.currentTimeMillis();
//            System.out.println("���ճɹ�,��ʱ��" + (end-start)+"����");
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



