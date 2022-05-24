

class Registro {
 float X,Y,Z,F,V;

 int len=5;
 

  public Registro(String[] pieces) {
    X = float(pieces[0]);
    Y= float(pieces[1]);
    Z = int(pieces[2]);
    F = float(pieces[3]);
    V = float(pieces[4]);
  
  }
 String  Get()
 { String salida;
      salida=X+ "\t"+Y+ "\t"+Z+ "\t"+F+ "\t"+V;
   return (salida);
 }
}
