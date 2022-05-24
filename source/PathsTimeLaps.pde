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
void update() 
{if(!show) return;
  pushStyle();
fill(31, 31, 31);  
stroke(255);
rect(int((Px-7)*escala), int((Py-7)*escala), int(540*escala), int(380*escala), 7);

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
void pressed()
{ 


 
 if(SliderR.pressed()) {XIni=-590;  XFin=590;  }
 if(SliderL.pressed()) {XIni=590;  XFin=-590;  }
 
 if(TiltU.pressed()) {ZIni=-45;  ZFin=45;  }
 if(TiltD.pressed()) {ZIni=45;  ZFin=-45;  }
 
 if(PanR.pressed()) {YIni=-45;  YFin=45;  }
 if(PanL.pressed()) {YIni=45;  YFin=-45;  }
 
}
//control para interruptores
void released() 
{  if(Exit.released()) Hide();
SliderR.released();
SliderL.released();
TiltU.released();
TiltD.released();
PanR.released();
PanL.released();
}

void dragged()
{ 
}

void FlechaR(int x,int y)
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

void FlechaL(int x,int y)
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

void FlechaU(int x,int y)
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
void FlechaD(int x,int y)
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
void Show()
{ show=true;
}

void Hide()
{show=false;
}
}
