package print;

import numbers.Value;
import simplex.Tableau;

public class VoidPrinter extends Printer{


    //adding a self-destructed description
    public void setDesc(String desc){
    }
    public String getDesc(){
        return "";
    }
    public void setState(String state){

    }
    public String getState(){
        return "";
    }

    public void printTableau(Tableau tableau){

    }
    // Para imprimir no terminal, inteiros estão de bom tamanho
    // Simplesmente copiamos e passamos para inteiros
    public void printTableau(Value[][] A, Value[] c, Value[] b, Value z, boolean[] basis){

    }

    public void printTableau(double[][] A, double[] c, double[] b, double z, boolean[] basis){

    }

    public void printCert(Value[] cert){

    }

    public void printCert(double[] cert){

    }

    @Override
    protected int smallestSpacing(double[] array) {
        return 0;
    }

    @Override
    protected int spacing(int num) {
        return 0;
    }

    @Override
    protected int spacing(double num) {
        return 0;
    }

    @Override
    protected void printSpacesColumn(double elem, int col) {

    }

    @Override
    protected void printElem(double elem, int line, int col) {

    }

    @Override
    protected void printElem(double elem) {

    }

    @Override
    protected void printElem(double elem, int precision) {

    }

    @Override
    protected void printBasisElem(boolean basis) {

    }

    @Override
    protected void printArray(double[] array) {

    }

    @Override
    protected void printArray(double[] array, int line) {

    }

    @Override
    protected void printBasisArray(boolean[] array) {

    }

    @Override
    protected void printHorizontalLine(int[] minCols) {

    }

    @Override
    protected void printSeparator() {

    }

    @Override
    protected void setNumDecimals(int i) {

    }

    public void setPivotalRow(int index){

    }
    public void setPivotalColumn(int index){

    }


}
