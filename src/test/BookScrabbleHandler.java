package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class BookScrabbleHandler implements ClientHandler {
    private final String QUERY = "Q"; 
    private final String CHALLENGE = "C"; 
    
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) 
    {
        String line;
        try
        {
            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(inFromclient));
            PrintWriter sendToClient = new PrintWriter(outToClient);
            line = readFromClient.readLine();
            
            if( line != null)
                processLine(line, sendToClient);
            
            closeResources(readFromClient, sendToClient);
        }
        catch(IOException exception)
        {
            System.err.println("");
            exception.printStackTrace();
        }        
    }

    private void closeResources(BufferedReader readFromClient, PrintWriter sendToClient) throws IOException {
        readFromClient.close();
        sendToClient.close();
    }
    
    @Override
    public void close() 
    {
    }

    private void processLine(String line, PrintWriter sendToClient) {
        String[] splittedWords = line.split(",");
        String typeOfSearchingToInvoke = splittedWords[0];
        String[] argsForQueryOrChallenge = new String[splittedWords.length-1];
        System.arraycopy(splittedWords, 1, argsForQueryOrChallenge, 0, splittedWords.length - 1);
        
        if(typeOfSearchingToInvoke.equals(QUERY)) {
            sendToClient.println(DictionaryManager.get().query(argsForQueryOrChallenge));
            sendToClient.flush();
        }
        
        else if(typeOfSearchingToInvoke.equals(CHALLENGE)) {
            sendToClient.println(DictionaryManager.get().challenge(argsForQueryOrChallenge));
            sendToClient.flush();
        }
        
        else
        {
            sendToClient.println("Invalid Type of searching to invoke");
        }
    }
}
