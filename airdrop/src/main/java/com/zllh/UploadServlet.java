package com.zllh;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


//���������ļ����Ķ˿���3080

public class UploadServlet extends HttpServlet {

    public boolean flag=true;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("�յ�������");

        //������������Ҫ����
        InputStream open=null;
        FileOutputStream out = null;
//        File file= null;
        Socket socket = null;
        String uploadFileName = "";
        String url = null;
        int port = 0;
        byte bytes[]=new byte[1024];
        String message ="";
        boolean flag = true;
        String state="";
        long start=0;
        long end=0;
        char[] txt=new char[1024];

        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ��ȡServletFileUpload����
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("UTF-8");

        try {
                List<FileItem> fileItems = upload.parseRequest(req);
                for(FileItem item:fileItems){
                if (item.getFieldName().equals("upload"))
                {
                    state=item.getString();
                    System.out.println("��ǰ�˻�ȡ��������"+state);
                }
                }
                for (FileItem item : fileItems) {
                    if (item.isFormField()) {
                        if (item.getFieldName().equals("ip")) {
                            url = item.getString();
                            System.out.println("url��" + url);
                        } else if (item.getFieldName().equals("port")) {
                            port = Integer.parseInt(item.getString());
                            System.out.println("�˿ں���" + port);
                        }
                    }
                    else {
//                    ����ϴ����ļ���
                        System.out.print("��ǰ��״̬��:");
                        System.out.println(state);
                        uploadFileName = item.getName();
                        long size = item.getSize();
                        System.out.println("�ϴ����ļ�����" + uploadFileName);
                        System.out.println("�ϴ����ļ���С��:" + size);
                        message = uploadFileName + "#" + size + "#";
                        open = item.getInputStream();
                        socket = new Socket(url, port);//����socket
                        out = (FileOutputStream) socket.getOutputStream();
                        out.write(message.getBytes());

                        //���ݴ����ļ�������ѡ���ʵ�����
                        if(uploadFileName.contains("txt")){
                            start = System.currentTimeMillis();
                            InputStreamReader isr = new InputStreamReader(open,"UTF-8");
                            int n = isr.read(txt);
                            System.out.println("Ҫ����Ķ�����"+new String(txt,0,n));
                            out.write(new String(txt,0,n).getBytes());
                            while(n!=-1){
                                n = isr.read(txt);
                                if(n!=-1) {
                                    out.write(new String(txt, 0, n).getBytes());
                                }
                            }
                            end = System.currentTimeMillis();
                        }
//                    req.setAttribute("uploadFileName111",uploadFileName);
//                    System.out.println("�ϴ����ļ�����"+uploadFileName);
//
                        else {
                            start = System.currentTimeMillis();

                            int n = open.read(bytes, 0, bytes.length);
                            System.out.println("Ҫ����Ķ�����:" + new String(bytes, 0, n));
                            out.write(bytes, 0, n);
                            while (n != -1) {
                                System.out.println("�����ļ��Ƚϴ�");
                                n = open.read(bytes, 0, bytes.length);
                                if (n != -1) {
                                    out.write(bytes, 0, n);
                                }
                                out.flush();
                            }
                             end = System.currentTimeMillis();
                        }

//                    out = (FileOutputStream)socket.getOutputStream();//�����ļ������
//
//                    �����ļ��������ļ����û����������ж�
//                    String message = 1+uploadFileName;
//                    byte[] uploadArray = message.getBytes();
//                    out1.write(uploadArray);
//
//                    System.out.println("����������ɹ�");
//
//                    //��ʼ����
//                    byte[] b = new byte[1024];
//                    int n = open.read(b);//�״δ���
//

//
//                    System.out.println("��ʼ������д�뵽������");
//                    while (n != -1) {
//                        out.write(b, 0, n);
//                        n = open.read(b);
//                    }
//
//                        System.out.println("Ҫ������ļ���·����:");
//                        System.out.println("D:\\Desktop\\send\\" + uploadFileName);
//                       file = new File("D:\\Desktop\\send\\" + uploadFileName);
//                        String buff="";
//                        String temp="";
//                        open = (FileInputStream)item.getInputStream();
//                        InputStreamReader isr = new InputStreamReader(open,"UTF-8");
//                        BufferedReader br = new BufferedReader(isr);
//                        while(true){
//                            temp=br.readLine();
//                            if(temp==null){
//                             break;
//                            }
//                            buff+=temp;
//                        }
                        String hms = String.valueOf(end - start);
                        req.setAttribute("time", hms);
                        System.out.println("���ͳɹ�,��ʱ��" + hms);
                        System.out.println("ллʹ��");
//                        req.getRequestDispatcher("/success.jsp").forward(req,resp);
//                        �޸ı�־λ
                        System.out.println(state);
                        System.out.println("�յ�ǰ�˵ı�־λ��ֵΪ"+state);
//                        if("finished".equals(state)){
//                            flag=false;
//                        }
//                        if(flag){
//                            resp.sendRedirect("/upload.do");
//                        }
//                        else {
//                            resp.sendRedirect("/success.jsp");
//                        }
                        if("finished".equals(state)){
                            System.out.println("�������޸ı�־λ�Ĵ��룡");
                            Server1.flag= false;
                            String msg = "exit";
                            req.setAttribute("eject",msg);
                            req.setAttribute("hms",hms);
//                            resp.sendRedirect("/success.jsp");
                            req.getRequestDispatcher("/success.jsp").forward(req,resp);
                        }
                        else{
                            String msg = "ok";
                             req.setAttribute("eject",msg);
                             req.setAttribute("hms",hms);
                             //resp.sendRedirect("/index.jsp");
                            req.getRequestDispatcher("/index.jsp").forward(req,resp);
                        }
                    }
                }
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
//        �ر������ͷ���Դ
        finally{
            if(open!=null) {
                open.close();
            }
            if(out!=null){
            out.close();
            }
            if(socket!=null) {
                socket.close();
            }
        }
    }

}

