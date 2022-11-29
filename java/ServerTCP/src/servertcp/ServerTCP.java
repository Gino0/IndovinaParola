/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servertcp;

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
    Socket socket;
    static int numOfUsers = 0;
    static List<ClientHandler> clients;
    ServerSocket serversocket;

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

    
    private void log(String messaggio) {
        System.out.println(messaggio);
    }
    
    private void addClient(ClientHandler client){
        clients.add(client);
    }
    
    public static List<ClientHandler> getClients(){
        return clients;
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
    
}
