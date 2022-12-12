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
import java.util.Collections;
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
    private static ArrayList<ClientStatistics> statistics;
    private ServerSocket serversocket;
    private static String p="";
    private static int num;

    public ServerTCP() throws IOException {
        clients = new ArrayList<>();
        statistics = new ArrayList<>();
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
            if (!alreadyExistingStatistic("user" + numOfUsers)) {
                addClientStatistics("user" + numOfUsers);
            }
            thread.start();
        }
    }

    
    private static void log(String messaggio) {
        System.out.println(messaggio);
    }
    
    private void addClient(ClientHandler client){
        clients.add(client);
    }
    
    private boolean alreadyExistingStatistic(String clientName) {
        ClientStatistics s;
        for (int i=0; i < statistics.size(); i++) {
            s = statistics.get(i);
            if (clientName.equals(s.getClientName())){
                return true;
            }
        }
        return false;
    }
    
    private void addClientStatistics(String clientName){
        statistics.add(new ClientStatistics(clientName));
    }
    
    public static List<ClientHandler> getClients(){
        return clients;
    }

    public void rimuoviClient(ClientHandler c) {
        clients.remove(c);
        numOfUsers--;
    }
    
    public void incrementaTentativi(String clientName) {
        ClientStatistics s;
        for (int i=0; i < statistics.size(); i++) {
            s = statistics.get(i);
            if (clientName.equals(s.getClientName())){
                s.incTentativi();
            }
        }
    }
    
    public void aggiornaMedia(String clientName) {
        ClientStatistics s;
        for (int i=0; i < statistics.size(); i++) {
            s = statistics.get(i);
            if (clientName.equals(s.getClientName())){
                s.aggiornaMedia();
            }
        }
    }
    
    public void azzeraTentativi() {
        ClientStatistics s;
        for (int i=0; i < statistics.size(); i++) {
            s = statistics.get(i);
            s.azzeraTentativi();
        }
    }
    
    public String getStatistics() {
        String risultato="";
        ClientStatistics s;
        ArrayList<ClientStatistics> statistics_copia = creaClassifica();
        for (int i=0; i < 10 && i < statistics_copia.size(); i++) {
            s = statistics_copia.get(i);
            risultato += s.getNomeMedia()+ "###";
        }
        System.out.print("risultato:" + risultato);
        return risultato;
    }
    
    private ArrayList<ClientStatistics> creaClassifica() {
        ArrayList<ClientStatistics> statistics_copia = (ArrayList<ClientStatistics>)statistics.clone();
        for(int i = 0; i < statistics_copia.size(); i++) {
            for (int j = i + 1 ; j < statistics_copia.size(); j++) {
                if (statistics_copia.get(i).getMedia() > statistics_copia.get(j).getMedia()) {
                    Collections.swap(statistics_copia, i, j);
                }
            }
        }
        return statistics_copia;
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
