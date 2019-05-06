package numbers;

public class Fraction {

    public Value num;
    public Value den;
    public Value val;

    public Fraction(int num, int den, int val){
        this.num = new Value(num);
        this.den = new Value(den);
        this.val = new Value(val);
    }
    public Fraction(double num, double den, double val){
        this.num = new Value(num);
        this.den = new Value(den);
        this.val = new Value(val);
    }
    public Fraction(Value num, Value den, Value val){
        this.num = new Value(num);
        this.den = new Value(den);
        this.val = new Value(val);
    }
}
