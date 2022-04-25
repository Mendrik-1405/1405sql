package serveur;

import conf.Config;
import exceptions.ConnectException;
import exceptions.DbException;
import exceptions.InexistantException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.InexistantException;
import exceptions.TypeDataException;


public class TraitementServeur extends Thread {
    private Socket s;
    Config tconf;
    public TraitementServeur(){
        super();
    }
    public TraitementServeur(Socket sock){
        super();
        this.s = sock;
        this.tconf=new Config();
    }
    @Override
    public void run(){
        
            try {
        
                boolean connectee = true;
                while(connectee){
                    InputStreamReader in = new InputStreamReader(s.getInputStream());
                    BufferedReader bf = new BufferedReader(in);
                    PrintWriter pr = new PrintWriter(s.getOutputStream());
//                    if(req.getReq().equalsIgnoreCase("")||req.getReq()==null){
//                        // PrintWriter pr = new PrintWriter(s.getOutputStream());
//                        pr.println("tsy misy dikany");
//                        pr.flush();
//                        continue;
//                    }
//                    if(req.getReq().equalsIgnoreCase("END SESSION")){
//           
//                        // PrintWriter pr = new PrintWriter(s.getOutputStream());
//                        pr.println("bye");
//                        pr.flush();
//                        connectee = false; 
//                    }
//                    if(!req.requeteValide()){
//                        // PrintWriter pr = new PrintWriter(s.getOutputStream());
//                        pr.println("syntaxe incorrect");
//                        pr.flush();
//                    } else {
                        try{
                    Requete req = new Requete(bf.readLine(),this.tconf);
                            if(req.isCREATE()){
                                if(req.tableExiste(req.getReqTab())){
                                    
                                    // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    pr.println("table dejas existante");
                                    pr.println("!!!");
                                    pr.flush();
                                    
                                } else {
                                    
                                    req.createTable();
                                    // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    pr.println("table cree");
                                    pr.println("!!!");
                                    pr.flush();
                                    
                                }
                            }else if(req.isDROP()){
                
                                req.dropTable();
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                pr.println("Table supprime");
                                pr.println("!!!");
                                pr.flush();
                                
                            }else if(req.isSHOW()){
                                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                String reponse = /*Requete*/req.showTable();
                                pr.println("liste tables: "+reponse);
                                pr.println("!!!");
                                pr.flush();
                
                            }else if(req.isINSERT()){
                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                req.insertIntoTable();
                                pr.println("Donnee inseree");
                                pr.println("!!!");
                                pr.flush();
                
                            }else if(req.isUSEDB()){
                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                req.useDB();
                                pr.println("DB connectee");
                                pr.println("!!!");
                                pr.flush();
                
                            }else if(req.isCREATEDB()){
                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                req.createDB();
                                pr.println("DB creer");
                                pr.println("!!!");
                                pr.flush();
                
                            }else if(req.isDROPDB()){
                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                req.dropDB();
                                pr.println("DB droped");
                                pr.println("!!!");
                                pr.flush();
                
                            }                            
                            else if(req.isSELECT()){
                
                                // PrintWriter pr = new PrintWriter(s.getOutputStream());
                                Vector vec = req.selectTable();
                                String slct = "";
                                for(int i=0;i<vec.size();i++){
                                    pr.println((String)vec.elementAt(i));
                                }
//                                pr.println(slct);
                                pr.println("!!!");
                                pr.flush();
                                System.out.println("Donnee selectionne");
             
                            }
                        }catch(InexistantException e){
                            
                            // PrintWriter pr = new PrintWriter(s.getOutputStream());
                            pr.println(e.getMessage());
                            pr.println("!!!");
                            pr.flush();
                        } catch (TypeDataException ex) {
                            pr.println(ex.getMessage());
                            pr.println("!!!");
                            pr.flush();
                        } catch (ConnectException ex) {
                            pr.println(ex.getMessage());
                            pr.println("!!!");
                            pr.flush();
                            this.s.close();
                        } catch (DbException ex) {
                            pr.println(ex.getMessage());
                            pr.println("!!!");
                            pr.flush();
                        } catch (NullPointerException ex) {
                            pr.println(ex.getMessage());
                            pr.println("!!!");
                            pr.flush();
                        }
//                    }
//                    System.out.println("Client :"+req.getReq());
                }
        
            } catch (IOException ex) {
                Logger.getLogger(ServeurSGBD.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
