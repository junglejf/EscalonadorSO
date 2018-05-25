/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escalonadorso;
import java.util.*;
/**
 *
 * @author jungl
 */
public class Main {

    public static void main(String[] args) {
        int vazio[] = {0,0,0,0};
        SistemaOperacional so = new SistemaOperacional();
        Random random = new Random();
        ArrayList<Processo> listap = new ArrayList();
        Fila fila_inicial = new Fila(listap, "inicial");
        for(int i=0; i<10;i++){
            Processo p = new Processo(i,random.nextInt(1),random.nextInt(20),random.nextInt(512),vazio,"disponível");
            System.out.print( so.inserir(p, fila_inicial.getListap()));
            
        }
        System.out.println();
        fila_inicial.imprimeFila(fila_inicial);
        
        // Processadores
        
        //Memoria 
        
    }
    
}
