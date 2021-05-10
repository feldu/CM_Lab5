package lab5.function;


import lab5.table.Table;
import lombok.Getter;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

@Getter
public enum Functions {
    f1(x -> 0.5 * x, "y = 0.5x"),
    f2(Math::sin, "y = sin(x)"),
    f3(x -> pow(x, 3) - x + 4, "y = x^3 - x + 4"),
    f4(x -> 2*sin(pow(x, 2)), "y = 2*sin(x^2)");
    private Function<Double, Double> function;
    private String textView;
    private double left;
    private double right;
    private final double POINTS_CNT = 10;

    Functions(Function<Double, Double> function, String textView) {
        this.function = function;
        this.textView = textView;


    }

    public void setLimits(double a, double b) {
        if (b > a) {
            this.left = a;
            this.right = b;
        } else throw new RuntimeException("Правая граница интервала должна быть больше левой");
    }

    public Table convertFunctionToTable(int size) {
        SortedMap<Double, Double> tableMap = new TreeMap<>();
        double inc = (right - left) / (size - 1);
        for (double i = left; i <= right; i += inc) {
            tableMap.put(i, function.apply(i));
        }
        return new Table(tableMap);
    }

}
