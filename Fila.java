import java.util.*;


public class Fila{
	private ArrayList<Processo> listap;
	private String nome;
	
	
	public Fila(ArrayList<Processo> listap, String nome){
		this.listap = listap;
		this.nome = nome;
	}
	



}

public class FilaFeedback extends Fila{
	private static final int quantum = 2;
	
	public FilaUsuario(Pusuario[] listap, String nome,int quantum){
		super(listap);
		super(nome);
	}
	
	public String feedback(Pusuario puser, Fila[]f){
		
	}
}

public class FilaExec {
	private ArrayList<Fila> filas;
	int qtdProcessos;
	
	public FilaExec(ArrayList<Fila> filas, int qtdProcessos){
		this.filas = filas;
		this.qtdProcessos = qtdProcessos;
	}
	
}
	
public class FilaMaster {
	private Fila [] filas;
	
	public FilaMaster(Fila[] filas){
		this.filas = filas;
	}
		
	public String inserir (Processo p, Fila f){
	}	
	public String remover(Processo p, Fila f){
	}	

//__________SUSPENSAO_____________

	//troca de processo entre as filas
	public String swapper(int posEscolhida, Fila fatual, Fila fdestino,  MemoriaRam mram, Memoria hd){
		Pusuario processoEscolhido = fatual[posEscolhida]);
		remover(processoEscolhido,fatual);
		inserir(processoEscolhido,fdestino);
		processoEscolhido.setEstado(fdestino.getNome());
		//altera memoria de acordo com estado que processo vai
		if(fdestino.getNome().equals("SUSPENSOBLOQUEADO") || fdestino.getNome().equals("PRONTOSUSPENSO")){
			//hd.setEspacoAlocado(hd.getEspacoAlocado() + processoEscolhido.getTamnho());
			mram.setEspacoAlocado(mram.getEspacoAlocado() - processoEscolhido.getTamnho());	
		}
		if(fdestino.getNome().equals("BLOQUEADO") || fdestino.getNome().equals("PRONTO")){
			mram.setEspacoAlocado(mram.getEspacoAlocado() + processoEscolhido.getTamnho());	
		}
		
		
	}

	//coloca processos em ordem de tamanho e desaloca
	public String desalocaMemo(int memoLiberada, int tamFatual, Fila fatual, Fila fdestino, MemoriaRam mram, Memoria hd){
		int i, n = 2;
		int vetorTam [tamFatual][n];	//vetor que ordena processos prontos em tamanho decrescente
		for(i = 0; i < tamFatual; i++){
			vetorTam[i][0] = i;
			vetorTam[i][1] = fatual.getFila(i).getTamnho();
				
		}

		//FAZER: ordena vetorTam por tamanho
		
		i = 0;
		while(memoLiberada < tamTr){
			memoLiberada += vetorTam[i][1];
			swapper(i, fatual, fdestino, mram, hd);
			i++;
		}

	}
	
	public String suspenderProc(Ptemporeal processoTr, Fila fupronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, Memoria hd){
		int tamTr = processoTr.getTamnho();
		int tamFpronto = fupronto.getTamnho();
		int tamFbloq = fbloqueado.getTamnho();

		int memoriaLiberada = 0;
		//achar espaço para processo tempo real
		while(memoriaLiberada < tamTr){
			//checar se tem processo na fila de prontos
			if(!fbloqueado.isEmpty()){
				desalocaMemo(memoriaLiberada, tamFbloq, fbloqueado, fbloqsuspenso, mram, hd);
			}
			if(!fpronto.isEmpty()){
				desalocaMemo(memoriaLiberada, tamFpronto, fupronto, fprontosuspenso, mram, hd);
			
			}
		}
	}
	

//____________EXECUCAO TR_____________

	//Define quem será executado
	public String executarProcessoTr(Fila fprontotr, Fila fexec, Fila upronto, Fila fbloqueado,  Fila fbloqsuspenso, Fila fprontosuspenso, MemoriaRam mram, Memoria hd){
		int espacoAlocado = mram.getEspacoAlocado();
		if(espacoAlocado + fprontotr.getFila(0).getTamanho() > m.getEspaco()){ //se proximo proc tr nao cabe na memoria
			suspenderProc(fprontotr.getFila(0), fupronto, fbloqueado, fbloqsuspenso, fprontosuspenso, mram, hd);
		}
		inserir(f.getFila(0),fexec);	
	}
	
}
