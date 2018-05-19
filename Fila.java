package escalonador;

import java.util.ArrayList;

//Fila generica composta por uma lista de processos e um nome
public class Fila{ 
    private ArrayList<Processo> listap;
    private String nome;
	
	
    public Fila(ArrayList<Processo> listap, String nome){
    	this.listap = listap;
	this.nome = nome;
    }

    public ArrayList<Processo> getListap() {
        return listap;
    }

    public void setListap(ArrayList<Processo> listap) {
        this.listap = listap;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}




	

