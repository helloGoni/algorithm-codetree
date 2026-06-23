import java.util.*;
import java.io.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static int n;
    static int[][] grid;
    static boolean[][] visited;

    static int count = 0, size = 1;

    static int[] dy = {-1, 0, 0, 1};
    static int[] dx = {0, -1, 1, 0};

    public static void main(String[] args) throws Exception {
        input();    
        for(int y = 0 ; y < n ; y++) {
            for(int x = 0 ; x < n ; x++) {
                if(!visited[y][x]) {
                    visited[y][x] = true;
                    find(y, x);
                }
            }
        }
        sb.append(count).append(" ").append(size);
        System.out.print(sb);
    }

    public static void input() throws Exception {
        n = Integer.parseInt(br.readLine());
        grid = new int[n][n];
        visited = new boolean[n][n];
        for(int y = 0 ; y < n ; y++) {
            st = new StringTokenizer(br.readLine());
            for(int x = 0 ; x < n ; x++) {
                grid[y][x] = Integer.parseInt(st.nextToken());
            }
        }
    }

    public static void find(int y, int x) {
        int thisSize = 1;
        int point = grid[y][x];
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{y, x});
        while(!q.isEmpty()) {
            int[] now = q.poll();

            for(int i = 0 ; i < 4 ; i++) {
                int ny = now[0] + dy[i];
                int nx = now[1] + dx[i];

                if(ny < 0 || ny >= n || nx < 0 || nx >= n) continue;
                if(visited[ny][nx] || point != grid[ny][nx]) continue;

                visited[ny][nx] = true;
                thisSize++;
                q.offer(new int[]{ny, nx});

            }
        }

        if(thisSize > 3) count++;
        size = Math.max(size, thisSize);
    }   
}