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
 
    w = int(base.width*esc);
    h = int(base.height*esc);
    x =int(ix*esc)- w/2;
    y = int(iy*esc)- h/2; 
   currentimage = base;
  }
  
    //metodo completo de llamada
 

void update() 
  {
    over();
    pressed();
    if(on) 
      currentimage = down;
    else
      currentimage = base;


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
    image(currentimage, x, y);
  }
}
