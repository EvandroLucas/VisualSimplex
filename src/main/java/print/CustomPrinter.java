package print;

import numbers.Value;

public class CustomPrinter {

    private String desc = "";
    private String state = "";

    private int[] minCols;

    private int numDecimals = 1;
    private int numSeparators = 0;

    private int pivotalColumn = -2;
    private int pivotalLine = -2;
    private int currentLine = 0;
    private int currentCol = 0;

    //adding a self-destructed description
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        String stateToReturn = this.state;
        this.state = "";
        return stateToReturn;
    }

    // Para imprimir no terminal, inteiros estão de bom tamanho
    // Simplesmente copiamos e passamos para inteiros
    public void printTableau(Value[][] A, Value[] c, Value[] b, Value z, boolean[] basis){

        int[][] newA = new int[A.length][A[0].length];
        int[] newc = new int[c.length];
        int[] newb = new int[b.length];
        int newz = z.intValue();

        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[i].length; j++){
                newA[i][j] = A[i][j].intValue();
            }
        }
        for(int i = 0; i < c.length; i++){
            newc[i] = c[i].intValue();
        }
        for(int i = 0; i < b.length; i++){
            newb[i] = b[i].intValue();
        }

        printTableau(newA,newc,newb,newz,basis);
    }

    public void printTableau(int[][] A, int[] c, int[] b, int z, boolean[] basis){
        System.out.println("===========================================================================");

        if(!desc.equals("")) System.out.println("Desc: " + getDesc());
        if(!state.equals("")) System.out.println("State: " + getState());

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

        printArray(c,-1);
        printSeparator();
        printElem(z, -1, A[0].length);
        printHorizontalLine(minCols);
        for (int i = 0; i < A.length; i++){
            printArray(A[i],i);
            printSeparator();
            printElem(b[i], i, A[0].length);
            System.out.println();
        }
        printBasisArray(basis);
        System.out.println("===========================================================================");
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

    private void printElem(double elem, int line, int col){
        currentLine = line;
        currentCol = col;
        printElem(elem,this.numDecimals);
    }
    private void printElem(double elem){
        printElem(elem,this.numDecimals);
    }
    private void printElem(double elem,int precision){
        printSpacesColumn(elem,currentCol);
        String format;
        String toPrint;
            if(elem >= 0) {
                format = " %."+ precision +"f";
            }
            else {
                format = "%." + precision + "f";
            }
            //System.out.printf(format, elem);
            toPrint = String.format(format,elem);
            if((currentCol == pivotalColumn) && (currentLine != pivotalLine))
                ColorPrint.printGreenBack(toPrint);
            if((currentLine == pivotalLine) && (currentCol != pivotalColumn))
                ColorPrint.printGreenBack(toPrint);
            if((currentLine == pivotalLine) && (currentCol == pivotalColumn))
                ColorPrint.printBlueBack(toPrint);
            if((currentLine != pivotalLine) && (currentCol != pivotalColumn))
                ColorPrint.printBlack(toPrint);


    }

    private void printBasisElem(boolean basis){
        printSpacesColumn(0.0,currentCol);
        String format = " %."+ this.numDecimals +"f";
        String sizeStr;
        sizeStr = String.format(format,0.0);
        String toPrint;
        if(basis){
            toPrint = "^";
        }
        else {
            toPrint = " ";
        }
        for(int i = 0; i< sizeStr.length()-1; i++){
            System.out.print(" ");
        }
        ColorPrint.printBlue(toPrint);
    }

    private void printArray(int [] array){
        printArray(array,-1);
    }
    private void printArray(int [] array, int line){
        currentLine = line;
        for(int j = 0; j < array.length;j++){
            currentCol = j;
            printElem(array[j]);
        }
    }
    private void printBasisArray(boolean [] array){
        for(int j = 0; j < array.length;j++){
            currentCol = j;
            printBasisElem(array[j]);
        }
        System.out.println();
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

    public void setPivotalRow(int index){
        pivotalLine = index;
    }
    public void setPivotalColumn(int index){
        pivotalColumn = index;
    }


}
