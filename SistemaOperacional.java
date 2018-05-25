package escalonadorso;

import java.util.ArrayList;

//Responsavel por chamar agentes para inserir, remover, executar, suspender, bloquear e finalizar processos
public class SistemaOperacional {  //antigo FilaMaster
    
    public SistemaOperacional(){
    }
    
//__________CONTROLE INFERIOR____________
    public String inserir (Processo p, Fila fila){
        fila.addProcesso(p);
        p.setEstado(fila.getNome());
        return "p"+p.intToString(p.getId());
    }
    
    public String inserirCpu (Processo p, Cpu processador){
        p.setEstado("EXECUTANDO");
        processador.setUtilizador(p);
        processador.setQuantum(2);
        processador.setDisponibilidade(false);
        return "p"+p.intToString(p.getId());
    }
    
    public String remover(Fila fo){
        if(!fo.getListap().isEmpty()){
            Processo p = fo.getListap().get(0);
            p.setEstadoAnterior(fo.getNome());
            String s = "p"+p.intToString(p.getId());
            fo.getListap().remove(p);
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
    public String finalizar(Processo p, Fila fexec, MemoriaRam mram){
        //se tempo de servico de p == 0 -> processo finalizado
        mram.setEspacoAlocado(mram.getEspacoAlocado() - p.getTamanho());
        remover(fexec);
        p.setEstado("Finalizado");
        return "";
    }

//__________CONTROLE SUPERIOR__________   

    //_________BLOQUEIO
    
    public String bloquear(Fila fbloqueado, Fila fpronto ){

        Processo p = fpronto.getListap().get(0);
        inserir(p,fbloqueado);
        remover(fpronto);

        return "";
        /* Processo continua consumindo memoria ram, e a fila de bloqueados nao possui ordem.
        */
    }
    
    //__________SUSPENSAO_____________

    //troca de processo entre memorias
    public String swapper(int posEscolhida, Fila fatual, Fila fdestino,  MemoriaRam mram, MemoriaHd hd){
        Processo processoEscolhido = fatual.getListap().get(posEscolhida); //pega processo escolhida da fila
	inserir(processoEscolhido,fdestino);                   //insere na nova fila  
        remover(fatual);                     //remove da fila atual
	
	processoEscolhido.setEstado(fdestino.getNome());                   //atualiza estado do processo
        
	//altera memoria de acordo com estado que processo vai
	if(fdestino.getNome().equals("BLOQUEADO SUSPENSO") || fdestino.getNome().equals("PRONTO SUSPENSO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - processoEscolhido.getTamanho());
	}
	if(fdestino.getNome().equals("BLOQUEADO") || fdestino.getNome().equals("PRONTO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() + processoEscolhido.getTamanho());	

	}
	return "";	
    }

    //coloca processos em ordem de tamanho e desaloca
    public String desalocaMemo(int tamTr, int memoLiberada, Fila fatual, Fila fdestino, MemoriaRam mram, MemoriaHd hd){
	int tamFatual = fatual.getListap().size();
        int i = 0, n = 2;
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
	while(memoLiberada < tamTr && i < tamFatual){  //enquanto nao libera memoria suficiente ou nao chega no fim da fila
            memoLiberada += vetorTam[i][1];                         //pega tamanho do processo i
            swapper(vetorTam[i][0], fatual, fdestino, mram, hd);    //suspende processo i
            i++;                                                    //anda na fila
	}
        
        //retorna quando liberou memoria suficiente ou acabou a fila
        return "";
    }
	
    public String suspenderProc(Processo processoTr, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
	int tamTr = processoTr.getTamanho();            //pega tamanho do processo tr
	int tamFpronto = fupronto.getListap().size();   //pega tamanho da fila de usuario pronto
	int tamFbloq = fbloqueado.getListap().size();   //pega tamanho da fila de usuario bloqueado
	int memoriaLiberada = 0;
        
	//liberar espaco para processo tempo real
	while(memoriaLiberada < tamTr){
            //checar se tem processo na fila de bloqueados
            if(!fbloqueado.getListap().isEmpty()){
		desalocaMemo(tamTr, memoriaLiberada, fbloqueado, fbloqsuspenso, mram, hd);
            }
            //se memoria liberada insuficiente, tira processos de usuario da fila de prontos
            if(!fupronto.getListap().isEmpty()){
		desalocaMemo(tamTr, memoriaLiberada, fupronto, fprontosuspenso, mram, hd);
            }
            //caso contrario espera algum processo sair de execucao
	}
        return "";
    }
	
    //____________EXECUCAO____________
    //chamada quando CPU ociosa
    
    public String processa(Cpu processador, Fila fexec, Fila fbck1, Fila fbck2, Fila fbck3, MemoriaRam mram){
        Processo proc = processador.getUtilizador();
        if(proc == null){
            return "Nao tem processo.";
        }
        proc.setTemposervico(proc.getTemposervico() - 1);
        if(proc.getTemposervico() == 0){
            finalizar(proc, fbck1, mram);
            processador.setDisponibilidade(true);
            processador.setUtilizador(null);
        }
        if(proc.getPrioridade() != 0){
            processador.setQuantum(processador.getQuantum() - 1);
            if(processador.getQuantum() == 0){
                String f = proc.getEstadoAnterior();
                if(f.equals(fbck1.getNome())){
                    inserir(proc, fbck2);
                    remover(fbck1);
                }else if(f.equals(fbck2.getNome())){
                    inserir(proc, fbck3);
                    remover(fbck2);
                }else if(f.equals(fbck3.getNome())){
                    inserir(proc, fbck1);
                    remover(fbck3);
                }
                processador.setDisponibilidade(true);
            }
        }
        return "p"+proc.intToString(proc.getId())+" :"+proc.getEstadoAnterior()+" -> "+proc.getEstado();
    }
    
    public String escolheProcesso(Cpu processador, Fila fexec, Fila fbck1, Fila fbck2, Fila fbck3, Fila fprontotr, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
        if(!fprontotr.getListap().isEmpty()){
            return executarProcessoTr(processador, fprontotr,fexec,fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso,  mram,  hd);
        }else{
            System.out.println("pegou errado");
            return executarProcessoUsuario(processador, fexec, fbck1,  fbck2,  fbck3);
        }
    }
    //____________EXECUCAO TR_____________
    //Executa primeiro processo da fila de pronto
    public String executarProcessoTr(Cpu processador, Fila fprontotr, Fila fexec, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
	Processo proximoTr = fprontotr.getListap().get(0);    //pega primeiro processo tr da fila de prontos
        int espacoAlocado = mram.getEspacoAlocado();          //quanto de memoria esta sendo usada
	if(espacoAlocado + fprontotr.getListap().get(0).getTamanho() > mram.getEspaco()){ //se memoria insuficiente
            suspenderProc(fprontotr.getListap().get(0), fupronto, fbloqueado, fbloqsuspenso, fprontosuspenso, mram, hd);//suspende processo usuario
	}
	inserirCpu(proximoTr, processador);               //insere processo tr na fila de execucao
        proximoTr.setTemposervico(proximoTr.getTemposervico()-1);//menos 1 pro fim da execucao
        remover(fprontotr);          //remove processo tr da fila de pronto
        
        return "p"+proximoTr.intToString(proximoTr.getId());
    }
    
    
    //__________EXECUCAO USUARIO__________
    public String executarProcessoUsuario(Cpu processador, Fila fexec, Fila fbck1, Fila fbck2, Fila fbck3){
        if(!fbck1.getListap().isEmpty()){
            Processo proximoPu = fbck1.getListap().get(0);
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
    
    //__Transforma o ID de um processo em uma string para dar print na tela__

}
