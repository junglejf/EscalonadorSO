package escalonadorso;

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
    private int [] listarec = {   0,             0,         0,       0    }; //lista de inteiros com 4 elementos, um pra cada recurso. Tempo real -> 0, 0, 0, 0
    //             listarec =     [0]          [1]        [2]      [3]  <<< posições
    //                       <# impressoras>,<# scanners>,<# modems>,<# CDs>
    private String estado;          //NOVO, PRONTO, EXECUTANDO, BLOQUEADO (usuario), BLOQUEADO SUSPENSO (usuario), PRONTO SUSPENSO (usuario), FINALIZADO;
    private static int numeroDoProcesso = 0;
    private final int id;
    private String estado_anterior;

    public Processo(int tempochegada, int prioridade, int temposervico, int tamanho, int [] listarec, String estado, String estado_anterior) {
        this.tempochegada = tempochegada;
        this.prioridade = prioridade;
        this.temposervico = temposervico;
        this.tamanho = tamanho;
        this.listarec = listarec;
        this.estado = estado;
        this.estado_anterior = estado_anterior;
        this.id = numeroDoProcesso++;
    }

    public int getTempochegada() {
        return this.tempochegada;
    }

    public void setTempochegada(int tempochegada) {
        this.tempochegada = tempochegada;
    }

    public int getPrioridade() {
        return this.prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getTemposervico() {
        return this.temposervico;
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
    public int getDispositivoDaListaRec(int pos){
        //1,2 ->impressoras
        //2-> scanners
        //3 -> modems
        //4,5-> CD's
        return (this.getListarec()[pos]);
    }
    public void setDisipositivoDaListaRec(int pos, int liga_ou_desliga){
        this.getListarec()[pos] = liga_ou_desliga;
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
    
    public String getEstadoAnterior() {
        return estado_anterior;
    }

    public void setEstadoAnterior(String estado) {
        this.estado_anterior = estado;
    }
    
    public int getId(){
        return this.id;
    }
    public static int getTotalProcessosGerados(int id){
        return numeroDoProcesso;
    }
    
    public void imprimeProcesso(Processo p){
        //<arrival time>, <priority>, <processor time(Tempo de Serviço)>, <Mbytes>, <# impressoras>, 
        //<# scanners>, <# modems>, <# CDs>
        System.out.print("P"+intToString(p.getId())+" -> ");
        System.out.print("<"+p.getTempochegada()+">, ");
        System.out.print("<"+intToString(p.getPrioridade())+">, ");
        System.out.print("<"+intToString(p.getTemposervico())+">, ");
        System.out.print("<"+intToString(p.getTamanho())+">, ");
        
        for(int i =0; i<p.getListarec().length;i++){
            System.out.print("<"+intToString(p.getDispositivoDaListaRec(i))+">, ");
        }
        System.out.println();
    }
    public String intToString(int n){
        Integer ns = n;
        String s = ns.toString();
        return s;
    }
        
}





