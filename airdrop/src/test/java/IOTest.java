import java.io.*;

public class IOTest {

    public static void main(String[] args) throws IOException {

        File f = new File("D:\\Desktop\\1.txt");
        byte[] bytes = new byte[1024];
//        FileOutputStream os = new FileOutputStream(f);
//        String s = 1+"";
        FileInputStream is = new FileInputStream(f);
        is.read(bytes);
//        FileReader fr = new FileReader(f);
//        InputStreamReader ir = new InputStreamReader(is,"UTF-8");
//        BufferedReader br = new BufferedReader(ir);
//        System.out.println(br.readLine());
//        System.out.println(br.toString().length());
      for (byte b : bytes) {
           System.out.println("¸Ã×Ö½ÚÊÇ"+b);
            System.out.println(b==49);
        }
//        os.write(s.getBytes());
//        os.flush();
//        os.close();
//        ir.close();
//        br.close();
          is.close();
    }
}
