import java.util.*;
import java.io.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    static int yLen, xLen, k;
    static int[][] grid;

    static int sum = 0;

    static int[] dy = {-1, 0, 1, 0};
    static int[] dx = {0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        input();
        System.out.print(sum);
    }

    public static void input() throws Exception {
        st = new StringTokenizer(br.readLine());
        yLen = Integer.parseInt(st.nextToken());
        xLen = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        grid = new int[yLen + 3][xLen + 1];
        for(int i = 0 ; i < k ; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int doorDir = Integer.parseInt(st.nextToken());
            
            goGolem((i+1)*2, x, doorDir);
        }
    }

    public static void goGolem(int index, int x, int doorDir) {
        int y = 1;

        while(true) {
            if(canMove(y, x, 2)) {
                y++;
            } else if(canMove(y, x, 3) && canMove(y, x-1, 2) ) {
                x--;
                y++;
                doorDir = (doorDir + 3) % 4;
            } else if(canMove(y, x, 1) && canMove(y, x+1, 2)) {
                x++;
                y++;
                doorDir = (doorDir + 1) % 4;
            } else {
                break;
            }
        }

        if(y < 4) {
            for(int[] row : grid) Arrays.fill(row, 0);
        } else {
            grid[y][x] = index;
            for(int i = 0 ; i < 4 ; i++) {
                int ny = y + dy[i];
                int nx = x + dx[i];
                grid[ny][nx] = index;
                if(i == doorDir) grid[ny][nx]++;
            }
            sum += findRoute(y, x) - 2;
        }
    }

    // 홀수를 문으로 지정, 짝수는 일반 장소
    public static int findRoute(int y, int x) {
        int maxY = -1;
        boolean[][] visited = new boolean[100][100];
        Queue<int[]> q = new LinkedList<>(); 
        q.offer(new int[]{y, x});
        while(!q.isEmpty()) {
            int[] nowPos = q.poll();
            int nowNum = grid[nowPos[0]][nowPos[1]];
            maxY = Math.max(maxY, nowPos[0]);            

            for(int i = 0 ; i < 4 ; i++) {
                int ny = nowPos[0] + dy[i];
                int nx = nowPos[1] + dx[i];

                if(ny < 3 || nx < 1 || ny >= yLen + 3 || nx > xLen) continue;
                if(visited[ny][nx]) continue;
                if(grid[ny][nx] == 0) continue;

                int nextNum = grid[ny][nx];

                if((nowNum / 2 == nextNum / 2) || (nowNum % 2 == 1)) {
                    q.offer(new int[]{ny, nx});
                    visited[ny][nx] = true;
                }
            }
        }

        return maxY;
    }

    // HELPER
    public static boolean canMove(int y, int x, int dir) {
        for(int i = 0 ; i < 4 ; i++) {
            int ny = y + dy[dir] + dy[i];
            int nx = x + dx[dir] + dx[i];

            if(ny < 0 || nx < 1 || ny >= yLen + 3 || nx > xLen) return false;
            if(grid[ny][nx] > 0) return false;
        }
        return true;
    }
}