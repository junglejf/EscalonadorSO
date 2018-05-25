package escalonadorso;

import java.util.ArrayList;

//Memoria principal com tamanho limitado que recebe processos prontos, bloqueados e executando
public class MemoriaRam extends Memoria{
    private int espaco;                     //tamanho fixo de 8192
    private int espacoAlocado;              //total de memoria ocupada pelos processos
    private ArrayList<Fila> filas;     //lista de processos presentes na memoria
	
    public MemoriaRam(Processo utilizador, boolean disponibilidade, String nome,int espaco, int espacoAlocado, ArrayList<Fila> filas){
        super(utilizador,disponibilidade,nome,espaco,espacoAlocado);
	this.espaco = 8192;
        this.espacoAlocado = espacoAlocado;
        this.filas = filas;
    }

    public int getEspaco() {
        return espaco;
    }

    public void setEspaco(int espaco) {
        this.espaco = espaco;
    }

    public int getEspacoAlocado() {
        return espacoAlocado;
    }

    public void setEspacoAlocado(int espacoAlocado) {
        this.espacoAlocado = espacoAlocado;
    }

    public ArrayList<Fila> getFilas() {
        return this.filas;
    }
    

    public void setFilas(ArrayList<Fila> fila) {
        this.filas = fila;
    }
    
    
        
        
}
