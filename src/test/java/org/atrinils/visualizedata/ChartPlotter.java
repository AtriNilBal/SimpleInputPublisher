package org.atrinils.visualizedata;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartPlotter {

    private String graphTitle;
    private String graphSubTitle;
    private String y1AxisLabel;
    private String y2AxisLabel;
    private String xAxisLabel;
    private String[] xAxisLabels;
    private XYSeriesCollection dataSet;

    private void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    private void setGraphSubTitle(String graphSubTitle) {
        this.graphSubTitle = graphSubTitle;
    }

    private void setY1AxisLabel(String y1AxisLabel) {
        this.y1AxisLabel = y1AxisLabel;
    }

    private void setY2AxisLabel(String y2AxisLabel) {
        this.y2AxisLabel = y2AxisLabel;
    }

    private void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public String getGraphSubTitle() {
        return graphSubTitle;
    }

    public String getY1AxisLabel() {
        return y1AxisLabel;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    /***
    * @param1 unique test run identifier
    * @param2 list of a pair of readings taken with respect to a common benchmark...
    * eg; cpu and mem reading taken at one point of time
    * sample list element string template can be used- TIME HH:MM:SSS-CPU 0.0-MEM 0.0 where CPU and MEM can be used as the
    * two y-axis labels while the value that follows after each is the reading
    * */
    public ChartPlotter(String testRunIdentifier, List<String> pairOfDataReadingAgainstTime) {
        setGraphSubTitle(testRunIdentifier);
        String sampleReading = pairOfDataReadingAgainstTime.get(0);
        String[] readingSplitter = sampleReading.split("-");
        setY1AxisLabel(readingSplitter[1].split(" ")[0]);
        setY2AxisLabel(readingSplitter[2].split(" ")[0]);
        setxAxisLabel(readingSplitter[0].split(" ")[0]);

        XYSeries series1=new XYSeries("y1AxisLabel");
        XYSeries series2=new XYSeries("y2AxisLabel");
        xAxisLabels=new String[pairOfDataReadingAgainstTime.size()];
        int xAxisValue=0;

        for(String reading:pairOfDataReadingAgainstTime) {
            String[] splitReading = reading.split("-");
            series1.add(xAxisValue, Double.parseDouble(splitReading[1].split(" ")[1]));
            series2.add(xAxisValue, Double.parseDouble(splitReading[2].split(" ")[1]));
            xAxisLabels[xAxisValue] = String.valueOf(splitReading[0].split(" ")[1]);
            xAxisValue++;
        }

        dataSet = new XYSeriesCollection();
        dataSet.addSeries(series1);
        dataSet.addSeries(series2);
        double getMaxValY1Series = series1.getMaxY();
        double getMaxValY2Series = series2.getMaxY();
        setGraphSubTitle(String.format("%s \nMax %s Val - %s\n Max %s Val - %s ",
                readingSplitter[1].split(" ")[0], getMaxValY1Series,
                readingSplitter[2].split(" ")[0], getMaxValY2Series));

    }

    public void generateGraph(File file, int width, int height) {
        JFreeChart chart= ChartFactory.createXYLineChart(
                getGraphTitle()
                ,getxAxisLabel()
                ,getY1AxisLabel()
                ,dataSet
                ,PlotOrientation.VERTICAL
                ,true
                ,true
                ,false
		);
        XYPlot plot=(XYPlot) chart.getPlot();
        SymbolAxis xAxis=new SymbolAxis(this.xAxisLabel, xAxisLabels);
        plot.setDomainAxis(xAxis);
        plot.setBackgroundPaint(Color.WHITE);
        plot.getRangeAxis().setMinorTickCount(1);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        NumberAxis axis2=new NumberAxis(this.y2AxisLabel);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataSet);
        plot.mapDatasetToRangeAxis(1,1);

        TextTitle message=new TextTitle(this.graphSubTitle);
        message.setPaint(Color.BLACK);
        message.setVisible(true);
        chart.addSubtitle(message);
        try{
            ChartUtils.saveChartAsPNG(file, chart, width, height, new ChartRenderingInfo());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
