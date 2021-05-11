package lab5;

import lab5.function.Functions;
import lab5.io.ConsoleReader;
import lab5.io.ConsoleWriter;
import lab5.io.Reader;
import lab5.io.Writer;
import lab5.method.InterpolationMethod;
import lab5.method.LagrangePolynomialMethod;
import lab5.method.NewtonPolynomialMethod;
import lab5.plot.Plot;
import lab5.plot.Series;
import lab5.table.Table;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.function.Function;


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
            Table table = getValuesTable();
            double interpolationX = in.readDoubleWithMessage("Введите значение аргумента интерполяции: ");
            double functionValue = method.solve(table, interpolationX);
            drawPlot(table, interpolationX, functionValue);
            out.printInfo("Приближённое значение функции, при x=" + interpolationX + ": " + functionValue);

        } catch (InputMismatchException e) {
            log.error("Incorrect input type");
            out.printError("Введённые данные некоректны");
        } catch (NumberFormatException e) {
            log.error("Incorrect input type");
            out.printError("Введённые данные некоректны");
            out.printError(e.getMessage());
        } catch (Exception e) {
            out.printError(e.getMessage());
        }
    }

    private static void drawPlot(Table table, double interpolationX, double interpolationY) {
        Series inputSeries, interpolationNodes = null;
        if (function == null) {
            inputSeries = new Series("Таблица");
            inputSeries.setXData(table.getXData());
            inputSeries.setYData(table.getYData());
            inputSeries.setHideLines(true);

        } else {
            inputSeries = new Series(function.getTextView(), function.getFunction(), function.getLeft(), function.getRight());
            inputSeries.setHidePoints(true);
            interpolationNodes = new Series("Узлы интерполяции");
            interpolationNodes.setXData(table.getXData());
            interpolationNodes.setYData(table.getYData());
            interpolationNodes.setHideLines(true);
        }
        Function<Double, Double> interpolatedFunction = x -> method.solve(table, x);
        Series interpolatedSeries = new Series("Интерполяционная функция", interpolatedFunction, table.getLeftBorder(), table.getRightBorder());
        interpolatedSeries.setHidePoints(true);
        Series answer = new Series("Ответ");
        answer.setXData(Collections.singletonList(interpolationX));
        answer.setYData(Collections.singletonList(interpolationY));
        answer.setHideLines(true);
        Plot plot = new Plot("Интерполяция", interpolatedSeries, inputSeries, answer);
        if (interpolationNodes != null) plot.addSeries(interpolationNodes);
        plot.save("Интерполяция");
    }

    private static Table readTable() {
        int n = in.readIntWithMessage("Введите количество точек:");
        out.printInfo("Вводите точки таблицы в формате: x(i) y(i)");
        Table table = in.readTable(n);
        if (table.getMap().size() != n) throw new RuntimeException("Введены точки с одинаковым значением X.");
        return table;
    }

    @SneakyThrows
    private static void configure(String[] args) {
        in = new ConsoleReader();
        out = new ConsoleWriter();
        if (args.length != 1)
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
            if (selectedValue > Functions.values().length) {
                table = readTable();
                function = null;
            } else {
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
