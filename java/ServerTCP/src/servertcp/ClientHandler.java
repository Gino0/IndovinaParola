/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servertcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.in;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ginisi_gabriele
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Scanner scan;
    private String name;
    private boolean isLoggedIn;
    private DataInputStream input;
    private DataOutputStream output;
    private ServerTCP s;
    private String parolacontrollata="";

    public ClientHandler(Socket socket, String name, ServerTCP s) throws IOException {
        this.socket = socket;
        scan = new Scanner(System.in);
        this.name = name;
        isLoggedIn = true;
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        this.s=s;
        
    }

    @Override
    public void run() {
        String ricevuti;
        write(output, "your name: " + name+"\n");
        broadcastClient("la_parola_e'_lunga_"+s.getP().length());
        while (true) {
            ricevuti = read();
            if(ricevuti.equals("EXIT") || ricevuti.equals("exit")){
                s.rimuoviClient(this);
                forwardToClient("DISCONNECTED");
                return;
            }
            if(ricevuti.equals("jolly"))
            {
                forwardToClient("Il_tuo_gioco_finisce_qui_la_parola_era:"+s.getP());
            }else{
                 parolacontrollata=ControllaParola(s.getP(),ricevuti);
                forwardToClient(parolacontrollata);
                //System.out.println("PAROLA CONTROLLATA = " + parolacontrollata);
                if(ControllaVinto(parolacontrollata,s.getP())) {
                broadcastClient(name + "_ha_indovinato!");
                s.aggiornaParola();
                broadcastClient("la_nuova_parola_e'_lunga"+s.getP().length());
            }
            }
        }
        
    }

    private String read() {
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return line;
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void closeStream() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void forwardToClient(String received){
        //message
        StringTokenizer tokenizer = new StringTokenizer(received);
        //String recipient = tokenizer.nextToken().trim();
        String message = tokenizer.nextToken().trim();
        
//      for(ClientHandler c : ServerTCP.getClients()){
//            if(c.isLoggedIn && c.name.equals(recipient)){
                
//                if(message.equals())
//                {
                    write(this.output,message);
                    log(name + " --> "+ message);
                    //break;
//                }
                
//            }
        //}
        
    }
    
    private void broadcastClient(String received){
        //message
        StringTokenizer tokenizer = new StringTokenizer(received);
        //String recipient = tokenizer.nextToken().trim();
        String message = tokenizer.nextToken().trim();
        
        for(ClientHandler c : ServerTCP.getClients()){
//            if(c.isLoggedIn && c.name.equals(recipient)){
                
//                if(message.equals())
//                {
                    write(c.output,message);
                    log(c.name + " --> "+ message);
                    //break;
//                }
                
//            }
        }
    }
    
    
     private void write(DataOutputStream output , String message){
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            log("write : " + ex.getMessage());
        }
    }
     
    private void log(String msg){
        System.out.println(msg);
    }
    
    private String ControllaParola(String p,String msg){
        String indovinata="";
        int lunghezzaParola=msg.length();
        int differenza=0;
        if(msg.length()<p.length()){
            differenza=p.length()-msg.length();
            for(int x=0;x<differenza;x++){
                msg+="*";
            }
        }
       
        for(int i=0;i<p.length();i++){
            if(p.charAt(i)==msg.charAt(i)){
                indovinata+=msg.charAt(i);
            }
            else{
                indovinata+="*";
            }
        }
        
        return indovinata;
    }
    
    public Boolean ControllaVinto(String indo,String p){
        boolean check=false;
        if(indo.equals(p)){
            check=true;
            return check;
        }else{
            
         return check;   
        }
    }
}
