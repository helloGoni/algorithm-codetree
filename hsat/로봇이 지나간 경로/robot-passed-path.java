import java.util.*;
import java.io.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static char[][] grid;
    static int yLen, xLen;
    static List<int[]> points = new ArrayList<>();

    static int[] dy = {-1, 0, 1, 0};
    static int[] dx = {0, -1, 0, 1};
    static String dd = "^<v>";

    static int bestCount = Integer.MAX_VALUE;
    static char bestDir;
    static int bestY, bestX;
    static String bestRoute;

    public static void main(String[] args) throws Exception {
        input();
        findPos();

        for(int[] point : points) {
            calcPos(point);
        }

        sb.append(bestY + 1).append(" ").append(bestX + 1).append("\n");
        sb.append(bestDir).append("\n");
        sb.append(bestRoute);
        System.out.print(sb);
    }

    public static void input() throws Exception {
        st = new StringTokenizer(br.readLine().trim());
        yLen = Integer.parseInt(st.nextToken());
        xLen = Integer.parseInt(st.nextToken());
        grid = new char[yLen][xLen];

        for(int y = 0 ; y < yLen ; y++) {
            String str = br.readLine().trim();
            for(int x = 0 ; x < xLen ; x++) {
                grid[y][x] = str.charAt(x);
            }
        }
    }

    public static void findPos() {
        boolean visited[][] = new boolean[yLen][xLen];

        for(int x = xLen - 1 ; x >= 0 ; x--) {
            for(int y = yLen - 1 ; y >= 0 ; y--) {
                if(!visited[y][x] && grid[y][x] == '#') {
                    Queue<int[]> q = new LinkedList<>();
                    visited[y][x] = true;
                    q.offer(new int[]{y, x});

                    while(!q.isEmpty()) {
                        int[] now = q.poll();
                        int count = 0;

                        for(int i = 0 ; i < 4 ; i++) {
                            int ny = now[0] + dy[i];
                            int nx = now[1] + dx[i];

                            if(ny < 0 || nx < 0 || ny >= yLen || nx >= xLen) continue;
                            if(grid[ny][nx] == '.') continue;
                            count++;
                            if(visited[ny][nx]) continue;

                            visited[ny][nx] = true;
                            q.offer(new int[]{ny ,nx});
                        }

                        if(count == 1) {
                            points.add(new int[]{now[0], now[1]});
                        }
                    }
                    return;
                }
            }
        }
    }

    public static void calcPos(int[] point) {
        StringBuilder route = new StringBuilder();
        char startDir = 'X';
        int nowDir = -1;

        boolean visited[][] = new boolean[yLen][xLen];

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{point[0], point[1]});
        visited[point[0]][point[1]] = true;

        while(!q.isEmpty()) {
            int[] now = q.poll();

            for(int i = 0 ; i < 4 ; i++) {
                int ny = now[0] + dy[i] * 2;
                int nx = now[1] + dx[i] * 2;

                if(ny < 0 || nx < 0 || ny >= yLen || nx >= xLen) continue;

                if(grid[ny][nx] == '#' && !visited[ny][nx] && grid[now[0] + dy[i]][now[1] + dx[i]] == '#') {
                    visited[ny][nx] = true;
                    q.offer(new int[]{ny, nx});
                    
                    if(nowDir == -1) {
                        startDir = dd.charAt(i);
                        nowDir = i;
                        route.append("A");
                    } else {
                        if (i == nowDir) {
                            route.append("A");
                        } else if ( i == (nowDir + 1) % 4) {
                            route.append("LA");
                        } else if (i == (nowDir + 3) % 4) {
                            route.append("RA");
                        }
                        nowDir = i;
                    }
                    break;
                }
            }
        }

        boolean isBetter = false;

        if (route.length() < bestCount) {
            isBetter = true; 
        } else if (route.length() == bestCount) {
            if (point[0] > bestY) {
                isBetter = true;
            } else if (point[0] == bestY && point[1] > bestX) {
                isBetter = true;
            }
        }

        if (isBetter) {
            bestCount = route.length();
            bestY = point[0];
            bestX = point[1];
            bestDir = startDir;
            bestRoute = route.toString();
        }

    }
}
