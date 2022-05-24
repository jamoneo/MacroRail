import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import processing.opengl.*; 
import processing.serial.*; 

import co.jimezam.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SuperMacroRail_W10 extends PApplet {

/**
*Control Slider Windows v2.0
*by José Angel Moneo

Control del MacroRail desde Android por bluetouth
Permite conectarse por bluetouth y programar los tiempos, los desplazamientos
y los tiempos. 
Luego permite la ejecución.
*
*/
 
 



Serial myPort;

fileDialog file;
String Version="ScoposMacroRail ESP 2.2";

Menu MenuMain;
Move MoveScreen;
Datos DatosScreen;
Connect ConnectScreen;


boolean iniCom=false;
int ScreenSel=1;
boolean isConfiguring = true;

String received,info;  //Comando recibido
float escala;


String buffer="",pru="";
String Respuesta="";
boolean Update=false;



int lineas=0;

String[] Buffer=new String[5000]; //buffer de comandos
int pBuffer=0;  //Puntero buffer comandos
int LonBuffer=0;  //Longitud buffer comandos
boolean Running=false,ErrorFlag=false,AlarmFlag=false;  //Orden de ejecución


public void setup()
{
   //************Configuración 
 escala=0.7f;  
 frameRate(30);
//fullScreen();
//orientation(PORTRAIT);
//escala=float(width)/771; 



//Creamos Menus
 MenuMain=new Menu("Connect","Move",escala); 
  MoveScreen= new Move();
    DatosScreen= new Datos();
    ConnectScreen= new Connect();
  ScreenSel=1;
}

public void draw()
{
  MenuMain.update();  //presenta el menú
  
 switch(ScreenSel)
   { case 1:
        ConnectScreen.update();
        break;
     case 2:
         MoveScreen.update();   //Presenta pantalla control Cardan
         break;

    }   
   Run();
}

public void mouseReleased() {
int select;
  switch(ScreenSel)
 {   case 1:
     select=ConnectScreen.released();
     if(select!=-1)
             if (iniCom==false) {
             myPort = new Serial(this, Serial.list()[select], 115200); 
             if(myPort!=null) { ConnectScreen.Conectado=true;
                                iniCom=true; 
                               // println(Serial.list()[select]); 
                              }  
                 }
     break;
   case 2:
     MoveScreen.released();
     break;
 } 
 
}

public void mousePressed() {
int s;
  // contol de opciones de menus
//Para el caso de android KetaiKeyboard.toggle(this);
 s=MenuMain.pressed();
 if (s!=0) {
           ScreenSel=s;  //si se ha seleccionado una opcion de menú se cambia de pantalla
          }
 switch(ScreenSel)
 { case 1:
     ConnectScreen.pressed();  
     break;
  case 2:
     MoveScreen.pressed();
     break;
 }  

}  

 public void mouseDragged()
{ switch(ScreenSel)
 { case 1:
     ConnectScreen.dragged();
     break;
   case 2:
     MoveScreen.dragged();
     break;
 } 
  

}






//dECODIFICA LA POSICIÓN SEGÚN EL PROTOCOLO
// <Idle|WPos:69.000,0.000,0.000|Bf:15,127|FS:0,0|Pn:Z>


public void GenComand(String buf)
{ int ini,fin;


  //println(buf); 
   //Recibido Ok
  if(buf.substring(0, 2).equals("Ok")) DatosScreen.Ok=true;
  else DatosScreen.Ok=false;
 //if(buf.length()>6) if(buf.substring(0, 7).equals("ALARM:2")) ErrorFlag=true;
 //print("Subcadena :"+buf.substring(0, 1)+"\n");

  //Report
  if(buf.substring(0, 1).equals("<")&&buf.indexOf(">")>0)   //se HA RECIBIDO EL REPORT COMPLETO
    {  
       
     // Estados
     ini=1;
     fin=buf.indexOf("|"); 
      DatosScreen.State="";
      if(buf.substring(1, fin).equals("Run")) DatosScreen.State="Run";
      if(buf.substring(1, fin).equals("Idle")) DatosScreen.State="Idle"; 
      if(buf.substring(1, fin).equals("Home")) DatosScreen.State="Home"; 
       // println(buf.substring(1, fin));
           //POSICION X
                 ini=buf.indexOf("Pos:")+4;
                 fin=buf.indexOf(",");  //primer parámetro
               //   print("Subcadena x:"+buf.substring(ini+6, fin)+"\n");
                 //X
                  DatosScreen.posM=PApplet.parseFloat(buf.substring(ini,fin)); 
                  //Y
                  ini=fin+1;
                 fin=buf.substring(ini).indexOf(",")+ini; 
                 //print("Subcadena Y:"+buf.substring(ini, fin)+"\n");
                  DatosScreen.posG=PApplet.parseFloat(buf.substring(ini,fin)); 
                  //Z
                 ini=fin+1;
                 fin=buf.substring(ini).indexOf(",")+ini; 
                   //  print("Subcadena Z:"+buf.substring(ini, fin)+"\n");
                 DatosScreen.Z=PApplet.parseFloat(buf.substring(ini,fin)); 

  //Buffer
                ini=buf.indexOf("|Bf:")+4;

                 fin=buf.substring(ini).indexOf(",")+ini; 
                 //print("Subcadena Buffer_ins:"+buf.substring(ini, fin)+"\n");
                 DatosScreen.Buffer_Inst=PApplet.parseInt(buf.substring(ini,fin)); 
                 // print("Buffer_ins:"+int(buf.substring(ini, fin))+"\n");
       Update=true;
    }                 
         
 
 } 




public void Send(String dat)
{ 
//println("Send: "+dat+"\n");  //Debug
  Respuesta="";
if(iniCom) myPort.write(dat+"\n");
 
}


public void serialEvent(Serial myPort) {
  int inByte;    // Incoming serial data
  if(myPort.available()>0){
  inByte=myPort.read();
  //println(inByte);
  buffer+=PApplet.parseChar(inByte);
  if(inByte == '\n') {Respuesta=buffer; //print("recibido "+Respuesta);
                     buffer="";
                      GenComand(Respuesta);
                     }
  }                   

} 




public void Run()
{  if(Running&&Update)
       { if(pBuffer<LonBuffer) 
           {//  if(DatosScreen.Buffer_Inst==15) //envío cuanod hay espacio en el buffer de recepción
            if(DatosScreen.State.equals("Idle"))
                { //print("Send: "+Buffer[pBuffer]+"\n");
                
                 if(Buffer[pBuffer].substring(0, 2).equals("G4")) {//print("delay: "+int(Buffer[pBuffer].substring(3))+"\n");
                                                                    delay(PApplet.parseInt(Buffer[pBuffer++].substring(3)));}
                 
                 else Send(Buffer[pBuffer++]);
                 
                 }
           }
          else  {Running=false; pBuffer=0; LonBuffer=0;} // finaliza la ejecución del buffer
             
         Update=false;
       }
    if(DatosScreen.State.equals("Home")) Send("G10L2P1X-68.5");
   //Envía orden de refresco de datos cada 200 ms
     if(frameCount%10==0) Send("?"); //Solicita actualizar los datos cada 50*5=200ms
}
class Button
{
  int x, y;
  int w, h;

  boolean over = false;
  boolean pressed = false;  
  boolean on;
  
//Flanco entrada pulsado  
public boolean pressed() {
    if(overRect(x,y,w,h) && mousePressed&&!pressed) 
       { pressed=true;  //indica que se está presionando
         return true;}
    return false;
  }
  
//devuelve true si se ha soltado encima del botón
//para realizar la alción al soltar
public boolean released() {
   if(overRect(x,y,w,h) && !mousePressed&&pressed) {
      pressed=false;
      return true;   }
     return false;
  
  }
//Se está pulsando el botón  
public boolean press(){
    if(overRect(x,y,w,h) && mousePressed) {
      pressed=true;
      return true;   }
    else { pressed=false;  
     return false; 
    }
} 

public boolean overRect(int x, int y, int width, int height) {
  if (mouseX >= x && mouseX <= x+width && 
      mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  }
}
}
                                                                                              
class Connect
{
String[] puerto = Serial.list();
int select=-1;
//Pantalla de control de Luces
fileDialog file;
Pad Mov;
PrintWriter output;
boolean MovSel=false;
String fileName="";    
boolean Conectado=false;  

TextButton bActivar,bReset,bReconfig;
PImage conectadoblue,Error,Alarm;
char ctrlx=0x18;



Connect()
{//Crealos botones

  conectadoblue= loadImage("preferences_system_bluetooth.png");
  
    bReset= new TextButton("Reset",30,950,190,40,escala);    

     Error= loadImage("exclamation.png");  
        Alarm= loadImage("warning.png"); 
}  
//Refresco de pantalla
public void update()
{ DatosScreen.LastScreen=0; //indica trabajo Ninguno
  pushStyle();

fill(31, 31, 31);  
rect(PApplet.parseInt(50*escala),PApplet.parseInt(280*escala), PApplet.parseInt(450*escala),PApplet.parseInt(500*escala), 7);


fill(255);

if(Conectado) {image(conectadoblue,PApplet.parseInt(250*escala), PApplet.parseInt(850*escala));
               }
    if(ErrorFlag)  image(Error,PApplet.parseInt(400*escala), PApplet.parseInt(850*escala));  
   if(AlarmFlag)  image(Alarm,PApplet.parseInt(550*escala), PApplet.parseInt(850*escala)); 
getBluetoothInformation();
  text(Version, PApplet.parseInt(50*escala),PApplet.parseInt(100*escala));    
  text("(c) Jose Angel Moneo Fdz.", PApplet.parseInt(50*escala),PApplet.parseInt(1200*escala));                                   

    bReset.update();

  popStyle();  

}


//Control cciones al pulsar
public void pressed()
{ select=-1;
  if (mouseX >= PApplet.parseInt(50*escala) && mouseX <= PApplet.parseInt(500*escala) && 
      mouseY >=PApplet.parseInt(305*escala) && mouseY <= PApplet.parseInt(500*escala)) {
        select=(mouseY-PApplet.parseInt(305*escala))/PApplet.parseInt(40*escala);
        if(select>=Serial.list().length) select=-1;
      }   
      
//  println(select);   
      
  //return select;



}


//control de acciones al soltar
public int released() {

    if(bReset.released()) {if(iniCom) { //myPort.write(ctrlx);
                                       Send("$X");   //  desbloqueo
                                       Send("$10=3");
                                        Send("$H"); //Realiza home
                                       ErrorFlag=false;}
                                      }   

  return select;                        
                              
 }
 

public void dragged()
{

}
 
 
public void getBluetoothInformation()
{  int i;
  textSize(30*escala);
 text( "List Com:\n",PApplet.parseInt(70*escala), PApplet.parseInt(310*escala));  
  for (i=0; i<Serial.list().length; i++)
     { if (i==select) fill(153, 204, 255);
       else fill(255,255,255);
       text( "["+i+"] "+puerto[i],PApplet.parseInt(70*escala),PApplet.parseInt(345*escala)+PApplet.parseInt(40*escala)*i);
     }   

}



} 
class Contrave
{
int valor;
int px,py;  
  
Contrave(int x1,int y1)
{px=x1;
py=y1;
valor=0;
}

public void update()
{ pushStyle();
flechas(px,py, true);
digitos(px,py+35);
flechas(px,py+90, false);

  popStyle(); 
  
  
}  

public float GetVal()
{
 return PApplet.parseFloat(valor); 
}  
//cajas para numeros
public void digitos(int x,int y)
{
  stroke(255);
  fill(59,59,59); 
for(int a=0;a<4;a++)
   rect(x+a*43,y,40,50, 7);
    
fill(255);
textSize(30);
for(int a=0;a<4;a++)
   text(PApplet.parseInt((valor%pow(10,4-a))/pow(10,3-a)) ,x+10+a*43,y+30);    
}
//cajas para numeros
public void flechas(int x,int y, boolean sentido)
{ stroke(255);
  fill(59,59,59); 
for(int a=0;a<4;a++)
   rect(x+a*43,y,40,30, 7);
    
fill(255);
for(int a=0;a<4;a++)
    if(sentido) triangle(x+10+a*43,y+25,x+20+a*43,y+5,x+30+a*43,y+25);
    else  triangle(x+10+a*43,y+5,x+20+a*43,y+25,x+30+a*43,y+5);
}

public void pressed()
{for(int a=0;a<4;a++)
    {if(over(px+a*43,py,40,30)) valor+=pow(10,3-a);
     if(over(px+a*43,py+90,40,30)) valor-=pow(10,3-a);
    }
  valor%=10000;  
}

public boolean over(int x,int y,int w,int h)
{
    if (mouseX > x && mouseX < x+w &&
       mouseY > y && mouseY < y+h) 
      return true;
    else
      return false;
    
  }
}
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

public void update() 
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
rect(PApplet.parseInt(30*escala), PApplet.parseInt((1060)*escala), PApplet.parseInt(710*escala), PApplet.parseInt(180*escala), 7); 
line(PApplet.parseInt(380*escala), PApplet.parseInt((1060)*escala), PApplet.parseInt(380*escala), PApplet.parseInt(1240*escala));
//TExto
fill(255);



 textAlign(LEFT,LEFT);
 paso=20;
  textSize(paso*escala);
  int i;
//Datos generales
for(i=0;i<ind&&i<8;i++)
     text(Info[i],  PApplet.parseInt(35*escala), PApplet.parseInt((1080+paso*i)*escala));
//segunda columna
 paso=20;
 textSize(paso*escala);
for(;i<ind;i++)
     text(Info[i],  PApplet.parseInt(385*escala), PApplet.parseInt((1080+paso*(i-8))*escala));
                  
  }


}  
class HScrollbar 
{
 int x,y,w,h; // coordenadas de las esquinas
 int xt1,xt2; //posicion textos
int vx,oldvx;  //Valores del pad en x e y
int limit;  //escala de punto máximo
boolean pressed;
String Text1,Text2; 
float Escala;
boolean pulsado;

HScrollbar (int x1,int y1, int w1,int h1,int limite,String text,float escala)
{ Escala=escala;
xt1=PApplet.parseInt(x1*escala); //Posicion texto
 x=PApplet.parseInt((x1+350)*escala);//Posicion barra
 xt2=x+PApplet.parseInt(w1*escala)+PApplet.parseInt(15*escala); // Posicion valor
 y=PApplet.parseInt(y1*escala);
 w=PApplet.parseInt(w1*escala);
 h=PApplet.parseInt(h1*escala);
 limit=limite;
 Text1=text;
 pulsado=false;
 vx=oldvx=0;
}  


public void update() 
  {
    display();
  }  

//devuele si se pulsó para poder tomar el valor que se desee  
public boolean pressed() {
    if(over() && mousePressed) 
       { vx=(mouseX-x); 
         if (vx<0) vx=0;
          pulsado=true;  return true;}
    return false;   
  }

public boolean dragged()
{ boolean f;
  //    pressed();
    if(over()) vx=(mouseX-x); 
    if (vx<0) vx=0;
    if(vx!=oldvx) {oldvx=vx; f=true;}
    else f=false;
    return f;
                                
}  
 
public boolean released(){
   if(!mousePressed) return true;
   else return false;     
} 
 
 public boolean over() {
  if (mouseX >= x-5 && mouseX <= x+w+5 && 
      mouseY >= y-5 && mouseY <= y+h+5) 
     return true;
  else 
     return false;
  } 

  public void display() {
    noStroke();
    fill(153, 204, 255);
    rect(x, y, w, h,7);
    if(over() || pulsado) {
      fill(255, 0, 0);
    } else {
      fill(0, 0, 255);
    }
    rect(vx+x-h/2, y, h, h,8);
       textAlign(LEFT);
  textSize(PApplet.parseInt(35*Escala));

  fill(0, 102, 153); 
   textAlign (LEFT);
  text( Text1, xt1, y+h);   

  
  text( PApplet.parseInt(vx*limit/w), xt2, y+h);  
  }
 public int  getPos() {
    return PApplet.parseInt(vx*limit/w);
  }
  
  public void setPos(int pos) {
    vx= pos;
  }
}
class HScrollbarLog
{
 int x,y,w,h; // coordenadas de las esquinas
 int xt1,xt2; //posicion textos
int vx,oldvx;  //Valores del pad en x e y
int limit;  //escala de punto máximo
boolean pressed;
String Text1,Text2; 
float Escala;
boolean pulsado;

HScrollbarLog (int x1,int y1, int w1,int h1,int limite,String text,float escala)
{ Escala=escala;
xt1=PApplet.parseInt(x1*escala); //Posicion texto
 x=PApplet.parseInt((x1+350)*escala);//Posicion barra
 xt2=x+PApplet.parseInt(w1*escala)+PApplet.parseInt(15*escala); // Posicion valor
 y=PApplet.parseInt(y1*escala);
 w=PApplet.parseInt(w1*escala);
 h=PApplet.parseInt(h1*escala);
 limit=limite;
 Text1=text;
 pulsado=false;
 vx=oldvx=0;
}  


public void update() 
  {
    display();
  }  

//devuele si se pulsó para poder tomar el valor que se desee  
public boolean pressed() {
    if(over() && mousePressed) 
       { vx=(mouseX-x); 
         if (vx<0) vx=0;
          pulsado=true;  return true;}
    return false;   
  }

public boolean dragged()
{ boolean f;
  //    pressed();
    if(over()) vx=(mouseX-x); 
    if (vx<0) vx=0;
    if(vx!=oldvx) {oldvx=vx; f=true;}
    else f=false;
    return f;
                                
}  
 
public boolean released(){
    if(!mousePressed)   return true;
    return false;     
} 
 
 public boolean over() {
  if (mouseX >= x-PApplet.parseInt(5*Escala) && mouseX <= x+w+PApplet.parseInt(5*Escala) && 
      mouseY >= y-PApplet.parseInt(5*Escala) && mouseY <= y+h+PApplet.parseInt(5*Escala)) 
     return true;
  else 
     return false;
  } 

  public void display() {
 
    noStroke();
    fill(153, 204, 255);
    rect(x, y, w, h,7);
    if(over() || pulsado) {
      fill(255, 0, 0);
    } else {
      fill(0, 0, 255);
    }
    rect(vx+x-h/2, y, h, h,8);

    textSize(PApplet.parseInt(35*Escala));
  fill(0, 102, 153); 
  textAlign (LEFT);
  
  text( Text1, xt1, y+h); 
  text( PApplet.parseInt(pow(vx*limit/w,2)/limit), xt2, y+h);  
  }
  
 public int  getPos() {
    return PApplet.parseInt(pow(vx*limit/w,2)/limit);
  }
 
 public void setPos(int pos) {
  vx=oldvx=PApplet.parseInt( sqrt(pos*limit)*w/limit);
  }
}
class ImageButton extends Button 
{
  PImage base;
  PImage roll;
  PImage down;
  PImage currentimage;
  float escala;
  //metodo simple de llamada. Tamaño automático
  ImageButton(int ix, int iy,String fbase, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 roll=base;
 down = loadImage(fdown); 
 
    w = PApplet.parseInt(base.width*esc);
    h = PApplet.parseInt(base.height*esc);
    x =PApplet.parseInt(ix*esc)- w/2;
    y = PApplet.parseInt(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }
    //metodo escalado con tamaño definido
  ImageButton(int ix, int iy,int wi, int he,String fbase, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 roll=base;
 down = loadImage(fdown); 
 
    w = PApplet.parseInt(wi*esc);
    h = PApplet.parseInt(he*esc);
    x =PApplet.parseInt(ix*esc)- w/2;
    y = PApplet.parseInt(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }
  
    //metodo completo de llamada
  ImageButton(int ix, int iy,String fbase,String froll, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 roll=loadImage(froll);
 down = loadImage(fdown); 
 
    w = PApplet.parseInt(base.width*esc);
    h = PApplet.parseInt(base.height*esc);
    x = PApplet.parseInt(ix*esc)- w/2;
    y = PApplet.parseInt(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }

public void update() 
  {
    over();
    pressed();
    if(pressed) {
      currentimage = down;
    } else if (over){
      currentimage = roll;
    } else {
      currentimage = base;
    }
    display();
  }
  
  public void over() 
  {
    if( overRect(x, y, w, h) ) {
      over = true;
    } else {
      over = false;
    }
  }
  
  public void display() 
  {
    image(currentimage, x, y,w,h);
  }
}
class ImageInterrupt extends Interrupt
{
  PImage base;
  PImage down;
  PImage currentimage;
  
  //metodo simple de llamada
  ImageInterrupt(int ix, int iy,String fbase, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 down = loadImage(fdown); 
 
    w = PApplet.parseInt(base.width*esc);
    h = PApplet.parseInt(base.height*esc);
    x =PApplet.parseInt(ix*esc)- w/2;
    y = PApplet.parseInt(iy*esc)- h/2; 
   currentimage = base;
  }
  
    //metodo completo de llamada
 

public void update() 
  {
    over();
    pressed();
    if(on) 
      currentimage = down;
    else
      currentimage = base;


    display();

  }
  
  public void over() 
  {
    if( overRect(x, y, w, h) ) {
      over = true;
    } else {
      over = false;
    }
  }
  
  public void display() 
  {
    image(currentimage, x, y);
  }
}
class Interrupt
{
  int x, y;
  int w, h;
    boolean over = false;
  boolean pressed = false; 
  boolean on=false; //indica que está en on
  
  

  
public void pressed() {
    if(over && mousePressed) 
       pressed=true;  //indica que se ha presionado
  }
  
  
  
public boolean released() {
      
    if( over) 
       { if(on) on=false;
         else on=true;
         pressed=false;
      //  println(on);
       return true; 
       }
    else return false;

  }
  
  
  
public boolean overRect(int x, int y, int width, int height) {
  if (mouseX >= x && mouseX <= x+width && 
      mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  } 

}
}
class Menu
{int Sel=0,OldSel=0;  //menu seleccionado
 int nMen=0;
 String[] Text;
 float escala;
 
 
Menu(String t0,String t1,String t2,String t3,String t4,String t5,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
      Text[4]=t4;
       Text[5]=t5;
       escala=esc;
   nMen=6;    
} 
Menu(String t0,String t1,String t2,String t3,String t4,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
      Text[4]=t4;
       escala=esc;
   nMen=5;    
}

Menu(String t0,String t1,String t2,String t3,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
       escala=esc;
   nMen=4;    
} 


Menu(String t0,String t1,String t2,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
           escala=esc;
   nMen=3;    
}  
 
Menu(String t0,String t1,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
          escala=esc;
   nMen=2;    
}  
 
public int pressed()
{ int sel=0;

//Gestión de menú
// si no se ha pulsado el ratón o no está nezona de menú se sale
  if(!mousePressed||mouseY>PApplet.parseInt(50*escala)) return 0;
  
Sel=mouseX/(width/nMen);

//Devuelve el menú en el que está el ratón indicando el primer menú como 1.
 return (Sel+1);
 
}


public void update()
{ //presenta el menú
  pushStyle();
    background(159, 159,159);
    stroke(255);
    textSize(PApplet.parseInt(24*escala));
  for(int i=0;i<nMen;i++)
   {     if (i==Sel)
                    { noStroke();
                      fill(159, 159,159);  }
          else   { fill(0);
                    stroke(255);}
      rect(i*width/nMen, 0, width/nMen, PApplet.parseInt(50*escala));
        fill(255);
       text(Text[i],i*width/nMen+5, PApplet.parseInt(30*escala)); 
   }
  popStyle();  
  
}


}
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
public void update() 
{

  pushStyle();
  
fill(31, 31, 31);  
stroke(255);
rect(PApplet.parseInt((sx-5)*escala), PApplet.parseInt((sy-20)*escala), PApplet.parseInt(710*escala), PApplet.parseInt(270*escala), 7);

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

public void wait(int t)
{Buffer[indice++]="G4P"+t;
  //if(DatosScreen.State.equals("Idle"))
  //     delay(t);
}


public void Shoot()
{
Buffer[indice++]="M8";
 wait(Tdisparo.getPos());
 Buffer[indice++]="M9";
 // Send("M8");
 //delay(Tdisparo.getPos());
// Send("M9");
}

public void SendMove(String s)
{Buffer[indice++]=s;
 // if(DatosScreen.State.equals("Idle")) Send(s);
} 


public void Go()
{ int nsecuencias;  //numero de secuencias satacker
   float av;

nsecuencias=1;
indice=0;

//si se dio un angulo se calcula el numero de secuencias necesarias
if(AvGiro.getPos()>0) {
  nsecuencias= 360/AvGiro.getPos(); //Secuencias necesarias para completar un circulo
    SendMove ("G90G0Y0F180"); //se coloca al inicio la mesa de giro

}
  
  av=PApplet.parseFloat(Avance.getPos())/1000;
 
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
public void pressed()
{if(Mov.pressed())  {Send("$J=G21G91X"+PApplet.parseFloat(Mov.vx)/10+"F500"); MovSel=true;}
if(Giro.pressed())  {Send("$J=G21G91Y"+PApplet.parseFloat(Giro.vx)/10+"F500"); GiroSel=true;}
Avance.pressed();
AvGiro.pressed();
Tiempo.pressed();
Tdisparo.pressed();
Tgrabado.pressed();
}

public void dragged()
{ 
if(Mov.dragged()&&MovSel)  Send("$J=G21G91X"+PApplet.parseFloat(Mov.vx)/10+"F500");
if(Giro.dragged()&&GiroSel)  Send("$J=G21G91Y"+PApplet.parseFloat(Giro.vx)/10+"F500");
Avance.dragged();
AvGiro.dragged();
Tiempo.dragged();
Tdisparo.dragged();
Tgrabado.dragged();
}  

//control para interruptores
public void released() 
{  

if(Go.released()) Go();

if(Home.released())  Send("$H");
if(Inicio.released()) DatosScreen.posIni=DatosScreen.posM;
      
if(Fin.released()) DatosScreen.posFin=DatosScreen.posM;
if(Cancel.released()) Send("$X");

}


public void accion(String info)
{
}


}
class Pad
{
int x,y,w,h; // coordenadas de las esquinas
int xmed,ymed;  //punto central
int vx,vy,oldvx,oldvy;  //Valores del pad en x e y
int limit;  //escala de punto máximo
float escala;
boolean pressed;
String Text;

Pad(int x1,int y1, int w1,int h1,int limite,float es)
{x=PApplet.parseInt(x1*es);
 y=PApplet.parseInt(y1*es);
 w=PApplet.parseInt(w1*es);
 h=PApplet.parseInt(h1*es);
 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es; 
 Text="PAD";
}  

Pad(int x1,int y1, int w1,int h1,int limite,String text,float es)
{x=PApplet.parseInt(x1*es);
 y=PApplet.parseInt(y1*es);
 w=PApplet.parseInt(w1*es);
 h=PApplet.parseInt(h1*es);
 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es;  
 Text=text;
}  
 
public void SetLimits(int lim)
{limit=lim;
}


public void update() 
  {

    display();

  }  
  
//devuele si se pulsó para poder tomar el valor que se desee  
public boolean pressed() {
    if(over() && mousePressed) 
       { vx=PApplet.parseInt(mouseX-xmed)*2*limit/w; 
       if(mouseX<xmed) vx=-vx;
     vy=PApplet.parseInt(ymed-mouseY)*2*limit/h; 
     if(mouseY>ymed) vy=-vy; 
   return true;}
    return false;   
  }
  
public boolean dragged()
{ boolean f;
    pressed();
    if(vx!=oldvx || vy!=oldvy) {oldvx=vx; oldvy=vy;
                                f=true;}
    else f=false;
    return f;
                                
}  
 
 public boolean over() {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) 
     return true;
  else 
     return false;
  }  
  
public boolean released(){
   if(!mousePressed)
        {vx=0;
         vy=0;
         return true;}
    return false;     
}

public void display() 
  {
    pushStyle();
      fill(0, 102, 153);
         textAlign(LEFT);
   textSize(35*escala);
   text(Text,x+5,y-PApplet.parseInt(5*escala)); 
stroke(255, 255, 255);  
noFill();
rect(x-1, y-1, w+2, h+2);  
stroke(156, 156, 157);
fill(31, 31, 31);  
rect(x, y, w, h);
  popStyle();  
  } 
  

  
  
  
}
class PadLog
{
int x,y,w,h; // coordenadas de las esquinas
int xmed,ymed;  //punto central
int vx,vy,oldvx,oldvy;  //Valores del pad en x e y
int limit;  //escala de punto máximo
float escala;
boolean pressed;
String Text;
int tipo=0;

PadLog(int x1,int y1, int w1,int h1,int limite,float es)
{tipo=3;  //dos ejes
if(w1==0){w=PApplet.parseInt(70*es); tipo=1;} //Se usa solo un lado //Y
else  w=PApplet.parseInt(w1*es);
 if(h1==0){ h=PApplet.parseInt(70*es); tipo=2;}  //X
 else  h=PApplet.parseInt(h1*es);
 x=PApplet.parseInt(x1*es);
 y=PApplet.parseInt(y1*es);

 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es; 
 Text="PAD";
}  

PadLog(int x1,int y1, int w1,int h1,int limite,String text,float es)
{tipo=3;  //dos ejes
if(w1==0){w=PApplet.parseInt(70*es); tipo=1;} //Se usa solo un lado //Y
else  w=PApplet.parseInt(w1*es);
 if(h1==0){ h=PApplet.parseInt(70*es); tipo=2;}  //X
 else  h=PApplet.parseInt(h1*es);
  
  x=PApplet.parseInt(x1*es);
 y=PApplet.parseInt(y1*es);
 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es;  
 Text=text;
}  
 
public void SetLimits(int lim)
{limit=lim;
}


public void update() 
  {

    display();

  }  
  
//devuele si se pulsó para poder tomar el valor que se desee  
public boolean pressed() {
    if(over() && mousePressed) 
       { //vx=(mouseX-xmed)*2*escala/w; 
         vx=PApplet.parseInt(pow((mouseX-xmed)*2*limit/w,2)*limit/pow(limit,2));
         if(mouseX<xmed) vx=-vx;
         
        // vy=(ymed-mouseY)*2*escala/h;
          vy=PApplet.parseInt(pow((ymed-mouseY)*2*limit/w,2)*limit/pow(limit,2));
         if(mouseY>ymed) vy=-vy; 
         return true;}
    return false;   
  }
    
  
public boolean dragged()
{ boolean f;
    pressed();
    if(vx!=oldvx || vy!=oldvy) {oldvx=vx; oldvy=vy;
                                f=true;}
    else f=false;
    return f;
                                
}  
 
 public boolean over() {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) 
     return true;
  else 
     return false;
  }  
  
public boolean released(){
   if(!mousePressed)
        {vx=0;
         vy=0;
         return true;}
    return false;     
}

public void display() 
  {
    pushStyle();
      fill(0, 102, 153);
   textSize(35*escala);
   textAlign(LEFT);
   text(Text,x+5,y-PApplet.parseInt(5*escala)); 
stroke(255, 255, 255);  
noFill();
rect(x-1, y-1, w+2, h+2);  
stroke(156, 156, 157);
fill(31, 31, 31);  
rect(x, y, w, h);

stroke(126);

if(tipo==3||tipo==1)line(x,y+h/2,x+w,y+h/2);
if(tipo==3||tipo==2)line(x+w/2,y,x+w/2,y+h);
  popStyle();  
  } 
  

  
  
  
}
class PathsTimeLaps
{  boolean show;
 private
  int Px=150,Py=400;


ImageButton SliderR,SliderL,TiltU,TiltD,PanR,PanL;  
float XIni=0,YIni=0,ZIni=0,FIni=0;  //Posición inicial
float XFin=0,YFin=0,ZFin=0,FFin=0;  //Posición final
TextButton Exit;

PathsTimeLaps()
  {
   show=false;
 SliderR=new ImageButton(Px+125,Py+50,"Slider.png","Slider1.png",escala);

 SliderL=new ImageButton(Px+125+275,Py+50,"Slider.png","Slider1.png",escala);   
  
 TiltU=new ImageButton(Px+125,Py+50+100,"Tilt.png","Tilt1.png",escala);
 TiltD=new ImageButton(Px+125+275,Py+50+100,"Tilt.png","Tilt1.png",escala);     
    
 PanR=new ImageButton(Px+125,Py+50+200,"Pan.png","Pan1.png",escala);
 PanL=new ImageButton(Px+125+275,Py+50+200,"Pan.png","Pan1.png",escala);  
 Exit=new TextButton("Exit",Px+125+40,Py+50+265,200,40,escala); 

  }
  
//Refresco de pantalla standar  
public void update() 
{if(!show) return;
  pushStyle();
fill(31, 31, 31);  
stroke(255);
rect(PApplet.parseInt((Px-7)*escala), PApplet.parseInt((Py-7)*escala), PApplet.parseInt(540*escala), PApplet.parseInt(380*escala), 7);

FlechaR(Px,Py);
FlechaL(Px+265,Py);
FlechaU(Px,Py+100);
FlechaD(Px+265,Py+100);
FlechaR(Px,Py+200);
FlechaL(Px+265,Py+200);
//Imagene

  SliderR.update();
  SliderL.update();
  PanR.update();
  PanL.update();
  TiltU.update();
  TiltD.update();
   Exit.update();
 popStyle(); 
}

//Control de pulsadores e interuptores
public void pressed()
{ 


 
 if(SliderR.pressed()) {XIni=-590;  XFin=590;  }
 if(SliderL.pressed()) {XIni=590;  XFin=-590;  }
 
 if(TiltU.pressed()) {ZIni=-45;  ZFin=45;  }
 if(TiltD.pressed()) {ZIni=45;  ZFin=-45;  }
 
 if(PanR.pressed()) {YIni=-45;  YFin=45;  }
 if(PanL.pressed()) {YIni=45;  YFin=-45;  }
 
}
//control para interruptores
public void released() 
{  if(Exit.released()) Hide();
SliderR.released();
SliderL.released();
TiltU.released();
TiltD.released();
PanR.released();
PanL.released();
}

public void dragged()
{ 
}

public void FlechaR(int x,int y)
{
 pushStyle();
  //Cuadros fondo
fill(31, 100, 31);  
stroke(255);
rect(x*escala, y*escala, 258*escala, 98*escala);
  strokeWeight (10); 
line((x+70)*escala,(y+20)*escala,(x+200)*escala,(y+20)*escala);
line((x+190)*escala,(y+10)*escala,(x+200)*escala,(y+20)*escala);
line((x+190)*escala,(y+30)*escala,(x+200)*escala,(y+20)*escala);
 popStyle(); 
} 

public void FlechaL(int x,int y)
{pushStyle();
  //Cuadros fondo
fill(31, 100, 31);  
stroke(255);
rect(x*escala, y*escala, 258*escala, 98*escala);
  strokeWeight (10); 
line((x+70)*escala,(y+20)*escala,(x+200)*escala,(y+20)*escala);
line((x+70)*escala,(y+10)*escala,(x+60)*escala,(y+20)*escala);
line((x+70)*escala,(y+30)*escala,(x+60)*escala,(y+20)*escala);
 popStyle(); 
} 

public void FlechaU(int x,int y)
{pushStyle();
  //Cuadros fondo
fill(31, 100, 31);  
stroke(255);
rect(x*escala, y*escala, 258*escala, 98*escala);
  strokeWeight (10); 
line((x+20)*escala,(y+10)*escala,(x+20)*escala,(y+90)*escala);
line((x+10)*escala,(y+20)*escala,(x+20)*escala,(y+10)*escala);
line((x+20)*escala,(y+10)*escala,(x+30)*escala,(y+20)*escala);
 popStyle(); 
} 
public void FlechaD(int x,int y)
{pushStyle();
  //Cuadros fondo
fill(31, 100, 31);  
stroke(255);
rect(x*escala, y*escala, 258*escala, 98*escala);
  strokeWeight (10); 
line((x+20)*escala,(y+10)*escala,(x+20)*escala,(y+90)*escala);
line((x+10)*escala,(y+80)*escala,(x+20)*escala,(y+90)*escala);
line((x+20)*escala,(y+90)*escala,(x+30)*escala,(y+80)*escala);
 popStyle(); 
} 
public void Show()
{ show=true;
}

public void Hide()
{show=false;
}
}
class Pilot
{ int x, y;
  int w, h;
  PImage on;
  PImage off;
  PImage currentimage;
  boolean state;
  float escala;

  
  //metodo simple de llamada
  Pilot(int ix, int iy,String fon, String foff,float es) 
  { 
on = loadImage(fon);
 off = loadImage(foff); 
 
    w = on.width;
    h = on.height;
    x = ix- w/2;
    y = iy- h/2; 
   currentimage = on;
   state=false;
   escala=es;
  }
  

  
//enciende el piloto  
public void on()
{state=true;
}

//Apaga el piloto
public void off()
{state=false;
}

//la actualizacion mediante comando on
public void update() 
{    if(state) 
      currentimage = on;
     else
      currentimage = off;
    display();
  }
  

  
  public void display() 
  {
    image(currentimage, x*escala, y*escala);
  }
}

class ProgressBar
{int px,py;
int ancho,alto;
 int min, max;
 int valor;
 
 
 
 
ProgressBar(int x,int y,int w,int h)
{px=x;
py=y;
alto=h;
ancho=w;
min=0;
max=1;
valor=0;
}


public void update()
{ pushStyle();
fill(91, 91, 91);  
stroke(255);
rect(px, py, ancho, alto, 7);  //caja

//textos de valores extremos
 fill(255);    
 text( min, px,py-10);      
 text( max, px+ancho-30,py-10); 
 text(valor, px+ancho/2,py-10); 
 //barra del progeso
fill(11, 155,12);  
noStroke();
if(max>0 && valor<=max)
  rect(px+1, py+1, valor*(ancho-2)/max,alto-2, 7);  //caja
stroke(255);  
 fill(229,229,229);  
for(int i=(px+ancho/10); i<px+ancho;i+=ancho/10)  //10 barras
   line(i,py+2,i,py+alto-2); 
   popStyle(); 
}  
  
public void SetMin(int m)
{
  min=m;
}  
public void SetMax(int m)
{
 max=m;

}  

public void SetVal(int v)
{valor=v;
}
  
}  


class Registro {
 float X,Y,Z,F,V;

 int len=5;
 

  public Registro(String[] pieces) {
    X = PApplet.parseFloat(pieces[0]);
    Y= PApplet.parseFloat(pieces[1]);
    Z = PApplet.parseInt(pieces[2]);
    F = PApplet.parseFloat(pieces[3]);
    V = PApplet.parseFloat(pieces[4]);
  
  }
 public String  Get()
 { String salida;
      salida=X+ "\t"+Y+ "\t"+Z+ "\t"+F+ "\t"+V;
   return (salida);
 }
}
// permite seleccionar entre una lista de valores mediante una ventana de scroll

class Selector
{String[] Valor;
String Text;
int x,y,w,h,xmed,ymed;
float escala;
int max=0,indice=0;
boolean press=false;
int oldx,oldvy=0,vx,vy;   //Control darged

Selector(int ix,int iy,int iw,int ih,String iText,float esc)
{ Text=iText;
    escala=esc;
    w = PApplet.parseInt(iw*escala);
    h = PApplet.parseInt(ih*escala);
    x = PApplet.parseInt(ix*escala);
    y = PApplet.parseInt(iy*escala); 
     xmed=x+w/2;
     ymed=y+h/2;
    max=0;indice=0;
    Valor= new String[50];
    
}

//devuele si se pulsó para poder tomar el valor que se desee  
public boolean pressed() {
     if(over() && mousePressed) 
       { press=true;
        vx= mouseX; vy=mouseY;  //almacena el punto donde se incia el toque      
    return true;}
    return false;   
  }
  
public void dragged()
{ 
    if(over() && mousePressed) 
    {
     //testea el incremento. Si es una linea,cambia el indice mostrado
    if( vy>oldvy+PApplet.parseInt(20*escala)) {oldvy=vy;
                                   Rt();}
      if( vy<oldvy-PApplet.parseInt(20*escala)) {oldvy=vy;
                                   Av();};
    }                                
                               
}  
 
 public boolean over() {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) 
     return true;
  else 
     return false;
  }  
  
public boolean released(){
   if(over() && !mousePressed&&press)
        {
      if(mouseY>ymed)  Av();
      else  Rt();
         press=false;
         return true;}
    return false;     
}


//añade un texto
public void Add(String Texto)
{ 
  Valor[max]=Texto;
  max++;
}  

//elimina un texto
public void Sub(int ind)
{for(int i=ind;i<max;i++)
    Valor[i]=Valor[i+1];
 max--;   
}

  
public void Av()  
{ if (indice<max-1)indice++;
}
  
public void Rt()
{if(indice>0) indice--;
} 

//Devuelve el valor del indice seleccionado
public int Get()
{return indice;
}

public void update()
{pressed();
//released();
  pushStyle();
   fill(0, 102, 153);
    textAlign(LEFT);
   textSize(35*escala);
   text(Text,x+5,y-PApplet.parseInt(5*escala)); 
stroke(255, 255, 255);  
noFill();
rect(x-1, y-1, w+2, h+2);  
stroke(156, 156, 157);
fill(31, 31, 31);  
rect(x, y, w, h);
  noStroke ();
fill(204, 102, 0);
stroke(204, 102, 0);
textAlign(LEFT);
textSize(30*escala);

//coloca 5 valores en la ventana, centrando el indice en el medio 
for(int i=0;i<5;i++)  
   { 
    if (indice <3) 
    { 
        if(i==indice) fill(0, 102, 153);
        else fill(204, 102, 0);
         text(Valor[i],x+5,y+PApplet.parseInt(30*escala)*(i+1));

      }
    else if (indice >max-3) 
    { 
        if(i==5-(max-indice)) fill(0, 102, 153);
        else fill(204, 102, 0);
        text(Valor[max-5+i],x+5,y+PApplet.parseInt(30*escala)*(i+1));

      }
    else
        { if(i==2) fill(0, 102, 153);
           else fill(204, 102, 0);
          text(Valor[i+indice-2],x+5,y+PApplet.parseInt(30*escala)*(i+1));
          }
        
    }
  
 strokeWeight (4);
  
 popStyle(); 
}  

}
class TextButton extends Button 
{   String Text;
   float escala; 

   int colorf;
  //metodo simple de llamada
 TextButton(String iText,int ix, int iy) 
  { Text=iText;
    escala=1;
    w = PApplet.parseInt(Text.length()*18*escala);
    h = PApplet.parseInt(40*escala);
    x = ix;
    y = iy; 
  }
    //metodo simple de llamada
 TextButton(String iText,int ix, int iy,int col) 
  { Text=iText;
    escala=1;
    w = PApplet.parseInt(Text.length()*18*escala);
    h = PApplet.parseInt(40*escala);
    x = ix;
    y = iy; 
    colorf=col;
  }
  TextButton(String iText,int ix, int iy,int iw,int ih) 
  { Text=iText;
    escala=1;
    w = iw;
    h = ih;
    x = ix;
    y = iy; 
  }
  
    //metodo completo de llamada
  TextButton(String iText,int ix, int iy,int iw,int ih,float iescala) 
  {Text=iText; 
   escala=iescala;
    w = PApplet.parseInt(iw*iescala);
    h = PApplet.parseInt(ih*iescala);
    x = PApplet.parseInt(ix*iescala);
    y = PApplet.parseInt(iy*iescala); 
  }
  
public void SetText(String iText) 
  {Text=iText; 
    }
    
public void update() 
  {
    over();
    pressed();
    if(pressed) {
      colorf=0xffE8F1F2;
    } else if (over){
      colorf=0xff0594AF;
    } else {
      colorf=0xff0537AF;
    }
    display();
  }
  
 public boolean over() 
  {
    if( overRect(x, y, w, h)) {
      over = true;
      return true;
    } else {
      over = false;
      return false;
    }
  }
  
  public void display() 
  {   pushMatrix();
     strokeWeight(4);
     noFill();
     stroke(255);
     rect(x-2,y-2,w+2,h+2);
     stroke(0); 
     rect(x+2,y+2,w,h);
     noStroke();
     strokeWeight(1);
     fill(colorf);
     rect(x,y,w,h);
     textSize(30*escala);
     fill(255);
     textAlign(CENTER,CENTER);
     text(Text, x+w/2, y+h/2-3);
     popMatrix();
  }
}
class TextInterruptor extends Interrupt
{   int x, y;
  int w, h;
  
  String Text;
   float escala; 
   int colorf;
   
   
  //metodo simple de llamada
 TextInterruptor(String iText,int ix, int iy) 
  { Text=iText;
    escala=1;
    w = PApplet.parseInt(Text.length()*18*escala);
    h = PApplet.parseInt(40*escala);
    x = ix;
    y = iy; 
  }
    //metodo simple de llamada
 TextInterruptor(String iText,int ix, int iy,int col) 
  { Text=iText;
    escala=1;
    w = PApplet.parseInt(Text.length()*18*escala);
    h = PApplet.parseInt(40*escala);
    x = ix;
    y = iy; 
    colorf=col;
  }
  TextInterruptor(String iText,int ix, int iy,int iw,int ih) 
  { Text=iText;
    escala=1;
    w = iw;
    h = ih;
    x = ix;
    y = iy; 
  }
  
    //metodo completo de llamada
  TextInterruptor(String iText,int ix, int iy,int iw,int ih,float iescala) 
  {Text=iText; 
   escala=iescala;
    w = PApplet.parseInt(iw*iescala);
    h = PApplet.parseInt(ih*iescala);
    x = PApplet.parseInt(ix*iescala);
    y = PApplet.parseInt(iy*iescala); 
  }

public void SetText(String iText) 
  {Text=iText; 
    }


public void update() 
  {
    over();
    pressed();
      if(on) colorf=0xff0BF230;
      else colorf=0xffF2330B;
     
    display();
  }
  

  
 public boolean over() 
  {
    if( overRect(x, y, w, h)) {
      over = true;
      return true;
    } else {
      over = false;
      return false;
    }
  }
  
  public void display() 
  {   pushMatrix();
     strokeWeight(4);
     noFill();
     stroke(255);
     rect(x-2,y-2,w+2,h+2);
     stroke(0); 
     rect(x+2,y+2,w,h);
     noStroke();
     strokeWeight(1);
     fill(colorf);
     rect(x,y,w,h);
     textSize(30*escala);
     fill(255);
     textAlign(CENTER,CENTER);
     text(Text, x+w/2, y+h/2-3);
     popMatrix();
  }
}
class fileDialog
{int fileSelect=-1,iniSelect;
 String filedef;
 String path;
 String[] filenames;
 int Px,Py; 
 int w,h;
 int roll;  //indice del roll
 float escala;
 boolean Show=false;

 int interlineado;
 
 
fileDialog(int x, int y, int ancho, int alto,String Path, String nameDef,float esc)
{    escala=esc;
      Px=PApplet.parseInt(x*escala); Py=PApplet.parseInt(y*escala);
      w=PApplet.parseInt(ancho*escala);
      h=PApplet.parseInt(alto*escala);
      path=Path;
      roll=0;
     refresh();
     filedef=nameDef;  //nombre por defecto
    fileSelect=-1;
    interlineado=PApplet.parseInt(25*escala);
}  

public void show()
{ refresh();
  Show=true;
  fileSelect=-1;
  
}  
public void hide()
{
  Show=false;
  fileSelect=-1;
  
}  

public void update()
{ 
  if(!Show) return;
  pushStyle();
//Lista de ficheros    
  fill(31, 31, 31);  
  stroke(255);
  rect(Px, Py, w, h, 7);
  fill(0, 102, 153);
   textSize((h/8-1)*escala);
   text("Files",Px+PApplet.parseInt(5*escala),Py-5*escala); 
  for (int i = 0; i < filenames.length && i<h/interlineado; i++) 
        {if((i+roll)==fileSelect)  fill(255, 0,0); 
         else fill(255);
         if(w/PApplet.parseInt(interlineado)>filenames[i+roll].length())  text(filenames[i+roll],Px+PApplet.parseInt(5*escala),Py+PApplet.parseInt(25*escala)+interlineado*i);
         else text(filenames[i+roll],Px+PApplet.parseInt(5*escala),Py+PApplet.parseInt(25*escala)+interlineado*i);          
        }
 popStyle();  
}  


  
public void refresh()
{
  filenames = listFileNames(path);
}

public String fileselect()
{//print(filenames[fileSelect]);
return filenames[fileSelect]; }

public boolean released(){
//control listado de ficheros
if(mouseY>=Py && mouseY<= Py+h)
   if(mouseX>Px && mouseX< Px+w) 
    {fileSelect=-1;
    if((mouseY-Py-PApplet.parseInt(25*escala))/interlineado<filenames.length)
        fileSelect=(mouseY-Py)/interlineado+roll;
     if (fileSelect!=-1&&fileSelect<filenames.length) { return true; }
     }
return false;     
}


  //scroll
public void dragged()
{if(mouseY>=Py-interlineado && mouseY<= Py+h+interlineado)
   if(mouseX>Px && mouseX< Px+w) 
       {  if(mouseY<Py && roll>0) roll--;
          if(mouseY>Py+h && (roll+h/interlineado)<filenames.length) roll++;
          if((mouseY-Py-2)/interlineado+roll<filenames.length)
             fileSelect=(mouseY-Py-2)/interlineado+roll;
        }
  
}


public void pressed()
{ if(mouseY>=Py && mouseY<= Py+h)
   if(mouseX>Px && mouseX< Px+w)    
    if((mouseY-Py-2)/interlineado<filenames.length)
      fileSelect=(mouseY-Py-2)/interlineado+roll;
}


// This function returns all the files in a directory as an array of Strings  
public String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}  
}
  public void settings() { 
size(540,900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SuperMacroRail_W10" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
