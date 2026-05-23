import java.util.*;
import java.io.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static int[][] grid;
    static int gridLen, warriorCount;
    static int curAttacks, curMoves, curRocks;

    static Point medusaPos, parkPos;
    static List<Warrior> warriors = new ArrayList<>();

    static Stack<Point> route = new Stack<>();

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static int[] dy2 = {0, 0, -1, 1};
    static int[] dx2 = {-1, 1, 0, 0};

    static class Point {
        int y, x;
        Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    static class Warrior {
        Point pos;
        boolean isRock = false;
        boolean isDie = false;

        Warrior(Point pos) {
            this.pos = pos;
        }

        public void move(int[][] isSafe) {
            if(isRock || isDie) return;
            //첫 번째 이동
            int dirY = Integer.signum(medusaPos.y - pos.y);
            int dirX = Integer.signum(medusaPos.x - pos.x);

            int ny = pos.y + dirY;
            int nx = pos.x + dirX;
            
            if(dirY != 0 && ny >= 0 && ny < gridLen && isSafe[ny][pos.x] >= 0) {
                pos.y = ny;
                curMoves++;
            } else if(dirX != 0 && nx >= 0 && nx < gridLen && isSafe[pos.y][nx] >= 0) {
                pos.x = nx;
                curMoves++;
            }

            //두 번째 이동
            dirY = Integer.signum(medusaPos.y - pos.y);
            dirX = Integer.signum(medusaPos.x - pos.x);

            ny = pos.y + dirY;
            nx = pos.x + dirX;
            
            if(dirX != 0 && nx >= 0 && nx < gridLen && isSafe[pos.y][nx] >= 0) {
                pos.x = nx;
                curMoves++;
            } else if(dirY != 0 && ny >= 0 && ny < gridLen && isSafe[ny][pos.x] >= 0) {
                pos.y = ny;
                curMoves++;
            }

            if(medusaPos.y == pos.y && medusaPos.x == pos.x) {
                this.isDie = true;
                curAttacks++;
                return;
            }
        }

    }
    public static void main(String[] args) throws Exception {
        input();
        if(!findMedusaRoute()) {
            System.out.print("-1");
            return;
        }
        doTurn();
        System.out.print(sb);
    }
    public static void input() throws Exception {
        st = new StringTokenizer(br.readLine());
        gridLen = Integer.parseInt(st.nextToken());
        warriorCount = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        medusaPos = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        parkPos = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));

        st = new StringTokenizer(br.readLine());
        for(int i = 0 ; i < warriorCount ; i++) {
            Point p = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            warriors.add(new Warrior(p));
        }
        grid = new int[gridLen][gridLen];
        for(int y = 0 ; y < gridLen ; y++) {
            st = new StringTokenizer(br.readLine());
            for(int x = 0 ; x < gridLen ; x++) {
                grid[y][x] = Integer.parseInt(st.nextToken());
            }
        }
    }

    public static boolean findMedusaRoute() {
        Queue<Point> q = new LinkedList<>();
        boolean[][] visited = new boolean[gridLen][gridLen];
        Point[][] before = new Point[gridLen][gridLen];

        q.offer(medusaPos);
        visited[medusaPos.y][medusaPos.x] = true;     

        while(!q.isEmpty()) {
            int size = q.size();
            for(int i = 0 ; i < size ; i++) {
                Point now = q.poll();
                
                for(int d = 0 ; d < 4 ; d++) {
                    int ny = now.y + dy[d];
                    int nx = now.x + dx[d];

                    if(ny < 0 || nx < 0 || ny >= gridLen || nx >= gridLen) continue;
                    if(visited[ny][nx] || grid[ny][nx] == 1) continue;

                    q.offer(new Point(ny, nx));
                    visited[ny][nx] = true;
                    before[ny][nx] = now;

                    if(ny == parkPos.y && nx == parkPos.x) {
                        Point prev = now;
                        while(prev != null) {
                            route.add(prev);
                            prev = before[prev.y][prev.x];
                        }
                        route.pop();

                        return true;
                    }
                }
            }
        }
        return false;
    }   

    public static void doTurn() {
        while(!route.isEmpty()) {
            Point now = route.pop();
            medusaPos.y = now.y;
            medusaPos.x = now.x;

            for (Warrior w : warriors) {
                if (!w.isDie && w.pos.y == medusaPos.y && w.pos.x == medusaPos.x) {
                    w.isDie = true;
                }
            }
            reset();
            warriorMove(medusaLook());
            output();
        }
        sb.append("0\n");
    }

    public static void reset() {
        curAttacks = 0;
        curMoves = 0;
        curRocks = 0;
    }

    public static int[][] medusaLook() {
        List<Warrior>[][] wMap = new ArrayList[gridLen][gridLen];
        for (int i = 0; i < gridLen; i++) {
            for (int j = 0; j < gridLen; j++) wMap[i][j] = new ArrayList<>();
        }
        for (Warrior w : warriors) {
            if (!w.isDie) wMap[w.pos.y][w.pos.x].add(w);
        }

        int bestDir = 0;        
        List<Warrior> bestRocks = new ArrayList<>();
        int[][] bestState = new int[gridLen][gridLen];
        
        for(int i = 0 ; i < 4 ; i++) {
            List<Warrior> curRocks = new ArrayList<>();
            int[][] curState = new int[gridLen][gridLen];
            checkSight(i, wMap, curState, curRocks);
            
            if(curRocks.size() > bestRocks.size()) {
                bestDir = i;
                bestRocks = curRocks; 
                bestState = curState; 
            }
        }

        curRocks += bestRocks.size();

        for (Warrior w : bestRocks) {
            w.isRock = true;
        }

        return bestState;
    }

    public static void checkSight(int dir, List<Warrior>[][] wMap, int[][] state, List<Warrior> rocks) {
        // 센터
        int cy = medusaPos.y;
        int cx = medusaPos.x;

        // dist -> 거리, k -> 얼마나 퍼지는지
        for (int dist = 1, k = 1; dist <= gridLen; dist++, k++) {
            // 특정 위치의 중앙 점에서
            int targetY = cy + dy[dir] * dist;
            int targetX = cx + dx[dir] * dist;

            if (targetY < 0 || targetY >= gridLen || targetX < 0 || targetX >= gridLen) {
                break;
            }

            for (int i = -k; i <= k; i++) {
                int y = targetY + dy2[dir] * i;
                int x = targetX + dx2[dir] * i;

                if (y < 0 || y >= gridLen || x < 0 || x >= gridLen) continue;

                if (state[y][x] == 1) continue;
                state[y][x] = -1;

                if (!wMap[y][x].isEmpty()) {
                    rocks.addAll(wMap[y][x]);
                    markShadow(state, dir, y, x);
                }
            }
        }
    }

    public static void markShadow(int[][] state, int dir, int wy, int wx) {
        int relativeY = wy - medusaPos.y;
        int relativeX = wx - medusaPos.x;
        
        int spanSign = (dir == 0 || dir == 1) ? Integer.signum(relativeX) : Integer.signum(relativeY);

        for (int d = 1; d <= gridLen; d++) {
            int baseY = wy + dy[dir] * d;
            int baseX = wx + dx[dir] * d;

            int maxSide = (spanSign == 0) ? 0 : d;

            for (int s = 0; s <= maxSide; s++) {
                int sy = baseY;
                int sx = baseX;
                
                if (dir == 0 || dir == 1) {
                    sx += s * spanSign;
                } else {
                    sy += s * spanSign;
                }

                if (sy >= 0 && sy < gridLen && sx >= 0 && sx < gridLen) {
                    state[sy][sx] = 1;
                }
            }
        }
    }
    
    public static void warriorMove(int[][] isSafe) {
        for(Warrior w : warriors) {
            w.move(isSafe);
            w.isRock = false;
        }

        warriors.removeIf(w -> w.isDie);
    }    

    public static void output() {
        sb.append(curMoves).append(" ");
        sb.append(curRocks).append(" ");
        sb.append(curAttacks).append("\n");
    }
}
