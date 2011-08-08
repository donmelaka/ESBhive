/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoapp;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author pubudu
 */
public class ChartBuilder {

  HashMap<Integer, HashMap<Integer, ArrayList<ResponseDataCalculator>>> dataMap =
          new HashMap<Integer, HashMap<Integer, ArrayList<ResponseDataCalculator>>>();

  void addCalculatedDataItem(ResponseDataCalculator rdc) {
    if (dataMap.containsKey(rdc.getRequestsPerClient())) {
      HashMap<Integer, ArrayList<ResponseDataCalculator>> entry =
              dataMap.get(rdc.getRequestsPerClient());
      if (entry.containsKey(rdc.getNumESBs())) {
        ArrayList list = entry.get(rdc.getNumESBs());
        list.add(rdc);
      } else {
        ArrayList list = new ArrayList();
        list.add(rdc);
        entry.put(rdc.getNumESBs(), list);
      }
    } else {
      HashMap<Integer, ArrayList<ResponseDataCalculator>> entry =
              new HashMap<Integer, ArrayList<ResponseDataCalculator>>();
      ArrayList list = new ArrayList();
      list.add(rdc);
      entry.put(rdc.getRequestsPerClient(), list);
    }
  }

  public void createChart() throws MalformedURLException, IOException{
    saveChart(createChartString(),"Test.png");
  }

  private String createChartString() {
// EXAMPLE CODE START

    // Defining lines
    final int NUM_POINTS = 25;
    final double[] competition = new double[NUM_POINTS];
    final double[] mywebsite = new double[NUM_POINTS];
    for (int i = 0; i < NUM_POINTS; i++) {
      competition[i] = 100 - (Math.cos(30 * i * Math.PI / 180) * 10 + 50) * i / 20;
      mywebsite[i] = (Math.cos(30 * i * Math.PI / 180) * 10 + 50) * i / 20;
    }
    Line line1 = Plots.newLine(Data.newData(mywebsite), Color.newColor("CA3D05"), "My Website.com");
    line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
    line1.addShapeMarkers(Shape.DIAMOND, Color.newColor("CA3D05"), 12);
    line1.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
    Line line2 = Plots.newLine(Data.newData(competition), Color.SKYBLUE, "Competition.com");
    line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
    line2.addShapeMarkers(Shape.DIAMOND, Color.SKYBLUE, 12);
    line2.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);


    // Defining chart.
    LineChart chart = GCharts.newLineChart(line1, line2);
    chart.setSize(600, 450);
    chart.setTitle("Web Traffic|(in billions of hits)", Color.WHITE, 14);
    chart.addHorizontalRangeMarker(40, 60, Color.newColor(Color.RED, 30));
    chart.addVerticalRangeMarker(70, 90, Color.newColor(Color.GREEN, 30));
    chart.setGrid(25, 25, 3, 2);

    // Defining axis info and styles
    AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.WHITE, 12, AxisTextAlignment.CENTER);
    AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("Nov", "Dec", "Jan", "Feb", "Mar");
    xAxis.setAxisStyle(axisStyle);
    AxisLabels xAxis2 = AxisLabelsFactory.newAxisLabels("2007", "2007", "2008", "2008", "2008");
    xAxis2.setAxisStyle(axisStyle);
    AxisLabels yAxis = AxisLabelsFactory.newAxisLabels("", "25", "50", "75", "100");
    AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Month", 50.0);
    xAxis3.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 14, AxisTextAlignment.CENTER));
    yAxis.setAxisStyle(axisStyle);
    AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Hits", 50.0);
    yAxis2.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 14, AxisTextAlignment.CENTER));
    yAxis2.setAxisStyle(axisStyle);

    // Adding axis info to chart.
    chart.addXAxisLabels(xAxis);
    chart.addXAxisLabels(xAxis2);
    chart.addXAxisLabels(xAxis3);
    chart.addYAxisLabels(yAxis);
    chart.addYAxisLabels(yAxis2);

    // Defining background and chart fills.
    chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
    LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
    fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
    chart.setAreaFill(fill);
    String url = chart.toURLString();

    return url;
  }

  private void saveChart(String url, String fileName) throws MalformedURLException, IOException {
    URL u = new URL(url);
    URLConnection uc = u.openConnection();
    String contentType = uc.getContentType();
    int contentLength = uc.getContentLength();
    if (contentType.startsWith("text/") || contentLength == -1) {
      throw new IOException("This is not a binary file.");
    }
    InputStream raw = uc.getInputStream();
    InputStream in = new BufferedInputStream(raw);
    byte[] data = new byte[contentLength];
    int bytesRead = 0;
    int offset = 0;
    while (offset < contentLength) {
      bytesRead = in.read(data, offset, data.length - offset);
      if (bytesRead == -1)
        break;
      offset += bytesRead;
    }
    in.close();

    if (offset != contentLength) {
      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
    }

    FileOutputStream out = new FileOutputStream(fileName);
    out.write(data);
    out.flush();
    out.close();

  }
}
