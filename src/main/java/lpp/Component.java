package lpp;

import numbers.Value;
import print.ColorPrint;

public class Component extends Variable{

    private Value multiplier;

    public Component(String group, Integer index, Value multiplier) {
        super(group, index);
        this.multiplier = multiplier;
    }
    public Component(Variable var, Value multiplier) {
        super(var.getGroup(), var.getIndex());
        this.multiplier = multiplier;
    }
    public Component(Component cp) {
        super(cp);
        this.multiplier = new Value(cp.getMultiplier());
    }

    public Value getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Value multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        String sign = "+";
        if(getMultiplier().isNegative()){
            sign = "-";
        }
        stringBuilder.append(sign);

        stringBuilder.append(ColorPrint.cyan("("));
        if(getMultiplier().isNegative()){
            stringBuilder.append(ColorPrint.blue(getMultiplier().toString().replace("-","")));
        }
        else {
            stringBuilder.append(ColorPrint.blue(getMultiplier().toString()));
        }
        stringBuilder.append(ColorPrint.cyan(")")).append(ColorPrint.red(getName() + " "));
        return stringBuilder.toString();
    }
}
