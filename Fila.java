package escalonadorso;

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
    
    public void addProcesso(Processo p){
        this.listap.add(p);
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
    
    public void imprimeFila(Fila f){
        System.out.println(f.getNome());
        for(int i=0; i<f.getListap().size();i++){
            Processo p = f.getListap().get(i);
            p.imprimeProcesso(p);
        }
    }
}




	

