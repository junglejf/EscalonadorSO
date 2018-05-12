public class Recurso{
	private Processo [] listaEmExecucao;
	private boolean disponibilidade;
	private String nome;
	
	public Recurso(Processo [] listaEmExecucao, boolean disponibilidade, String nome){
		this.listaEmExecucao = listaEmExecucao;
		this.disponibilidade = disponibilidade;
		this.nome = nome;
	}
}

public Memoria extends Recurso{
	private int espaco;
	private int espacoAlocado;
	
	public Memoria(int espaco, int espacoAlocado){
		this. espaco = espaco;
		this.espacoAlocado = espacoAlocado;
	}
	
	//public int getEs
	
}

public MemoriaRam extends Memoria{
	
	public MemoriaRam(int espaco){
		this.espaco = 8192;
	}
}