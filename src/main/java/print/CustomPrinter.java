package print;

public class CustomPrinter {

    private int[] minCols;

    private int numDecimals = 1;
    private int numSeparators = 0;


    public void printTableau(int[][] A, int[] c, int[] b, int z){

        numSeparators++; //incrementa-se para cada matriz passada como parâmetro
        minCols = new int[A[0].length+1];
        int[] thisColumn = new int[A.length+1];

        for (int k = 0; k < minCols.length; k++){

            if(k <  A[0].length) thisColumn[0] = c[k];
            if(k == A[0].length) thisColumn[0] = z;

            for (int i = 0; i < A.length; i++) {
                if(k < A[0].length) {
                    thisColumn[i + 1] = A[i][k];
                }
                if(k == A[0].length) {
                    thisColumn[i + 1] = b[i];
                }
            }

            minCols[k] = smallestSpacing(thisColumn);
        }

        System.out.println("\nImprimindo Tableau:\n");
        printArray(c);
        printSeparator();
        printElem(z,A[0].length);
        printHorizontalLine(minCols);
        for (int i = 0; i < A.length; i++){
            printArray(A[i]);
            printSeparator();
            printElem(b[i],A[0].length);
            System.out.println();
        }

    }


    private int smallestSpacing(int[] array){
        int minSpacing = 2;
        boolean hasNegative = false;
        for(int i = 0; i < array.length;i++){
            if(spacing(array[i]) > minSpacing){
                minSpacing = spacing(array[i]);
            }
            if(array[i] < 0) hasNegative = true;
        }
        if(hasNegative)
            minSpacing = minSpacing + 1;
        return minSpacing;
    }

    private int spacing(int num){
        return spacing((double) num);
    }
    private int spacing(double num){
        double i = num;
        if (i == 0)
            return 1;
        if(i < 0)
            i = (-1)*i;

        return (int) Math.log10(i) + 1;
    }

    private void printSpacesColumn(double elem,int col){
        for(int i =0; i< minCols[col] - spacing(elem);i++){
            System.out.print(" ");
        }
    }
    private void printElem(double elem,int col){
        printElem(elem,col,this.numDecimals);
    }
    private void printElem(double elem,int col, int precision){
        printSpacesColumn(elem,col);
        String format;
            if(elem >= 0) {
                format = " %."+ precision +"f";
                System.out.printf(format, elem);
            }
            else {
                format = "%."+ precision +"f";
                System.out.printf(format, elem);
            }
    }

    private void printArray(int [] array){
        for(int j = 0; j < array.length;j++){
            printElem(array[j],j);
        }
    }
    private void printHorizontalLine(int[] minCols){
        System.out.print("\n");
        for(int i = 0; i < numSeparators; i++){
            System.out.print("——");
        }
        for(int i = 0; i < minCols.length; i++){
            if(numDecimals > 0) {
                for (int j = 0; j < minCols[i] + 2; j++) {
                    System.out.print("—");
                }
                for (int j = 0; j < numDecimals; j++) {
                    System.out.print("—");
                }
            }
            else{
                for (int j = 0; j < minCols[i] + 1; j++) {
                    System.out.print("—");
                }
            }
        }
        System.out.println();
    }
    private void printSeparator(){
        System.out.print(" │");
    }

    private void setNumDecimals(int i){
        numDecimals = i;
    }

}
