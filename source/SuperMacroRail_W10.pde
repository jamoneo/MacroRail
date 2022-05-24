/**
*Control Slider Windows v2.0
*by José Angel Moneo

Control del MacroRail desde Android por bluetouth
Permite conectarse por bluetouth y programar los tiempos, los desplazamientos
y los tiempos. 
Luego permite la ejecución.
*
*/
 
 
import processing.serial.*;
import processing.opengl.*;

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


void setup()
{
   //************Configuración 
size(540,900); escala=0.7;  
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

void draw()
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

void mouseReleased() {
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

void mousePressed() {
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

 void mouseDragged()
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


void GenComand(String buf)
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
                  DatosScreen.posM=float(buf.substring(ini,fin)); 
                  //Y
                  ini=fin+1;
                 fin=buf.substring(ini).indexOf(",")+ini; 
                 //print("Subcadena Y:"+buf.substring(ini, fin)+"\n");
                  DatosScreen.posG=float(buf.substring(ini,fin)); 
                  //Z
                 ini=fin+1;
                 fin=buf.substring(ini).indexOf(",")+ini; 
                   //  print("Subcadena Z:"+buf.substring(ini, fin)+"\n");
                 DatosScreen.Z=float(buf.substring(ini,fin)); 

  //Buffer
                ini=buf.indexOf("|Bf:")+4;

                 fin=buf.substring(ini).indexOf(",")+ini; 
                 //print("Subcadena Buffer_ins:"+buf.substring(ini, fin)+"\n");
                 DatosScreen.Buffer_Inst=int(buf.substring(ini,fin)); 
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


void serialEvent(Serial myPort) {
  int inByte;    // Incoming serial data
  if(myPort.available()>0){
  inByte=myPort.read();
  //println(inByte);
  buffer+=char(inByte);
  if(inByte == '\n') {Respuesta=buffer; //print("recibido "+Respuesta);
                     buffer="";
                      GenComand(Respuesta);
                     }
  }                   

} 




void Run()
{  if(Running&&Update)
       { if(pBuffer<LonBuffer) 
           {//  if(DatosScreen.Buffer_Inst==15) //envío cuanod hay espacio en el buffer de recepción
            if(DatosScreen.State.equals("Idle"))
                { //print("Send: "+Buffer[pBuffer]+"\n");
                
                 if(Buffer[pBuffer].substring(0, 2).equals("G4")) {//print("delay: "+int(Buffer[pBuffer].substring(3))+"\n");
                                                                    delay(int(Buffer[pBuffer++].substring(3)));}
                 
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
