package client;

import conf.Config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 */
public class Client1 {
    
    public static void main(String[] args) {
        try {
            Config conf=new Config();
//                System.out.println("IP du serveur:");
//                BufferedReader br;
//                br = new BufferedReader(new InputStreamReader(System.in));
//                String ip = br.readLine();
// 
//                System.out.println("Port du serveur:");
//                BufferedReader br1;
//                br1 = new BufferedReader(new InputStreamReader(System.in));
//                if(ip!=null){
//                    conf.setIp(ip);
//                }
//                if(br1.readLine()!=null){
//                    Integer port =new Integer(br1.readLine());
//                    conf.setPort(port.intValue());
//                }
            Socket s = new Socket(conf.getIp(),conf.getPort());

            
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            while(true){
                BufferedReader bf1;
                bf1 = new BufferedReader(new InputStreamReader(System.in));
                String req = bf1.readLine();
            
                PrintWriter pr = new PrintWriter(os);
                pr.println(req);
                pr.flush();
                
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(isr);
                String str1 = bf.readLine();
                String str =null;
            
                while (true) {                    
                    System.out.println("1405sql> "+str1);
                    str=str1;
                    str1=bf.readLine();
                    if (str1.equalsIgnoreCase("!!!")) {
                        break;
                    }
                }                              
                if( str.equalsIgnoreCase("client deconnexion") ){
                    break;
                }
            }
            is.close();
            os.close();
            s.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Client1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
