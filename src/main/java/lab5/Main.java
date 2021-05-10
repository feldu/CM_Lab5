package lab5;

import lab5.function.Functions;
import lab5.io.ConsoleReader;
import lab5.io.ConsoleWriter;
import lab5.io.Reader;
import lab5.io.Writer;
import lab5.method.InterpolationMethod;
import lab5.method.LagrangePolynomialMethod;
import lab5.method.NewtonPolynomialMethod;
import lab5.table.Table;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.InputMismatchException;


@Slf4j
public class Main {
    private static final String commandFormat = "CM_Lab5 -nl \n" +
            "-n -- Newton's method" +
            "-l -- Lagrange's method";
    private static final int POINTS_CNT = 10;
    private static Reader in;
    private static Writer out;
    private static InterpolationMethod method;
    private static Functions function;

    public static void main(String[] args) {
        configure(args);
        try {
            Table table =  getValuesTable();
            double x = in.readDoubleWithMessage("Введите значение аргумента интерполяции: ");
            double functionValue = method.solve(table, x);
            out.printInfo("Приближённое значение функции, при x=" + x + ": " + functionValue);
//            table.getMap().forEach((x, y) -> System.out.println("x " + x + ", y " + y));
        } catch (InputMismatchException e) {
            log.error("Incorrect input type");
            out.printError("Введённые данные некоректны");
        } catch (NumberFormatException e) {
            log.error("Incorrect input type");
            out.printError("Введённые данные некоректны");
            out.printError(e.getMessage());
        } catch (Exception e) {
            out.printError(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Table readTable() {
        int n = in.readIntWithMessage("Введите количество точек:");
        if (n < 12) {
            log.error("Less than 12 points");
        }
        out.printInfo("Вводите точки таблицы в формате: x(i) y(i)");
        return in.readTable(n);
    }

    @SneakyThrows
    private static void configure(String[] args) {
        in = new ConsoleReader();
        out = new ConsoleWriter();
        if (args.length > 1)
            throw new RuntimeException("Неверное количество аргументов\n" + commandFormat);
        if (args[0].equals("-l")) method = new LagrangePolynomialMethod();
        else if (args[0].equals("-n")) method = new NewtonPolynomialMethod();
        else throw new RuntimeException("Неверное формат команды\n" + commandFormat);

    }

    private static Table getValuesTable() {
        Table table;
        StringBuilder message = new StringBuilder("Выберите функцию:\n");
        for (int i = 0; i < Functions.values().length; i++) {
            message.append(i + 1).append("). ").append(Functions.values()[i].getTextView()).append("\n");
        }
        //fuck \n after last line
        message.append(Functions.values().length + 1).append("). ").append("Ввести таблицу вручную");
        try {
            int selectedValue = in.readIntWithMessage(message.toString());
            if (selectedValue > Functions.values().length) table = readTable();
            else {
                function = Functions.values()[selectedValue - 1];
                log.info("Chosen function is: {}", function.getTextView());
                chooseLimits();
                table = function.convertFunctionToTable(POINTS_CNT);
            }
            return table;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Номер функции вне диапазона");
        }
    }

    private static void chooseLimits() {
        function.setLimits(in.readDoubleWithMessage("Введите левую границу: "), in.readDoubleWithMessage("Введите правую границу: "));
        log.info("Chosen interval is [{}; {}]", function.getLeft(), function.getRight());
    }
}
