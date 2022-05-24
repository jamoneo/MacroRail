class Button
{
  int x, y;
  int w, h;

  boolean over = false;
  boolean pressed = false;  
  boolean on;
  
//Flanco entrada pulsado  
boolean pressed() {
    if(overRect(x,y,w,h) && mousePressed&&!pressed) 
       { pressed=true;  //indica que se está presionando
         return true;}
    return false;
  }
  
//devuelve true si se ha soltado encima del botón
//para realizar la alción al soltar
boolean released() {
   if(overRect(x,y,w,h) && !mousePressed&&pressed) {
      pressed=false;
      return true;   }
     return false;
  
  }
//Se está pulsando el botón  
boolean press(){
    if(overRect(x,y,w,h) && mousePressed) {
      pressed=true;
      return true;   }
    else { pressed=false;  
     return false; 
    }
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
