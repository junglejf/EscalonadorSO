package escalonadorso;
import java.util.*;

//2 impressoras, 1 scanner, 1 modem, 2 cds
public class Recurso{
    Processo utilizador;            //objeto processo que esta usando recurso, null se nao estiver sendo usado
    private boolean disponibilidade;        //se recurso ta sendo usando ou ta livre
    private String nome;            
            
    public Recurso(){
        
    }
    public Recurso(Processo utilizador, boolean disponibilidade, String nome){
	this.utilizador = utilizador;
	this.disponibilidade = disponibilidade;
	this.nome = nome;
    }

    public Processo getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Processo utilizador) {
        this.utilizador = utilizador;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
        
        
}

