package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MyServer {
    private final int ONE_SECOND = 1000;
    
    private ClientHandler clientHandler;
    private int port;
    private volatile boolean stop;

        public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.stop = false;
    }   
    
    public void start()
    {
        new Thread(()-> runServer()).start();
    }

    public void stop()
    {
        this.stop = true;
    }

    public void close()
    {
        stop();
        clientHandler.close();
    }

    private void runServer()
    {
        ServerSocket server = null;
        try
        {
            server = handleServerSocket();
        }
        catch(IOException exception)
        {
            System.err.println("An IO Exception occurred, please see stack trace below for additional information.");
            exception.printStackTrace();
        }        
        finally 
        {
            try 
            {
                if (server != null && !server.isClosed()) 
                {
                    server.close();
                }
            } 
            catch (IOException e) 
            {
                System.err.println("Failed to close server socket properly.");
            }
        }
    }

    private ServerSocket handleServerSocket() throws IOException, SocketException {
        ServerSocket server;
        server = new ServerSocket(port);
        server.setSoTimeout(ONE_SECOND);
        while(!stop)
        {
            try 
            {
                Socket aClient = server.accept();
                try
                {
                    clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());               
                }
             
                catch(IOException exception)
                {
                    System.err.println("An IO Exception occurred, please see stack trace below for additional information.");
                    exception.printStackTrace();
                }
                finally 
                {
                    CloseClientHandlerStreams(aClient);
                }
            }
            
            // Expeceted when client are not connecting within the Timout Threshold
            catch(SocketTimeoutException ignored){}                
        }
        server.close();
        return server;
    }

    private void CloseClientHandlerStreams(Socket aClient) 
    {        
        try 
        {
            if (!aClient.isClosed()) 
            {
                aClient.close();
            }
        }
        catch (IOException exception) 
        {
            System.err.println("Failed to close client socket properly.");
            exception.printStackTrace();
        }
    }
}
