package print;


public class ColorPrint {


    public static final String ANSI_RESET   = "\u001B[0m";
    public static final String ANSI_BLACK   = "\u001B[30m";
    public static final String ANSI_RED     = "\u001B[31m";
    public static final String ANSI_GREEN   = "\u001B[32m";
    public static final String ANSI_YELLOW  = "\u001B[33m";
    public static final String ANSI_BLUE    = "\u001B[34m";
    public static final String ANSI_PURPLE  = "\u001B[35m";
    public static final String ANSI_CYAN    = "\u001B[36m";
    public static final String ANSI_WHITE   = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND  = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND    = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND  = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND   = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND   = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND  = "\u001B[47m";


    public static void main(String[] args) {

        System.out.println(ANSI_RED    + " Teste " + ANSI_RESET);
        System.out.println(ANSI_YELLOW + " Teste " + ANSI_RESET);
        System.out.println(ANSI_GREEN  + " Teste " + ANSI_RESET);
        System.out.println(ANSI_CYAN   + " Teste " + ANSI_RESET);
        System.out.println(ANSI_BLUE   + " Teste " + ANSI_RESET);
        System.out.println(ANSI_PURPLE + " Teste " + ANSI_RESET);

        System.out.println();

        System.out.println(ANSI_RED_BACKGROUND    + "                       " + ANSI_RESET);
        System.out.println(ANSI_YELLOW_BACKGROUND + "                       " + ANSI_RESET);
        System.out.println(ANSI_GREEN_BACKGROUND  + "                       " + ANSI_RESET);
        System.out.println(ANSI_CYAN_BACKGROUND   + "                       " + ANSI_RESET);
        System.out.println(ANSI_BLUE_BACKGROUND   + "                       " + ANSI_RESET);
        System.out.println(ANSI_PURPLE_BACKGROUND + "                       " + ANSI_RESET);

    }


    public static void printRed(String str){
        System.out.println(ANSI_RED + str + ANSI_RESET);
    }
    public static void printYellow(String str){
        System.out.println(ANSI_YELLOW + str + ANSI_RESET);
    }
    public static void printGreen(String str){
        System.out.println(ANSI_GREEN + str + ANSI_RESET);
    }
    public static void printCyan(String str){
        System.out.println(ANSI_CYAN + str + ANSI_RESET);
    }
    public static void printBlue(String str){
        System.out.println(ANSI_BLUE + str + ANSI_RESET);
    }
    public static void printPurple(String str){
        System.out.println(ANSI_PURPLE + str + ANSI_RESET);
    }

    public static void printRedBack(String str){
        System.out.println(ANSI_RED_BACKGROUND + str + ANSI_RESET);
    }
    public static void printYellowBack(String str){
        System.out.println(ANSI_YELLOW_BACKGROUND + str + ANSI_RESET);
    }
    public static void printGreenBack(String str){
        System.out.println(ANSI_GREEN_BACKGROUND + str + ANSI_RESET);
    }
    public static void printCyanBack(String str){
        System.out.println(ANSI_CYAN_BACKGROUND + str + ANSI_RESET);
    }
    public static void printBlueBack(String str){
        System.out.println(ANSI_BLUE_BACKGROUND + str + ANSI_RESET);
    }
    public static void printPurpleBack(String str){
        System.out.println(ANSI_PURPLE_BACKGROUND + str + ANSI_RESET);
    }
    public static void printWhiteBack(String str){
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + str + ANSI_RESET);
    }
    public static void printBlackBack(String str){
        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_WHITE + str + ANSI_RESET);
    }



}
