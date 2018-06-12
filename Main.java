package escalonadorso;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        
    //************RECURSOS**************
        int contador = 0;
        Scanner keyboard = new Scanner(System.in);

        SistemaOperacional so = new SistemaOperacional();
        
        Recurso imp1 = new Recurso(null, true, "IMPRESSORA1");
        Recurso imp2 = new Recurso(null, true, "IMPRESSORA2");
        Recurso modem = new Recurso(null, true, "SCANNER");
        Recurso scan = new Recurso(null, true, "SCANNER");
        Recurso cd1 = new Recurso(null, true, "CD1");
        Recurso cd2 = new Recurso(null, true, "CD2");
            //_______MEMORIA_____
        ArrayList<Processo> listaMram = new ArrayList();
        MemoriaRam mram = new MemoriaRam(null, true, "MP", 8192, 0, listaMram);
        MemoriaHd hd = new MemoriaHd(null, true, "HD", 100000000,0, null);
        
            //_______PROCESSADORES_________
        Cpu cpu1 = new Cpu(null, true, "cpu1", 2);
        Cpu cpu2 = new Cpu(null, true, "cpu2", 2);
        Cpu cpu3 = new Cpu(null, true, "cpu3", 2);
        Cpu cpu4 = new Cpu(null, true, "cpu4", 2);
        
        
        //_________LISTA-PROCESSOS_______
        ArrayList<Processo> listaEntrada = new ArrayList();
        ArrayList<Processo> listaPronto_tr = new ArrayList();
        ArrayList<Processo> listaPronto_u = new ArrayList();
        ArrayList<Processo> listaFbck_1 = new ArrayList();
        ArrayList<Processo> listaFbck_2 = new ArrayList();
        ArrayList<Processo> listaFbck_3 = new ArrayList();
        ArrayList<Processo> listaBloqs = new ArrayList();
        ArrayList<Processo> listaBloqSusp = new ArrayList();
        ArrayList<Processo> listaProntoSusp = new ArrayList();
        
        ArrayList<Processo> executados = new ArrayList(); //lista de processos que acabaram de passar pela CPU
  
        
        //____________FILAS____________
        Fila fentrada = new Fila(listaEntrada, "ENTRADA");
        Fila fpronto_tr = new Fila(listaPronto_tr, "PRONTO-TR");
        Fila fpronto_u = new Fila(listaPronto_u, "PRONTO-U"); 
        Fila fbck1 = new Fila(listaFbck_1,"F1");    
        Fila fbck2 = new Fila(listaFbck_2, "F2");
        Fila fbck3 = new Fila(listaFbck_3, "F3");
        Fila fbloqueado = new Fila(listaBloqs, "BLOQUEADO");
        Fila fbloqsuspenso = new Fila(listaBloqSusp, "BLOQUEADO SUSPENSO");
        Fila fprontosuspenso = new Fila(listaProntoSusp,"PRONTO SUSPENSO");
        
    //**********************************
        
        
    //________TESTE________
        Random random = new Random();
        int[] fila_rec = {0, 0, 0, 0};
        
        // CASO TESTE
        Processo p0 = new Processo(0, 0, 8, 265, fila_rec, "NOVO", "");
        Processo p1 = new Processo(1, 0, 13, 360, fila_rec, "NOVO", ""); 
        Processo p2 = new Processo(2, 3, 15, 477, fila_rec, "NOVO", ""); 
        Processo p3 = new Processo(3, 1, 6, 445, fila_rec, "NOVO", ""); 
        Processo p4 = new Processo(4, 2, 6, 375, fila_rec, "NOVO", ""); 
        Processo p5 = new Processo(5, 0, 7, 157, fila_rec, "NOVO", ""); 
        Processo p6 = new Processo(6, 3, 7, 82, fila_rec, "NOVO", ""); 
        Processo p7 = new Processo(7, 2, 18, 118, fila_rec, "NOVO", ""); 
        Processo p8 = new Processo(8, 0, 8, 489, fila_rec, "NOVO", ""); 
        Processo p9 = new Processo(9, 2, 8, 463, fila_rec, "NOVO", "");
        
        so.inserir(p0, fentrada);
        so.inserir(p1, fentrada);
        so.inserir(p2, fentrada);
        so.inserir(p3, fentrada);
        so.inserir(p4, fentrada);
        so.inserir(p5, fentrada);
        so.inserir(p6, fentrada);
        so.inserir(p7, fentrada);
        so.inserir(p8, fentrada);
        so.inserir(p9, fentrada);

        /*
        // MOD
        //cria processos randomicamente e acrescenta na fila
        for(int i=0; i<10;i++){//impressoras        modem            scanner             cds  
            //int[] fila_rec = {random.nextInt(3),random.nextInt(2),random.nextInt(2),random.nextInt(3)};
                                //tchegada  prioridade          tservico             tamanho       recursos   estado estado anterior
                                
            Processo p = new Processo(i, random.nextInt(4), random.nextInt(20), random.nextInt(512), fila_rec, "NOVO", "");
            so.inserir(p, fentrada);
        }
        */
        
    //___________INICIALIZACAO_____________
        fentrada.imprimeFila(fentrada);
        System.out.println();
                
        System.out.println("MP: "+mram.getEspacoAlocado()+" MBytes ");
                
        
        
        
        
        System.out.println();
        System.out.println("#############################################");
        System.out.println("#                                           #");
        System.out.println("#  CPU1:                                    #");
        System.out.println("#  SEM PROCESSO.                            #");
        System.out.println("#                                           #");
        System.out.println("#  CPU2:                                    #");
        System.out.println("#  SEM PROCESSO.                            #");
        System.out.println("#                                           #");
        System.out.println("#  CPU3:                                    #");
        System.out.println("#  SEM PROCESSO.                            #");
        System.out.println("#                                           #");
        System.out.println("#  CPU4:                                    #");
        System.out.println("#  SEM PROCESSO.                            #");
        System.out.println("#                                           #");
        System.out.println("#############################################");
        
        
     //_____________EXECUCAO_____________
        while(true){
            String resp = keyboard.nextLine();
            if(resp.equals("")){
                
                System.out.println("t = "+(contador));
                
                System.out.println("Novo(s) processo(s)");
                for(int j = 0; j < fentrada.getListap().size(); j++){
                    Processo novo = fentrada.getListap().get(j);
                    if(contador == novo.getTempochegada()){ 
                        if(novo.getPrioridade() == 0){
                            so.inserir(novo, fpronto_tr);
                        }else{
                            so.inserir(novo, fpronto_u);
                        }
                        novo.imprimeProcesso(novo);
                        mram.setEspacoAlocado(mram.getEspacoAlocado()+novo.getTamanho());
                        mram.getFila_proc().add(novo);
                         
                    }else if(contador < novo.getTempochegada()){
                        break;
                    }
                }
                System.out.println("MP: "+mram.getEspacoAlocado()+" MBytes ");
                
                for(int k = 0; k < mram.getFila_proc().size(); k++){
                    System.out.print("P"+mram.getFila_proc().get(k).getId()+" ");
                }
                System.out.println();
                
                fpronto_tr.imprimeProc(fpronto_tr);
                fpronto_u.imprimeProc(fpronto_u);
                
                System.out.println();
                System.out.println("#############################################");
                System.out.println("#                                           #");
                System.out.println("#  CPU1:                                    #");
                if(cpu1.isDisponibilidade()){
                    so.dispatch(executados, cpu1, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    
                    System.out.println("   "+so.processa(executados, cpu1, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(executados, cpu1, fbck1, fbck2, fbck3, mram));
                }
                
                System.out.println("executados: ");
                for(int k = 0; k < executados.size(); k++){
                    System.out.print("P"+executados.get(k).getId()+" ");
                }
                System.out.println();
                
                System.out.println("#                                           #");
                System.out.println("#  CPU2:                                    #");
                if(cpu2.isDisponibilidade()){
                    so.dispatch(executados, cpu2, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(executados, cpu2, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(executados, cpu2, fbck1, fbck2, fbck3, mram));
                }
                
                System.out.println("executados: ");
                for(int k = 0; k < executados.size(); k++){
                    System.out.print("P"+executados.get(k).getId()+" ");
                }
                System.out.println();
                
                System.out.println("#                                           #");
                System.out.println("#  CPU3:                                    #");
                if(cpu3.isDisponibilidade()){
                    so.dispatch(executados, cpu3, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(executados, cpu3, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(executados, cpu3, fbck1, fbck2, fbck3, mram));
                }
                
                System.out.println("executados: ");
                for(int k = 0; k < executados.size(); k++){
                    System.out.print("P"+executados.get(k).getId()+" ");
                }
                System.out.println();
                
                System.out.println("#                                           #");
                System.out.println("#  CPU4:                                    #");
                if(cpu4.isDisponibilidade()){
                    so.dispatch(executados, cpu4, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(executados, cpu4, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(executados, cpu4, fbck1, fbck2, fbck3, mram));
                }
                
                System.out.println("executados: ");
                for(int k = 0; k < executados.size(); k++){
                    System.out.print("P"+executados.get(k).getId()+" ");
                }
                System.out.println();
                               
                contador++;
                System.out.println("#                                           #");
                System.out.println("#############################################");
                
                fbck1.imprimeProc(fbck1);
                fbck2.imprimeProc(fbck2);
                fbck3.imprimeProc(fbck3);
                //fbloqueado.imprimeProc(fbloqueado);
                //fprontosuspenso.imprimeProc(fprontosuspenso);
                //fbloqsuspenso.imprimeProc(fbloqsuspenso);
                
                //MODH
                System.out.println("executados: ");
                for(int k = 0; k < executados.size(); k++){
                    System.out.print("P"+executados.get(k).getId()+" ");
                }
                System.out.println();
                
                while(!executados.isEmpty()){
                    executados.remove(0);
                }
                
                System.out.println();
            }
            
        }
        
    }
    
}
