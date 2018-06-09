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
        
        ArrayList<Processo> fexec = new ArrayList();
  
        
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
        //int[] fila_rec = {0, 0, 0, 0};
        
        //cria processos randomicamente e acrescenta na fila
        for(int i=0; i<10;i++){//impressoras        modem            scanner             cds  
            int[] fila_rec = {random.nextInt(2),random.nextInt(2),random.nextInt(2),random.nextInt(2)};
                                //tchegada  prioridade          tservico             tamanho       recursos   estado estado anterior
            Processo p = new Processo(i, 1, 5, random.nextInt(512), fila_rec, "NOVO", "");
            so.inserir(p, fentrada);
        }
        
    //___________INICIALIZACAO_____________
        fentrada.imprimeFila(fentrada);
        System.out.println();
                
        System.out.println("MP: "+mram.getEspacoAlocado()+" MBytes ");
                
        /*
        fpronto_tr.imprimeProc(fpronto_tr);
        fpronto_u.imprimeProc(fpronto_u);
        
        
        fbloqueado.imprimeProc(fbloqueado);
        fprontosuspenso.imprimeProc(fprontosuspenso);
        fbloqsuspenso.imprimeProc(fbloqsuspenso);
        */
        
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
                
                
                System.out.println();
                System.out.println("#############################################");
                System.out.println("#                                           #");
                System.out.println("#  CPU1:                                    #");
                if(cpu1.isDisponibilidade()){
                    so.dispatch(fexec, cpu1, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    
                    System.out.println("   "+so.processa(fexec, cpu1, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(fexec, cpu1, fbck1, fbck2, fbck3, mram));
                }
                if(cpu1.getUtilizador() != null && !so.executando(cpu1.getUtilizador(), fexec)){
                    fexec.add(cpu1.getUtilizador());
                    //System.out.println("proc1: "+cpu1.getUtilizador().getId());
                }
                
                System.out.println("#                                           #");
                System.out.println("#  CPU2:                                    #");
                if(cpu2.isDisponibilidade()){
                    so.dispatch(fexec, cpu2, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(fexec, cpu2, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(fexec, cpu2, fbck1, fbck2, fbck3, mram));
                }
                
                if(cpu2.getUtilizador() != null && !so.executando(cpu2.getUtilizador(), fexec)){
                    fexec.add(cpu2.getUtilizador());
                    //System.out.println("proc2: "+cpu2.getUtilizador().getId());
                }
                
                System.out.println("#                                           #");
                System.out.println("#  CPU3:                                    #");
                if(cpu3.isDisponibilidade()){
                    so.dispatch(fexec, cpu3, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(fexec, cpu3, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(fexec, cpu3, fbck1, fbck2, fbck3, mram));
                }
                
                if(cpu3.getUtilizador() != null && !so.executando(cpu3.getUtilizador(), fexec)){
                    fexec.add(cpu3.getUtilizador());
                    //System.out.println("proc3: "+cpu3.getUtilizador().getId());
                }
                
                System.out.println("#                                           #");
                System.out.println("#  CPU4:                                    #");
                if(cpu4.isDisponibilidade()){
                    so.dispatch(fexec, cpu4, fpronto_tr, fpronto_u, fbck1, fbck2, fbck3, fbloqueado,
                            fbloqsuspenso, fprontosuspenso,  mram,  hd, imp1, imp2, modem, scan, cd1, cd2);
                    System.out.println("   "+so.processa(fexec, cpu4, fbck1, fbck2, fbck3, mram));
                }else{
                    System.out.println("   "+so.processa(fexec, cpu4, fbck1, fbck2, fbck3, mram));
                }
                
                if(cpu4.getUtilizador() != null && !so.executando(cpu4.getUtilizador(), fexec)){
                    fexec.add(cpu4.getUtilizador());
                    //System.out.println("proc4: "+cpu4.getUtilizador().getId());
                }
                
                
                contador++;
                System.out.println("#                                           #");
                System.out.println("#############################################");
                /*
                System.out.println(fexec.size());
                
                for(int k = 0; k < fexec.size(); k++){
                    System.out.print("P"+fexec.get(k).getId()+" ");
                }
                
                for(int k = 0; k < fexec.size(); k++){
                    if(!fexec.get(k).getEstado().equals("EXECUTANDO")){
                        fexec.remove(fexec.get(k));
                    }
                }
                */
                
                fpronto_u.imprimeFila(fpronto_u);
                
                fbloqueado.imprimeFila(fbloqueado);
                
                fbck1.imprimeFila(fbck1);
                fbck2.imprimeFila(fbck2);
                 fbck3.imprimeFila(fbck3);
                System.out.println();
                so.processa_bloqueado(fbloqueado,fpronto_u,imp1, imp2,modem,scan,cd1,cd2);
        
            }
            
        }
        
    }
    
}
