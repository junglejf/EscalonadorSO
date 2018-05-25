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
        Cpu cpu1 = new Cpu(null, true, "cpu1", 2);
        Cpu cpu2 = new Cpu(null, true, "cpu2", 2);
        Cpu cpu3 = new Cpu(null, true, "cpu3", 2);
        Cpu cpu4 = new Cpu(null, true, "cpu4", 2);
        Random random = new Random();
        ArrayList<Processo> listap = new ArrayList();
        ArrayList<Processo> lp = new ArrayList();
        ArrayList<Processo> lp1;
        ArrayList<Processo> lp2;
        ArrayList<Processo> lp3;
        ArrayList<Processo> lp4;
        ArrayList<Processo> lp5;
        ArrayList<Processo> lp6;
        ArrayList<Processo> lp7;
        ArrayList<Processo> lp8;
        ArrayList<Processo> lp9;
        
        Fila fila_inicial = new Fila(listap, "inicial");
        Fila f2 = new Fila(lp,"f2");
        Fila fexec = new Fila(lp1 = new ArrayList(),"fexec");
        Fila fbck1 = new Fila(lp2 = new ArrayList(),"fbck1");
        Fila fbck2 = new Fila(lp3 = new ArrayList(), "fbck2");
        Fila fbck3 = new Fila(lp4 = new ArrayList(), "fbck3");
        Fila fprontotr = new Fila(lp5 =new ArrayList(), "fprontotr"); 
        Fila fupronto = new Fila(lp6 = new ArrayList(), "fupronto"); 
        Fila fbloqueado = new Fila(lp7 = new ArrayList(), "fbloqueado");
        Fila fbloqsuspenso = new Fila(lp8= new ArrayList(), "fbloqsuspenso");
        Fila fprontosuspenso = new Fila(lp9 = new ArrayList(),"fprontpsuspenso");
        
        MemoriaRam mram = new MemoriaRam(null,true,"mram",8192,0,null);
        MemoriaHd hd = new MemoriaHd(null,true,"hd",100000000,0,null);
        
        for(int i=0; i<10;i++){
            Processo p = new Processo(i,random.nextInt(1),random.nextInt(20),random.nextInt(512),vazio,"inicial","");
            System.out.print( so.inserir(p, fila_inicial));
    
        }
        for(int i=0; i<10;i++){
         
            Processo p = new Processo(i,random.nextInt(1),random.nextInt(20),random.nextInt(512),vazio,"f2","");
            System.out.print( so.inserir(p, f2));         
        }
        
        System.out.println();
        fila_inicial.imprimeFila(fila_inicial);
        //f2.imprimeFila(f2);
        
        //so.inserir(fila_inicial.getListap().get(0),f2);
        //so.remover(fila_inicial);
        
        //fila_inicial.imprimeFila(fila_inicial);
        //f2.imprimeFila(f2);
        String professora = "insatisfeita";
        
        while(!fila_inicial.getListap().isEmpty()){
            
            System.out.print("tam ="+ fila_inicial.getListap().size()+" | ");
            
            
            
         
            /*if(!cpu1.isDisponibilidade()){
                System.out.println(so.processa(cpu1, fexec, fila_inicial, fbck2, fbck3, mram));
                if(cpu1.isDisponibilidade()){
                    System.out.println("Escolhido: "+so.escolheProcesso(cpu1, fexec,fila_inicial, fbck2,fbck3, fprontotr, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd));
                }//filainicial NO LUGAR DE FBCK1 PARA TESTE!!!!!!!!!!!!!!
            }else{
                    System.out.println("Escolhido: "+so.escolheProcesso(cpu1, fexec,fila_inicial, fbck2,fbck3, fprontotr, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd));
            } */
            
            if(cpu1.isDisponibilidade()){
                System.out.println("Escolhido: "+so.escolheProcesso(cpu1, fexec,fbck1, fbck2,fbck3, fila_inicial, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd));
                System.out.println(so.processa(cpu1, fexec, fila_inicial, fbck2, fbck3, mram));
            }else{
                System.out.println(so.processa(cpu1, fexec, fila_inicial, fbck2, fbck3, mram));
            }
            
            /*
            if(cpu2.isDisponibilidade()){
                so.escolheProcesso(fexec,fbck1, fbck2,fbck3, fprontotr, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            }
            if(cpu3.isDisponibilidade()){
                so.escolheProcesso(fexec,fbck1, fbck2,fbck3, fprontotr, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            }
            if(cpu4.isDisponibilidade()){
                so.escolheProcesso(fexec,fbck1, fbck2,fbck3, fprontotr, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            } */  
            
        
        }
        
        // Processadores
        
        //Memoria 
        
    }
    
}
