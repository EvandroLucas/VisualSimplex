package simplex;

import logging.Logger;
import numbers.Value;

public class AuxSimplex extends Simplex{

    PrimalSimplex auxPrimal;

    public AuxSimplex(Value[][] tableauInput) {
        super(tableauInput);
    }

    @Override
    public void run() {
        Logger.println("info","Running aux simplex");

        int i = 0;
        int j = 0;
        int result = 0;

        //aloca o tableua auxiliar
        boolean[] auxBasis = new boolean[basis.length];
        Value[][] tableauAux = new Value[A.length][A.length+constraintNum];

        Logger.println("debug","Creating auxiliar tableau");
        //cria um tableau auxiliar e o pivoteia
        createAuxTableau(tableauAux,auxBasis);
        Logger.println("debug","Creating primal to solve the aux");
        //passa-se o tableau pivoteado para o simplex
        auxPrimal = new PrimalSimplex(tableauAux);
        auxPrimal.basis = auxBasis.clone();
        auxPrimal.run();
        auxPrimal.printer.setDesc("Solved Aux");
        auxPrimal.printThisTableau();

        //se o tableau auxiliar tiver um valor objetivo negativo,
        //a PL original eh inviavel
        if(auxPrimal.getZ().isNegative()){
            Logger.println("severe","Simplex inviavel");
            isInfeasible = true;
            return;
        }

        Logger.println("debug","Pivoting found basis");
        pivotAuxBasis();

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

    protected void createAuxTableau(Value[][] tableauAux, boolean[] auxBasis){
        auxBasis = new boolean[basis.length];
        //aloca o tableua auxiliar
        tableauAux = new Value[A.length][A.length+constraintNum];
        for(int i=0; i< tableauAux.length; i++){
            for(int j=0; j< tableauAux[i].length; j++){
                tableauAux[i][j] = new Value(0);
            }
        }

        int lp = 0;

        //preenche vetor c
        //zero em tudo, 1 acima da identidade
        for(int i=0;i<=0;i++){
            //acima de A
            for(j=0;j<n;j++){
                tableauAux[i][j] = 0;
            }
            //acima da "identidade" original
            for(j=n;j<b;j++){
                tableauAux[i][j] = 0;
            }
            //acima da identidade auxiliar
            for(j=b;j<b+m;j++){
                tableauAux[i][j] = 1;
            }
            for(j=b+m;j<=b+m;j++){
                tableauAux[i][j] = 0;
            }
        }
        //preenche tableau auxiliar
        for(int i=1;i<=m;i++){
            //multiplica por -1 uma linha onde b eh negativo
            //os dois loops estao ai para caso seja necessaria
            //alguma modificacao futura
            for(int j=0;j<b;j++){
                if((tableau[i][b] >= 0) ){
                    tableauAux[i][j] = tableau[i][j];
                }
                else{
                    if(tableau[i][j] != 0){
                        tableauAux[i][j] = -1*tableau[i][j];
                        tableau[i][j] = -1*tableau[i][j];
                    }
                    else{
                        tableauAux[i][j] = 0;
                        tableau[i][j] = 0;
                    }
                }
                //se a linha é negativada, perdemos uma base
                base[j] = false;
            }
            for(int j=b;j<=b;j++){
                if((tableau[i][b] >= 0) ){
                    tableauAux[i][j+m] = tableau[i][j];
                }
                else{
                    if(tableau[i][j] != 0){
                        tableauAux[i][j+m] = -1*tableau[i][j];
                        tableau[i][j] = -1*tableau[i][j];
                    }
                    else{
                        tableauAux[i][j+m] = 0;
                        tableau[i][j] = 0;
                    }
                }
            }

        }
        //preenche a diagonal das variaveis auxiliares
        for(i=1;i<=m;i++){
            for(j=b;j<b+m;j++){
                if(j-b+1==i){
                    tableauAux[i][j] = 1;
                    base[j] = true;
                }
                else{
                    tableauAux[i][j] = 0;
                }
            }
        }
        fprintf(registra,"\nSegue o tableau original com sinais corrigidos");
        imprime_tableau_arquivo(m,n,tableau,-1,-1,base,registra);
        fprintf(registra,"\nSegue o tableau auxiliar");
        imprime_tableau_arquivo(m,n+m,tableauAux,-1,-1,base,registra);
        fprintf(registra,"\nAgora pivotearemos o tableau auxiliar");
        for(j=0;j<b+m;j++){
            if(tableauAux[0][j] == 1){
                for(i=1;i<=m;i++){
                    if(tableauAux[i][j] == 1){
                        lp = i;
                    }
                }
                fprintf(registra,"\nVai pivotear: linha %i, coluna %i",lp,j);
                imprime_tableau_arquivo(m,n+m,tableauAux,lp,j,base,registra);
                pivoteia(m,n+m,lp,j,tableauAux,base,registra);
                fprintf(registra,"\nPivoteado:");
                imprime_tableau_arquivo(m,n+m,tableauAux,lp,j,base,registra);
            }
        }


        return;


    }


    private void pivotAuxBasis(){
        int i = 0;
        int l = 0;
        int salvaLinha = 0;
        boolean achouPivotValido = true;

        Logger.println("debug","Pivoting aux basis");

        for(int j=0; j < auxPrimal.A.length;j++){
            achouPivotValido = false;
            if(auxPrimal.basis[j]){
                for(i=0;i<auxPrimal.A.length; i++){
                    if(auxPrimal.A[i][j].isEqualTo(1)){
                        salvaLinha = i;
                        if(!auxPrimal.A[i][j].isZero()){
                            achouPivotValido = true;
                            printer.setPivotalColumn(i);
                            printer.setPivotalRow(j);
                            printer.setDesc("Before pivoting");
                            printer.printTableau(A,c,b,z,auxPrimal.basis);
                            pivot(i,j,A,auxPrimal.basis);
                            printer.setDesc("After pivoting");
                            printer.printTableau(A,c,b,z,auxPrimal.basis);
                        }
                        break;
                    }
                }
                if(!achouPivotValido){
                    //se nao achou um pivot valido,
                    //somamos essa linha por outra
                    i = salvaLinha;
                    for(int k=0;k<A.length;k++){
                        if(!A[k][j].isZero()){
                            for(l=0;l<A[0].length;l++){
                                A[salvaLinha][l].assign( A[salvaLinha][l].add(A[k][l]));
                            }
                            b[salvaLinha].assign(b[salvaLinha].add(b[k]));
                            break;
                        }
                    }
                    Logger.println("debug","Added one line to pivot");
                    pivot(salvaLinha,j,A,auxPrimal.basis);
                }
            }
        }


    }

}
