import java.io.*;
import java.util.*;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();
    
    public static List<AntHouse> antHouseList = new ArrayList<>();
    public static int antHouseSize = 0, lastIndex = -1;

    public static class AntHouse {
        public int prevAntHouseIdx;
        public int nextAntHouseIdx;
        public int curAntHouseIdx;
        public int pos;
        boolean isDeleted = false;

        AntHouse(int pos) {

            this.curAntHouseIdx = antHouseSize++;
            this.pos = pos;

            if(lastIndex == -1) lastIndex = 0;
            else {
                prevAntHouseIdx = lastIndex;
                antHouseList.get(prevAntHouseIdx).nextAntHouseIdx = curAntHouseIdx;

                lastIndex = curAntHouseIdx;
                nextAntHouseIdx = -1;        
            }
        }

        public void delete() {
            isDeleted = true;
            if(nextAntHouseIdx != -1) {
                antHouseList.get(nextAntHouseIdx).prevAntHouseIdx = prevAntHouseIdx;
            } else {
                lastIndex = prevAntHouseIdx;

            }

            if(prevAntHouseIdx != -1) {
                antHouseList.get(prevAntHouseIdx).nextAntHouseIdx = nextAntHouseIdx;
            }

        }
        
    }

    public static void main(String[] args) throws Exception {
        antHouseList.add(new AntHouse(0));
        input();
        System.out.print(sb.toString());
    }

    public static void input() throws Exception {
        int q = Integer.parseInt(br.readLine().trim());

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            order();
        }
    }

    public static void order() {
        int type = Integer.parseInt(st.nextToken());
        switch(type) {
            case 100: buildVillage();       break;
            case 200: buildAntHouse();      break;
            case 300: destroyAntHouse();    break;
            case 400: patrolAntHouse();     break;
        }
    }

    public static void buildVillage() {
        int count = Integer.parseInt(st.nextToken());
        while(count-- > 0) {
            buildAntHouse();
        }
    }

    public static void buildAntHouse() {
        int pos = Integer.parseInt(st.nextToken());

        AntHouse antHouse = new AntHouse(pos);
        antHouseList.add(antHouse);
        antHouseList.get(antHouse.curAntHouseIdx);
    }

    public static void destroyAntHouse() {
        int targetIdx = Integer.parseInt(st.nextToken());
        antHouseList.get(targetIdx).delete();
    }

    public static void patrolAntHouse() {
        int maxCount = Integer.parseInt(st.nextToken());
        int time = 1_000_000_000;

        int left = 1, right = 1_000_000_000;
        while(left <= right) {
            int mid = (left + right) / 2;
            
            if(canMove(mid, maxCount)) {
                time = Math.min(time, mid);
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        sb.append(time - 1).append("\n");
    }

    public static boolean canMove(int time, int maxCount) {
        int nowIdx = antHouseList.get(0).nextAntHouseIdx;
        if(nowIdx == -1) return true;

        int antCount = 1;
        int dist = 0;

        while(true) {
            AntHouse nowHouse = antHouseList.get(nowIdx);
            if(nowHouse.nextAntHouseIdx == -1) break;

            AntHouse nextHouse = antHouseList.get(nowHouse.nextAntHouseIdx);
            dist += nextHouse.pos - nowHouse.pos;

            if(dist >= time) {
                antCount++;
                dist = 0;
            }

            nowIdx = nowHouse.nextAntHouseIdx;
            if(antCount > maxCount) return false;

        }

        return true;
    }
}