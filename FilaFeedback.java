package escalonadorso;

import java.util.ArrayList;

//Classe que representa cada fila do feedback, total de 3
public class FilaFeedback extends Fila{
    private static final int quantum = 2;
	
    public FilaFeedback(ArrayList<Processo> listap, String nome){
	super(listap, nome);
    }
	
	
}
