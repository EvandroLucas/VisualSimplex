package numbers;

import logging.Logger;

public class Value{

    private Double value;

    public Value(Integer value){
        this.value = (double) value;
    }
    public Value(Double value){
        this.value = value;
    }
    public Value(String value){
        this(Double.parseDouble(value));
    }
    public Value(Value value){
        this.value = value.doubleValue();
    }
    public Value(){
        this(0);
    }

    public void assign (Integer value){
        assign(new Value(value));
    }

    public void assign (Double value){
        assign(new Value(value));
    }

    public void assign (Value value){
        this.value =  value.doubleValue();
        this.value = Double.parseDouble(String.format("%.14f", this.value).replaceAll(",","."));
        if((this.value < 0.000000009) && (this.value > -0.000000009)){
            this.value = 0.0000;
        }
        this.value = this.value + 0.0;
    }

    public void round(){
        this.value = Double.parseDouble(String.format("%.6f", this.value).replaceAll(",","."));

        if(this.value > 0) {
            if (Math.ceil(this.value) - this.value  < 0.000009) {
                this.value = Math.ceil(this.value);
            }
            if( this.value - Math.floor(this.value) < 0.000001){
                this.value = Math.floor(this.value);
            }
        }
        else {
            if ( -1*(Math.ceil(this.value) + this.value) < -0.000001) {
                this.value = Math.ceil(this.value);
            }
            if( -1*(this.value + Math.floor(this.value)) < -0.000001){
                this.value = Math.floor(this.value);
            }
        }
        if((this.value < 0.0009) && (this.value > -0.000009)){
            this.value = 0.0000;
        }
        this.value = this.value + 0.0;

    }

    public Value roundUp() {
        return(new Value(Math.ceil(this.value)));
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

    public Integer ceil() {
        return (int) Math.round(Math.ceil(this.value));
    }

    public Integer floor() {
        return (int) Math.round(Math.floor(this.value));
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

    public boolean isInteger(){
        Value v = new Value(this);
        v.round();
        Double d = v.doubleValue();
        return (d == Math.floor(d)) && !Double.isInfinite(d);
    }

    public boolean isSmallerThan(Value v){
        return (this.doubleValue() < v.doubleValue());
    }
    public boolean isSmallerEqualsThan(Value v){ return (this.doubleValue() <= v.doubleValue()); }
    public boolean isGreaterThan(Value v){
        return (this.doubleValue() > v.doubleValue());
    }
    public boolean isGreaterEqualsThan(Value v){
        return (this.doubleValue() >= v.doubleValue());
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
        Double valueToPrint = Double.parseDouble(String.format("%.4f", this.value).replaceAll(",","."));
        if(valueToPrint == (double) this.intValue())
            return "" + this.intValue();
        else
            return "" + valueToPrint;
    }
}
