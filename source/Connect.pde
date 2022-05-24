                                                                                              import processing.serial.*;
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
void update()
{ DatosScreen.LastScreen=0; //indica trabajo Ninguno
  pushStyle();

fill(31, 31, 31);  
rect(int(50*escala),int(280*escala), int(450*escala),int(500*escala), 7);


fill(255);

if(Conectado) {image(conectadoblue,int(250*escala), int(850*escala));
               }
    if(ErrorFlag)  image(Error,int(400*escala), int(850*escala));  
   if(AlarmFlag)  image(Alarm,int(550*escala), int(850*escala)); 
getBluetoothInformation();
  text(Version, int(50*escala),int(100*escala));    
  text("(c) Jose Angel Moneo Fdz.", int(50*escala),int(1200*escala));                                   

    bReset.update();

  popStyle();  

}


//Control cciones al pulsar
void pressed()
{ select=-1;
  if (mouseX >= int(50*escala) && mouseX <= int(500*escala) && 
      mouseY >=int(305*escala) && mouseY <= int(500*escala)) {
        select=(mouseY-int(305*escala))/int(40*escala);
        if(select>=Serial.list().length) select=-1;
      }   
      
//  println(select);   
      
  //return select;



}


//control de acciones al soltar
int released() {

    if(bReset.released()) {if(iniCom) { //myPort.write(ctrlx);
                                       Send("$X");   //  desbloqueo
                                       Send("$10=3");
                                        Send("$H"); //Realiza home
                                       ErrorFlag=false;}
                                      }   

  return select;                        
                              
 }
 

void dragged()
{

}
 
 
void getBluetoothInformation()
{  int i;
  textSize(30*escala);
 text( "List Com:\n",int(70*escala), int(310*escala));  
  for (i=0; i<Serial.list().length; i++)
     { if (i==select) fill(153, 204, 255);
       else fill(255,255,255);
       text( "["+i+"] "+puerto[i],int(70*escala),int(345*escala)+int(40*escala)*i);
     }   

}



} 
