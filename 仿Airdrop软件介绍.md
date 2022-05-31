# 仿Airdrop软件介绍

## 关键功能

1. 支持与服务器在同同一局域网下的设备上传txt文件，上传.jpg和.png格式的照片。

   **补充1**：(为防止用户文件和服务器已有文件重名导致原来的文件被覆盖,将要保存到服务器的文件的文件名后加了随机生成的一串字符，以确保不会有覆盖)

   **补充2**(为防止大文件耗费服务器的资源,用户的文件在上传会进行判断，如大小大于65536，那么将它保存到服务器的tmp目录下,正常的文件则保存到normal目录下)

   **补充3**(用户在上传了之前可以选择是否还要上传,如果不上传,那么跳转到成功页面,如果继续上传那么转发到当前页面并根据是否上传成功在前端弹出提示消息,同时保持服务器一直开启)

2. 支持共享与服务器在同一局域网的设备的剪切版,将设备的剪切板内容保存到服务器的Clipper下的txt的文件中。

   **补充1**(复制剪切版不需要用户提供ip和服务器端口,通过读取resource目录下的config.properties获取,服务器管理人员可以根据需要修改，以增加程序的灵活性)

## 关键代码

### 功能1

1. 获取表单中的文件域(使用了apache的commons.io工具类)

   **解析请求后遍历表单中的input项,从中提取出用户输入的ip和端口**

````java
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
````

**获取要上传的文件的输出流**

```java
//                    获得上传的文件名
                        System.out.print("当前的状态是:");
                        System.out.println(state);
                        uploadFileName = item.getName();
                        long size = item.getSize();
                        System.out.println("上传的文件名是" + uploadFileName);
                        System.out.println("上传的文件大小是:" + size);
                        message = uploadFileName + "#" + size + "#";
//                     获得文件的输入流
                        open = item.getInputStream();
                        socket = new Socket(url, port);//创建socket
                        out = (FileOutputStream) socket.getOutputStream();
                        out.write(message.getBytes());
```

**根据不同类型的文件选择不同的流**

```jade
                       //根据传输文件的类型选择适当的流
                        //如果是文本文件,那么用inputstream包装普通的输入流,防止中文乱码                    
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
                        //如果是图片,那么直接使用获取的字节传输流即可
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
```

**防止文件同名**和**服务器根据文件大小将文件放到不同的目录下**以及**根据不同的文件定制不同的存储后缀**

```jade
            if(info[0].contains("txt")){
            //判断文件的大小
                if(Long.parseLong(info[1])>65536){
                    dir = "temp\\";
                }
              //给文件名加随机字符串和订制不同的后缀
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
            }
```

用户可以自主选择是否继续上传

```java
  //获取前端用户选择的信息,
			if (item.getFieldName().equals("upload"))
                {
                    state=item.getString();
                    System.out.println("从前端获取的内容是"+state);
                }

//利用获取的状态信息判断
//后端向前端传输eject,前端根据eject判断要不要弹出提示(用户是否继续上传)
 			if("finished".equals(state)){
                      System.out.println("进入了修改标志位的代码！");
                      Server1.flag= false;
                      String msg = "exit";
                      req.setAttribute("eject",msg);
                      req.setAttribute("hms",hms);
//                    resp.sendRedirect("/success.jsp");
                      req.getRequestDispatcher("/success.jsp").forward(req,resp);
                }
             else{
                      String msg = "ok";
                      req.setAttribute("eject",msg);
                      req.setAttribute("hms",hms);
                      //resp.sendRedirect("/index.jsp");
                      req.getRequestDispatcher("/index.jsp").forward(req,resp);
                   }
```

### 功能2

**剪切板实现**

1. 动态获取配置文件中的内容,即服务器管理员设定的服务器ip地址和端口号

```java
        
		动态获取
		//通过类加载器获取相应的资源
        InputStream is =Server2.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(is);
        int port  = Integer.parseInt(properties.getProperty("port"));
```

2. 客户端与服务器建立socket通信

   ```java
           FileOutputStream out = null;
           //通过类加载器获取相应的资源
           InputStream is =CopyServlet.class.getClassLoader().getResourceAsStream("config.properties");
           Properties properties = new Properties();
           properties.load(is);
           String ip = properties.getProperty("ip");
           int port  = Integer.parseInt(properties.getProperty("port"));
           Socket socket =new Socket(ip,port);
           try {
               out= (FileOutputStream) socket.getOutputStream();
               String text = req.getParameter("copy");
               System.out.println("收到的客户端内容是"+text);
               out.write(text.getBytes());
               System.out.println("传输成功");
               req.getRequestDispatcher("/success1.jsp").forward(req,resp);
           }
   ```

   3. 服务器响应阻塞等待客户端连接并将剪切板内容写到本地

      ```java
         path = "D:\\Desktop\\Clipper\\";
                  server = new ServerSocket(port);    //服务端口
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
                      System.out.println("接收成功,耗时:" + (end - start) + "毫秒");
                  }
      ```

## 单元测试

### 测试条件,ipad与服务器连接在同一局域网下

### 网页样式

### ![页面样式.png](https://s2.loli.net/2022/06/01/HkXsuKQPMBDV1Sl.jpg)

### 传输图片

*ipad显示(选择我还要传)*

![选择我还要传.png](https://s2.loli.net/2022/06/01/DHBnZ7CoJ2uhyda.jpg)

*ipad显示(选择上传完成)*

![选择上传完成.png](https://s2.loli.net/2022/06/01/LhOzRIFD9XUTeGj.jpg)

*服务器和客户端Servlet输出*

* 服务器

  ![收到图片后服务端控制台.png](https://s2.loli.net/2022/06/01/KLXQBaqJFMj6oDZ.png)

* 客户端

  ![收到图片后客户端控制台.png](https://s2.loli.net/2022/06/01/LsEr1h25nMwcAbu.png)

*最终存储到服务器中的样子*

![服务器收到图片.png](https://s2.loli.net/2022/06/01/CAqvIzE5LjKbYHx.png)

### 传输txt文件

*服务器和客户端Servlet输出*

* 用户页面

  ![上传txt文件前.png](https://s2.loli.net/2022/06/01/VWXQxNUSq5EC1lL.png)

* 服务器

  ![传输文件后服务器控制台输出.png](https://s2.loli.net/2022/06/01/DStlNJuX95IxqjE.png)

* 客户端

  ![传输txt文件客户端控制台输出.png](https://s2.loli.net/2022/06/01/a5Y6q3WkRTZ4gfm.png)

*最终存储到服务器中的样子*

![传输文件后服务器收到的.png](https://s2.loli.net/2022/06/01/2HxOevy5pofC4LU.png)

### 共享剪切板

*客户端*

![共享剪切板前.PNG](https://s2.loli.net/2022/06/01/rum8UtsYLB6eTqn.png)

![共享剪切板后.PNG](https://s2.loli.net/2022/06/01/jFhNKkV9IRDQUyP.png)

*服务端*

![服务器端收到的剪切板内容.png](https://s2.loli.net/2022/06/01/rbV2wQuMcvaxK95.png)

## 使用教程

### 项目结构

![项目结构.png](https://s2.loli.net/2022/06/01/PB6xkdVe3hOsTpK.png)

#### 项目构建(Maven),需要的依赖

![项目依赖1.png](https://s2.loli.net/2022/06/01/McdS3HYekImfaXl.png)

![项目依赖2.png](https://s2.loli.net/2022/06/01/12GJv6lNDBdX3Tw.png)

### Java类功简介

#### UploadServlet

用户传输文件时点击页面中的确定上传后请求会交由UploadServlet处理

#### CopyServlet

用户点击复制请求会交由CopyServlet处理

#### Server1

负责接收用户上传的文件并按照规定写入到服务器对应位置

#### Server2

负责接收用户贡献剪切板的请求，并把剪切板的内容写到服务器对应位置

### 使用流程

1. 将项目打成war包,配置tomcat，将网页发布到当前局域网的ip上

2. 启动两个Server类，等待用户发起请求

3. 用户登录到网页,url是局域网的ip地址，进程运行在8080端口上

4. 服务端要新建两个文件夹,我的是这样配置的

   上传文件时,会保存到D:\\Desktop\\receove下，大文件在tmp目录,小文件在normal目录

   复制剪切板时，剪切板的内容会保存到D:\Clipper\下



