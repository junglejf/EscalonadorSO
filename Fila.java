import java.util;

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
		
	public String inserir (Processo p, Fila f ){
	}	
	public String remover(Processo p, Fila f){
	}	
	
	//responsável por fazer a troca de processo entre as filas
	public String swapper(Fila fatual, Fila fdestino,  MemoriaRam mram,Memoria hd){
		Pusuario ultimoProcesso = fatual.get(fatual.size()-1);
		remover(ultimoProcesso,fatual);
		inserir(ultimoProcesso,fdestino);
		ultimoProcesso.setEstado(fdestino.getNome);
		hd.setEspacoAlocado(hd.getEspacoAlocado() + ultimoProcesso.getTamnho());
		mram.setEspacoAlocado(mram.getEspacoAlocado() + ultimoProcesso.getTamnho());
		
	}
	
	public String suspenderPuser(Ptemporeal processoTr, Fila fpronto,FilaExec fexecUser,  Fila fsuspenso, MemoriaRam mram, Memoria hd){
		int tamTr = processoTr.getTamnho();
		int memoriaLiberada = 0;
		//achar espaço para processo tempo real
		while(memoriaLiberada < tamTr){
			//checar se tem processo na fila de prontos
			if(!fpronto.isEmpty()){
				int tamPuser = fpronto.get(fpronto.size()-1).getTamnho(); // pegar o tamanho do processo e verificar se liberou mem suficiente
				memoriaLibera += tamPuser; 
				
				swapper(fpronto, fsuspenso, mram, hd);
			}else if(!fexecUser.isEmpty()){
				
				int tamPuser = fexec.get(fexec.size()-1).getTamnho(); // pegar o tamanho do processo e verificar se liberou mem suficiente
				memoriaLibera += tamPuser; 
				
				swapper(fexec, fsuspenso, mram, hd);
			}
		}
	}
	
	//Define quem será executado
	public String executarProcessoTr(Fila fpronto, Fila fexec, Fila fsuspenso, MemoriaRam m, Memoria hd){
		int espacoAlocado = m.getEspacoAlocado();
		if(espacoAlocado + fpronto.getFila(0).getTamnho() < m.getEspaco()){
			inserir(f.getFila(0),fexec);
		}else{
			suspender()
		}		
	}
	
}