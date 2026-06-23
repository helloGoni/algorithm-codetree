import java.io.*;
public class Main {
    public static void main(String[] args) {
        int a = 1, b = 5, c = 3;
        a = c;
        a = a + c;
        b = b - c;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
}