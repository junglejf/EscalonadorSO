package escalonadorso;

import java.util.ArrayList;

//Responsavel por chamar agentes para inserir, remover, executar, suspender, bloquear e finalizar processos
public class SistemaOperacional {  //antigo FilaMaster
    
    public SistemaOperacional(){
    }
    
//__________CONTROLE INFERIOR____________
    
    //insercao generica fila
    public String inserir (Processo p, Fila fila){
        fila.addProcesso(p);
        p.setEstado(fila.getNome());
        return "";
    }

    public String intToString(int n){
        Integer ns = n;
        String s = ns.toString();
        return s;
    }
    
    //fila organizada com prioridade
    public String inserePriori(Processo p, Fila fatual){
        fatual.addProcesso(p);
        ArrayList<Processo> priori1 = new ArrayList();
        ArrayList<Processo> priori2 = new ArrayList();
        ArrayList<Processo> priori3 = new ArrayList();
        ArrayList<Processo> nova = new ArrayList();
        for(int i = 0; i < fatual.getListap().size(); i++){
            switch (fatual.getListap().get(i).getPrioridade()) {
                case 1:
                    priori1.add(fatual.getListap().get(i));
                    break;
                case 2:
                    priori2.add(fatual.getListap().get(i));
                    break;
                case 3:
                    priori3.add(fatual.getListap().get(i));
                    break;
                default:
                    break;
            }
        }
        
        for(int i = 0; i < priori1.size(); i++){
            nova.add(priori1.get(i));
        }
        for(int i = 0; i < priori2.size(); i++){
            nova.add(priori2.get(i));
        }
        for(int i = 0; i < priori3.size(); i++){
            nova.add(priori3.get(i));
        }
        
        fatual.setListap(nova);
        return "";
    }
    
    
    public String remover(Fila forigem){
        if(!forigem.getListap().isEmpty()){
            Processo p = forigem.getListap().get(0);
            p.setEstadoAnterior(forigem.getNome());
            forigem.getListap().remove(p);
            return "";
        }else{
            return "Fila vazia";
        }
    }
    
    public String removerPos(Fila forigem, int pos){
        if(!forigem.getListap().isEmpty()){
            Processo p = forigem.getListap().get(pos);
            p.setEstadoAnterior(forigem.getNome());
            forigem.getListap().remove(pos);
            return "";
        }else{
            return "Fila vazia";
        }
    }

    
    public String finalizar(ArrayList<Processo> executados, Processo p, Cpu processador, MemoriaRam mram){
        if(mram.getEspacoAlocado() > 0){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - p.getTamanho()); //libera memoria
        }else{
            mram.setEspacoAlocado(0);
        }
        mram.getFila_proc().remove(p);
        processador.setUtilizador(null);        //ocioso
        processador.setDisponibilidade(true);   //disponivel
        //fexec.remove(p); MODH
        //System.out.println("removeu exec "+p.getId());
        p.setEstado("FINALIZADO");
        return "";
    }
    
    public boolean listaZerada(int [] lista){
        boolean resp = false;
        int tam = lista.length;
        for(int i = 0; i < tam; i++){
            resp = (lista[i] == 0);
        }
        return resp; //true se lista zerada
    }

//__________CONTROLE SUPERIOR__________   
    
    
    //_____________CPU_____________
    
    //escolhe processo que sera executado
    public String dispatch(ArrayList<Processo> executados, Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbck1, Fila fbck2, Fila fbck3, 
            Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd, Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        if(!fpronto_tr.getListap().isEmpty()){
            return executarProcessoTr(executados, processador, fpronto_tr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  fbck1,  fbck2,  fbck3, mram,  hd);
        }else{
            return executarProcessoUsuario(executados, processador, fpronto_u, fbloqueado, fbck1,  fbck2,  fbck3, imp1, imp2, modem, scan, cd1, cd2);
        }
    }
    
    //encontrou processo que vai ser executado pela CPU
    public String inserirCpu (ArrayList<Processo> executados, Processo p, Cpu processador){
        //System.out.println("~ "+executando(p, fexec));
        //if(!p.getEstado().equals("EXECUTANDO") && !executando(p, fexec)){ MOD
        boolean r = false;
        for(int x = 0; x < executados.size(); x++){
            r = (p.getId() == executados.get(x).getId()); //true se ta na lista de executados
            if(r){ //true se achou
                break;
            }
        }
        if(!r){ //se nao ta na lista de executados, adiciona
            executados.add(p);
        }
        
        
            p.setEstado("EXECUTANDO");
            processador.setUtilizador(p);
            processador.setQuantum(2);
            processador.setDisponibilidade(false);
            //fexec.add(p); MOD
        
        //}
        
        return "p"+p.intToString(p.getId());
    }
    
    public boolean executando(Processo p, ArrayList<Processo> executados){
        boolean r = false;
        for(int i = 0; i < executados.size(); i++){
            if(p.getId() == executados.get(i).getId()){
                r = true;
                break;
            }
        }
        return r;
    }
    
    //executa processo
    public String processa(ArrayList<Processo> executados, Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram){
        Processo proc = processador.getUtilizador();
        if(proc == null){
            return "SEM PROCESSO. ";
        }
        proc.setTemposervico(proc.getTemposervico() - 1);
        if(proc.getTemposervico() <= 0){
            //se tempo de servico de p == 0 -> processo finalizado
            finalizar(executados, proc, processador, mram);
        }else
        if(proc.getPrioridade() != 0){//se processo de usuario
            processador.setQuantum(processador.getQuantum() - 1);
            if(processador.getQuantum() == 0){
                executados.add(proc);
                //MODH
                String f_anterior = proc.getEstadoAnterior();
                if(f_anterior.equals(fbck1.getNome())){ //se tava na fila 1 vai pra fila 2
                    if(proc.getTemposervico() > 0){ 
                        //proc.setEstado("F2");
                        inserePriori(proc, fbck2);
                        
                    }
                }else if(f_anterior.equals(fbck2.getNome())){ //se tava na fila 2 vai pra fila 3
                    if(proc.getTemposervico() > 0){ 
                        //proc.setEstado("F3");
                        inserePriori(proc, fbck3);
                    }
                }else if(f_anterior.equals(fbck3.getNome())){ //se tava na fila 3 vai pra fila 1 
                    if(proc.getTemposervico() > 0){
                        //proc.setEstado("F1");
                        inserePriori(proc, fbck1);
                    }
                }
                //fexec.remove(proc); MODH
                //System.out.println("removeu exec "+proc.getId());
                processador.setDisponibilidade(true);
                
            }
        }
        return "ts = "+proc.getTemposervico()+" | p"+proc.intToString(proc.getId())+": "+proc.getEstadoAnterior()+" -> "+proc.getEstado();
    }
    
    //____________EXECUCAO____________
    //chamada quando CPU ociosa
    
    
        //____________EXECUCAO TR_____________
        //Executa primeiro processo de tempo real pronto
    public String executarProcessoTr(ArrayList<Processo> executados, Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram, MemoriaHd hd){
	Processo proximoTr = fpronto_tr.getListap().get(0);    //pega primeiro processo tr da fila de prontos
        int espacoAlocado = mram.getEspacoAlocado();          //quanto de memoria esta sendo usada
	if(espacoAlocado + fpronto_tr.getListap().get(0).getTamanho() > mram.getEspaco()){ //se memoria insuficiente
            suspenderProc(fpronto_tr.getListap().get(0), fpronto_u, fbloqueado, fbloqsuspenso, fprontosuspenso, fbck1,  fbck2,  fbck3, mram, hd);//suspende processo usuario
	}
	inserirCpu(executados, proximoTr, processador);               //insere processo tr na fila de execucao
        remover(fpronto_tr);          //remove processo tr da fila de pronto
        
        return "p"+proximoTr.intToString(proximoTr.getId());
    }
    
    
        //__________EXECUCAO USUARIO__________
        ////Executa primeiro processo de usuario pronto
    public String executarProcessoUsuario(ArrayList<Processo> executados, Cpu processador, Fila fpronto_u, Fila fbloqueado, Fila fbck1, Fila fbck2, Fila fbck3,
            Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        if(!fpronto_u.getListap().isEmpty()){//se fila nao vazia
            Processo proximoPu = fpronto_u.getListap().get(0);
            int [] l = proximoPu.getListarec();
            if(!listaZerada(l)){            //se processo usa recursos
                bloquear(fbloqueado, fpronto_u, imp1, imp2, modem, scan, cd1, cd2,proximoPu);
            }else{
                inserePriori(proximoPu, fbck1);//insere na primeira fila do feedback com prioridade
                remover(fpronto_u);
            }
        }
        return(executaFeedback(executados, processador, fbck1, fbck2, fbck3));   
    }

    public String executaFeedback(ArrayList<Processo> executados, Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3){
        if(!fbck1.getListap().isEmpty()){           //se primeira fila nao vazia
            int pos = 0;
            while(pos < fbck1.getListap().size()){
                Processo proximoPu = fbck1.getListap().get(pos);//executa primeiro processo
                if(!executando(proximoPu, executados)){
                    inserirCpu(executados, proximoPu, processador);
                    removerPos(fbck1, pos);
                    return "p"+proximoPu.intToString(proximoPu.getId());
                }else{
                    pos++;
                }
            }
        
            
        }if(!fbck2.getListap().isEmpty()){
            int pos = 0;
            while(pos < fbck2.getListap().size()){
                Processo proximoPu = fbck2.getListap().get(pos);//executa primeiro processo
                if(!executando(proximoPu, executados)){
                    inserirCpu(executados, proximoPu, processador);
                    removerPos(fbck2, pos);
                    return "p"+proximoPu.intToString(proximoPu.getId());
                }else{
                    pos++;
                }
            }
            
        }if(!fbck3.getListap().isEmpty()){
            int pos = 0;
            while(pos < fbck3.getListap().size()){
                Processo proximoPu = fbck3.getListap().get(pos);//executa primeiro processo
                if(!executando(proximoPu, executados)){
                    inserirCpu(executados, proximoPu, processador);
                    removerPos(fbck3, pos);
                    return "p"+proximoPu.intToString(proximoPu.getId());
                }else{
                    pos++;
                }
            }
        }
        return "";
    }

    //_________BLOQUEIO______________
    
    public String bloquear(Fila fbloqueado, Fila fpronto_u, Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2,Processo p){

       
        boolean ok = false; //se conseguiu usar recurso
        System.out.println("imp1: "+p.im+" IMP1:"+imp1.isDisponibilidade()+" IMP2:"+imp2.isDisponibilidade());
        System.out.println("scanner: "+p.s+" Scanner:" +scan.isDisponibilidade());
        System.out.println("modem: "+p.m+" modem:"+modem.isDisponibilidade());
        System.out.println("Tcd1: "+p.cd+" cd1:"+cd1.isDisponibilidade()+ " cd2:"+cd2.isDisponibilidade());

        //__________IMPRESSORA__________
            if(p.getListarec()[0] == 1 && imp1.isDisponibilidade()){//se usa impressora,se impressora esta disponivel, e se o tempo de uso da impressora > 0

                    imp1.setDisponibilidade(false);
                    ok = true;//ainda nao sei pra que serve
                    p.ocupando=imp1;//qual recurso p esta ocupando
            
  
            }else if(p.getListarec()[0] == 1 && imp2.isDisponibilidade()){
                    
                    imp2.setDisponibilidade(false);
                    ok = true;
                    p.ocupando=imp2;
            }
            //_____________SCANNER______________
            else if(p.getListarec()[1] == 1 && scan.isDisponibilidade()){   
                    p.s--;
                    scan.setDisponibilidade(false);
                    ok = true;
                    p.ocupando=scan;   
            }  
        //_____________MODEM______________
            else if(p.getListarec()[2] == 1 && modem.isDisponibilidade()){
                
                    p.m--;
                    modem.setDisponibilidade(false);
                    ok = true;
                    p.ocupando=modem; 
            }
          
        //_____________CD______________
            else if(p.getListarec()[3] == 1 && cd1.isDisponibilidade() ){
                    cd1.setDisponibilidade(false);
                    ok = true;
                    p.ocupando=cd1;
                           
            }else if(p.getListarec()[3] == 1 && cd2.isDisponibilidade() ){
                    cd2.setDisponibilidade(false);
                    ok = true;
                    p.ocupando=cd2;
   

            }if(ok == true){
                inserir(p,fbloqueado);
                fpronto_u.getListap().remove(p);
                p.marca=true;
                return "";
            }
        
        return "";
    }
    
    //__________SUSPENSAO_____________

    //se proximo processo a executar excede memoria
    public String suspenderProc(Processo processoTr, Fila fpronto_u, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram, MemoriaHd hd){
	int tam_procTr = processoTr.getTamanho();            //pega tamanho do processo tr
	int tamFpronto = fpronto_u.getListap().size();   //pega tamanho da fila de usuario pronto
	int tamFbloq = fbloqueado.getListap().size();   //pega tamanho da fila de usuario bloqueado
	int memoriaLiberada = 0;
        
	//liberar espaco para processo tempo real
	while(memoriaLiberada < tam_procTr){
            //checar se tem processo na fila de bloqueados
            if(!mram.getFila_proc().isEmpty()){
		desalocaMemo(tam_procTr, memoriaLiberada, fpronto_u, fprontosuspenso, fbloqueado, fbloqsuspenso, fbck1,  fbck2,  fbck3, mram, hd);
            }
	}
        return "";
    }
    
    //coloca processos da fila atual em ordem de tamanho e desaloca
    public String desalocaMemo(int tam_procTr, int memoLiberada, Fila fpronto_u, Fila fprontosuspenso, Fila fbloqueado, Fila fbloqsuspenso, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram, MemoriaHd hd){
	int tamFatual = mram.getFila_proc().size();
        ArrayList<Processo> priori1 = new ArrayList();
        ArrayList<Processo> priori2 = new ArrayList();
        ArrayList<Processo> priori3 = new ArrayList();
        ArrayList<Processo> nova = new ArrayList();
        for(int i = 0; i < mram.getFila_proc().size(); i++){
            switch (mram.getFila_proc().get(i).getPrioridade()) {
                case 1:
                    priori1.add(mram.getFila_proc().get(i));
                    break;
                case 2:
                    priori2.add(mram.getFila_proc().get(i));
                    break;
                case 3:
                    priori3.add(mram.getFila_proc().get(i));
                    break;
                default:
                    break;
            }
        }
        
        for(int i = 0; i < priori1.size(); i++){
            nova.add(priori1.get(i));
        }
        for(int i = 0; i < priori2.size(); i++){
            nova.add(priori2.get(i));
        }
        for(int i = 0; i < priori3.size(); i++){
            nova.add(priori3.get(i));
        }
        
		
	int i = 0;
        //percorre vetor desalocando processo de maior tamanho
	while(memoLiberada < tam_procTr && i < tamFatual){  //enquanto nao libera memoria suficiente ou nao chega no fim da fila
            switch (nova.get(i).getEstado()) {
                case "BLOQUEADO":
                    swapper(nova.get(i), fbloqueado, fbloqsuspenso, mram, hd);
                    break;
                case "PRONTO-U":
                    swapper(nova.get(i), fpronto_u, fprontosuspenso, mram, hd);
                    break;
                case "F1":
                    swapper(nova.get(i), fbck1, fprontosuspenso, mram, hd);
                    break;
                case "F2":
                    swapper(nova.get(i), fbck2, fprontosuspenso, mram, hd);
                    break;
                case "F3":
                    swapper(nova.get(i), fbck3, fprontosuspenso, mram, hd);
                    break;
                default:
                    break;
            }
                //suspende processo pos i da fatual pra destino
            memoLiberada += nova.get(i).getTamanho();                         //pega tamanho do processo i
            nova.remove(i);
            i++;                                                    //anda na fila
	}
        
        //retorna quando liberou memoria suficiente ou acabou a fila
        return "";
    }
    
    //troca de processo entre memorias
    public String swapper(Processo p, Fila fatual, Fila fdestino,  MemoriaRam mram, MemoriaHd hd){
	inserir(p,fdestino);                   //insere na nova fila  
        fatual.getListap().remove(p);                     //remove da fila atual
        
	//altera memoria de acordo com estado que processo vai
	if(fdestino.getNome().equals("BLOQUEADO SUSPENSO") || fdestino.getNome().equals("PRONTO SUSPENSO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - p.getTamanho()); //sai da MP
	}
	if(fdestino.getNome().equals("BLOQUEADO") || fdestino.getNome().equals("PRONTO-U")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() + p.getTamanho()); //sai da MS	

	}
	return "";	
    }
   //__Transforma o ID de um processo em uma string para dar print na tela__
    public void processa_bloqueado(Fila fbloqueado, Fila fpronto_u, Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        Processo p;
        System.out.println("Processando bloqueados: tamanho da lista de bloqueados: "+ fbloqueado.getListap().size());
        int contador=0;                
        Processo lp[]= new Processo[6];
        if(fbloqueado.getListap().size()>0){
            for (int i =0; i<fbloqueado.getListap().size();i++){					//percorremos uma vez cada processo ocupando um dispositivo
                    p=fbloqueado.getListap().get(i);    //p=processo na posicao i
                    //p.setTemposervico(p.getTemposervico() -1); //Diminuimos o tempo geral de execucao 
                    int qualrecurso=0;// [0]=impressora , [1]=scanner, [2]=modem , [3]=cdrom
                    int tempodebloqueio=0;

                    //sequencia de testes pra saber o que esta ocupando e diminuir o tempo dessa ocupacao.
                    if(p.ocupando==imp1){
                        System.out.println("tempo de bloqueio antes: "+ p.im);
                        tempodebloqueio=--p.im;   
                    }
                    else if(p.ocupando==imp2){
                        System.out.println("tempo de bloqueio antes: "+ p.im);
                        tempodebloqueio=--p.im;    
                    }
                    else if(p.ocupando==scan){
                        System.out.println("tempo de bloqueio antes: "+ p.s);
                        tempodebloqueio=--p.s;
                        qualrecurso=1;
                    }
                    
                    else if(p.ocupando==modem){
                        System.out.println("tempo de bloqueio antes: "+ p.m);
                        tempodebloqueio=--p.m;
                        qualrecurso=2;
                    }
                    else if(p.ocupando==cd1){
                        System.out.println("tempo de bloqueio antes: "+ p.cd);
                        tempodebloqueio=--p.cd;
                        qualrecurso=3;
                    }
                    else if(p.ocupando==cd2){
                        System.out.println("-----------------------------------------\ntempo de bloqueio antes: "+ p.cd);
                        tempodebloqueio=--p.cd;
                        qualrecurso=3;
                    }
                    System.out.println("tempo de bloqueio depois: "+ tempodebloqueio);
                    System.out.println("Dados referentes a P"+p.getId()+"\n------------------------------------------");



                    if ( tempodebloqueio<=0){ //se o processo nao precisa continuar usando recurso

                            //mudanca de estados
                            

                            //atualizamos as informacoes das listas e dos recursos

                            //fbloqueado.getListap().remove(p);
                            lp[contador++]=p;
                            System.out.println("listaRec de P"+ p.getId()+" Antes: "+ p.getListarec()[qualrecurso]);
                            int [] nova = p.getListarec();//atualizar a lista de recursos necessarios no processo
                            nova[qualrecurso]=nova[qualrecurso]-1;
                            p.setListarec(nova);
                            System.out.println("listaRec de P"+ p.getId()+" depois: "+ p.getListarec()[qualrecurso]);
                            

                    }


            }
            for(int j=0;j<contador;j++){    
                if(lp[j]!=null){
                    Processo proc =lp[j];
                    fbloqueado.getListap().remove(proc);
                    System.out.println("FILA DE BLOQUEIO NO FINAL DA REMOCAO");
                    fbloqueado.imprimeFila(fbloqueado);
                    //System.out.println("Saiu de bloqueado p"+(proc.getId()));
                    System.out.println("processo P"+ proc.getId()+" Voltou a fila de prontos");
                    
                    proc.setEstadoAnterior("BLOQUEADO");
                    proc.setEstado("PRONTO");
                    fpronto_u.addProcesso(proc);
                    
                    (proc.ocupando).setDisponibilidade(true);//recurso esta disponivel ->ta funcionando
                    
                    
                    proc.ocupando.setUtilizador(null);//ngm esta usando o recurso
                    proc.ocupando=null;//processo nao usa recurso nenhum

                    }//em testes a ocupacao desta ok
            }

        }
    }

}
