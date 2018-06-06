package escalonadorso;

import java.util.ArrayList;

int [] recdisponiveis = {2,1,1,2} // Fila de recursos disponiveis


Fila [] fbloq = Fila[6] // Fila de bloqueados

public void addRec (Fila fbloq,int * recdisponiveis,Processo p){ // ve um recurso do processo que esteja disponivel e o aloca.
	if((p.getDispositivoDaListaRec(0)>0) && (recdisponiveis[0]>0)) { //Verifica se dispositivo usa impressoras e se ha uma disponivel
		if(p.getDispositivoDaListaRec(0)==1){//checa se o programa usa apenas uma vez a impressora
			if (fbloq[0]==NULL){ 			//se a primeira impressora nao estiver sendo usada, designamos ela.
			 	fbloq[0]=p;					//ocupamos a 1 impressora com o processo p
				p.setDispositivoDaListaRec(0,0);// dizemos agora que p nao precisa mais usar impressoras ao terminar seu tempo.
			}else if (fbloq[1]==null){ 	//caso a segunda nao esteja sendo usada, designamos ela.
			 	fbloq[1]=p;
				p.setDispositivoDaListaRec(0,0);
			}
		if(p.getDispositivoDaListaRec(0)==2) // checa se usa duas vezes a impressora
			if (fbloq[0]==NULL){			//se a primeira nao estiver em uso, designamos ela
			 	fbloq[0]=p;
				p.setDispositivoDaListaRec(0,1);
			}else if (fbloq[1]==null){		// caso contrario usamos a segunda
			 	fbloq[1]=p;
				p.setDispositivoDaListaRec(0,1);
			}
		}
	}
	if((p.getDispositivoDaListaRec(1)>0 )&&( recdisponiveis[1]>0)) {//Checamos se p usa scanner, e se o nosso esta disponivel
		if (fbloq[2]==NULL){										//estou sendo redundante, mas quero saber se o scanner esta sendo ocupado por um processo
			 	fbloq[2]=p;											//ocupamos o scanner com o processo p
				p.setDispositivoDaListaRec(1,0);					//dizemos agora que o processo ao sair de scanner, nao precisa mais voltar
			}
		}
	if((p.getDispositivoDaListaRec(2)>0) && (recdisponiveis[2]>0) {	//checamos se p usa modem, e se o nosso esta disponivel
		if (fbloq[3]==NULL){										//novamente redundante, vendo se o modem esta ocupado com algum processo
			 	fbloq[3]=p;											//ocupamos o modem com P
				p.setDispositivoDaListaRec(2,0);					//dizemos que ao processo sair do modem, nao precisa mais voltar.
		}
	}
	if((p.getDispositivoDaListaRec(3)>0) && (rec[3]>0)) {       	//checamos se P usa CD-Rom e se temos um disponivel
		if(p.getDispositivoDaListaRec(3)==1)						//se P usa 1 CD_rom
			if (fbloq[4]==NULL){									//se CDROM1 esta sem processos
			 	fbloq[4]=p;											//Ocupamos cdrom1 com o processo P
				p.setDispositivoDaListaRec(3,0);					//dizemos que ao sair, P nao precisara mais usar o cdrom
			}else if (fbloq[5]==null){								//se CDROM2 esta sem processos
			 	fbloq[5]=p;											//Ocupamos cdrom2 com o processo P
				p.setDispositivoDaListaRec(3,0);					//Dizemos que ao sair, P nao precisa mais usar o cdrom
			}
		if(p.getDispositivoDaListaRec(0)==2)						//Se P usa 2 Cd-Rom
			if (fbloq[4]==NULL){										
			 	fbloq[4]=p;
				p.setDispositivoDaListaRec(3,1);					//dizemos que ao sair, p ainda precisara usar o cdrom
				
			}else if (fbloq[5]==null){
			 	fbloq[5]=p;
				p.setDispositivoDaListaRec(3,1);  					//dizemos que ao sair, p ainda precisara usar o cdrom

			}

}
public void exrec(fbloq, recdisponiveis,cpu){ //reduz o tempo dos processos bloqueados.
	for (int i =0;i<6,i++){					//percorremos uma vez cada processo ocupando um dispositivo

	 	fbloq[i].setTemposervico( fbloq[i].getTemposervico() - cpu.getQuantum() ); //Diminuimos o tempo geral de execucao 

		fbloq[i].setDisipositivoDaListaRec(fbloq[i].getDispositivoTempoRecursos(i)-cpu.getQuantum() )//diminuimos o tempo necessario para usar o recurso

		if ( fbloq[i].getDisipositivoDaListaRec(i)<=0 ){ //se o processo nao precisa continuar usando recurso

			Processo p = fbloq[i];
		 	fbloq[i]=NULL;								//tiramos o processador do recurso
			add.fdbck1(p);								//a devolvemos a fila de feedback1
			recdisponiveis[i]=recdisponiveis[i]++;		//atualizamos a quantidade de recursos disponiveis

		}
	}

}