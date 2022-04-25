/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import serveur.Requete;

/**
 *
 * @author P14A-Mendrik
 */
public class Config {
    String ip;
    int port;
    String chemin;

    public Config() {
        this.ip=(getConfig()).get(0);
        this.port=Integer.parseInt((getConfig()).get(1));
        this.chemin=(getConfig()).get(2);
    }
    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = (getConfig()).get(2)+chemin;
    }
    public void setdefaultChemin(String chemin) {
        this.chemin = chemin;
    }
    public Vector<String> getConfig(){
        
        File f;
        f = new File("./config.txt");
            Vector<String> result = new Vector();
            String line;
        try {
            FileReader fw = new FileReader(f); 
            BufferedReader bw=new BufferedReader(fw);
            while((line = bw.readLine()) != null){
                    result.add((line.split("="))[1]);
                }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
