package escalonadorso;

public class Cpu extends Recurso {
    int quantum;
    
    public Cpu(Processo utilizador, boolean disponibilidade, String nome, int quantum){
        super(utilizador, disponibilidade, nome);
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
    
    
}
