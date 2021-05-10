package lab5.io;


public class ConsoleWriter implements Writer {
    @Override
    public void printInfo(String s) {
        System.out.println(s);
    }

    @Override
    public void printError(String s) {
        System.err.println(s);
    }

}
