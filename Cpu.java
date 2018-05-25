/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escalonadorso;

/**
 *
 * @author jungl
 */
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
