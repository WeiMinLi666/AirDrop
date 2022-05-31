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
        File target;    //���յ����ļ������λ��
        FileOutputStream save = null;  //�����յ����ļ�д�����
        FileInputStream in = null;     //��ȡ���͹����������ļ�
        ServerSocket server = null;    //������
        Socket socket = null;          //�׽���
        String path="";

        //����ͻ��˵�����
        try {
            path = "D:\\Desktop\\Clipper\\";
            server = new ServerSocket(3090);    //����˿�
            //�ȴ��ͻ��˵ĺ���
            System.out.println("���ڵȴ��ͻ��˵ĺ���........");

            while(true) {
                String name = UUID.randomUUID().toString();
                target = new File(path+name+".txt");
                save = new FileOutputStream(target);

                socket = server.accept();   //����
                System.out.println("���յ��˿ͻ��˵�����");
                in = (FileInputStream) socket.getInputStream();
                //��������
                byte[] b = new byte[64];
                int n = in.read(b);
                int start = (int) System.currentTimeMillis();
                while (n != -1) {
                    save.write(b, 0, n);    //д��ָ���ط�
                    n = in.read(b);
                }
                int end = (int) System.currentTimeMillis();
                System.out.println("���ճɹ�,��ʱ��" + (end - start) + "����");
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