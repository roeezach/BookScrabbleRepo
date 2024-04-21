package test;

import java.util.LinkedHashMap;

public class LFU implements CacheReplacementPolicy {
    private LinkedHashMap<String,Integer> cache;
    public LFU() {
        this.cache = new LinkedHashMap<String,Integer>();
    }

    @Override
    public void add(String word) {        
        int frequency = cache.getOrDefault(word, 0) + 1;
        cache.put(word, frequency);
    }

    @Override
    public String remove() {
        int min = Integer.MAX_VALUE;
        String leastUsedKey = "";
        for (String key : cache.keySet()) {
            if (cache.get(key) < min)
            {
                min = cache.get(key);
                leastUsedKey = key;
            }
        }
        cache.remove(leastUsedKey);
        return leastUsedKey;
    }
}
