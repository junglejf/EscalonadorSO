package escalonadorso;

import java.util.*;


public class Memoria extends Recurso{
	private int espaco;
	private int espacoAlocado;
	
 
	public Memoria(Processo utilizador, boolean disponibilidade, String nome,int espaco, int espacoAlocado){
            super(utilizador,disponibilidade,nome);
            this.espaco = espaco;
            this.espacoAlocado = espacoAlocado;
	}
	
	//public int getEs
	
}
