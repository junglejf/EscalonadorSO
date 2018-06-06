package escalonadorso;

import java.util.ArrayList;

//Memoria principal com tamanho limitado que recebe processos prontos, bloqueados e executando
public class MemoriaRam extends Memoria{
    private int espaco;                     //tamanho fixo de 8192
    private int espacoAlocado;              //total de memoria ocupada pelos processos
    private ArrayList<Processo> fila_proc;     //lista de processos presentes na memoria
	
    public MemoriaRam(Processo utilizador, boolean disponibilidade, String nome,int espaco, int espacoAlocado, ArrayList<Processo> fila_proc){
        super(utilizador,disponibilidade,nome,espaco,espacoAlocado);
	this.espaco = 8192;
        this.espacoAlocado = espacoAlocado;
        this.fila_proc = fila_proc;
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

    public ArrayList<Processo> getFila_proc() {
        return fila_proc;
    }

    public void setFila_proc(ArrayList<Processo> fila_proc) {
        this.fila_proc = fila_proc;
    }
    
    
        
        
}
