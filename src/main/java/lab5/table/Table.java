package lab5.table;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Getter
@Setter
public class Table {
    private double leftBorder, rightBorder;
    private SortedMap<Double, Double> map;
    private List<Double> xData, yData;

    public Table(SortedMap<Double, Double> map) {
        this.map = map;
        this.leftBorder = map.firstKey();
        this.rightBorder = map.lastKey();
        xData = new ArrayList<>();
        yData = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            xData.add(entry.getKey());
            yData.add(entry.getValue());
        }
    }
}
