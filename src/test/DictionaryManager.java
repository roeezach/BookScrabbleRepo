package test;
import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    private Map<String,Dictionary> bookMapper;

    public DictionaryManager() 
    {
        this.bookMapper = new HashMap<>();
    }

    public static DictionaryManager get(){
        if(instance == null)
        {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public int getSize()
    {
        return bookMapper.size();
    }

    public boolean query(String ... args)
    {
        boolean isExist = false;
        for (int i = 0; i < args.length - 1; i++) 
        {
            if(!bookMapper.containsKey(args[i]))
                bookMapper.put(args[i], new Dictionary(args[i]));
            
            if(bookMapper.get(args[i]).query(args[args.length-1]))
                isExist = true;
        }
        return isExist;
    }
    
    public boolean challenge(String ... args)
    {
        boolean isExist = false;
        for (int i = 0; i < args.length - 1; i++) 
        {
            if(!bookMapper.containsKey(args[i]))
                bookMapper.put(args[i], new Dictionary(args[i]));
            
            if(bookMapper.get(args[i]).challenge(args[args.length-1]))
                isExist = true;
        }
        return isExist;
    }    
}
