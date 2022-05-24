class Move
{int sx=35,sy=100;  //posición 
  int T=100; // Tiempo entre refrescos de posición
boolean MovSel=false,GiroSel=false;
int indice=0;
//Slider
HScrollbarLog  Tiempo,Tdisparo,Tgrabado, Avance,AvGiro;
PadLog Mov,Giro;
TextButton Inicio,Fin,Go,Home,Cancel;;


Move()
{//Crealos botones int(320*escala)
   
  Tiempo = new HScrollbarLog(sx, sy, 250, 30,1000,"Est. (ms)",escala);
  Tdisparo = new HScrollbarLog(sx, (sy+50),250, 30,1000,"Dis. (ms)",escala);
  Tgrabado = new HScrollbarLog(sx, (sy+100), 250, 30,1000,"Grab.(ms)",escala);
  Avance = new HScrollbarLog(sx, (sy+150), 250, 30,100,"Av. (um)",escala);
  AvGiro=new HScrollbarLog(sx, (sy+200), 250, 30,360,"AVG. (º)",escala);

 
 
 Mov=new PadLog(30,730,400,0,30,"Slider",escala);
 Giro=new PadLog(30,870,400,0,30,"Giro",escala);
 
  
  Inicio= new TextButton("Inicio",sx,(sy+290),200,40,escala);   
  Fin= new TextButton("Fin",sx,(sy+350),200,40,escala);
  Go= new TextButton("Go",sx,(sy+410),200,40,escala);
  Home= new TextButton("Home",sx,sy+470,200,40,escala);
  Cancel= new TextButton("Reset",sx,(sy+530),200,40,escala);

 }  
 
  
//Refresco de pantalla standar  
void update() 
{

  pushStyle();
  
fill(31, 31, 31);  
stroke(255);
rect(int((sx-5)*escala), int((sy-20)*escala), int(710*escala), int(270*escala), 7);

  Tiempo.update();
  Avance.update();
   AvGiro.update();
  Tdisparo.update();
  Tgrabado.update();
 
  Inicio.update();
  Fin.update();
  Go.update();
  Home.update();
  Cancel.update();
  Mov.update();
  Giro.update();
 DatosScreen.update();
  popStyle();  

}

void wait(int t)
{Buffer[indice++]="G4P"+t;
  //if(DatosScreen.State.equals("Idle"))
  //     delay(t);
}


void Shoot()
{
Buffer[indice++]="M8";
 wait(Tdisparo.getPos());
 Buffer[indice++]="M9";
 // Send("M8");
 //delay(Tdisparo.getPos());
// Send("M9");
}

void SendMove(String s)
{Buffer[indice++]=s;
 // if(DatosScreen.State.equals("Idle")) Send(s);
} 


void Go()
{ int nsecuencias;  //numero de secuencias satacker
   float av;

nsecuencias=1;
indice=0;

//si se dio un angulo se calcula el numero de secuencias necesarias
if(AvGiro.getPos()>0) {
  nsecuencias= 360/AvGiro.getPos(); //Secuencias necesarias para completar un circulo
    SendMove ("G90G0Y0F180"); //se coloca al inicio la mesa de giro

}
  
  av=float(Avance.getPos())/1000;
 
 for (int i=0;i<nsecuencias;i++)
 {  //al siguiente movimiento
     SendMove ("G90G1X"+DatosScreen.posIni+"F200");
  if(DatosScreen.posFin>DatosScreen.posIni) 
    for(float p=DatosScreen.posIni+av;p<DatosScreen.posFin;p+=av) 
      {    
        wait(Tiempo.getPos());
        Shoot();
         wait(Tgrabado.getPos());
        SendMove("G90G1X"+p+"F200");
      }
     
  else
     {  SendMove("G90G1X"+DatosScreen.posIni+"F200");
      for(float p=DatosScreen.posFin;i>DatosScreen.posIni;i-=av) 
      {  wait(Tiempo.getPos());
        Shoot();
        wait(Tgrabado.getPos());
        SendMove( "G90G1X"+p+"F200");
      }
     }
       SendMove("G91G1Y"+AvGiro.getPos()+"F100");
 }
  SendMove("G90G0X0Y0F200");
 LonBuffer=indice;
 Running=true;
}
 //<>// //<>// //<>// //<>// //<>// //<>//



//Control de pulsadores e interuptores
void pressed()
{if(Mov.pressed())  {Send("$J=G21G91X"+float(Mov.vx)/10+"F500"); MovSel=true;}
if(Giro.pressed())  {Send("$J=G21G91Y"+float(Giro.vx)/10+"F500"); GiroSel=true;}
Avance.pressed();
AvGiro.pressed();
Tiempo.pressed();
Tdisparo.pressed();
Tgrabado.pressed();
}

void dragged()
{ 
if(Mov.dragged()&&MovSel)  Send("$J=G21G91X"+float(Mov.vx)/10+"F500");
if(Giro.dragged()&&GiroSel)  Send("$J=G21G91Y"+float(Giro.vx)/10+"F500");
Avance.dragged();
AvGiro.dragged();
Tiempo.dragged();
Tdisparo.dragged();
Tgrabado.dragged();
}  

//control para interruptores
void released() 
{  

if(Go.released()) Go();

if(Home.released())  Send("$H");
if(Inicio.released()) DatosScreen.posIni=DatosScreen.posM;
      
if(Fin.released()) DatosScreen.posFin=DatosScreen.posM;
if(Cancel.released()) Send("$X");

}


void accion(String info)
{
}


}
