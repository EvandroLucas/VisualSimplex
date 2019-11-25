package print;

public class ArrayPrinter {

    public static String getPrint(Object[] array){
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for(int i = 0; i < array.length; i++){
            if(i != 0){
                stringBuilder.append(" , ");
            }
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

}
