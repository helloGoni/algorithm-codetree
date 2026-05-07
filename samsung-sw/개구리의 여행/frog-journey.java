import java.util.*;
import java.io.*;

public class Main {

    static final int INF = Integer.MAX_VALUE;

    static final int[] dy = {-1, 0, 0, 1};
    static final int[] dx = {0, -1, 1, 0};

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static int gridLen;
    static char[][] grid;


    public static class Data implements Comparable<Data>{
        int y, x, power, time;

        Data(int y, int x, int power, int time) {
            this.y = y;
            this.x = x;
            this.power = power;
            this.time = time;
        }

        @Override
        public int compareTo(Data d) {
            return this.time - d.time;
        }
    }

    public static void main(String[] args) throws Exception {
        input();
        System.out.print(sb.toString());
    }

    public static void input() throws Exception {
        gridLen = Integer.parseInt(br.readLine().trim());
        grid = new char[gridLen][gridLen];

        for(int y = 0 ; y < gridLen ; y++) {
            String str = br.readLine().trim();
            for(int x = 0 ; x < gridLen ; x++) {
                grid[y][x] = str.charAt(x);
            }
        }
        int queryCount = Integer.parseInt(br.readLine().trim());
        while(queryCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            outputDistance();
        }
    }


    public static void outputDistance() {
        int[][][] dist = new int[gridLen][gridLen][6];
        for(int y = 0 ; y < gridLen ; y++) {
            for(int x = 0 ; x < gridLen ; x++) {
                Arrays.fill(dist[y][x], INF);
            }
        }

        int startY = Integer.parseInt(st.nextToken()) - 1;
        int startX = Integer.parseInt(st.nextToken()) - 1;
        int endY = Integer.parseInt(st.nextToken()) - 1;
        int endX = Integer.parseInt(st.nextToken()) - 1;

        PriorityQueue<Data> pq = new PriorityQueue<>();
        dist[startY][startX][1] = 0;
        pq.offer(new Data(startY, startX, 1, 0));

        while(!pq.isEmpty()) {
            Data d = pq.poll();

            if(d.time > dist[d.y][d.x][d.power]) continue;
            if(d.y == endY && d.x == endX) {
                sb.append(d.time).append("\n");
                return;
            }
            
            for(int i = 0 ; i < 4 ; i++) {
                int ny = d.y;
                int nx = d.x;

                boolean canJump = true;

                for(int p = 1 ; p <= d.power ; p++) {
                    ny += dy[i];
                    nx += dx[i];

                    if(ny < 0 || nx < 0 || ny >= gridLen || nx >= gridLen || grid[ny][nx] == '#')  {
                        canJump = false;
                        break;
                    }
                }

                if(!canJump || grid[ny][nx] == 'S') continue;

                if(dist[ny][nx][d.power] > d.time + 1) {
                    dist[ny][nx][d.power] = d.time + 1;
                    pq.offer(new Data(ny, nx, d.power, d.time + 1));
                }
            }

            if(d.power < 5) {
                int cost = (d.power + 1) * (d.power + 1);
                if(dist[d.y][d.x][d.power+1] > d.time + cost) {
                    dist[d.y][d.x][d.power+1] = d.time + cost;
                    pq.offer(new Data(d.y, d.x, d.power+1, d.time + cost));
                }               
            }

            for(int power = 1 ; power < d.power ; power++) {
                if(dist[d.y][d.x][power] > d.time + 1) {
                    dist[d.y][d.x][power] = d.time + 1;
                    pq.offer(new Data(d.y, d.x, power, d.time + 1));
                }                   
            }
        }

        sb.append(-1).append("\n");
    }
}