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
 
    w = int(base.width*esc);
    h = int(base.height*esc);
    x =int(ix*esc)- w/2;
    y = int(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }
    //metodo escalado con tamaño definido
  ImageButton(int ix, int iy,int wi, int he,String fbase, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 roll=base;
 down = loadImage(fdown); 
 
    w = int(wi*esc);
    h = int(he*esc);
    x =int(ix*esc)- w/2;
    y = int(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }
  
    //metodo completo de llamada
  ImageButton(int ix, int iy,String fbase,String froll, String fdown,float esc) 
  { 
 base = loadImage(fbase);
 roll=loadImage(froll);
 down = loadImage(fdown); 
 
    w = int(base.width*esc);
    h = int(base.height*esc);
    x = int(ix*esc)- w/2;
    y = int(iy*esc)- h/2; 
   currentimage = base;
   escala=esc;
  }

void update() 
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
  
  void over() 
  {
    if( overRect(x, y, w, h) ) {
      over = true;
    } else {
      over = false;
    }
  }
  
  void display() 
  {
    image(currentimage, x, y,w,h);
  }
}
