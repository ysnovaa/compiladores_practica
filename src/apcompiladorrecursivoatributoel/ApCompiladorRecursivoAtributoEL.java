
package apcompiladorrecursivoatributoel;
// este aplicativo utiliza el metodo recursivo descendente para reconocer
// secuencias aritmeticas lógicas y relacionales


import java.util.*;
import java.io.*;

public class ApCompiladorRecursivoAtributoEL {

    
    static Lexico lexico = new Lexico();
    static String cad = "1|0¬";// "((5*4)+2)*5.4¬";
    static String cad1 = "0123456789.";
    // variable indice es global y controla el indice del objwro lex1
    static int indice=0;
    static char sim=' ';
    static Lexico lex1 = new Lexico();
    static String cadavance="";
    
    public static void main(String[] args) {
       /* InputStreamReader isr= new InputStreamReader(System.in);
        BufferedReader flujoE = new BufferedReader(isr);*/
        analisisLexico();
        cad=lex1.cadenaLexico();
        sim=lex1.darElemento(indice).darTipo();
        cadavance=cadavance+sim;
        
        procS();
        if (sim=='¬')
            System.out.println("Se acepta la secuencia ");
        else
            System.out.println("Se rechaza la secuencia ");
    
    }
    
    public static void procS(){
        // <S> --> <E> 
        
        boolean res=false;
        
        switch (sim) {
            case 'i':case '(':
                    NoTerminal s1 = new NoTerminal("s1",0,0);
                    procELO(s1);
                    resultado(s1.getValor());
                    return;
            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }        
    }
    
        
        public static void procELO(NoTerminal s1){
        // <E> --> <T><E_L>
        
        switch (sim) {
            case 'i':case '(':
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procEL2(s2);
                    procELO_L(s2.getValor(),s1);
                    return;

            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }
    }
        
        public static void procEL2(NoTerminal s1) {

        switch (sim) {
            case 'i': case '(':
                NoTerminal s2 = new NoTerminal("s2", 0, 0);
                procER(s2);
                procEL2_L(s2.getValor(), s1);
                return;

            default:
                System.out.println("Secuencia" + cad + " no se acepta");
                rechace();
        }
    }
        
        public static void procEL2_L(double i1, NoTerminal s1) {

        switch (sim) {
            case '&':
                    avance();
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procER(s2);
                    NoTerminal s3 = new NoTerminal("s3",0,0);
                    and(i1,s2.getValor(),s3);
                    procEL2_L(s3.getValor(),s1);
                    return;
                    

            default: 
                    System.out.println("Secuencia procel "+cad+" no se acepta");
                    rechace();
        }
    }
        
        public static void procER(NoTerminal s1){
            
            switch (sim) {
            case 'i':case '(': 
                    avance();
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procE(s2);
                    procER_L(s2.getValor(),s1);
                    return;                  
                
            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
            }
            
            
        }

        public static void procER_L(double i1, NoTerminal s1){
            switch (sim) {
            case '<':case '>': case '=':case '!': 
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procE(s2);
                    procOR(s2.getValor(),s1);
                    return;                  
                
            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
            }
        }
        
        public static void procOR(double i1, NoTerminal s1) {
        switch (sim) {
            case '<':
                avance();
                NoTerminal s2 = new NoTerminal("s2",0,0);
                procME(s2);
                return;
            case '>':
                avance();
                NoTerminal s2 = new NoTerminal("s2",0,0);
                procMA(s2);
                return;
            case '!':
                avance();
                NoTerminal s2 = new NoTerminal("s2",0,0);
                procDI(s2);
                return;
                
            default:
                System.out.println("Secuencia" + cad + " no se acepta");
                rechace();
        }
    }
        
        public static void procME(double i1){
            
            NoTerminal s1 = new NoTerminal("s1",0,0);

            switch (sim) {
                case '=':
                        NoTerminal s2 = new NoTerminal("s2",0,0);
                        menor_igual(i1,s1.getValor(),s2);
                        return;
                default:
                        NoTerminal s3 = new NoTerminal("s3",0,0);
                        menor(i1,s1.getValor(),s3);
            }
            
        }
        
        public static void procMA(double i1,NoTerminal s1){
            
            switch (sim) {
                case '=':
                        NoTerminal s2 = new NoTerminal("s2",0,0);
                        mayor_igual(i1,s1.getValor(),s2);
                        return;
                default:
                        NoTerminal s3 = new NoTerminal("s3",0,0);
                        mayor(i1,s1.getValor(),s3);
            }
            
        }
        
        public static void procIG(double i1,NoTerminal s1){
            
            switch (sim) {
                case '=':
                        NoTerminal s2 = new NoTerminal("s2",0,0);
                        igual(i1,s1.getValor(),s2);
                        return;
                default:
                        System.out.println("Secuencia" + cad + " no se acepta");
                        rechace();
            }
            
        }
        
        public static void procDI(double i1,NoTerminal s1){
            
            switch (sim) {
                case '!':
                        NoTerminal s2 = new NoTerminal("s2",0,0);
                        distinto(i1,s1.getValor(),s2);
                        return;
                default:
                        System.out.println("Secuencia" + cad + " no se acepta");
                        rechace();
            }
            
        }
        
        public static void procELO_L(double i1, NoTerminal s1){
        //2. <ELO> ®<EL2> <ELO_L>
        //3. <ELO_L>® | <EL2> <ELO_L>
        //4. <ELO_L>®Î
        
        switch (sim) {
            case '|':
                    avance();
                    NoTerminal s2 = new NoTerminal("s2",0,0);
    
                    procEL2(s2);
                    pRelacional(i1,s2);
                    NoTerminal s4 = new NoTerminal("s4",0,0);
                    //No es procOR sino una nueva función
                    procOr(i1,s2,s4);
                    NoTerminal s5 = new NoTerminal("s5",0,0);
                    procELO_L(s4.getValor(),s5);
                    return;
                    //s5 puede ser s1?

            default: 
                    System.out.println("Secuencia procel "+cad+" no se acepta");
                    rechace();
        }
    }
    
        
    
    public static void procE(NoTerminal s1){
        // <E> --> <T><E_L>
        
        switch (sim) {
            case 'i':case '(':
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procT(s2);
                    procEL(s2.getValor(),s1);
                    return;

            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }
    }
    public static void procEL(double i1,NoTerminal s1){
        // <E_L> --> +<T><E_L>
        // <E_L> --> -<T><E_L>  no está implementada
        // <E_L> --> e
        
        switch (sim) {
            case '+':
                    avance();
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procT(s2);
                    NoTerminal s3 = new NoTerminal("s3",0,0);
                    suma(i1,s2.getValor(),s3);
                    procEL(s3.getValor(),s1);
                    return;
                    
            case '-':
                    avance();
                    NoTerminal s4 = new NoTerminal("s2", 0, 0);
                    procT(s4);
                    NoTerminal s5 = new NoTerminal("s3", 0, 0);
                    resta(i1, s4.getValor(), s5);
                    procEL(s5.getValor(), s1);
                    return;

            case ')': case '¬':
                    s1.setValor(i1);
                    return;

            default: 
                    System.out.println("Secuencia procel "+cad+" no se acepta");
                    rechace();
        }
    }
    
    public static void procT(NoTerminal s1){
        // <T> --> <P><T_L>
        
        switch (sim) {
            case 'i':case '(':
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    
                    procP(s2);
                    System.out.println("Valor de <P> "+s2.getValor());
                    procTL(s2.getValor(),s1);
                    
                    return;
                    

            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }
    }
    public static void procTL(double i1, NoTerminal s1){
        // <T_L> --> *<P><T_L>
        // <T_L> --> /<P><T_L>   no está implementada
        // <T_L> --> e
        
        switch (sim) {
            case '*': 
                    
                    avance();
                    NoTerminal s2 = new NoTerminal("s2",0,0);
                    procP(s2);
                    NoTerminal s3 = new NoTerminal("s3",0,0);
                    mul(i1,s2.getValor(),s3);
                    procTL(s3.getValor(),s1);
                    return;
                    
            case '/': 
                    
                    avance();
                    NoTerminal s4 = new NoTerminal("s2",0,0);
                    procP(s4);
                    NoTerminal s5 = new NoTerminal("s3",0,0);
                    div(i1,s4.getValor(),s5);
                    procTL(s5.getValor(),s1);
                    return;
                 
            case '+': case '-': case ')': case '¬':
                    s1.setValor(i1);
                    return;
                    
            
       
            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }
    }
    
    
        public static void procP(NoTerminal s1){
        // <P> --> i
        // <P> --> ( <E> )
        
        switch (sim) {
            case 'i':
                    Elemento ele= lex1.darElemento(indice);
                    s1.setValor(ele.darValor());
                    System.out.println("Valor del i = "+ele.darValor());
                    avance();
                    return;

            case '(':
                    avance();
                    
                    procE(s1);
                    if (sim==')') {avance();
                    }
                    else {
                        System.out.println("Secuencia"+cad+" no se acepta");
                        rechace();
                    }
                    break;    
            default: 
                    System.out.println("Secuencia"+cad+" no se acepta");
                    rechace();
        }
        
    }
        
    public static void suma(double i1, double i2,NoTerminal s3){
        System.out.println("Elementos a sumar "+i1+"  "+i2);
        s3.setValor(i1+i2);
    }
    
    public static void resta(double i1, double i2,NoTerminal s3){
        System.out.println("Elementos a restar "+i1+"  "+i2);
        s3.setValor(i1-i2);
    }
    
    public static void mul(double i1, double i2,NoTerminal s3){
        s3.setValor(i1*i2);
    }
    
    public static void div(double i1, double i2,NoTerminal s3){
        s3.setValor(i1/i2);
    }
    
    public static void or(double i1,double i2,NoTerminal s3){
        boolean a=i1==1d;
        boolean b=i2==1d;
        s3.setValor(a||b?1d:0d);
    }
    
    public static void and(double i1,double i2,NoTerminal s3){
        boolean a=i1==1d;
        boolean b=i2==1d;
        s3.setValor(a||b?1d:0d);
    }
    
    public static void menor_igual(double i1,double i2,NoTerminal s3){
        s3.setValor(i1<=i2?1d:0d);
    }
    
    public static void mayor_igual(double i1,double i2,NoTerminal s3){
        s3.setValor(i1>=i2?1d:0d);
    }
    
    public static void menor(double i1,double i2,NoTerminal s3){
        s3.setValor(i1<i2?1d:0d);
    }
    
    public static void mayor(double i1,double i2,NoTerminal s3){
        s3.setValor(i1>i2?1d:0d);
    }
    
    public static void igual(double i1,double i2,NoTerminal s3){
        s3.setValor(i1==i2?1d:0d);
    }
    
    public static void distinto(double i1,double i2,NoTerminal s3){
        s3.setValor(i1!=i2?1d:0d);
    }
    
    public static void resultado(double res){
        System.out.println("Resultado ="+res);
    }
    
    public static void analisisLexico(){
        // Este analizador es sencillo determina solo constantes enteras y reales positivas
        // Trabaja los diferentes elementos en un ArrayList que trabaja con la clase Clexico
        // la cual define el ArrayList con la clase CElemento
        // Almacen los valores para poder hallar los resultados
        
        Elemento ele1;
        
        int i=0;
        int ind=0;
        char tip=0;
        char sim1=cad.charAt(i);
        double val=0;
        
        while (sim1!='¬'){
            // determina si sim1 esta en la cadena de digitos cad1 que es global
            if (cad1.indexOf(sim1)!=-1){
                String num="";
                while(cad1.indexOf(sim1)!=-1){  
                    num=num+sim1;
                    i++;
                    sim1=cad.charAt(i);
        
                }
                // en el String num se almacena el posible real
                // DeterminarNumero aplica el autómata para enteros y reales
                if (determinarNumero(num)){
                    val=Double.parseDouble(num);
                    tip='i';
                    // se tipifica el valor como i
                }
                else{ 
                    System.out.println("Se rechaza la secuencia");
                    System.exit(0);
                }
               }
            else {
               // si el simbolo de entrada no esta en cad1 lo tipifica como tal ej
               // +,-,* (,) etc.
                
               tip=(char)sim1;
               i++;
               sim1=cad.charAt(i);
               val=0;
             }
        
            // con los elementos establecidos anteriormente se crea el elemento y se lo
            // adicina a lex1 que es el objeto de la clase Clexico
            
            ele1=new Elemento(tip,val,ind);
            lex1.adicionarElemento(ele1);
            
            ind=ind+1;
            //System.out.print("indice ="+ind);
               
        } // después del while se adiciona el fin de secuencia al léxico
        ele1=new Elemento('¬',0,ind);
        lex1.adicionarElemento(ele1);
        lex1.mostrarLexico();
        System.out.println(" cadena"+lex1.cadenaLexico());
    }
    
    public static boolean determinarNumero(String numero){
        // Aplica el autómata para determinar si es un número valido
        // Recibe el estrin numero y entrega una variable boolena
        // con el resultado
        
        int estado=1,i=0;
        char simbolo;
        boolean b=true;
        while (i<numero.length()&&b) {
            simbolo = numero.charAt(i);
            switch (simbolo) {
                case '0':case '1':case '2':case '3':case '4':case '5':case '6':  
                case '7':case '8':case '9':    
                    switch (estado) {
                        case 1:
                           estado=2;
                           i++;
                           break;
                        case 2:
                           estado=2;
                           i++;
                           break;
                        case 3:
                           estado=4;
                           i++;
                           break;
                        case 4:
                            estado=4;
                           i++;
                           break;
                    }
                    break;
                case '.':    
                    switch (estado) {
                        case 1:case 3: case 4:
                           b=false;
                           break;
                        case 2:
                           estado=3;
                           i++;
                           break;
                    }
            }
        }
        return b;
    }
    
    public static void avance(){
           indice++;
       if (indice<cad.length()) {
            sim=lex1.darElemento(indice).darTipo();
            cadavance=cadavance+sim;
            System.out.println("Cadena procesada "+cadavance);
       }
    }
    
    public static void mostrarContador(int i2){
    System.out.println("Cantidad de unos "+i2);
    }
    
    public static void rechace(){
        System.out.println("Se rechaza la secuencia");
        System.exit(0);
    }
    
}
