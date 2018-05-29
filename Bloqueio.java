package escalonadorso;

import java.util.ArrayList;

int [] recdisponiveis = {2,1,1,2} // Fila de recursos disponiveis


Fila [] fbloq = Fila[6] // Fila de bloqueados

public void addRec (fbloq,recdisponiveis,p){ // ve um recurso do processo que esteja disponivel e o aloca.
	if((p.getDispositivoDaListaRec(0)>0) && (rec[0]>=p.getDispositivoDaListaRec(0))) {
		if(p.getDispositivoDaListaRec(0)==1)
			if (fbloq[0]==NULL){ 
			 	fbloq[0]=p;
				p.setDispositivoDaListaRec(0,0);
			}else if (fbloq[1]==null){
			 	fbloq[1]=p;
				p.setDispositivoDaListaRec(0,0);
			}
		if(p.getDispositivoDaListaRec(0)==2)
			if (fbloq[0]==NULL){
			 fbloq[0]=p;
				p.setDispositivoDaListaRec(0,1);
			}else if (fbloq[1]==null){
			 	fbloq[1]=p;
				p.setDispositivoDaListaRec(0,1);
			}
	if((p.getDispositivoDaListaRec(1)>0 )&&( recdisponiveis[1]>=p.getDispositivoDaListaRec(1))) {
		if (fbloq[0]==NULL){
			 	fbloq[2]=p;
				p.setDispositivoDaListaRec(1,0);
	if((p.getDispositivoDaListaRec(2)>0) && (recdisponiveis[2]>=p.getDispositivoDaListaRec(2))) {
		if (fbloq[0]==NULL){
			 	fbloq[3]=p;
				p.setDispositivoDaListaRec(2,0);
	}
	if((p.getDispositivoDaListaRec(3)>0) && (rec[3]>=p.getDispositivoDaListaRec(3))) {
		if(p.getDispositivoDaListaRec(3)==1)
			if (fbloq[4]==NULL){
			 	fbloq[4]=p;
				p.setDispositivoDaListaRec(3,0);
			}else if (fbloq[5]==null){
			 	fbloq[5]=p;
				p.setDispositivoDaListaRec(3,0);
			}
		if(p.getDispositivoDaListaRec(0)==2)
			if (fbloq[4]==NULL){
			 	fbloq[4]=p;
				p.setDispositivoDaListaRec(3,1);
				
			}else if (fbloq[5]==null){
			 	fbloq[5]=p;
				p.setDispositivoDaListaRec(3,1);

			}

}
public void exrec(fbloq, recdisponiveis,cpu){ //reduz o tempo dos processos bloqueados.
	for (int i =0;i<6,i++){
	 	fbloq[i].setTemposervico( fbloq[i].setTemposervico() - cpu.getQuantum() );

		fbloq[i].setDisipositivoDaListaRec(i fbloq[i].getDisipositivoDaListaRec(i)-cpu.getQuantum() )

		if ( fbloq[i].getDisipositivoDaListaRec(i)>=0 ){

			Processo p = fbloq[i];
		 	fbloq[i]=NULL;
			add.fdbck1(p);
			recdisponiveis[i]=recdisponiveis[i];

		}
	}

}