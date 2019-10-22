package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;
import print.ColorPrint;

public class AuxSimplex extends Simplex{

    Tableau tableauAux;

    public AuxSimplex(Value[][] tableauInput) {
        super(tableauInput);
        for (Value value : tableau.c) {
            value.assign(value.mult(-1));
        }
    }


    @Override
    public void run() {

        Logger.println("info","Running aux simplex");

        Logger.println("debug","Fixing negatives in b");
        fixReceivedTableauSigns(tableau);
        printer.setDesc("All positive b entries (lines multiplied by -1)");
        printThisTableau(tableau);


        Logger.println("debug","Creating auxiliar tableau");
        //cria um tableau auxiliar e o pivoteia
        tableauAux = createAuxTableau(this.tableau);


        //System.out.println("And stopping");
        //System.exit(1);

        Logger.println("debug","Solving the aux using the primal");
        //passa-se o tableau pivoteado para o simplex
        PrimalSimplex auxPrimal = new PrimalSimplex(tableauAux,false);
        auxPrimal.run();
        auxPrimal.printer.setDesc("Solved Aux");
        auxPrimal.printThisTableau();

        //se o tableau auxiliar tiver um valor objetivo negativo,
        //a PL original eh inviavel
        if(tableauAux.z.isNegative()){
            Logger.println("severe","Simplex is infeasible");
            isInfeasible = true;
            printInfStatus(tableauAux);
            tableau.certInf = tableauAux.certInf;
            return;
        }

        Logger.println("debug","Pivoting found basis");
        pivotAuxBasis();

        printer.setDesc("Original lpp with aux-pivoted-basis");
        printThisTableau(tableau);

        Logger.println("debug","Running primal simplex after founding valid basis");

        tableau.pivotColumnIndex = -2;
        tableau.pivotRowIndex = -2;
        PrimalSimplex finalPrimal = new PrimalSimplex(tableau,false);
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

    private Tableau createAuxTableau(Tableau tableau){

        //aloca o tableua auxiliar
        //linhas: linhas de A + c
        //colunas: colunas de A + numero de restricoes(para a nova identidade)

        tableauAux = new Tableau(tableau,true,true);

        printer.setDesc("Auxiliar aux tableau: ");
        tableauAux.pivotRowIndex = -2;
        tableauAux.pivotColumnIndex = -2;
        printThisTableau(tableauAux);

        Logger.println("info", "Pivoting aux tableau");

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

        Logger.println("debug","Pivoting aux basis");

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
