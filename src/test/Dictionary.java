package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    final int LRU_SIZE = 400;
    final int LFU_SIZE = 100;
    final int BLOOM_FILTER_SIZE = 256;

    private String[] dictionaryFileNames;
    private CacheManager lruExistCache;
    private CacheManager lfuNotExistCache;
    private BloomFilter bloomFilter;
    
    
    public Dictionary(String... fileNames) 
    {
        this.lruExistCache = new CacheManager(LRU_SIZE, new LRU());
        this.lfuNotExistCache = new CacheManager(LFU_SIZE, new LFU());
		this.bloomFilter =new BloomFilter(BLOOM_FILTER_SIZE,"MD5","SHA1");
        this.dictionaryFileNames = new String[fileNames.length];
        System.arraycopy(fileNames, 0, dictionaryFileNames, 0, fileNames.length);
        addToBloomFilter(this.bloomFilter, this.dictionaryFileNames);        
    }

    public void addToBloomFilter(BloomFilter bloomFilter, String... fileNames) {
        for (String fileName : fileNames) 
        {
            File file = new File(fileName);
            if (!file.exists() || !file.canRead()) 
            {
                System.err.println("Cannot read file or file does not exist: " + fileName);
                continue;
            }
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    // Assuming words are separated by spaces
                    // Split the line into words
                    String[] words = line.toLowerCase().split("\\s+");
                    for (String word : words) 
                    {
                        bloomFilter.add(word);
                    }
                }
            }
            catch (IOException e) 
            {
                System.err.println("IOException occurred when reading the file: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public boolean query(String word)
    {
        if (lruExistCache.query(word))
            return true;
        if (lfuNotExistCache.query(word))
            return false;
        if (bloomFilter.contains(word))
        {
            bloomFilter.add(word);
            return true;
        }
        
        lfuNotExistCache.add(word);
        return false;
    }

    public boolean challenge(String word)
    {
        return IOSearcher.search(word, dictionaryFileNames);
    }
}
