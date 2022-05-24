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
xt1=int(x1*escala); //Posicion texto
 x=int((x1+350)*escala);//Posicion barra
 xt2=x+int(w1*escala)+int(15*escala); // Posicion valor
 y=int(y1*escala);
 w=int(w1*escala);
 h=int(h1*escala);
 limit=limite;
 Text1=text;
 pulsado=false;
 vx=oldvx=0;
}  


void update() 
  {
    display();
  }  

//devuele si se pulsó para poder tomar el valor que se desee  
boolean pressed() {
    if(over() && mousePressed) 
       { vx=(mouseX-x); 
         if (vx<0) vx=0;
          pulsado=true;  return true;}
    return false;   
  }

boolean dragged()
{ boolean f;
  //    pressed();
    if(over()) vx=(mouseX-x); 
    if (vx<0) vx=0;
    if(vx!=oldvx) {oldvx=vx; f=true;}
    else f=false;
    return f;
                                
}  
 
boolean released(){
   if(!mousePressed) return true;
   else return false;     
} 
 
 boolean over() {
  if (mouseX >= x-5 && mouseX <= x+w+5 && 
      mouseY >= y-5 && mouseY <= y+h+5) 
     return true;
  else 
     return false;
  } 

  void display() {
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
  textSize(int(35*Escala));

  fill(0, 102, 153); 
   textAlign (LEFT);
  text( Text1, xt1, y+h);   

  
  text( int(vx*limit/w), xt2, y+h);  
  }
 int  getPos() {
    return int(vx*limit/w);
  }
  
  void setPos(int pos) {
    vx= pos;
  }
}
