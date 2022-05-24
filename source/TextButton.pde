class TextButton extends Button 
{   String Text;
   float escala; 

   int colorf;
  //metodo simple de llamada
 TextButton(String iText,int ix, int iy) 
  { Text=iText;
    escala=1;
    w = int(Text.length()*18*escala);
    h = int(40*escala);
    x = ix;
    y = iy; 
  }
    //metodo simple de llamada
 TextButton(String iText,int ix, int iy,int col) 
  { Text=iText;
    escala=1;
    w = int(Text.length()*18*escala);
    h = int(40*escala);
    x = ix;
    y = iy; 
    colorf=col;
  }
  TextButton(String iText,int ix, int iy,int iw,int ih) 
  { Text=iText;
    escala=1;
    w = iw;
    h = ih;
    x = ix;
    y = iy; 
  }
  
    //metodo completo de llamada
  TextButton(String iText,int ix, int iy,int iw,int ih,float iescala) 
  {Text=iText; 
   escala=iescala;
    w = int(iw*iescala);
    h = int(ih*iescala);
    x = int(ix*iescala);
    y = int(iy*iescala); 
  }
  
void SetText(String iText) 
  {Text=iText; 
    }
    
void update() 
  {
    over();
    pressed();
    if(pressed) {
      colorf=#E8F1F2;
    } else if (over){
      colorf=#0594AF;
    } else {
      colorf=#0537AF;
    }
    display();
  }
  
 boolean over() 
  {
    if( overRect(x, y, w, h)) {
      over = true;
      return true;
    } else {
      over = false;
      return false;
    }
  }
  
  void display() 
  {   pushMatrix();
     strokeWeight(4);
     noFill();
     stroke(255);
     rect(x-2,y-2,w+2,h+2);
     stroke(0); 
     rect(x+2,y+2,w,h);
     noStroke();
     strokeWeight(1);
     fill(colorf);
     rect(x,y,w,h);
     textSize(30*escala);
     fill(255);
     textAlign(CENTER,CENTER);
     text(Text, x+w/2, y+h/2-3);
     popMatrix();
  }
}
