package serveur;

import conf.Config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.InexistantException;

/**
 *
 * 
 */

public class ServeurSGBD extends Thread{
    @Override
    public void run(){
        try {
            Config conf=new Config();
            ServerSocket ss = new ServerSocket(conf.getPort());
            while(true){
                Socket s = ss.accept();

                System.out.println("client connecte");
                new TraitementServeur(s).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServeurSGBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public static void main(String[] args) throws IOException {
	new ServeurSGBD().start();
    }
}
