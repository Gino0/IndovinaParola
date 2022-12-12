/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

/**
 *
 * @author ricca
 */
public class ClientStatistics {
    private String clientName;
    private float media;
    private int somma;
    private int tentativi;
    private int vittorie;

    public ClientStatistics(String clientName) {
        this.clientName = clientName;
        this.media = 999;
        this.somma = 0;
        this.tentativi = 0;
        this.vittorie = 0;
    }

    public String getClientName() {
        return clientName;
    }

    public float getMedia() {
        return media;
    }

    public int getTentativi() {
        return tentativi;
    }

    public int getVittorie() {
        return vittorie;
    }
    
    public void incTentativi() {
        tentativi++;
    }
    
    public void azzeraTentativi() {
        tentativi=0;
    }
    
    public void incVittorie(){
        vittorie++;
    }
    
    public void aggiornaMedia() {
        somma = somma + tentativi;
        incVittorie();
        media = (float) somma / vittorie;
    }
    
    public String getNomeMedia() {
        return clientName + ":" + media;
    }
}
