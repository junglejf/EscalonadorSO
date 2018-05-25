package escalonadorso;

import java.util.ArrayList;

//Memoria secundaria com espaco ilimitado que recebe processos suspensos
public class MemoriaHd extends Memoria{     //antigo Memoria
    private int espacoAlocado;              //total de memoria ocupada pelos processos
    private ArrayList<Fila> filas;     //lista de processos presentes na memoria
	
    public MemoriaHd(Processo utilizador, boolean disponibilidade, String nome,int espaco, int espacoAlocado, ArrayList<Fila> filas){
        super(utilizador,disponibilidade,nome,espaco,espacoAlocado);
        this.espacoAlocado = espacoAlocado;
        this.filas = filas;
    }

    public int getEspacoAlocado() {
        return espacoAlocado;
    }

    public void setEspacoAlocado(int espacoAlocado) {
        this.espacoAlocado = espacoAlocado;
    }

    public ArrayList<Fila> getFilas() {
        return filas;
    }

    public void setFilas(ArrayList<Fila> filas) {
        this.filas = filas;
    }

    
    
    
    
	
}
