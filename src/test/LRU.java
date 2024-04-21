package test;
import java.util.LinkedHashMap;

//in Remove it should return the string that is Least Recently used (oldest) 
// insert order ltr : A,B,C,A should return B
// we can use priority queue and make the priority such that at the top we have the items that was inserted the last.

public class LRU implements CacheReplacementPolicy{
    LinkedHashMap<String, Long> cacheMap;
    public LRU() {
        this.cacheMap = new LinkedHashMap<>(16, 0.75f, true);
    }

	@Override
    public void add(String word){
        if(cacheMap.containsKey(word)){
            cacheMap.remove(word);
            cacheMap.put(word, System.nanoTime());
        }    
        else cacheMap.put(word, System.nanoTime());
    }

	@Override
	public String remove(){
        String leastRecentlyUsed = cacheMap.keySet().iterator().next();
        cacheMap.remove(leastRecentlyUsed);
        return leastRecentlyUsed;
    }
}
