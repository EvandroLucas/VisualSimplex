package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;
import print.ColorPrint;

import java.util.ArrayList;

public class AuxSimplex extends Simplex{

    private Tableau tableauAux;

    public AuxSimplex(Value[][] tableauInput) {
        super(tableauInput);
        for (Value value : tableau.c) {
            value.assign(value.mult(-1));
        }
    }

    public AuxSimplex(Tableau tableauInput) {
        super(tableauInput);
        for (Value value : tableau.c) {
            value.assign(value.mult(-1));
        }
    }


    @Override
    public void run() {

        logger.println("info","Running aux simplex");

        logger.println("debug","Fixing negatives in b");
        //fixReceivedTableauSigns(tableau);
        // Found here:
        /*https://www.math.ucla.edu/~tom/LP.pdf  (page 24)
        * and here:
        *
        * https://math.stackexchange.com/questions/1137363/simplex-method-with-negative-r-h-s
        *
        * */
        isInfeasible = ! pivotOnNegative(tableau);
        if(isInfeasible){
            logger.println("severe","Simplex is infeasible");
            tableauAux = createAuxTableau(this.tableau);
            printInfStatus(tableauAux);
            tableau.certInf = tableauAux.certInf;
            return;
        }

        printer.setDesc("Aux simplex to solve");
        printThisTableau(tableau);


        logger.println("debug","Creating auxiliar tableau");
        //cria um tableau auxiliar e o pivoteia
        tableauAux = createAuxTableau(this.tableau);


        //System.out.println("And stopping");
        //System.exit(1);

        logger.println("debug","Solving the aux using the primal");
        //passa-se o tableau pivoteado para o simplex
        PrimalSimplex auxPrimal = new PrimalSimplex(tableauAux,false);
        if(hide)
            auxPrimal.hideOutput();
        auxPrimal.run();
        auxPrimal.printer.setDesc("Solved Aux");
        auxPrimal.printThisTableau();

        //se o tableau auxiliar tiver um valor objetivo negativo,
        //a PL original eh inviavel
        if(tableauAux.z.isNegative()){
            isInfeasible = true;
        }
        //se o tableau auxiliar tiver uma variável não-basica na base, já era
        //a PL original eh inviavel
        for(int i =tableauAux.numVar; i < tableauAux.c.length;i++){
            if(tableauAux.basis[i]) {
                //isInfeasible = true;
                break;
            }
        }

        if(isInfeasible){
            logger.println("severe","Simplex is infeasible");
            printInfStatus(tableauAux);
            tableau.certInf = tableauAux.certInf;
            return;
        }

        logger.println("debug","Pivoting found basis");
        pivotAuxBasis();

        printer.setDesc("Original lpp with aux-pivoted-basis");
        printThisTableau(tableau);

        logger.println("debug","Running primal simplex after founding valid basis");

        tableau.pivotColumnIndex = -2;
        tableau.pivotRowIndex = -2;
        PrimalSimplex finalPrimal = new PrimalSimplex(tableau,false);
        if(hide)
            finalPrimal.hideOutput();
        finalPrimal.run();

        tableau.round();
        printer.setDesc("Final result: ");
        printThisTableau(tableau);
        isInfeasible = finalPrimal.isInfeasible;
        isUnbounded = finalPrimal.isUnbounded;
        isOptimal = finalPrimal.isOptimal;

    }

    @Override
    protected void updatePivotRow() {

    }

    @Override
    protected void updatePivotColumn() {

    }

    private void fixReceivedTableauSigns(Tableau tableau){
        for(int i = 0; i < tableau.b.length; i++){
            if(tableau.b[i].isNegative()){
                tableau.b[i].assign(tableau.b[i].mult(-1));
                for(int j = 0; j < tableau.A[i].length; j++){
                    tableau.A[i][j].assign(tableau.A[i][j].mult(-1));
                    if(tableau.basis[j]){
                        if(!tableau.A[i][j].isZero()){
                            tableau.basis[j] = false;
                        }
                    }
                }
            }
        }
    }
    private boolean pivotOnNegative(Tableau tableau){
        boolean foundNegativeB = true;
        while (foundNegativeB){
            foundNegativeB = false;
            for(int i = 0; i < tableau.b.length; i++) {
                if (tableau.b[i].isNegative()) {
                    ArrayList<Integer> marked = new ArrayList<>();
                    foundNegativeB = true;
                    logger.println("info","Found negative b entry on b["+i+"] : "  + tableau.b[i]);
                    boolean foundNegativeOnRow = false;
                    for (int j = 0; j < tableau.A[i].length; j++) {
                        if (tableau.A[i][j].isNegative()) {
                            marked.add(j);
                            foundNegativeOnRow = true;
                        }
                    }
                    for (Integer mark : marked){
                        int pivotColumn = mark;
                        int pivotRow = findPivotRow(pivotColumn,tableau);
                        tableau.pivotColumnIndex = pivotColumn;
                        tableau.pivotRowIndex = pivotRow;
                        pivot(tableau,true);
                    }
                    if (!foundNegativeOnRow) {
                        logger.println("info","Infeasible: no negative found on row with negative b ");
                        return false;
                    }
                    break;
                }
                else {
                    logger.println("info","Found positive b entry on b["+i+"] : "  + tableau.b[i]);
                }
            }
        }
        return true;
    }

    protected int findPivotRow(int problematicColumnIndex, Tableau tableau){
        logger.println("debug","Finding pivot row");
        int pivotRowIndex = 0;
        boolean first = false;
        boolean foundPositive = false;
        Fraction ratio = new Fraction(0,1,0);
        Fraction ratioMin = new Fraction(0,1,0);;

        for(int i=0;i<tableau.A.length;i++){
            ratio.num.assign(tableau.b[i]);
            ratio.den.assign(tableau.A[i][problematicColumnIndex]);
            logger.println("debug","Lendo num = " + ratio.num.doubleValue() + ", den = " + ratio.den.doubleValue());

            if((ratio.num.isZero()) && (ratio.den.isPositive())){ //regra de bland
                logger.println("debug","   - Achou por regra de bland");
                pivotRowIndex = i;
            }
            if( ((ratio.den.isPositive()) && (ratio.num.isPositive())) || ((ratio.den.isNegative()) && (ratio.num.isNegative()))){ //se o denominador e o numerador forem positivos, testamos
                ratio.val = ratio.num.div(ratio.den);
                ratio.val.round();
                logger.println("debug" , ", valor = " + ratio.val.doubleValue() );
                //como convencao, a primeira ratio valida sera a minima
                //para o resto, a menor ratio positiva eh o que queremos
                if((ratio.val.isSmallerThan(ratioMin.val)) || (!first)){
                    logger.println("debug" , "- Achou minima ratio positiva: " +
                            "num = "+ratio.num.doubleValue() +
                            ", den= "+ ratio.den.doubleValue() +
                            ", valor = "+ ratio.val.doubleValue());
                    pivotRowIndex = i;
                    ratioMin.val.assign(ratio.val);
                    ratioMin.val.round();
                    ratioMin.num.assign(ratio.num);
                    ratioMin.den .assign(ratio.den);
                    if(!first) first = true;
                }
            }
        }
        logger.println("debug","Pivot row found : " + pivotRowIndex);
        printer.setPivotalRow(pivotRowIndex);
        return pivotRowIndex;
    }


    private Tableau createAuxTableau(Tableau tableau){

        //aloca o tableua auxiliar
        //linhas: linhas de A + c
        //colunas: colunas de A + numero de restricoes(para a nova identidade)

        tableauAux = new Tableau(tableau,true,true);

        printer.setDesc("Auxiliar aux tableau: ");
        tableauAux.pivotRowIndex = -2;
        tableauAux.pivotColumnIndex = -2;
        printThisTableau(tableauAux);

        logger.println("info", "Pivoting aux tableau");

        int pr = 0;
        for(int j=0;j<tableauAux.A[0].length;j++){
            if(tableauAux.c[j].isEqualTo(1)){
                for(int i=1; i<tableau.A.length; i++){
                    if(tableauAux.A[i][j].isEqualTo(1)){
                        pr = i;
                    }
                }
                tableauAux.pivotRowIndex = pr;
                tableauAux.pivotColumnIndex = j;
                pivot(tableauAux);
            }
        }
        return tableauAux;
    }



    private void pivotAuxBasis(){

        logger.println("debug","Pivoting aux basis");

        System.arraycopy(tableauAux.basis, 0, tableau.basis, 0, tableau.basis.length);

        boolean foundPivot = false;
        int saveLine = 0;
        for(int j=0; j < tableau.A[0].length;j++){
            if(tableau.basis[j]){
                foundPivot = false;
                tableau.pivotColumnIndex = j;
                for(int i = 0; i < tableau.A.length; i++){
                    if(tableauAux.A[i][j].isEqualTo(1)){
                        saveLine = i;
                        if(!tableau.A[i][j].isZero()){
                            tableau.pivotRowIndex = i;
                            foundPivot = true;
                        }
                    }
                }
                if(!foundPivot){
                    for(int i=1;i<tableau.A.length;i++){
                        if(!tableau.A[i][j].isZero()){
                            for(int k=0;k<tableau.A[0].length;k++){
                                tableau.A[saveLine][k].assign( tableau.A[saveLine][k].add(tableau.A[i][k]));
                            }
                            break;
                        }
                    }
                }

                pivot(tableau,true);
            }
        }

        
    }



}
