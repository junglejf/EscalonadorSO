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
                bloquear(fbloqueado, fpronto_u, imp1, imp2, modem, scan, cd1, cd2);
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
    
    public String bloquear(Fila fbloqueado, Fila fpronto, Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        
        //inserir processo na lista de processos, mudar recurso, chama execrec
        //addRec(....)
        
        
        Processo p = fpronto.getListap().get(0);
        int t = p.getTamanho();
        int nrec = 1; //No minimo CPU
        for(int i = 0; i < p.getListarec().length; i++){
            nrec += p.getListarec()[i];
        }
        int porcentagem = t/nrec; //tempo em cada recurso
        boolean ok = false; //se conseguiu usar recurso
        int i1 = porcentagem, i2 = porcentagem, m = porcentagem, s = porcentagem, c1 = porcentagem, c2 = porcentagem;
        while(!listaZerada(p.getListarec())){
            
        //__________IMPRESSORA__________
            if(p.getListarec()[0] == 1){
                if(imp1.isDisponibilidade() && i1 > 0){
                    i1--;
                    imp1.setDisponibilidade(false);
                    ok = true;
                    if(i1 == 0){
                        int [] nova = p.getListarec();
                        nova[0] = 0;
                        p.setListarec(nova);
                        imp1.setDisponibilidade(true);
                    }
                }else if(imp2.isDisponibilidade()){
                    i2--;
                    imp2.setDisponibilidade(false);
                    ok = true;
                    if(i2 == 0){
                        int [] nova = p.getListarec();
                        nova[0] = 0;
                        p.setListarec(nova);
                        imp2.setDisponibilidade(true);
                    }
                }
            }else if(p.getListarec()[0] == 2){
                if(i1 > 0 && i2 > 0){
                    if(imp1.isDisponibilidade() && imp2.isDisponibilidade()){
                        i1--;
                        i2--;
                        imp1.setDisponibilidade(false);
                        imp2.setDisponibilidade(false);
                        ok = true;
                        if(i1 == 0 && i2 == 0){
                            int [] nova = p.getListarec();
                            nova[0] = 0;
                            p.setListarec(nova);
                            imp1.setDisponibilidade(true);
                            imp2.setDisponibilidade(true);
                        }
                    }else if(imp1.isDisponibilidade()){
                        i1--;
                        imp1.setDisponibilidade(false);
                        ok = true;
                        if(i1 == 0){
                            int [] nova = p.getListarec();
                            nova[0] = 1;
                            p.setListarec(nova);
                            imp1.setDisponibilidade(true);
                        }
                    }else if(imp2.isDisponibilidade()){
                        i2--;
                        imp2.setDisponibilidade(false);
                        ok = true;
                        if(i2 == 0){
                            int [] nova = p.getListarec();
                            nova[0] = 1;
                            p.setListarec(nova);
                            imp2.setDisponibilidade(true);
                        }
                    }
                }
                
            }
        
        //_____________CD______________
            if(p.getListarec()[3] == 1){
                if(cd1.isDisponibilidade() && c1 > 0){
                    c1--;
                    cd1.setDisponibilidade(false);
                    ok = true;;
                    if(c1 == 0){
                        int [] nova = p.getListarec();
                        nova[3] = 0;
                        p.setListarec(nova);
                        cd1.setDisponibilidade(true);
                    }
                }else if(cd2.isDisponibilidade()){
                    c2--;
                    cd2.setDisponibilidade(false);
                    ok = true;
                    if(c2 == 0){
                        int [] nova = p.getListarec();
                        nova[3] = 0;
                        p.setListarec(nova);
                        cd2.setDisponibilidade(true);
                    }
                }
            }else if(p.getListarec()[3] == 2){
                if(c1 > 0 && c2 > 0){
                    if(cd1.isDisponibilidade() && cd2.isDisponibilidade()){
                        c1--;
                        c2--;
                        cd1.setDisponibilidade(false);
                        cd2.setDisponibilidade(false);
                        ok = true;
                        if(c1 == 0 && c2 == 0){
                            int [] nova = p.getListarec();
                            nova[3] = 0;
                            p.setListarec(nova);
                            cd1.setDisponibilidade(true);
                            cd2.setDisponibilidade(true);
                        }
                    }else if(cd1.isDisponibilidade()){
                        c1--;
                        cd1.setDisponibilidade(false);
                        ok = true;
                        if(c1 == 0){
                            int [] nova = p.getListarec();
                            nova[3] = 1;
                            p.setListarec(nova);
                            cd1.setDisponibilidade(true);
                        }
                    }else if(cd2.isDisponibilidade()){
                        c2--;
                        cd2.setDisponibilidade(false);
                        ok = true;
                        if(c2 == 0){
                            int [] nova = p.getListarec();
                            nova[3] = 1;
                            p.setListarec(nova);
                            cd2.setDisponibilidade(true);
                        }
                    }
                }
                
            }
            
        //_____________MODEM______________
            if(p.getListarec()[1] == 1){
                if(modem.isDisponibilidade() && m > 0){
                    m--;
                    modem.setDisponibilidade(false);
                    ok = true;
                    if(m == 0){
                        int [] nova = p.getListarec();
                        nova[1] = 0;
                        p.setListarec(nova);
                        modem.setDisponibilidade(true);
                    }
                }
            }
        
        //_____________SCANNER______________
            if(p.getListarec()[2] == 1){
                if(scan.isDisponibilidade() && s > 0){
                    s--;
                    scan.setDisponibilidade(false);
                    ok = true;
                    if(s == 0){
                        int [] nova = p.getListarec();
                        nova[2] = 0;
                        p.setListarec(nova);
                        scan.setDisponibilidade(true);
                    }
                }
            }
            
            if(ok == true){
                inserir(p,fbloqueado);
                remover(fpronto);
                p.setTemposervico(p.getTemposervico()-1);
            }
        }
        
        return "";
        /* Processo continua consumindo memoria ram, e a fila de bloqueados nao possui ordem.
        */
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

}
