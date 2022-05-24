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
    w = int(iw*escala);
    h = int(ih*escala);
    x = int(ix*escala);
    y = int(iy*escala); 
     xmed=x+w/2;
     ymed=y+h/2;
    max=0;indice=0;
    Valor= new String[50];
    
}

//devuele si se pulsó para poder tomar el valor que se desee  
boolean pressed() {
     if(over() && mousePressed) 
       { press=true;
        vx= mouseX; vy=mouseY;  //almacena el punto donde se incia el toque      
    return true;}
    return false;   
  }
  
void dragged()
{ 
    if(over() && mousePressed) 
    {
     //testea el incremento. Si es una linea,cambia el indice mostrado
    if( vy>oldvy+int(20*escala)) {oldvy=vy;
                                   Rt();}
      if( vy<oldvy-int(20*escala)) {oldvy=vy;
                                   Av();};
    }                                
                               
}  
 
 boolean over() {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) 
     return true;
  else 
     return false;
  }  
  
boolean released(){
   if(over() && !mousePressed&&press)
        {
      if(mouseY>ymed)  Av();
      else  Rt();
         press=false;
         return true;}
    return false;     
}


//añade un texto
void Add(String Texto)
{ 
  Valor[max]=Texto;
  max++;
}  

//elimina un texto
void Sub(int ind)
{for(int i=ind;i<max;i++)
    Valor[i]=Valor[i+1];
 max--;   
}

  
void Av()  
{ if (indice<max-1)indice++;
}
  
void Rt()
{if(indice>0) indice--;
} 

//Devuelve el valor del indice seleccionado
int Get()
{return indice;
}

void update()
{pressed();
//released();
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
         text(Valor[i],x+5,y+int(30*escala)*(i+1));

      }
    else if (indice >max-3) 
    { 
        if(i==5-(max-indice)) fill(0, 102, 153);
        else fill(204, 102, 0);
        text(Valor[max-5+i],x+5,y+int(30*escala)*(i+1));

      }
    else
        { if(i==2) fill(0, 102, 153);
           else fill(204, 102, 0);
          text(Valor[i+indice-2],x+5,y+int(30*escala)*(i+1));
          }
        
    }
  
 strokeWeight (4);
  
 popStyle(); 
}  

}
