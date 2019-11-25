package print;

import logging.Logger;
import numbers.Value;
import simplex.Tableau;

public abstract class Printer {

    protected String desc = "";
    protected String state = "";

    protected int[] minCols;

    protected int numDecimals = 1;
    protected int numSeparators = 0;

    protected int pivotalColumn = -2;
    protected int pivotalLine = -2;
    protected int currentLine = 0;
    protected int currentCol = 0;


    public abstract void setDesc(String desc);
    public abstract  String getDesc();
    public abstract  void setState(String state);
    public abstract  String getState();

    public abstract  void printTableau(Tableau tableau);
    // Para imprimir no terminal, inteiros estão de bom tamanho
    // Simplesmente copiamos e passamos para inteiros
    public abstract  void printTableau(Value[][] A, Value[] c, Value[] b, Value z, boolean[] basis);

    public abstract  void printTableau(double[][] A, double[] c, double[] b, double z, boolean[] basis);

    public abstract  void printCert(Value[] cert);

    public abstract  void printCert(double[] cert);


    protected abstract  int smallestSpacing(double[] array);

    protected abstract  int spacing(int num);
    
    protected abstract  int spacing(double num);

    protected abstract  void printSpacesColumn(double elem,int col);

    protected abstract  void printElem(double elem, int line, int col);
    
    protected abstract  void printElem(double elem);
    
    protected abstract  void printElem(double elem,int precision);

    protected abstract  void printBasisElem(boolean basis);

    protected abstract  void printArray(double [] array);
    
    protected abstract  void printArray(double [] array, int line);
    
    protected abstract  void printBasisArray(boolean [] array);
    
    protected abstract  void printHorizontalLine(int[] minCols);
    
    protected abstract  void printSeparator();

    protected abstract  void setNumDecimals(int i);

    public abstract  void setPivotalRow(int index);
    public abstract  void setPivotalColumn(int index);

}
