import java.io.*;
import java.util.*;


public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static StringBuilder sb = new StringBuilder();

    static final int INF = 1_000_000_000;

    //LAND DATA
    static List<int[]> edges[];
    static int cityCount;

    //TRIP DATA
    static PriorityQueue<Data> pq = new PriorityQueue<>();
    static Map<Integer, Trip> trips = new HashMap<>();
    static int startIdx = 0;
    static int[] minDist;

    static class Trip {
        int idx, revenue, dest;
        boolean isDeleted = false;

        public Trip(int idx, int revenue, int dest) {
            this.idx = idx;
            this.revenue = revenue;
            this.dest = dest;
        }
    }

    static class Data implements Comparable<Data> {
        int idx, profit;

        public Data(int idx, int profit) {
            this.idx = idx;
            this.profit = profit;
        }

        @Override
        public int compareTo(Data d) {
            if(this.profit == d.profit) return this.idx - d.idx;
            return d.profit - this.profit;
        }
    }

    public static void main(String[] args) throws Exception {
        inputType();
        System.out.print(sb.toString());
    }

    public static void inputType() throws Exception {
        int queryCount = Integer.parseInt(br.readLine().trim());

        while(queryCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            switch(type) {
                case 100: init();       break;
                case 200: makeTrip();   break;
                case 300: cancelTrip(); break;
                case 400: sellTrip();       break;
                case 500: changeStart(); break;
            }
        }
    }


    public static void init() {
        int nodeCount = Integer.parseInt(st.nextToken());
        int edgeCount = Integer.parseInt(st.nextToken());

        cityCount = nodeCount;

        edges = new ArrayList[nodeCount];
        for(int i = 0 ; i < nodeCount ; i++) {
            edges[i] = new ArrayList<>();
        }

        for(int i = 0 ; i < edgeCount ; i++) {
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());

            edges[start].add(new int[]{end, value});
            edges[end].add(new int[]{start, value});
        }

        findDist();
    }

    public static void makeTrip() {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());

        Trip trip = new Trip(id, revenue, dest);
        trips.put(id, trip);

        pq.offer(new Data(id, revenue - minDist[dest]));
    }

    public static void cancelTrip() {
        int id = Integer.parseInt(st.nextToken());
        Trip trip = trips.get(id);
        if(trip == null) return;
        trip.isDeleted = true;
    }

    public static void sellTrip() {
        int result = -1;

        while(!pq.isEmpty()) {
            Data data = pq.poll();

            if(trips.get(data.idx).isDeleted) continue;
            if(data.profit < 0) break;

            trips.get(data.idx).isDeleted = true;
            result = data.idx;
            break;
        }


        sb.append(result).append("\n");
    }

    public static void changeStart() {
        startIdx = Integer.parseInt(st.nextToken());
        pq.clear();
        findDist();

        for(int id : trips.keySet()) {
            Trip trip = trips.get(id);
            if(!trip.isDeleted) {
                pq.offer(new Data(id, trip.revenue - minDist[trip.dest]));
            }
        }
    }

    public static void findDist() {
        int[] dist = new int[cityCount];
        Arrays.fill(dist, INF);
        dist[startIdx] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            return o1[1] - o2[1];
    });
        pq.offer(new int[]{startIdx, 0});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();

            if(dist[cur[0]] < cur[1]) continue;

            for(int[] edge : edges[cur[0]]) {
                int next = edge[0];
                int val = edge[1];

                if(dist[next] > cur[1] + val) {
                    pq.offer(new int[]{next, cur[1] + val});
                    dist[next] = cur[1] + val;
                }
            }

        }

        minDist = dist;
    }

}