package escalonadorso;

import java.util.ArrayList;

//Responsavel por chamar agentes para inserir, remover, executar, suspender, bloquear e finalizar processos
public class SistemaOperacional {  //antigo FilaMaster
    int[]  recdisponiveis = {2,1,1,2}; // Fila de recursos disponiveis

    Processo [] fbloq = new Processo[6]; // Fila de bloqueados
    //floq = {imp1, imp2, scanner, modem, cd1, cd2}
    
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
        //MOD 
        System.out.print(fatual.getNome()+": ");
        for(int i = 0; i < fatual.getListap().size(); i++){
            System.out.println(fatual.getListap().get(i).getId());
        }
        System.out.println();
        //
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

    
    public String finalizar(ArrayList<Processo> fexec, Processo p, Cpu processador, MemoriaRam mram){
        if(mram.getEspacoAlocado() > 0){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - p.getTamanho()); //libera memoria
        }else{
            mram.setEspacoAlocado(0);
        }
        mram.getFila_proc().remove(p);
        processador.setUtilizador(null);        //ocioso
        processador.setDisponibilidade(true);   //disponivel
        fexec.remove(p);
        //System.out.println("removeu exec "+p.getId());
        p.setEstado("FINALIZADO");
        return "";
    }
    
    public boolean listaZerada(int [] lista){
        boolean resp = true;
        int tam = lista.length;
        for(int i = 0; i < tam; i++){
            if(lista[i] != 0){
                resp = false;
            }
        }
        return resp; //true se lista zerada
    }

//__________CONTROLE SUPERIOR__________   
    
    
    //_____________CPU_____________
    
    //escolhe processo que sera executado
    public String dispatch(ArrayList<Processo> fexec, Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbck1, Fila fbck2, Fila fbck3, 
            Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd, Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        if(!fpronto_tr.getListap().isEmpty()){
            return executarProcessoTr(fexec, processador, fpronto_tr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  fbck1,  fbck2,  fbck3, mram,  hd);
        }else{
            return executarProcessoUsuario(fexec, processador, fpronto_u, fbloqueado, fbck1,  fbck2,  fbck3, imp1, imp2, modem, scan, cd1, cd2);
        }
    }
    
    //encontrou processo que vai ser executado pela CPU
    public String inserirCpu (ArrayList<Processo> fexec, Processo p, Cpu processador){
        //System.out.println("~ "+executando(p, fexec));
        //if(!p.getEstado().equals("EXECUTANDO") && !executando(p, fexec)){ MOD
            p.setEstado("EXECUTANDO");
            processador.setUtilizador(p);
            processador.setQuantum(2);
            processador.setDisponibilidade(false);
           
            //fexec.add(p); MOD
        //}
        return "p"+p.intToString(p.getId());
    }
    
    public boolean executando(Processo p, ArrayList<Processo> fexec){
        boolean r = false;
        for(int i = 0; i < fexec.size(); i++){
            if(p.getId() == fexec.get(i).getId()){
                r = true;
                break;
            }
        }
        return r;
    }
    
    //executa processo
    public String processa(ArrayList<Processo> fexec, Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram){
        Processo proc = processador.getUtilizador();
        if(proc == null){
            return "SEM PROCESSO. ";
        }
        proc.setTemposervico(proc.getTemposervico() - 1);
        if(proc.getTemposervico() <= 0){
            //se tempo de servico de p == 0 -> processo finalizado
            finalizar(fexec, proc, processador, mram);
        }
        if(proc.getPrioridade() != 0){//se processo de usuario
            processador.setQuantum(processador.getQuantum() - 1);
            if(processador.getQuantum() == 0){
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
                fexec.remove(proc);
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
    public String executarProcessoTr(ArrayList<Processo> fexec, Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram, MemoriaHd hd){
	Processo proximoTr = fpronto_tr.getListap().get(0);    //pega primeiro processo tr da fila de prontos
        int espacoAlocado = mram.getEspacoAlocado();          //quanto de memoria esta sendo usada
	if(espacoAlocado + fpronto_tr.getListap().get(0).getTamanho() > mram.getEspaco()){ //se memoria insuficiente
            suspenderProc(fpronto_tr.getListap().get(0), fpronto_u, fbloqueado, fbloqsuspenso, fprontosuspenso, fbck1,  fbck2,  fbck3, mram, hd);//suspende processo usuario
	}
	inserirCpu(fexec, proximoTr, processador);               //insere processo tr na fila de execucao
        remover(fpronto_tr);          //remove processo tr da fila de pronto
        
        return "p"+proximoTr.intToString(proximoTr.getId());
    }
    
    
        //__________EXECUCAO USUARIO__________
        ////Executa primeiro processo de usuario pronto
    public String executarProcessoUsuario(ArrayList<Processo> fexec, Cpu processador, Fila fpronto_u, Fila fbloqueado, Fila fbck1, Fila fbck2, Fila fbck3,
            Recurso imp1, Recurso imp2, Recurso modem, Recurso scan, Recurso cd1, Recurso cd2){
        if(!fpronto_u.getListap().isEmpty()){//se fila nao vazia
            Processo proximoPu = fpronto_u.getListap().get(0);
            int [] l = proximoPu.getListarec();
            if(!listaZerada(l)){            //se processo usa recursos
                bloquear(fbloqueado, fpronto_u, imp1, imp2, modem, scan, cd1, cd2);
                //bloqueio(this.fbloq, this.recdisponiveis, proximoPu);
                //remover(fpronto_u);
            }else{
                inserePriori(proximoPu, fbck1);//insere na primeira fila do feedback com prioridade
                remover(fpronto_u);
            }
        }
        return(executaFeedback(fexec, processador, fbck1, fbck2, fbck3));   
    }

    public String executaFeedback(ArrayList<Processo> fexec, Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3){
        if(!fbck1.getListap().isEmpty()){           //se primeira fila nao vazia
            Processo proximoPu = fbck1.getListap().get(0);//executa primeiro processo
            inserirCpu(fexec, proximoPu, processador);
            remover(fbck1);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else if(!fbck2.getListap().isEmpty()){
            Processo proximoPu = fbck2.getListap().get(0);
            inserirCpu(fexec, proximoPu, processador);
            remover(fbck2);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else if(!fbck3.getListap().isEmpty()){
            Processo proximoPu = fbck3.getListap().get(0);
            inserirCpu(fexec, proximoPu, processador);
            remover(fbck3);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else{
            return "Nenhum processo de usuario na fila.";
        }
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
        int i1=0,i2=0,c1=0,c2=0,m = p.getDispositivoDaListaRec(1)*porcentagem, s = p.getDispositivoDaListaRec(2)*porcentagem;
        if(p.getDispositivoDaListaRec(0)==2){
            i1 = p.getDispositivoDaListaRec(0)*porcentagem/2; 
            i2 = p.getDispositivoDaListaRec(0)*porcentagem/2;
        }else if(p.getDispositivoDaListaRec(0)==1){
            i1 = p.getDispositivoDaListaRec(0)*porcentagem;
        }
        if(p.getDispositivoDaListaRec(3)==2){
            c1=p.getDispositivoDaListaRec(3)*porcentagem/2;
            c2 = p.getDispositivoDaListaRec(3)*porcentagem/2;
        }else if(p.getDispositivoDaListaRec(3)==1){
            c1 = p.getDispositivoDaListaRec(3)*porcentagem;
        }
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
            System.out.println("aqui"+p.getId()+ok);
            if(p.getTemposervico()==0){
                
            }
            if(ok == true){
                inserir(p,fbloqueado);
                remover(fpronto);
                p.setTemposervico(p.getTemposervico()-1);
                
                return "";
                //int c [] = new int[4];
               // p.setListarec(c);
            }
        }
        if((!modem.isDisponibilidade())&&(!scan.isDisponibilidade())&&(!cd1.isDisponibilidade())&&(!cd2.isDisponibilidade())&&(!imp1.isDisponibilidade())&&(!imp2.isDisponibilidade())){
            remover(fbloqueado);
            inserir(p,fpronto);
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
        public void bloqueio (Processo[] fbloq,int[]  recdisponiveis,Processo p){ // ve um recurso do processo que esteja disponivel e o aloca.
            remover(p);
            if((p.getDispositivoDaListaRec(0)>0) && (recdisponiveis[0]>0)) { //Verifica se dispositivo usa impressoras e se ha uma disponivel
                    if(p.getDispositivoDaListaRec(0)==1){//checa se o programa usa apenas uma vez a impressora
                            if (fbloq[0]==null){//se a primeira impressora nao estiver sendo usada, designamos ela.
                                    fbloq[0]=p;	//ocupamos a 1 impressora com o processo p
                                    p.setDisipositivoDaListaRec(0,0);// dizemos agora que p nao precisa mais usar impressoras ao terminar seu tempo.
                            }else if (fbloq[1]==null){ 	//caso a segunda nao esteja sendo usada, designamos ela.
                                    fbloq[1]=p;
                                    p.setDisipositivoDaListaRec(0,0);
                            }
                    } 
                    if(p.getDispositivoDaListaRec(0)==2){ // checa se usa duas vezes a impressora
                            if (fbloq[0]==null){			//se a primeira nao estiver em uso, designamos ela
                                    fbloq[0]=p;
                                    p.setDisipositivoDaListaRec(0,1);
                            }else if (fbloq[1]==null){		// caso contrario usamos a segunda
                                    fbloq[1]=p;
                                    p.setDisipositivoDaListaRec(0,1);
                            }
                    }
            }
            if((p.getDispositivoDaListaRec(1)>0 )&&( recdisponiveis[1]>0)) {//Checamos se p usa scanner, e se o nosso esta disponivel
                    if (fbloq[2]==null){										//estou sendo redundante, mas quero saber se o scanner esta sendo ocupado por um processo
                                    fbloq[2]=p;											//ocupamos o scanner com o processo p
                                    p.setDisipositivoDaListaRec(1,0);					//dizemos agora que o processo ao sair de scanner, nao precisa mais voltar
                            }
                    }
            if((p.getDispositivoDaListaRec(2)>0) && (recdisponiveis[2]>0)) {	//checamos se p usa modem, e se o nosso esta disponivel
                    if (fbloq[3]==null){										//novamente redundante, vendo se o modem esta ocupado com algum processo
                                    fbloq[3]=p;											//ocupamos o modem com P
                                    p.setDisipositivoDaListaRec(2,0);					//dizemos que ao processo sair do modem, nao precisa mais voltar.
                    }
            }
            if((p.getDispositivoDaListaRec(3)>0) && (recdisponiveis[3]>0)) {       	//checamos se P usa CD-Rom e se temos um disponivel
                    if(p.getDispositivoDaListaRec(3)==1)						//se P usa 1 CD_rom
                            if (fbloq[4]==null){									//se CDROM1 esta sem processos
                                    fbloq[4]=p;											//Ocupamos cdrom1 com o processo P
                                    p.setDisipositivoDaListaRec(3,0);					//dizemos que ao sair, P nao precisara mais usar o cdrom
                            }else if (fbloq[5]==null){								//se CDROM2 esta sem processos
                                    fbloq[5]=p;											//Ocupamos cdrom2 com o processo P
                                    p.setDisipositivoDaListaRec(3,0);					//Dizemos que ao sair, P nao precisa mais usar o cdrom
                            }
                    if(p.getDispositivoDaListaRec(0)==2){						//Se P usa 2 Cd-Rom
                            if (fbloq[4]==null){										
                                    fbloq[4]=p;
                                    p.setDisipositivoDaListaRec(3,1);					//dizemos que ao sair, p ainda precisara usar o cdrom

                            }else if (fbloq[5]==null){
                                    fbloq[5]=p;
                                    p.setDisipositivoDaListaRec(3,1);  					//dizemos que ao sair, p ainda precisara usar o cdrom

                            }
                    }            
            }                    

    }
    public void processa_bloqueado(Fila fbloqueado,Recurso [] recdisponiveis, Fila fpronto){ //reduz o tempo dos processos bloqueados.
        Processo p;
        for (int i =0; i<fbloqueado.getListap().size();i++){					//percorremos uma vez cada processo ocupando um dispositivo
                p=fbloqueado.getListap().get(i);
                p.setTemposervico(p.getTemposervico() -1); //Diminuimos o tempo geral de execucao 

                //diminuimos o tempo necessario para usar o recurso

                if ( p.getTemposervico() <= 0){ //se o processo nao precisa continuar usando recurso

                        
                        p.setEstadoAnterior("BLOQUEADO");
                        p.setEstado("FINALIZADO");
                        System.out.println("#L636: Saiu de bloqueado p"+(p.intToString(p.getId())));
                        fbloqueado.getListap().remove(p);
                        //atualizamos a quantidade de recursos disponiveis

                }
                   
        }

    }

}
