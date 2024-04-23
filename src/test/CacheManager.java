package test;
import java.util.HashSet;
//* */

public class CacheManager{
    private HashSet<String> wordsInCache;
    CacheReplacementPolicy cacheReplacementPolicy;
    int size;
    public CacheManager(int size, CacheReplacementPolicy crp) {
        wordsInCache = new HashSet<String>();
        this.cacheReplacementPolicy = crp;
        this.size = size;
    }

    public boolean query(String word){
        return wordsInCache.contains(word);
    }

    public void add(String Word){
        if(!query(Word)){
            wordsInCache.add(Word);
            if(wordsInCache.size() > size)
                wordsInCache.remove(cacheReplacementPolicy.remove());
        }
        cacheReplacementPolicy.add(Word);
    }
}
