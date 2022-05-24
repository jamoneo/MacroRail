class Datos
{
int LastScreen=0;  //Indica el tipo de tarea programada. Según la última pantalla elegida 0= ninguna, 1- Timelaps, 2- Ciclo
float X,Y,Z; //Posiciones actuales




int Buffer_Inst;
boolean Run=false;
boolean Ok=false;

String State="";

float posM,posG;
float posIni,posFin;
float AvMov,AvGiro;
int Te,Td,Tg;

String Info[]=new String[50]; ;
int ind=0;

void update() 
  { int paso;
//Datos generales
   ind=0;
Info[ind++]="Pos Slider: "+posM;
Info[ind++]="Pos Giro: "+posG;
Info[ind++]="Pos Inicio: "+posIni;
Info[ind++]="Pos Fin: "+posFin;
 Info[ind++]=State;

Info[ind++]="Buffer: "+Buffer_Inst; //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva


//Segunda columna
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
Info[ind++]="";  //reserva
//Cuadro
fill(31, 31, 31);  
stroke(255);
rect(int(30*escala), int((1060)*escala), int(710*escala), int(180*escala), 7); 
line(int(380*escala), int((1060)*escala), int(380*escala), int(1240*escala));
//TExto
fill(255);



 textAlign(LEFT,LEFT);
 paso=20;
  textSize(paso*escala);
  int i;
//Datos generales
for(i=0;i<ind&&i<8;i++)
     text(Info[i],  int(35*escala), int((1080+paso*i)*escala));
//segunda columna
 paso=20;
 textSize(paso*escala);
for(;i<ind;i++)
     text(Info[i],  int(385*escala), int((1080+paso*(i-8))*escala));
                  
  }


}  
