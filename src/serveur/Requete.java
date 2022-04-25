package serveur;

import conf.Config;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.*;
import java.util.Date;

/**
 *
 * 
 */
public class Requete {
    
        private String req=null;
        private String typeReq;
        private String reqTab;
        private String reqData;
        private int longueur;
        private Config conf;
        
    public Requete(){}
    public String getTypeReq() {
        return typeReq;
    }

    public String getReqTab() {
        return reqTab;
    }

    public String getReqData() {
        return reqData;
    }

    public int getLongueur() {
        return longueur;
    }

    public String getReq() {
        return req;
    }
    public Requete(String req,Config co) throws InexistantException, ConnectException{ 
        this.req = req;
        this.conf=co;
        if((this.req).equalsIgnoreCase("")||(this.req)==null){
            throw new InexistantException("Requete");
        }
        if((this.req).equalsIgnoreCase("END SESSION")){          
            throw new ConnectException("Client");
        }
        String[] reqSep = req.split(" ");
        longueur = reqSep.length;
        if(longueur>1){
            typeReq = reqSep[0];
            reqTab = reqSep[1];
            if(longueur==3){
             reqData = reqSep[2];
            }
        }
        if(!this.requeteValide()){
            throw new InexistantException("valide requete");
        }
    }
    boolean typeRequeteValide(){
        
        String[] reqValide = new String[8];
        reqValide[0] = "DROP";
        reqValide[1] = "CREATE";
        reqValide[2] = "SHOW";
        reqValide[3] = "INSERT";
        reqValide[4] = "SELECT";
        reqValide[5] = "CREATEDB";
        reqValide[6] = "USEDB";
        reqValide[7] = "DROPDB";
        boolean result = false;
        for (int i=0;i<reqValide.length;i++ ) {
            //boolean estPaire = ((i%2)==0);
            boolean cond = reqValide[i].equalsIgnoreCase(this.getTypeReq());
            if(cond){
                result = true;
            }
        }
        return result;
    }
    boolean longueurRequeteValide(){
        return (this.getLongueur()==2||this.getLongueur()==3);
    }
    public boolean requeteValide(){
        return longueurRequeteValide()&&typeRequeteValide();
    }
    
     void testDBco() throws DbException{
         Config test=new Config();
         if ((test.getChemin()).compareToIgnoreCase((this.conf).getChemin())==0) {
             throw new DbException("aucun Db connecte");
         }
     }
    
    void createDB() throws DbException{
           Config test=new Config();
            File folder = new File(test.getChemin()+this.reqTab);
            if(!folder.exists()){
                  folder.mkdir();
            }else if(folder.exists()){
                 throw new DbException(this.reqTab+" existe deja");
            }
    }
    
    void useDB() throws DbException{
            Config test=new Config();
            File folder = new File(test.getChemin()+this.reqTab);
            if(folder.exists()){
                  (this.conf).setChemin(this.reqTab+"/");
            }
            if(!folder.exists()){
                 throw new DbException(this.reqTab+" introuvable");
            }
    }
    
       void dropDB() throws DbException{
            Config test=new Config();
            File f = new File(test.getChemin()+this.reqTab);
            if(!f.exists()){
                 throw new DbException("DB "+this.reqTab+" introuvable");
            }
            for (int i = 0; i < f.list().length; i++) {
                     File fc = new File(test.getChemin()+this.reqTab+"/"+f.list()[i]);
//                     System.out.println("itoooo "+test.getChemin()+this.reqTab+"/"+f.list()[i]);
                     fc.delete();
           }
            f.delete();
            (this.conf).setdefaultChemin(test.getChemin());
    } 
    
    void createTable() throws TypeDataException, InexistantException, DbException{
        this.testDBco();
        this.testcreatetab();
        File f;
        String table = reqTab.toLowerCase();
        String url = (this.conf).getChemin()+table+".txt";
        System.out.println(url);
        f = new File(url);
        
        try {
            f.createNewFile();
            FileWriter fw = null;
            fw = new FileWriter(f); 
            BufferedWriter bw=new BufferedWriter(fw);
            fw.write(getReqData());
            fw.flush();
            bw.newLine();
            bw.write("************************");
            bw.flush();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void dropTable() throws InexistantException, DbException{
        this.testDBco();
        if(tableInexistant()){
            throw new InexistantException(getTypeReq()+" table");
        }
        File f;
        String table = reqTab.toLowerCase();
        String url = (this.conf).getChemin()+table+".txt";
        f = new File(url);
        f.delete();
    }
    /*static*/ String showTable() throws InexistantException{
        
        String result = "";
        File f;
        String url = (this.conf).getChemin();
        f = new File(url);
        String[] liste = f.list();
        try{
            for (int i=0 ; i<liste.length ; i++) {
                String[] tay = liste[i].split("\\.");
                liste[i] = tay[0];
            }
         
            for (int i=0 ; i<liste.length ; i++) {
                if(i==0){
                    result = liste[i];
                }else{
                    result = result+","+liste[i];
                }
            }
        }catch(NullPointerException n){
            throw new InexistantException("table");
        }
        return result;
    }
    
    Vector<String> reqtypeattr() throws InexistantException {
        try{
        Vector<String> result = new Vector();
        String desctab = this.reqData;
        String[] attr=desctab.split("\\|");
         for (int i = 0; i < attr.length; i++) {
            result.add(attr[i]);    
         }    
            return result;
        }catch(NullPointerException n){
            throw new InexistantException("type et attr");
        }
    }
        Vector<String> reqtype() throws InexistantException{
        Vector<String> TA=this.reqtypeattr();
        Vector<String> result = new Vector();
        for (int i = 0; i < TA.size(); i++) {
            result.add(((TA.elementAt(i)).split("\\~"))[0]);           
        }
        return result;
    }
    
    Vector<String> reqattr() throws InexistantException{
        try{
        Vector<String> TA=this.reqtypeattr();
        Vector<String> result = new Vector();
        for (int i = 0; i < TA.size(); i++) {
            result.add(((TA.elementAt(i)).split("\\~"))[1]);           
        }
        return result;
        }catch(Exception ex){
            throw new InexistantException("type absent ou");
        }
    }
    
        Vector<String> typeattr() throws InexistantException{
        if(tableInexistant()){
            throw new InexistantException(getTypeReq()+" table");
        }
        Vector<String> result = new Vector();
        FileReader fr = null ;
        File f ;
        String table = reqTab.toLowerCase();
        String url = (this.conf).getChemin()+table+".txt";
        f = new File(url);
        String[] attr;
            try {
                fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String desctab= br.readLine();
                attr=desctab.split("\\|");
            for (String att : attr) {
                result.add(att);
            }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            return result;
    }
    
    Vector<String> type() throws InexistantException{
        Vector<String> TA=this.typeattr();
        Vector<String> result = new Vector();
        for (int i = 0; i < TA.size(); i++) {
            result.add(((TA.elementAt(i)).split("\\~"))[0]);           
        }
        return result;
    }
    
    Vector<String> attr() throws InexistantException{
        Vector<String> TA=this.typeattr();
        Vector<String> result = new Vector();
        for (int i = 0; i < TA.size(); i++) {
            result.add(((TA.elementAt(i)).split("\\~"))[1]);           
        }
        return result;
    }
    
     Vector<String>separdata() throws InexistantException{
         try{
        String[]ldata=(this.reqData).split("\\|");
        Vector<String> result = new Vector();
         for (int i = 0; i < ldata.length; i++) {
            result.add(ldata[i]);    
         }
        return result;
         }catch(Exception ex){
             throw new InexistantException("donnee");
         }
     }
    
     void testcreatetab() throws TypeDataException, InexistantException{
         String[]tdispo={"number","str","date"};
         Vector<String> TA=this.reqtype();
         Vector<String> A=this.reqattr();
         int[]test=new int[TA.size()];
         for (int i = 0; i < TA.size(); i++) {
             for (int j = 0; j < tdispo.length; j++) {
                 if(tdispo[j].compareToIgnoreCase(TA.elementAt(i))==0){
                     test[i]=1;
                 }
             }
         }
         for (int i = 0; i < test.length; i++) {
             if (test[i]!=1) {
                 throw new TypeDataException(A.elementAt(i));
             }
         }         
     }
     
     void testinsert() throws TypeDataException, InexistantException{
         String[]tdispo={"number","str","date"};
         Vector<String> TA=this.type();
         Vector<String> A=this.attr();
         Vector<String> data=this.separdata();
         int[]test=new int[TA.size()];
         for (int i = 0; i < data.size(); i++) {
             if((TA.elementAt(i)).compareToIgnoreCase(tdispo[0])==0){
                 try{
                     Double nb=new Double(data.elementAt(i));
                 }catch(NumberFormatException ex){
                     throw new TypeDataException(data.elementAt(i));
                 }    
             }
             if((TA.elementAt(i)).compareToIgnoreCase(tdispo[2])==0){
                 try{
                     Date str=new Date(data.elementAt(i));
                 }catch(Exception ex){
                     throw new TypeDataException(data.elementAt(i));
                 }    
             }       
         }
     }
     
    void insertIntoTable() throws InexistantException, TypeDataException, DbException {
        this.testDBco();
        this.testinsert();
        if(tableInexistant()){
            throw new InexistantException(getTypeReq()+" table");
        }
            try {
                String table = reqTab.toLowerCase();
                String url = (this.conf).getChemin()+table+".txt";
                FileWriter fw = new FileWriter(url,true); 
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(getReqData());
                bw.flush();
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    Vector<String> selectTable() throws InexistantException, DbException {
        this.testDBco();
        if(tableInexistant()){
            throw new InexistantException(getTypeReq()+" table");
        }
        Vector<String> result = new Vector();
        FileReader fr = null ;
        File f ;
        String table = reqTab.toLowerCase();
        String url = (this.conf).getChemin()+table+".txt";
        f = new File(url);
        
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            //StringBuffer sb = new StringBuffer();
            String line;
            try {
                while((line = br.readLine()) != null)
                {
                    result.add(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {  
            Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
        }
                System.out.println("data="+this.reqData);
             if (this.reqData!=null) {
                String[]where=(this.reqData).split("\\|");
                Vector<String> r2 = new Vector();
                 for (int i = 2; i < result.size(); i++) {
                   for (int a = 0; a < where.length; a++) {
                                         System.out.println("Bodata="+this.reqData+" "+result.size());
                            if ((result.elementAt(i)).split("\\|")[a].compareToIgnoreCase(where[a])==0 && where[a].compareToIgnoreCase("*")!=0) {
                                r2.add(result.elementAt(i));
                            }                      
                        }                        
                    }
                 return r2;
            }
        return result;
    }
    /*static*/ boolean tableExiste(String table){
        String lsTab = "";
            try {
                lsTab = this.showTable();
            } catch (InexistantException ex) {
                return false;
            }
        String[] listTab = lsTab.split(",");
        for(int i=0;i<listTab.length;i++){
            if(table.equalsIgnoreCase(listTab[i])){
                return true;
            }
        }
        return false;
    }
    public boolean tableInexistant(){
        return !(/*Requete*/this.tableExiste(this.getReqTab()));
    }
    public boolean isCREATE(){
        boolean result = typeReq.equalsIgnoreCase("CREATE");
        return result;
    }
    
    public boolean isCREATEDB(){
        boolean result = typeReq.equalsIgnoreCase("CREATEDB");
        return result;
    }
    
    public boolean isUSEDB(){
        boolean result = typeReq.equalsIgnoreCase("USEDB");
        return result;
    }    
        
    public boolean isDROPDB(){
        boolean result = typeReq.equalsIgnoreCase("DROPDB");
        return result;
    }  
    
    public boolean isDROP(){
        boolean result = typeReq.equalsIgnoreCase("DROP");
        return result;
    }
    
    public boolean isSHOW(){
        boolean result = typeReq.equalsIgnoreCase("SHOW");
        return result;
    }
    
    public boolean isINSERT(){
        boolean result = typeReq.equalsIgnoreCase("INSERT");
        return result;
    }
    
    public boolean isSELECT(){
        boolean result = typeReq.equalsIgnoreCase("SELECT");
        return result;
    }

 
}
