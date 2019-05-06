package numbers;

public class Value{

    private Double value;

    public Value(Integer value){
        this.value = new Double(value);
    }
    public Value(Double value){
        this.value = value;
    }
    public Value(Value value){
        this.value = value.doubleValue();
    }


    public void assign (Integer value){
        assign(new Value(value));
    }

    public void assign (Double value){
        assign(new Value(value));
    }

    public void assign (Value value){
        this.value =  value.doubleValue();
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
}
