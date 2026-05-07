import java.util.*;
import java.io.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static int fillIdx = 0, fillLen, time;
    static int[] fillArr;
    static int[][] grid = new int[5][5];

    static int[] dy = {-1, 0, 0, 1};
    static int[] dx = {0, -1, 1, 0};

    public static void main(String[] args) throws Exception {
        input();
        while(time-- > 0) {
            // 유물 하나도 못얻으면 즉시종료
            explorer();
        }
        System.out.print(sb.toString());
    }

    public static void input() throws Exception {
        st = new StringTokenizer(br.readLine().trim());
        time = Integer.parseInt(st.nextToken());
        fillLen = Integer.parseInt(st.nextToken());

        for(int y = 0 ; y < 5 ; y++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int x = 0 ; x < 5 ; x++) {
                grid[y][x] = Integer.parseInt(st.nextToken());
            }
        }

        fillArr = new int[fillLen];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0 ; i < fillLen ; i++) {
            fillArr[i] = Integer.parseInt(st.nextToken());
        }
    }

    public static void explorer() {
        List<int[]> bestList = new ArrayList<>();
        int[][] bestGrid = null;
        for(int t = 0 ; t < 3 ; t++) {
            for(int x = 0 ; x < 3 ; x++) {
                for(int y = 0 ; y < 3 ; y++) {
                    int[][] tempGrid = copyGrid(grid);

                    for(int i = 0 ; i <= t ; i++) {
                        spin(tempGrid, y, x);
                    }

                    List<int[]> list = getData(tempGrid);
                    if(list.size() > bestList.size()) {
                        bestList = list;
                        bestGrid = tempGrid;
                    }  
                }
            }
        }
        
        if(bestList.isEmpty()) return;

        grid = bestGrid;
        int count =  bestList.size();
        for(int[] pos : bestList) {
            grid[pos[0]][pos[1]] = 0;
        }
        fill();

        List<int[]> list = getData(grid);
        while(list.size() > 0) {
            count += list.size();
            for(int[] pos : list) {
                grid[pos[0]][pos[1]] = 0;
            }
            fill();
            list = getData(grid);
        }
        sb.append(count).append(" ");
    }

    public static void spin(int[][] target, int y, int x) {
        int[][] tmp = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tmp[i][j] = target[y + i][x + j];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                target[y + j][x + 2 - i] = tmp[i][j];
            }
        }
    }

    public static List<int[]> getData(int[][] grid) {
        boolean[][] visited = new boolean[5][5];
        List<int[]> list = new ArrayList<>();
        Queue<int[]> q = new LinkedList<>();

        for(int y = 0 ; y < 5 ; y++) {
            for(int x = 0 ; x < 5 ; x++) {
                if(!visited[y][x]) {
                    int count = 1;
                    visited[y][x] = true;
                    q.offer(new int[]{y, x});
                    list.add(new int[]{y, x});

                    int startValue = grid[y][x];

                    while(!q.isEmpty()) {
                        int[] cur = q.poll();

                        for(int i = 0 ; i < 4 ; i++) {
                            int ny = cur[0] + dy[i];
                            int nx = cur[1] + dx[i];

                            if(ny < 0 || ny >= 5 || nx < 0 || nx >= 5 || visited[ny][nx]) continue;
                            if(grid[ny][nx] == startValue) {
                                visited[ny][nx] = true;
                                list.add(new int[]{ny, nx});
                                q.offer(new int[]{ny, nx});
                                count++;
                            }
                        }
                    }

                    if(count < 3) {
                        while(count-- > 0) {
                            list.remove(list.size() - 1);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static void fill() {
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 4 ; y >= 0 ; y--) {
                if(grid[y][x] == 0) {
                    grid[y][x] = fillArr[fillIdx];
                    fillIdx = (fillIdx + 1) % fillLen;
                }
            }
        }
    }

    public static int[][] copyGrid(int[][] origin) {
        int[][] next = new int[5][5];
        for (int i = 0; i < 5; i++) {
            next[i] = origin[i].clone();
        }
        return next;
    }
}