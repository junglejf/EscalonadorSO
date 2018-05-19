package escalonador;

import java.util.ArrayList;

//Responsavel por chamar agentes para inserir, remover, executar, suspender, bloquear e finalizar processos
public class SistemaOperacional {  //antigo FilaMaster
    
    /*
    private Fila [] filas;
	
    public SistemaOperacional(Fila[] filas){
	this.filas = filas;
    }

    public Fila[] getFilas() {
        return filas;
    }

    public void setFilas(Fila[] filas) {
        this.filas = filas;
    }
    */
    
//__________CONTROLE INFERIOR____________
    public String inserir (Processo p, ArrayList<Processo> fila){
        fila.add(p);
    }
    public String remover(Processo p, ArrayList<Processo> fila){
        fila.remove(p);
        
    }
    public String finalizar(Processo p, Fila fexec, MemoriaRam mram){
        //se tempo de servico de p == 0 -> processo finalizado
        remover(p, fexec.getListap());
        remover(p, mram.getListap());
        p.setEstado("Finalizado");
    }

//__________CONTROLE SUPERIOR__________   
    
    //seleciona quem executar
    public String dispatch(Fila fprontotr, Fila fexec, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
        if(fprontotr.getListap().isEmpty()){ //processo de usuario so executado se nao tem tr na fila
            executarProcessoUsuario();
        }else{
            while(!fprontotr.getListap().isEmpty()){//enquanto tem processos tr executa
                executarProcessoTr(fprontotr, fexec, fupronto, fbloqueado,  fbloqsuspenso, fprontosuspenso, mram, hd);
            }
        }
    }

    //_________BLOQUEIO
    
    
    //__________SUSPENSAO_____________

    //troca de processo entre memorias
    public String swapper(int posEscolhida, Fila fatual, Fila fdestino,  MemoriaRam mram, MemoriaHd hd){
        Processo processoEscolhido = fatual.getListap().get(posEscolhida); //pega processo escolhida da fila
	remover(processoEscolhido,fatual.getListap());                     //remove da fila atual
	inserir(processoEscolhido,fdestino.getListap());                   //insere na nova fila  
	processoEscolhido.setEstado(fdestino.getNome());                   //atualiza estado do processo
        
	//altera memoria de acordo com estado que processo vai
	if(fdestino.getNome().equals("BLOQUEADO SUSPENSO") || fdestino.getNome().equals("PRONTO SUSPENSO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() - processoEscolhido.getTamanho());
            remover(processoEscolhido, mram.getListap());   //remove processo da ram
            inserir(processoEscolhido, hd.getListap());     //insere processo no hd
	}
	if(fdestino.getNome().equals("BLOQUEADO") || fdestino.getNome().equals("PRONTO")){
            mram.setEspacoAlocado(mram.getEspacoAlocado() + processoEscolhido.getTamanho());	
            remover(processoEscolhido, hd.getListap());   //remove processo do hd
            inserir(processoEscolhido, mram.getListap());     //insere processo na ram
	}
		
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
    }
	
    //____________EXECUCAO TR_____________

    //Executa primeiro processo da fila de pronto
    public String executarProcessoTr(Fila fprontotr, Fila fexec, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd){
	Processo proximoTr = fprontotr.getListap().get(0);    //pega primeiro processo tr da fila de prontos
        int espacoAlocado = mram.getEspacoAlocado();          //quanto de memoria esta sendo usada
	if(espacoAlocado + fprontotr.getListap().get(0).getTamanho() > mram.getEspaco()){ //se memoria insuficiente
            suspenderProc(fprontotr.getListap().get(0), fupronto, fbloqueado, fbloqsuspenso, fprontosuspenso, mram, hd);//suspende processo usuario
	}
	inserir(proximoTr, fexec.getListap());               //insere processo tr na fila de execucao
        proximoTr.setTemposervico(proximoTr.getTemposervico()-1);//menos 1 pro fim da execucao
        remover(proximoTr, fprontotr.getListap());          //remove processo tr da fila de pronto
    }
    
    //__________EXECUCAO USUARIO__________
    public String executarProcessoUsuario(Fila fexec, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, MemoriaHd hd)){
        
    }
	
}
