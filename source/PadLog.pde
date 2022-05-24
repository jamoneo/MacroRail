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
if(w1==0){w=int(70*es); tipo=1;} //Se usa solo un lado //Y
else  w=int(w1*es);
 if(h1==0){ h=int(70*es); tipo=2;}  //X
 else  h=int(h1*es);
 x=int(x1*es);
 y=int(y1*es);

 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es; 
 Text="PAD";
}  

PadLog(int x1,int y1, int w1,int h1,int limite,String text,float es)
{tipo=3;  //dos ejes
if(w1==0){w=int(70*es); tipo=1;} //Se usa solo un lado //Y
else  w=int(w1*es);
 if(h1==0){ h=int(70*es); tipo=2;}  //X
 else  h=int(h1*es);
  
  x=int(x1*es);
 y=int(y1*es);
 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es;  
 Text=text;
}  
 
void SetLimits(int lim)
{limit=lim;
}


void update() 
  {

    display();

  }  
  
//devuele si se pulsó para poder tomar el valor que se desee  
boolean pressed() {
    if(over() && mousePressed) 
       { //vx=(mouseX-xmed)*2*escala/w; 
         vx=int(pow((mouseX-xmed)*2*limit/w,2)*limit/pow(limit,2));
         if(mouseX<xmed) vx=-vx;
         
        // vy=(ymed-mouseY)*2*escala/h;
          vy=int(pow((ymed-mouseY)*2*limit/w,2)*limit/pow(limit,2));
         if(mouseY>ymed) vy=-vy; 
         return true;}
    return false;   
  }
    
  
boolean dragged()
{ boolean f;
    pressed();
    if(vx!=oldvx || vy!=oldvy) {oldvx=vx; oldvy=vy;
                                f=true;}
    else f=false;
    return f;
                                
}  
 
 boolean over() {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) 
     return true;
  else 
     return false;
  }  
  
boolean released(){
   if(!mousePressed)
        {vx=0;
         vy=0;
         return true;}
    return false;     
}

void display() 
  {
    pushStyle();
      fill(0, 102, 153);
   textSize(35*escala);
   textAlign(LEFT);
   text(Text,x+5,y-int(5*escala)); 
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
