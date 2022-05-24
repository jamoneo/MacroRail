class Contrave
{
int valor;
int px,py;  
  
Contrave(int x1,int y1)
{px=x1;
py=y1;
valor=0;
}

void update()
{ pushStyle();
flechas(px,py, true);
digitos(px,py+35);
flechas(px,py+90, false);

  popStyle(); 
  
  
}  

float GetVal()
{
 return float(valor); 
}  
//cajas para numeros
void digitos(int x,int y)
{
  stroke(255);
  fill(59,59,59); 
for(int a=0;a<4;a++)
   rect(x+a*43,y,40,50, 7);
    
fill(255);
textSize(30);
for(int a=0;a<4;a++)
   text(int((valor%pow(10,4-a))/pow(10,3-a)) ,x+10+a*43,y+30);    
}
//cajas para numeros
void flechas(int x,int y, boolean sentido)
{ stroke(255);
  fill(59,59,59); 
for(int a=0;a<4;a++)
   rect(x+a*43,y,40,30, 7);
    
fill(255);
for(int a=0;a<4;a++)
    if(sentido) triangle(x+10+a*43,y+25,x+20+a*43,y+5,x+30+a*43,y+25);
    else  triangle(x+10+a*43,y+5,x+20+a*43,y+25,x+30+a*43,y+5);
}

void pressed()
{for(int a=0;a<4;a++)
    {if(over(px+a*43,py,40,30)) valor+=pow(10,3-a);
     if(over(px+a*43,py+90,40,30)) valor-=pow(10,3-a);
    }
  valor%=10000;  
}

boolean over(int x,int y,int w,int h)
{
    if (mouseX > x && mouseX < x+w &&
       mouseY > y && mouseY < y+h) 
      return true;
    else
      return false;
    
  }
}
