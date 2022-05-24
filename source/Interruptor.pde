class Interrupt
{
  int x, y;
  int w, h;
    boolean over = false;
  boolean pressed = false; 
  boolean on=false; //indica que está en on
  
  

  
void pressed() {
    if(over && mousePressed) 
       pressed=true;  //indica que se ha presionado
  }
  
  
  
boolean released() {
      
    if( over) 
       { if(on) on=false;
         else on=true;
         pressed=false;
      //  println(on);
       return true; 
       }
    else return false;

  }
  
  
  
boolean overRect(int x, int y, int width, int height) {
  if (mouseX >= x && mouseX <= x+width && 
      mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  } 

}
}
