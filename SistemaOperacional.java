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
        return "p"+p.intToString(p.getId());
    }
    
    //fila organizada com prioridade
    public String inserePriori(Processo p, Fila fatual){
        int tamFatual = fatual.getListap().size();
        int i = 0, n = 2;//pos e prioridade
	int [][] vetorTam = new int[tamFatual][n];	//vetor de 2 colunas que guarda pos do processo e tamanho
        ArrayList<Processo> nova = new ArrayList();     //lista com processos ordenados pro prioridade
        
        if(tamFatual > i){                              //se fila nao vazia
            //armazena primeiro processo
            vetorTam[i][0] = i;                                     //coluna 0: posicao
            vetorTam[i][1] = fatual.getListap().get(i).getPrioridade();//coluna 1: tamanho 
        
            //armazena em ordem decrescente
            for(i = 1; i < tamFatual-1; i++){
                if(fatual.getListap().get(i).getPrioridade() > vetorTam[i-1][1]){ //se prox fila maior que atual no vetor
                    //salva processo atual
                    int tempprio = vetorTam[i-1][1];     
                    int temppos = vetorTam[i-1][0];     
                    //atualiza atual com maior
                    vetorTam[i-1][1] = fatual.getListap().get(i).getPrioridade();  //atualiza atual com maior
                    vetorTam[i-1][0] = i-1;
                    //atualiza proximo com menor (ordem decrescente)
                    vetorTam[i][1] = tempprio;           
                    vetorTam[i][0] = temppos;
                }
            }
            
            //cria lista ordenada
            for(i = 0; i < tamFatual; i++){
               nova.add(fatual.getListap().get(vetorTam[i][0])); //pega processo com maior prioridade (em ordem no vetor)
            }
            
            fatual.setListap(nova); //altera lista pra nova lista ordenada por prioridade
        }
        
        return "";
    }
    
    
    public String remover(Fila forigem){
        if(!forigem.getListap().isEmpty()){
            Processo p = forigem.getListap().get(0);
            p.setEstadoAnterior(forigem.getNome());
            String s = "p"+p.intToString(p.getId());
            forigem.getListap().remove(p);
            System.out.println("removeu "+s);
            return s;
        }else{
            return "Fila vazia";
        }
    }
    
    public String removerMidProc(Fila f, Processo p){
        return "";
    }
    public int buscar_processo(Fila f, Processo p){
        ArrayList <Processo> processos = f.getListap();
        for(int i=0; i<processos.size();i++){
            if(p.getId() == processos.get(i).getId()){
                return i;
            }
        }
        return -1;
    }
    public String finalizar(Processo p, Cpu processador, MemoriaRam mram){
        mram.setEspacoAlocado(mram.getEspacoAlocado() - p.getTamanho());    //libera memoria
        processador.setUtilizador(null);        //ocioso
        processador.setDisponibilidade(true);   //disponivel
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
    public String dispatch(Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbck1, Fila fbck2, Fila fbck3, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
        if(!fpronto_tr.getListap().isEmpty()){
            return executarProcessoTr(processador, fpronto_tr, fpronto_u, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
        }else{
            System.out.println("pegou errado");
            return executarProcessoUsuario(processador, fpronto_u, fbloqueado, fbck1,  fbck2,  fbck3);
        }
    }
    
    //encontrou processo que vai ser executado pela CPU
    public String inserirCpu (Processo p, Cpu processador){
        p.setEstado("EXECUTANDO");
        processador.setUtilizador(p);
        processador.setQuantum(2);
        processador.setDisponibilidade(false);
        return "p"+p.intToString(p.getId());
    }
    
    //executa processo
    public String processa(Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram){
        Processo proc = processador.getUtilizador();
        if(proc == null){
            return "Nao tem processo.";
        }
        proc.setTemposervico(proc.getTemposervico() - 1);
        if(proc.getTemposervico() < 0){
            //se tempo de servico de p == 0 -> processo finalizado
            finalizar(proc, processador, mram);
        }
        if(proc.getPrioridade() != 0){//se processo de usuario
            processador.setQuantum(processador.getQuantum() - 1);
            if(processador.getQuantum() == 0){
                String f_anterior = proc.getEstadoAnterior();
                if(f_anterior.equals(fbck1.getNome())){ //se tava na fila 1 vai pra fila 2
                    inserePriori(proc, fbck2);
                    remover(fbck1);
                }else if(f_anterior.equals(fbck2.getNome())){ //se tava na fila 2 vai pra fila 3
                    inserePriori(proc, fbck3);
                    remover(fbck2);
                }else if(f_anterior.equals(fbck3.getNome())){ //se tava na fila 3 vai pra fila 1 
                    inserePriori(proc, fbck1);
                    remover(fbck3);
                }
                processador.setDisponibilidade(true);
            }
        }
        return "ts = "+proc.getTemposervico()+" | p"+proc.intToString(proc.getId())+": "+proc.getEstadoAnterior()+" -> "+proc.getEstado();
    }
    
    //____________EXECUCAO____________
    //chamada quando CPU ociosa
    
    
        //____________EXECUCAO TR_____________
        //Executa primeiro processo de tempo real pronto
    public String executarProcessoTr(Cpu processador, Fila fpronto_tr, Fila fpronto_u, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
	Processo proximoTr = fpronto_tr.getListap().get(0);    //pega primeiro processo tr da fila de prontos
        int espacoAlocado = mram.getEspacoAlocado();          //quanto de memoria esta sendo usada
	if(espacoAlocado + fpronto_tr.getListap().get(0).getTamanho() > mram.getEspaco()){ //se memoria insuficiente
            suspenderProc(fpronto_tr.getListap().get(0), fpronto_u, fbloqueado, fbloqsuspenso, fprontosuspenso, mram, hd);//suspende processo usuario
	}
	inserirCpu(proximoTr, processador);               //insere processo tr na fila de execucao
        remover(fpronto_tr);          //remove processo tr da fila de pronto
        
        return "p"+proximoTr.intToString(proximoTr.getId())+" | tam: "+fpronto_tr.getListap().size();
    }
    
    
        //__________EXECUCAO USUARIO__________
        ////Executa primeiro processo de usuario pronto
    public String executarProcessoUsuario(Cpu processador, Fila fpronto_u, Fila fbloqueado, Fila fbck1, Fila fbck2, Fila fbck3){
        if(!fpronto_u.getListap().isEmpty()){//se fila nao vazia
            Processo proximoPu = fpronto_u.getListap().get(0);
            int [] l = proximoPu.getListarec();
            if(!listaZerada(l)){            //se processo usa recursos
                bloquear(fbloqueado, fpronto_u);
            }else{
                inserePriori(proximoPu, fbck1);//insere na primeira fila do feedback com prioridade
            }
            return executaFeedback(processador, fbck1, fbck2, fbck3);      
        }else{
            return "Nenhum processo de usuario na fila.";
        }
    }

    public String executaFeedback(Cpu processador, Fila fbck1, Fila fbck2, Fila fbck3){
        if(!fbck1.getListap().isEmpty()){           //se primeira fila nao vazia
            Processo proximoPu = fbck1.getListap().get(0);//executa primeiro processo
            inserirCpu(proximoPu, processador);
            remover(fbck1);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else if(!fbck2.getListap().isEmpty()){
            Processo proximoPu = fbck2.getListap().get(0);
            inserirCpu(proximoPu, processador);
            remover(fbck2);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else if(!fbck3.getListap().isEmpty()){
            Processo proximoPu = fbck3.getListap().get(0);
            inserirCpu(proximoPu, processador);
            remover(fbck3);
            return "p"+proximoPu.intToString(proximoPu.getId());
        }else{
            return "Nenhum processo de usuario na fila.";
        }
    }

    //_________BLOQUEIO______________
    
    public String bloquear(Fila fbloqueado, Fila fpronto){

        Processo p = fpronto.getListap().get(0);
        inserir(p,fbloqueado);
        remover(fpronto);

        return "";
        /* Processo continua consumindo memoria ram, e a fila de bloqueados nao possui ordem.
        */
    }
    
    //__________SUSPENSAO_____________

    //se proximo processo a executar excede memoria
    public String suspenderProc(Processo processoTr, Fila fpronto_u, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
	int tam_procTr = processoTr.getTamanho();            //pega tamanho do processo tr
	int tamFpronto = fpronto_u.getListap().size();   //pega tamanho da fila de usuario pronto
	int tamFbloq = fbloqueado.getListap().size();   //pega tamanho da fila de usuario bloqueado
	int memoriaLiberada = 0;
        
	//liberar espaco para processo tempo real
	while(memoriaLiberada < tam_procTr){
            //checar se tem processo na fila de bloqueados
            if(!fbloqueado.getListap().isEmpty()){
		desalocaMemo(tam_procTr, memoriaLiberada, fbloqueado, fbloqsuspenso, mram, hd);
            }
            //se memoria liberada insuficiente, tira processos de usuario da fila de prontos
            if(!fpronto_u.getListap().isEmpty()){
		desalocaMemo(tam_procTr, memoriaLiberada, fpronto_u, fprontosuspenso, mram, hd);
            }
            //caso contrario espera algum processo sair de execucao
	}
        return "";
    }
    
    //coloca processos da fila atual em ordem de tamanho e desaloca
    public String desalocaMemo(int tam_procTr, int memoLiberada, Fila fatual, Fila fdestino, MemoriaRam mram, MemoriaHd hd){
	int tamFatual = fatual.getListap().size();
        int i = 0, n = 2;//pos e tam
	int [][] vetorTam = new int[tamFatual][n];	//vetor de 2 colunas que guarda pos do processo e tamanho
        
        if(tamFatual > i){                              //se fila nao vazia
            //armazena primeiro processo
            vetorTam[i][0] = i;                                     //coluna 0: posicao
            vetorTam[i][1] = fatual.getListap().get(i).getTamanho();//coluna 1: tamanho 
        
            //armazena em ordem decrescente
            for(i = 1; i < tamFatual-1; i++){
                if(fatual.getListap().get(i).getTamanho() > vetorTam[i-1][1]){ //se prox fila maior que atual no vetor
                    //salva processo atual
                    int temptam = vetorTam[i-1][1];     
                    int temppos = vetorTam[i-1][0];     
                    //atualiza atual com maior
                    vetorTam[i-1][1] = fatual.getListap().get(i).getTamanho();  //atualiza atual com maior
                    vetorTam[i-1][0] = i-1;
                    //atualiza proximo com menor (ordem decrescente)
                    vetorTam[i][1] = temptam;           
                    vetorTam[i][0] = temppos;
                }
            }
        }
        
        //se lista vazia esperar processo que esta executando
		
	i = 0;
        //percorre vetor desalocando processo de maior tamanho
	while(memoLiberada < tam_procTr && i < tamFatual){  //enquanto nao libera memoria suficiente ou nao chega no fim da fila
            memoLiberada += vetorTam[i][1];                         //pega tamanho do processo i
            swapper(vetorTam[i][0], fatual, fdestino, mram, hd);    //suspende processo pos i da fatual pra destino
            i++;                                                    //anda na fila
	}
        
        //retorna quando liberou memoria suficiente ou acabou a fila
        return "";
    }
    
    //troca de processo entre memorias
    public String swapper(int posEscolhida, Fila fatual, Fila fdestino,  MemoriaRam mram, MemoriaHd hd){
        Processo processoEscolhido = fatual.getListap().get(posEscolhida); //pega processo escolhida da fila
	inserir(processoEscolhido,fdestino);                   //insere na nova fila  
        remover(fatual);                     //remove da fila atual
        
	//altera memoria de acordo com estado que processo vai
	if(fdestino.getNome().equals("BLOQUEADO SUSPENSO") || fdestino.getNome().equals("PRONTO SUSPENSO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - processoEscolhido.getTamanho()); //sai da MP
	}
	if(fdestino.getNome().equals("BLOQUEADO") || fdestino.getNome().equals("PRONTO-U")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() + processoEscolhido.getTamanho()); //sai da MS	

	}
	return "";	
    }
   //__Transforma o ID de um processo em uma string para dar print na tela__

}
