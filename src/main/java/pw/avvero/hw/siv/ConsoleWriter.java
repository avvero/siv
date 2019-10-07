package pw.avvero.hw.siv;

public class ConsoleWriter {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;92m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String CLEAR_LINE = "                                                                                  \r";
    private String bottomLine = "";

    public void newLine(String s){
        System.out.print(CLEAR_LINE);
        System.out.println(s);
        System.out.print(bottomLine + "\r");
    }

    public void newLineGreen(String s) {
        newLine(GREEN + s + RESET);
    }
    public void newLineGreenBold(String s) {
        newLine(GREEN_BOLD + s + RESET);
    }
    public void newLineBlue(String s) {
        newLine(BLUE + s + RESET);
    }
    public void newLineBlueBold(String s) {
        newLine(BLUE_BOLD + s + RESET);
    }

    public void bottomLine(String s) {
        bottomLine = s;
        System.out.print(CLEAR_LINE);
        System.out.print(bottomLine + "\r");
    }

    public void endLine(String end) {
        System.out.print(CLEAR_LINE);
        System.out.println(end);
    }
}
