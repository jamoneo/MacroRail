class Menu
{int Sel=0,OldSel=0;  //menu seleccionado
 int nMen=0;
 String[] Text;
 float escala;
 
 
Menu(String t0,String t1,String t2,String t3,String t4,String t5,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
      Text[4]=t4;
       Text[5]=t5;
       escala=esc;
   nMen=6;    
} 
Menu(String t0,String t1,String t2,String t3,String t4,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
      Text[4]=t4;
       escala=esc;
   nMen=5;    
}

Menu(String t0,String t1,String t2,String t3,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
     Text[3]=t3;
       escala=esc;
   nMen=4;    
} 


Menu(String t0,String t1,String t2,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
    Text[2]=t2;
           escala=esc;
   nMen=3;    
}  
 
Menu(String t0,String t1,float esc)
{
  Text = new String[6];
  Text[0]=t0;
   Text[1]=t1;
          escala=esc;
   nMen=2;    
}  
 
int pressed()
{ int sel=0;

//Gestión de menú
// si no se ha pulsado el ratón o no está nezona de menú se sale
  if(!mousePressed||mouseY>int(50*escala)) return 0;
  
Sel=mouseX/(width/nMen);

//Devuelve el menú en el que está el ratón indicando el primer menú como 1.
 return (Sel+1);
 
}


void update()
{ //presenta el menú
  pushStyle();
    background(159, 159,159);
    stroke(255);
    textSize(int(24*escala));
  for(int i=0;i<nMen;i++)
   {     if (i==Sel)
                    { noStroke();
                      fill(159, 159,159);  }
          else   { fill(0);
                    stroke(255);}
      rect(i*width/nMen, 0, width/nMen, int(50*escala));
        fill(255);
       text(Text[i],i*width/nMen+5, int(30*escala)); 
   }
  popStyle();  
  
}


}
