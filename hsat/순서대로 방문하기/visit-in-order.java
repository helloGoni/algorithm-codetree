import java.util.*;
import java.io.*;

public class Main {


    static int[] dx = {1, 0, 0, -1};
    static int[] dy = {0, -1, 1, 0};

    static int count = 0, n;
    static int[][] grid;
    static boolean[][] visited;

    static List<int[]> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        input();
        int startY = list.get(0)[0];
        int startX = list.get(0)[1];
        visited[startY][startX] = true;
        dfs(startY, startX, 1);
        System.out.print(count);
    }
    
    public static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        int visitPos = Integer.parseInt(st.nextToken());

        grid = new int[n][n];
        visited = new boolean[n][n];
        for(int y = 0 ; y < n ; y++) {
            st = new StringTokenizer(br.readLine());
            for(int x = 0 ; x < n ; x++) {
                grid[y][x] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i = 0 ; i < visitPos ; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken()) - 1;
            int x = Integer.parseInt(st.nextToken()) - 1;

            list.add(new int[]{y ,x});
            grid[y][x] = -1 * i;
        }
    }

    public static void dfs(int y, int x, int target) {
        if(y == list.get(target)[0] && x == list.get(target)[1]) {
            if(target == list.size() -1) {
                count++;
                return;
            } else {
                target++;
            }
        }

        for(int i = 0 ; i < 4 ; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];

            if(ny < 0 || nx < 0 || ny >= n || nx >= n) continue;
            if(grid[ny][nx] == 1 || visited[ny][nx]) continue;

            grid[ny][nx] = 1;
            dfs(ny, nx, target);
            grid[ny][nx] = 0;
        }
    }
}