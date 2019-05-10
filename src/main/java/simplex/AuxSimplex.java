package simplex;

import logging.Logger;
import numbers.Value;

public class AuxSimplex extends Simplex{

    PrimalSimplex auxPrimal;
    Tableau tableauAux;

    public AuxSimplex(Value[][] tableauInput) {
        super(tableauInput);
    }


    @Override
    public void run() {

        Logger.println("info","Running aux simplex");


        int i = 0;
        int j = 0;
        int result = 0;

        //aloca o tableau auxiliar


        Logger.println("debug","Fixing negatives in b");
        fixReceivedTableauSigns(tableau);
        printer.setDesc("All positive b entryes (lines multiplied by -1)");
        printThisTableau(tableau);


        Logger.println("debug","Creating auxiliar tableau");
        //cria um tableau auxiliar e o pivoteia
        tableauAux = createAuxTableau(this.tableau);


        //System.out.println("And stopping");
        //System.exit(1);

        Logger.println("debug","Solving the aux using the primal");
        //passa-se o tableau pivoteado para o simplex
        auxPrimal = new PrimalSimplex(tableauAux,false);
        auxPrimal.run();
        auxPrimal.printer.setDesc("Solved Aux");
        auxPrimal.printThisTableau();



        //se o tableau auxiliar tiver um valor objetivo negativo,
        //a PL original eh inviavel
        if(tableauAux.z.isNegative()){
            Logger.println("severe","Simplex inviavel");
            isInfeasible = true;
            return;
        }

        Logger.println("debug","Pivoting found basis");
        //pivotAuxBasis();

        //fprintf(registra,"\nMatriz original depois de ter sua base pivoteada");

        //fprintf(registra,"\nPassaremos essa matriz original modificada ao simplex primal");
        //agora podemos resolver o simplex primal:
        //result = simplexPrimal();


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

    protected Tableau createAuxTableau(Tableau tableau){

        //aloca o tableua auxiliar
        //linhas: linhas de A + c
        //colunas: colunas de A + numero de restricoes(para a nova identidade)

        tableauAux = new Tableau(tableau,true,true);

        printer.setDesc("Before Pivoting: ");
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
       /* int i = 0;
        int l = 0;
        int salvaLinha = 0;
        boolean achouPivotValido = true;

        Logger.println("debug","Pivoting aux basis");

        for(int j=0; j < auxPrimal.tableau.A.length;j++){
            achouPivotValido = false;
            if(auxPrimal.tableau.basis[j]){
                for(i=0;i<auxPrimal.tableau.A.length; i++){
                    if(auxPrimal.tableau.A[i][j].isEqualTo(1)){
                        salvaLinha = i;
                        if(!auxPrimal.tableau.A[i][j].isZero()){
                            achouPivotValido = true;
                            printer.setPivotalColumn(i);
                            printer.setPivotalRow(j);
                            printer.setDesc("Before pivoting");
                            printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,auxPrimal.tableau.basis);
                            pivot(i,j,A,auxPrimal.tableau.basis);
                            printer.setDesc("After pivoting");
                            printer.printTableau(tableau.A,tableau.c,b,z,auxPrimal.basis);
                        }
                        break;
                    }
                }
                if(!achouPivotValido){
                    //se nao achou um pivot valido,
                    //somamos essa linha por outra
                    i = salvaLinha;
                    for(int k=0;k<tableau.A.length;k++){
                        if(!tableau.A[k][j].isZero()){
                            for(l=0;l<tableau.A[0].length;l++){
                                tableau.A[salvaLinha][l].assign( tableau.A[salvaLinha][l].add(tableau.A[k][l]));
                            }
                            tableau.b[salvaLinha].assign(tableau.b[salvaLinha].add(tableau.b[k]));
                            break;
                        }
                    }
                    Logger.println("debug","Added one line to pivot");
                    pivot(salvaLinha,j,tableau.A,auxPrimal.tableau.basis);
                }
            }
        }

        */
    }



}
