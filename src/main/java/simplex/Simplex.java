package simplex;


import numbers.Value;
import print.CustomPrinter;

public abstract class Simplex {

    private Value[] c;
    private Value z;
    private Value[] b;
    private Value[] certOtim;
    private Value[] certIlim;
    private Value[] certInv;
    private Value[][] A;
    private Value[][] Reg;

    private int pivotRowIndex;
    private int pivotColumnIndex;

    public Simplex (Value[][] tableauInput){

        CustomPrinter printer = new CustomPrinter();
        int numVar = tableauInput[0].length -1;
        int numCons = tableauInput.length -1;

        c = new Value[numVar];
        b = new Value[numCons];
            for(int i =0; i < b.length; i++){
                b[i] = new Value(0);
            }
        certOtim = new Value[numVar];
        certIlim = new Value[numVar];
        certInv  = new Value[numVar];

        for(int i =0; i < numVar; i++){
            c[i] = new Value(0);
            certOtim[i] = new Value(0);
            certIlim[i] = new Value(0);
            certInv[i]  = new Value(0);
        }

        A  = new Value[numCons][numVar];
        for(int i =0; i < A.length; i++){
            for(int j =0; j < A[i].length; j++){
                A[i][j] = new Value(0);
            }
        }

        //monta o tableau
        for (int i = 0; i < tableauInput.length; i++){
            for(int j = 0; j < tableauInput[i].length;j++){
                if(i == 0 && j < numVar){
                    c[j].assign(tableauInput[i][j]);
                }
                if(i > 0 && j < numVar ){
                    A[i-1][j].assign(tableauInput[i][j]);
                }
                if(i < numCons && j==numVar){
                    b[i].assign(tableauInput[i+1][j]);
                }
            }
        }
        //TODO: trocar referencia
        z = new Value(0);
        printer.printTableau(A,c,b,z);

    }

    public abstract void run();


}
