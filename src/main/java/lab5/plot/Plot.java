package lab5.plot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
public class Plot {
    private XYChart chart;
    private List<Series> seriesList;

    public Plot(String plotName) {
        seriesList = new ArrayList<>();
        createChart(plotName);
    }

    public Plot(String plotName, Series... seriesArray) {
        seriesList = new ArrayList<>(asList(seriesArray));
        createChart(plotName);
        for (Series item : seriesList) {
            addSeriesToChart(item);
        }
    }

    private void createChart(String plotName) {
        chart = new XYChartBuilder().theme(Styler.ChartTheme.Matlab) //.width(100).height(600)
                .title(plotName).xAxisTitle("X").yAxisTitle("Y")
                .build();
        chart.setCustomXAxisTickLabelsFormatter((x) -> String.format("%.2f", x));
    }

    public void addSeries(Series series) {
        seriesList.add(series);
        addSeriesToChart(series);
    }

    private void addSeriesToChart(Series item) {
        XYSeries xySeries = chart.addSeries(item.getSeriesName(), item.getXData(), item.getYData());
        if (item.isHideLines()) xySeries.setLineStyle(SeriesLines.NONE);
        if (item.isHidePoints()) xySeries.setMarker(SeriesMarkers.NONE);
    }

    @SneakyThrows
    public void save(String filename) {
        log.info("Saving plot");
        BitmapEncoder.saveBitmap(chart, filename, BitmapEncoder.BitmapFormat.PNG);
        log.info("Plot saved as {}", filename);
    }


}
