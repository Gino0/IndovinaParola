/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clienttcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lettie
 */
public class ClientTCP {

    /**
     * @param args the command line arguments
     */
    private Scanner scan;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private InetAddress ip;
   
    public ClientTCP() throws UnknownHostException, IOException{
        ip = InetAddress.getByName("localhost");
        socket = new Socket (ip,1234);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        scan = new Scanner (System.in);
    }
   
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ClientTCP client = new ClientTCP();
        //thread 1->riceve i dati
        client.readMessageThread();
       
       
        //thread 2->invia i dati
        client.writeMessageThread();
    }
   
    private void readMessageThread()
    {
        Thread readmessage = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true){
                    String msg;
                    try {
                       
                        msg = input.readUTF();
                        log(msg); //metodo x visualizzare
                        if (msg.equals("DISCONNECTED")) {
                            System.exit(0);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                   
                }
            }
        });
        readmessage.start();
    }
   
    private void writeMessageThread(){
        Thread sendmessage = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true){
                    String msg = scan.nextLine();
                    try {
                        output.writeUTF(msg);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientTCP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        sendmessage.start();
    }
   
   
    private void log(String messaggio){
       System.out.println(messaggio);
    }

}
