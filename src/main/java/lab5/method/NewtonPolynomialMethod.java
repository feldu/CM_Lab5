package lab5.method;

import lab5.function.Functions;
import lab5.table.Table;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.function.Function;


/*
0,15 1,25
0,2 2,38
0,33 3,79
0,47 5,44
0,62 7,14

0,22

 */

@Slf4j
public class NewtonPolynomialMethod implements InterpolationMethod {
    @Override
    public double solve(Table table, double interpolationX) {
        SortedMap<Double, Double> data = table.getMap();
        List<Double> xData = table.getXData();
        int size = data.size();
        List<List<Double>> separatedDiffs = new ArrayList<>();
        List<Double> separatedDiffs0 = new ArrayList<>();
        for (int j = 1; j < data.size(); j++) {
            separatedDiffs0.add((data.get(xData.get(j)) - data.get(xData.get(j - 1))) / (xData.get(j) - xData.get(j - 1)));
        }
        separatedDiffs.add(separatedDiffs0);
        for (int i = 0; i < size - 2; i++) {
            List<Double> separatedDiffsI = new ArrayList<>();
            for (int j = 1; j < separatedDiffs.get(i).size(); j++) {
                separatedDiffsI.add((separatedDiffs.get(i).get(j) - separatedDiffs.get(i).get(j - 1)) / (xData.get(j + i + 1) - xData.get(j - 1)));
            }
            separatedDiffs.add(separatedDiffsI);
        }
        double f0 = table.getYData().get(0);
        logFunctionsValues(separatedDiffs);
        double Nn = f0;
        int cnt = 1;
        for (List<Double> separatedDiff : separatedDiffs) {
            double mulX = 1;
            for (int j = 0; j < cnt; j++) {
                mulX *= (interpolationX - xData.get(j));
            }
            cnt++;
            Nn += separatedDiff.get(0) * mulX;
        }
        log.info("y({})={}", interpolationX, Nn);
        return Nn;
    }

    private void logFunctionsValues(List<List<Double>> separatedDiffs) {
        final String[] message = {""};
        log.info("Functions values: ");
        separatedDiffs.forEach(l -> {
            l.forEach(i ->  message[0] += (i + "; "));
            log.info(message[0]);
            message[0] = "";
        });
    }
}
