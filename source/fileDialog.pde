class fileDialog
{int fileSelect=-1,iniSelect;
 String filedef;
 String path;
 String[] filenames;
 int Px,Py; 
 int w,h;
 int roll;  //indice del roll
 float escala;
 boolean Show=false;

 int interlineado;
 
 
fileDialog(int x, int y, int ancho, int alto,String Path, String nameDef,float esc)
{    escala=esc;
      Px=int(x*escala); Py=int(y*escala);
      w=int(ancho*escala);
      h=int(alto*escala);
      path=Path;
      roll=0;
     refresh();
     filedef=nameDef;  //nombre por defecto
    fileSelect=-1;
    interlineado=int(25*escala);
}  

void show()
{ refresh();
  Show=true;
  fileSelect=-1;
  
}  
void hide()
{
  Show=false;
  fileSelect=-1;
  
}  

void update()
{ 
  if(!Show) return;
  pushStyle();
//Lista de ficheros    
  fill(31, 31, 31);  
  stroke(255);
  rect(Px, Py, w, h, 7);
  fill(0, 102, 153);
   textSize((h/8-1)*escala);
   text("Files",Px+int(5*escala),Py-5*escala); 
  for (int i = 0; i < filenames.length && i<h/interlineado; i++) 
        {if((i+roll)==fileSelect)  fill(255, 0,0); 
         else fill(255);
         if(w/int(interlineado)>filenames[i+roll].length())  text(filenames[i+roll],Px+int(5*escala),Py+int(25*escala)+interlineado*i);
         else text(filenames[i+roll],Px+int(5*escala),Py+int(25*escala)+interlineado*i);          
        }
 popStyle();  
}  


  
void refresh()
{
  filenames = listFileNames(path);
}

String fileselect()
{//print(filenames[fileSelect]);
return filenames[fileSelect]; }

boolean released(){
//control listado de ficheros
if(mouseY>=Py && mouseY<= Py+h)
   if(mouseX>Px && mouseX< Px+w) 
    {fileSelect=-1;
    if((mouseY-Py-int(25*escala))/interlineado<filenames.length)
        fileSelect=(mouseY-Py)/interlineado+roll;
     if (fileSelect!=-1&&fileSelect<filenames.length) { return true; }
     }
return false;     
}


  //scroll
void dragged()
{if(mouseY>=Py-interlineado && mouseY<= Py+h+interlineado)
   if(mouseX>Px && mouseX< Px+w) 
       {  if(mouseY<Py && roll>0) roll--;
          if(mouseY>Py+h && (roll+h/interlineado)<filenames.length) roll++;
          if((mouseY-Py-2)/interlineado+roll<filenames.length)
             fileSelect=(mouseY-Py-2)/interlineado+roll;
        }
  
}


void pressed()
{ if(mouseY>=Py && mouseY<= Py+h)
   if(mouseX>Px && mouseX< Px+w)    
    if((mouseY-Py-2)/interlineado<filenames.length)
      fileSelect=(mouseY-Py-2)/interlineado+roll;
}


// This function returns all the files in a directory as an array of Strings  
String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}  
}
