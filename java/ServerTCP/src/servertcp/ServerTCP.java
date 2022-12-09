/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servertcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ginisi_gabriele
 */
public class ServerTCP {

    /**
     * @param args the command line arguments
     */
    private Socket socket;
    private static int numOfUsers = 0;
    private static List<ClientHandler> clients;
    private ServerSocket serversocket;
    private static String p="";
    private static int num;

    public ServerTCP() throws IOException {
        clients = new ArrayList<>();
        serversocket = new ServerSocket(1234);
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        // faccio partire il server
        ServerTCP server = new ServerTCP();
        // aseptto le connessioni dei clients
        server.waitConnection(); 
    }

    private void waitConnection() throws IOException {
        log("server running...");
         p=EstraiParola();
        System.out.println(p);
        while (true) {
            socket = serversocket.accept();
            log("Client accepted: " + socket.getInetAddress());
            numOfUsers++;
            ClientHandler handler
                    = new ClientHandler(socket, "user" + numOfUsers,this);
            Thread thread = new Thread(handler);
            addClient(handler);
            thread.start();
        }
    }

    
    private static void log(String messaggio) {
        System.out.println(messaggio);
    }
    
    private void addClient(ClientHandler client){
        clients.add(client);
    }
    
    public static List<ClientHandler> getClients(){
        return clients;
    }

    public void rimuoviClient(ClientHandler c) {
        clients.remove(c);
        numOfUsers--;
    }
    
    private static String EstraiParola(){
        String path="src\\servertcp\\parole.txt";
        File file = new File(path);
        List<String> nome=new ArrayList();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;

            while ((line = br.readLine()) != null) {            
               nome.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        num=(int)(Math.random()*(nome.size()));
        return nome.get(num);
    }
//    public String attivi(){
//        String tmp="utenti attivi:\n";
//        for (int i = 0; i < clients.size(); i++) {
//            if (clients.get(i).isLoggedIn) {
//               tmp+=clients.get(i).name+"\n";
//            }
//        }
//        return tmp;
//    }
    
    public void aggiornaParola() {
        p = EstraiParola();
        System.out.println(p);
    }
    
    public String getP() {
        return p;
    }
}
