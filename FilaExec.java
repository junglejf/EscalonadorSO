package escalonadorso;

import java.util.ArrayList;

//Fila de processos em execucao
public class FilaExec extends Fila{
    private ArrayList<Fila> filas;  //array de 2 filas de pronto(tr e usuario)
    private int qtdProcessos;               //0 a 4 processos

    public FilaExec(ArrayList<Processo> listap, String nome, ArrayList<Fila> filas, int qtdProcessos){
        super(listap, nome);
        this.filas = filas;
        this.qtdProcessos = qtdProcessos;
    }

    public ArrayList<Fila> getFilas() {
        return filas;
    }

    public void setFilas(ArrayList<Fila> filas) {
        this.filas = filas;
    }

    public int getQtdProcessos() {
        return qtdProcessos;
    }

    public void setQtdProcessos(int qtdProcessos) {
        this.qtdProcessos = qtdProcessos;
    }
	
}
