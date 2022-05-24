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
void on()
{state=true;
}

//Apaga el piloto
void off()
{state=false;
}

//la actualizacion mediante comando on
void update() 
{    if(state) 
      currentimage = on;
     else
      currentimage = off;
    display();
  }
  

  
  void display() 
  {
    image(currentimage, x*escala, y*escala);
  }
}
