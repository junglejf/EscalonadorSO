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
        SistemaOperacional so = new SistemaOperacional();
        
        //________RECURSOS_______
        
            //_______MEMORIA_____
        MemoriaRam mram = new MemoriaRam(null, true, "MP", 8192, 0, null);
        MemoriaHd hd = new MemoriaHd(null, true, "HD", 100000000,0, null);
        
            //_______PROCESSADORES_________
        Cpu cpu1 = new Cpu(null, true, "cpu1", 2);
        Cpu cpu2 = new Cpu(null, true, "cpu2", 2);
        Cpu cpu3 = new Cpu(null, true, "cpu3", 2);
        Cpu cpu4 = new Cpu(null, true, "cpu4", 2);
        
        
        //_________LISTA-PROCESSOS_______
        ArrayList<Processo> listaPronto_tr = new ArrayList();
        ArrayList<Processo> listaPronto_u = new ArrayList();
        ArrayList<Processo> listaFbck_1 = new ArrayList();
        ArrayList<Processo> listaFbck_2 = new ArrayList();
        ArrayList<Processo> listaFbck_3 = new ArrayList();
        ArrayList<Processo> listaBloqs = new ArrayList();
        ArrayList<Processo> listaBloqSusp = new ArrayList();
        ArrayList<Processo> listaProntoSusp = new ArrayList();
  
        
        //____________FILAS____________
        Fila fpronto_tr = new Fila(listaPronto_tr, "PRONTO-TR");
        Fila fpronto_u = new Fila(listaPronto_u, "PRONTO-U"); 
        Fila fbck1 = new Fila(listaFbck_1,"F1");    
        Fila fbck2 = new Fila(listaFbck_2, "F2");
        Fila fbck3 = new Fila(listaFbck_3, "F3");
        Fila fbloqueado = new Fila(listaBloqs, "BLOQUEADO");
        Fila fbloqsuspenso = new Fila(listaBloqSusp, "BLOQUEADO SUSPENSO");
        Fila fprontosuspenso = new Fila(listaProntoSusp,"PRONTO SUSPENSO");
        
        //________TESTE________
        ArrayList<Processo> lp = new ArrayList();
        Fila f2 = new Fila(lp,"f2");
        int[] fila_rec = {0,0,0,0};
        Random random = new Random();
        
        //cria processos randomicamente e acrescenta na fila
        for(int i=0; i<10;i++){
            Processo p = new Processo(i, random.nextInt(1), random.nextInt(20), random.nextInt(512), fila_rec, "PRONTO-TR","");
            System.out.print(so.inserir(p, fpronto_tr));
    
        }
        for(int i=0; i<10;i++){
         
            Processo p = new Processo(i,random.nextInt(1),random.nextInt(20),random.nextInt(512),fila_rec,"PRONTO-TR","");
            System.out.print(so.inserir(p, f2));         
        }
        
        System.out.println();
        fpronto_tr.imprimeFila(fpronto_tr);
        //f2.imprimeFila(f2);
        //so.inserir(fpronto_tr.getListap().get(0),f2);
        //so.remover(fpronto_tr);
        //fila_inicial.imprimeFila(fpronto_tr);
        //f2.imprimeFila(f2);
        
        while(!fpronto_tr.getListap().isEmpty()){
            
            if(cpu1.isDisponibilidade()){
                System.out.println("Escolhido: "+so.dispatch(cpu1, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd));
                System.out.println(so.processa(cpu1, fbck1, fbck2, fbck3, mram));
            }else{
                System.out.println(so.processa(cpu1, fbck1, fbck2, fbck3, mram));
            }
            
            /*
            if(cpu2.isDisponibilidade()){
                so.dispatch(fexec,fbck1, fbck2,fbck3, fprontotr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            }
            if(cpu3.isDisponibilidade()){
                so.dispatch(fexec,fbck1, fbck2,fbck3, fprontotr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            }
            if(cpu4.isDisponibilidade()){
                so.dispatch(fexec,fbck1, fbck2,fbck3, fprontotr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
            } */  
            
        
        }
        
        // Processadores
        
        //Memoria 
        
    }
    
}
