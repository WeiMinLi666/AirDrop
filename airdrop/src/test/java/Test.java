import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {

        Mutiply mutiply = new Mutiply();
        Thread t1 = new Thread(mutiply);
        Thread t2 = new Thread(mutiply);
        t1.start();
        t2.start();
    }
}



class Mutiply implements Runnable{

    ArrayList<Integer> countList = new ArrayList<Integer>();
    int count=1000;
    @Override
    public void run() {

        while(count>0){
            System.out.println("��"+count+"��");
            countList.add(count);
            count--;
            System.out.println("ѭ����"+countList.size()+"����");
        }
    }
}
