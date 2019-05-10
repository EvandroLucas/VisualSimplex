package numbers;

import logging.Logger;

public class Value{

    private Double value;

    public Value(Integer value){
        this.value = new Double(value);
        //this.value = Double.parseDouble(String.format("%.7f", this.value));
    }
    public Value(Double value){
        this.value = value;
        //this.value = Double.parseDouble(String.format("%.7f", this.value));
    }
    public Value(Value value){
        this.value = value.doubleValue();
        //this.value = Double.parseDouble(String.format("%.7f", this.value));
    }

    public void assign (Integer value){
        assign(new Value(value));
    }

    public void assign (Double value){
        assign(new Value(value));
    }

    public void assign (Value value){
        this.value =  value.doubleValue();
        this.value = Double.parseDouble(String.format("%.7f", this.value));
    }

    public void round(){
        this.value = Double.parseDouble(String.format("%.5f", this.value));
    }

    public Value add(Integer value) {
        return add(new Value(value));
    }

    public Value add(Double value) {
        return add(new Value(value));
    }

    public Value add(Value value) {
        return new Value(this.value + value.doubleValue());
    }

    public Value sub(Integer value) {
        return sub(new Value(value));
    }

    public Value sub(Double value) {
        return sub(new Value(value));
    }

    public Value sub(Value value) {
        return new Value(this.value - value.doubleValue());
    }


    public Value mult(Integer value) {
        return mult(new Value(value));
    }

    public Value mult(Double value) {
        return mult(new Value(value));
    }

    public Value mult(Value value) {
        return new Value(this.value * value.doubleValue());
    }


    public Value div(Integer value) {
        return div(new Value(value));
    }

    public Value div(Double value) {
        return div(new Value(value));
    }

    public Value div(Value value) {
        if(value.isZero()){
            Logger.println("error","Division by zero!!!!");
            System.exit(1);
        }
        return new Value(this.value/value.doubleValue());
    }


    public Integer intValue() {
        return this.value.intValue();
    }


    public Double doubleValue() {
        return this.value;
    }


    public boolean isZero(){
        return (this.value == 0);
    }

    public boolean isPositive(){
        return (this.value > 0);
    }

    public boolean isNegative(){
        return (this.value < 0);
    }

    public boolean isSmallerThan(Value v){
        return (this.doubleValue() < v.doubleValue());
    }
    public boolean isGreaterThan(Value v){
        return (this.doubleValue() > v.doubleValue());
    }
    public boolean isEqualTo(Value v){
        return (this.doubleValue().equals(v.doubleValue()));
    }
    public boolean isEqualTo(Integer v){
        return isEqualTo(new Value(v));
    }
    public boolean isEqualTo(Double v){
        return isEqualTo(new Value(v));
    }
    public boolean isNotEqualTo(Value v){
        return (!this.doubleValue().equals(v.doubleValue()));
    }
    public boolean isNotEqualTo(Integer v){
        return isNotEqualTo(new Value(v));
    }
    public boolean isNotEqualTo(Double v){
        return isNotEqualTo(new Value(v));
    }

    @Override
    public String toString(){
        return "" + this.doubleValue();
    }
}
