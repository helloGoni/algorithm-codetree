import java.util.*;
import java.io.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static int len;
    static String str;

    public static void main(String[] args) throws Exception {
        input();
        System.out.print(isOK() ? "Yes" : "No");
    }

    public static void input() throws Exception {
        len = Integer.parseInt(br.readLine());
        str = br.readLine();
    }

    public static boolean isOK() {
        if((len & 1) == 1) return false;

        int qCount = 0;
        int people = 0;

        for(int i = 0 ; i < len ; i++) {
            char c = str.charAt(i);

            if(c == '(') {
                people++;
            } else {
                people--;
                if(c == '?') qCount++;
            }
            
            if(people < 0) {
                if(qCount > 0) {
                    qCount--;
                    people += 2;
                } else return false;
            }
        }

        return people == 0;
    }
}