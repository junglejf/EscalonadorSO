
public class Processo {
	public static final int NOVO = 0 , PRONTO = 1, EXECUTANDO = 2, BLOQUEADO = 3, SUSPENSOBLOQUEADO = 4, PRONTOSUSPENSO = 5, SAIDA = 6 ;
	int estado; // NOVO = 0 , PRONTO = 1, EXECUTANDO = 2, BLOQUEADO = 3, SUSPENSOBLOQUEADO = 4, PRONTOSUSPENSO = 5, SAIDA = 6
	int tamanho;
	int tempochegada;
	int temposervico;
	
	public Processo(int estado, int tamanho, int tempochegada, int temposervico){
		this.estado= estado;
		this.tamanho = tamanho;
		this.tempochegada = tempochegada;
		this.temposervico = temposervico;
	}
	
	
}

public class Pusuario extends Processo{
	int prioridade;
	
	public Pusuario(int estado, int tamanho, int tempochegada, int temposervico, int taint prioridade){
		super(estado);
		super(tamanho);
		super(tempochegada);
		super(temposervico);
		this.prioridade = prioridade;
	}
	
}

public class Ptemporeal extends Processo{
	
	public Ptemporeal(int estado, int tamanho, int tempochegada, int temposervico){
		super(estado);
		super(tempochegada);
		super(temposervico);
		this.tamanho = 512;
		
	}
}

