import java.util.*;
import java.io.*;

public class Main {

    static int n;
    static int[] car;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws Exception {
        input();
        System.out.print(sb.toString());
    }
    
    public static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        car = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++)
            car[i] = Integer.parseInt(st.nextToken());
        
        Arrays.sort(car);

        while(q-- > 0) {
            int value = Integer.parseInt(br.readLine().trim());
            sb.append(find(value)).append("\n");
        }
     
    }

    public static int find(int target) {
        int leftIdx = 0, rightIdx = n-1;
        while(leftIdx <= rightIdx) {
            int mid = ( leftIdx + rightIdx ) / 2;
            if(car[mid] < target) {
                leftIdx = mid + 1;
            } else if(car[mid] > target) {
                rightIdx = mid - 1;
            } else {
                return (n - 1 - mid) * (mid);
            }
        }

        return 0;
    }
}