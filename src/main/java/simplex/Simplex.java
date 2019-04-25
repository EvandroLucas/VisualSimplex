package simplex;


import print.CustomPrinter;

public abstract class Simplex {

    private int[] c;
    private int z;
    private int[] b;
    private int[] certOtim;
    private int[] certIlim;
    private int[] certInv;
    private int[][] A;
    private int[][] Reg;

    private int pivotRowIndex;
    private int pivotColumnIndex;

    public Simplex (int[][] tableauInput){

        CustomPrinter printer = new CustomPrinter();
        int numVar = tableauInput[0].length -1;
        int numCons = tableauInput.length -1;

        c = new int[numVar];
        z = c[c.length-1];
        b = new int[numCons];
        certOtim = new int[numVar];
        certIlim = new int[numVar];
        certInv  = new int[numVar];

        A  = new int[numCons][numVar];

        //monta o tableau
        for (int i = 0; i < tableauInput.length; i++){
            for(int j = 0; j < tableauInput[i].length;j++){
                if(i == 0 && j < numVar){
                    c[j] = tableauInput[i][j];
                }
                if(i > 0 && j < numVar ){
                    A[i-1][j] = tableauInput[i][j];
                }
                if(i < numCons && j==numVar){
                    b[i] = tableauInput[i+1][j];
                }
            }
        }

        printer.printTableau(A,c,b,z);

    }

    public abstract void run();


}
