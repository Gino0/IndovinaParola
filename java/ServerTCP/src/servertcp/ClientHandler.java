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

    final Socket socket;
    final Scanner scan;
    String name;
    boolean isLoggedIn;
    private DataInputStream input;
    private DataOutputStream output;
    ServerTCP s;

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
        while (true) {
            ricevuti = read();
            if (ricevuti.equalsIgnoreCase("esci")) {
                //se il server riceve la parola esci chiude la connessione
                this.isLoggedIn = false;
                closeSocket();
                closeStream();
                break;
            }
            forwardToClient(ricevuti);
        }   
        closeStream();
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
//        String recipient = tokenizer.nextToken().trim();
        String message = tokenizer.nextToken().trim();
        
        for(ClientHandler c : ServerTCP.getClients()){
//            if(c.isLoggedIn && c.name.equals(recipient)){
                
                if(message.equals())
                {
                    write(c.output,message);
                    log(name + " --> "+ message);
                    break;
                }
                
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
    
    private int generaNumero(){
        int i=0;
        //int max= LUNGHEZZA FILE CON PAROLE
        int min=0;
        //int range=max-min+1;
        //i=(int)(Math.random() * range) + min;
        return i;
    }
    
    private String EstraiParola(){
        int size=0;
        String path="./parole.txt";
        File file = new File(path);
        String[] vett = new String[size];
        int x=generaNumero();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            int c=0;
            while ((line = br.readLine()) != null) {
//                for (int i = 0; i < parola.length(); i++) {
////                    chars[i]=parola.charAt(i);
//                }
//                System.out.println(Arrays.toString(chars));//ho ggiunto l'import "arrays
                if(c==x){
                    return line.trim();
                }
                c++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
