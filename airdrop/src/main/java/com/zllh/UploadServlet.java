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


//传输完整文件名的端口是3080

public class UploadServlet extends HttpServlet {

    public boolean flag=true;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("收到了请求");

        //申明传输中需要的流
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
//        获取ServletFileUpload对象
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("UTF-8");

        try {
                List<FileItem> fileItems = upload.parseRequest(req);
                for(FileItem item:fileItems){
                if (item.getFieldName().equals("upload"))
                {
                    state=item.getString();
                    System.out.println("从前端获取的内容是"+state);
                }
                }
                for (FileItem item : fileItems) {
                    if (item.isFormField()) {
                        if (item.getFieldName().equals("ip")) {
                            url = item.getString();
                            System.out.println("url是" + url);
                        } else if (item.getFieldName().equals("port")) {
                            port = Integer.parseInt(item.getString());
                            System.out.println("端口号是" + port);
                        }
                    }
                    else {
//                    获得上传的文件名
                        System.out.print("当前的状态是:");
                        System.out.println(state);
                        uploadFileName = item.getName();
                        long size = item.getSize();
                        System.out.println("上传的文件名是" + uploadFileName);
                        System.out.println("上传的文件大小是:" + size);
                        message = uploadFileName + "#" + size + "#";
                        open = item.getInputStream();
                        socket = new Socket(url, port);//创建socket
                        out = (FileOutputStream) socket.getOutputStream();
                        out.write(message.getBytes());

                        //根据传输文件的类型选择适当的流
                        if(uploadFileName.contains("txt")){
                            start = System.currentTimeMillis();
                            InputStreamReader isr = new InputStreamReader(open,"UTF-8");
                            int n = isr.read(txt);
                            System.out.println("要传输的东西是"+new String(txt,0,n));
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
//                    System.out.println("上传的文件名是"+uploadFileName);
//
                        else {
                            start = System.currentTimeMillis();

                            int n = open.read(bytes, 0, bytes.length);
                            System.out.println("要传输的东西是:" + new String(bytes, 0, n));
                            out.write(bytes, 0, n);
                            while (n != -1) {
                                System.out.println("看来文件比较大");
                                n = open.read(bytes, 0, bytes.length);
                                if (n != -1) {
                                    out.write(bytes, 0, n);
                                }
                                out.flush();
                            }
                             end = System.currentTimeMillis();
                        }

//                    out = (FileOutputStream)socket.getOutputStream();//创建文件输出流
//
//                    传输文件完整的文件名用户服务器端判定
//                    String message = 1+uploadFileName;
//                    byte[] uploadArray = message.getBytes();
//                    out1.write(uploadArray);
//
//                    System.out.println("输出流创建成功");
//
//                    //开始传送
//                    byte[] b = new byte[1024];
//                    int n = open.read(b);//首次传送
//

//
//                    System.out.println("开始将数据写入到数组中");
//                    while (n != -1) {
//                        out.write(b, 0, n);
//                        n = open.read(b);
//                    }
//
//                        System.out.println("要传输的文件的路径是:");
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
                        System.out.println("发送成功,耗时：" + hms);
                        System.out.println("谢谢使用");
//                        req.getRequestDispatcher("/success.jsp").forward(req,resp);
//                        修改标志位
                        System.out.println(state);
                        System.out.println("收到前端的标志位的值为"+state);
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
                            System.out.println("进入了修改标志位的代码！");
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
//        关闭流，释放资源
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

