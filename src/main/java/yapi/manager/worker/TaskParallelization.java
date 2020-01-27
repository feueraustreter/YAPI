package yapi.manager.worker;

import yapi.exceptions.YAPIException;

import java.util.*;

public class TaskParallelization<T> {

    private final Map<String, List<T>> tMap = new HashMap<>();

    public List<List<T>> split(List<T> ts, int poolSize) {
        if (poolSize > ts.size()) {
            throw new YAPIException("poolSize needs to be smaller than your input list");
        }

        int ad = (int)Math.round(ts.size() / (double)poolSize);
        List<List<T>> r = new ArrayList<>();
        int j = 0;
        List<T> g = new ArrayList<>();
        for (T t : ts) {
            g.add(t);
            j++;
            if (j == ad || j > ad) {
                r.add(g);
                g = new ArrayList<>();
                j = 0;
            }
        }
        if (!g.isEmpty()) {
            r.add(g);
        }
        return r;
    }

    public void addResult(T t) {
        String key = Thread.currentThread().getName() + Thread.currentThread().getId();
        synchronized (tMap) {
            if (tMap.containsKey(key)) {
                tMap.get(key).add(t);
            } else {
                List<T> ts = new ArrayList<>(Collections.singletonList(t));
                tMap.put(key, ts);
            }
        }
    }

    public List<T> merge() {
        List<T> merge = new ArrayList<>();
        synchronized (tMap) {
            if (tMap.isEmpty()) {
                return merge;
            }
            for (Map.Entry<String, List<T>> entry : tMap.entrySet()) {
                merge.addAll(entry.getValue());
            }
        }
        return merge;
    }

}