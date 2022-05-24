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
{x=int(x1*es);
 y=int(y1*es);
 w=int(w1*es);
 h=int(h1*es);
 xmed=x+w/2;
 ymed=y+h/2;
 limit=limite;
 escala=es; 
 Text="PAD";
}  

Pad(int x1,int y1, int w1,int h1,int limite,String text,float es)
{x=int(x1*es);
 y=int(y1*es);
 w=int(w1*es);
 h=int(h1*es);
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
       { vx=int(mouseX-xmed)*2*limit/w; 
       if(mouseX<xmed) vx=-vx;
     vy=int(ymed-mouseY)*2*limit/h; 
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
         textAlign(LEFT);
   textSize(35*escala);
   text(Text,x+5,y-int(5*escala)); 
stroke(255, 255, 255);  
noFill();
rect(x-1, y-1, w+2, h+2);  
stroke(156, 156, 157);
fill(31, 31, 31);  
rect(x, y, w, h);
  popStyle();  
  } 
  

  
  
  
}
