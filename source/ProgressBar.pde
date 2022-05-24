
class ProgressBar
{int px,py;
int ancho,alto;
 int min, max;
 int valor;
 
 
 
 
ProgressBar(int x,int y,int w,int h)
{px=x;
py=y;
alto=h;
ancho=w;
min=0;
max=1;
valor=0;
}


void update()
{ pushStyle();
fill(91, 91, 91);  
stroke(255);
rect(px, py, ancho, alto, 7);  //caja

//textos de valores extremos
 fill(255);    
 text( min, px,py-10);      
 text( max, px+ancho-30,py-10); 
 text(valor, px+ancho/2,py-10); 
 //barra del progeso
fill(11, 155,12);  
noStroke();
if(max>0 && valor<=max)
  rect(px+1, py+1, valor*(ancho-2)/max,alto-2, 7);  //caja
stroke(255);  
 fill(229,229,229);  
for(int i=(px+ancho/10); i<px+ancho;i+=ancho/10)  //10 barras
   line(i,py+2,i,py+alto-2); 
   popStyle(); 
}  
  
void SetMin(int m)
{
  min=m;
}  
void SetMax(int m)
{
 max=m;

}  

void SetVal(int v)
{valor=v;
}
  
}  
