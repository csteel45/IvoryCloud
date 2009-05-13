package jrdesktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import jrdesktop.server.rmi.Server;
import jrdesktop.viewer.rmi.Viewer;

/**
 * main.java
 * @author benbac
 */

public class main {
    
    public static final URL IDLE_ICON = main.class.getResource("images/idle.png");
    public static final URL ALIVE_ICON = main.class.getResource("images/background.png");        
    public static final URL WAIT_ICON = main.class.getResource("images/display.png");
    public static final URL START_ICON = main.class.getResource("images/player_play.png");
    public static final URL STOP_ICON = main.class.getResource("images/player_stop.png");
 
    public static String CONFIG_FILE;
    public static String SERVER_CONFIG_FILE;
    public static String VIEWER_CONFIG_FILE;
        
    public static void main (String args[]) {          
        if (System.getSecurityManager() == null)
	    System.setSecurityManager(new SecurityMng());       

        CONFIG_FILE = getCurrentDirectory() + "config";
        SERVER_CONFIG_FILE = getCurrentDirectory() + "server.config";
        VIEWER_CONFIG_FILE = getCurrentDirectory() + "viewer.config";       
    
        System.getProperties().remove("java.rmi.server.hostname");        
                          
        if (args.length > 0) {                    
            String arg;
            boolean serverSide = true;
            String server = "127.0.0.1";
            int port = 6666;
            
            arg = args[0];
            if (arg.equals("-help") || arg.equals("-?")) // display usage information
                displayHelp();                   
            else if (arg.equals("server")) // start server with default paramaters
                startServer(6666);
            else if (arg.equals("viewer")) // start viewer with default paramaters
                startViewer("127.0.0.1", 6666);            
            else if (arg.equals("display")) // display jrdesktop's main window
                mainFrame.main(null);            
            else {
                for (int i=0; i<args.length; i++) {
                    arg = args[i];                  
                    
                    if (arg.startsWith("-a:")) {
                        server = arg.substring(3);   
                        serverSide = false;
                    }
                    else if (arg.startsWith("-p:")) {
                        try {
                            port = Integer.parseInt(arg.substring(3));
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid port number, using default.");
                        }
                        if( port < 1 || port > 65535) {
                            port = 6666;
                            System.err.println("Invalid port number, using default.");
                        }
                    }
                }
                    
                if (serverSide)
                    startServer(port);
                else
                    startViewer(server, port);         
            }
        }
        else
            displayHelp();           
        
        Config.loadConfiguration();
        if (!Config.Systray_disabled)
            SysTray.Show();
        if (!Config.GUI_disabled)
            mainFrame.main(null);         
    }      
       
    public static void displayHelp() {
        System.out.println(                            
            "jrdesktop - Java Remote Desktop Basic Edition.\n" + 
            "http://jrdesktop.sourceforge.net/\n\n" + 
            
            "Usage: java -jar jrdesktop.jar <command> [options]\n\n" + 
               
            "   display     display main window.\n" +            
            "   server      start server using default parameters.\n" +
            "   viewer      start viewer using default parameters.\n" +            
            "   (default parameters : local machine with port 6666).\n\n" +
                
            "Options:\n" + 
            "   -a:address      server's address.\n" +
            "   -p:port         server's port.\n" +
            "   -help or -?     display usage information.\n"
        );
    }
    
    public static void startServer(int port) {
        
        jrdesktop.server.Config.SetConfiguration(port);
        
        Server.Start();
    }    
    
    public static void startViewer(String server, int port) {        
        jrdesktop.viewer.Config.SetConfiguration(server, port);       
        new Viewer().Start();     
    }                

    public static String getCurrentDirectory () {
        String currentDirectory = null;
        try {
            currentDirectory = new File(".").getCanonicalPath() + File.separatorChar;            
        } catch (IOException e) {
            e.getStackTrace();
        }
        return currentDirectory;
    } 
    
    public static void exit() {
        if (Server.isRunning())       
            Server.Stop();
        System.setSecurityManager(null);
        System.exit(0);
    }
}
