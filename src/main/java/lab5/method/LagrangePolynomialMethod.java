package lab5.method;

import lab5.table.Table;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LagrangePolynomialMethod implements InterpolationMethod {
    /*
0,1 1,25
0,2 2,38
0,3 3,79
0,4 5,44
0,5 7,14

0,35
     */

    @Override
    public double solve(Table table, double interpolationX) {
        int size = table.getMap().size();
        List<Double> l = new ArrayList<>();
        List<Double> xData = table.getXData();
        for (int i = 0; i < size; i++) {
            double currentX = xData.get(i);
            double numerator = xData.stream().filter(x -> x != currentX).mapToDouble(x -> (interpolationX - x)).reduce(1, (a, b) -> a * b);
            double denominator = xData.stream().filter(x -> x != currentX).mapToDouble(x -> currentX - x).reduce(1, (a, b) -> a * b);
            double yi = table.getMap().get(currentX);
            l.add(yi * numerator / denominator);
            log.info("y{} * l{}({})={}", i, i, interpolationX, l.get(i));
        }
        double Ln = l.stream().mapToDouble(Double::doubleValue).sum();
        log.info("L{}({})={}", size - 1, interpolationX, Ln);
        return Ln;
    }
}
