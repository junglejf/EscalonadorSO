package escalonador;

//objeto basico do programa, pode ser de tempo real ou de usuario
public class Processo {
    
    /*
    enum Estado{            //constantes definidas previamente
        NOVO, PRONTO, EXECUTANDO, BLOQUEADO, BLOQUEADOSUSPENSO, PRONTOSUSPENSO, FINALIZADO;
    }
    
    private Estado estado;          //tempo real nao pode ser suspenso nem bloqueado
    */
    private int tempochegada;
    private int prioridade;         //tempo real -> 0; usuario -> 1, 2 ou 3
    private int temposervico;
    private int tamanho;            //tempo real <= 512
    private int [] listarec;        //lista de inteiros com 4 elementos, um pra cada recurso. Tempo real -> 0, 0, 0, 0
    private String estado;          //NOVO, PRONTO, EXECUTANDO, BLOQUEADO (usuario), BLOQUEADO SUSPENSO (usuario), PRONTO SUSPENSO (usuario), FINALIZADO;
    

    public Processo(int tempochegada, int prioridade, int temposervico, int tamanho, int [] listarec, String estado) {
        this.tempochegada = tempochegada;
        this.prioridade = prioridade;
        this.temposervico = temposervico;
        this.tamanho = tamanho;
        this.listarec = listarec;
        this.estado = estado;
    }

    public int getTempochegada() {
        return tempochegada;
    }

    public void setTempochegada(int tempochegada) {
        this.tempochegada = tempochegada;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getTemposervico() {
        return temposervico;
    }

    public void setTemposervico(int temposervico) {
        this.temposervico = temposervico;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int[] getListarec() {
        return listarec;
    }

    public void setListarec(int[] listarec) {
        this.listarec = listarec;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
        
}





