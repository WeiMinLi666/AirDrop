public class StringTest {

    public static void main(String[] args) {

        String s = "1111-22222-3333-��ð�,����";
        String[] info = s.split("-");
        for (String s1 : info) {
            System.out.println(s1);
        }
    }
}
