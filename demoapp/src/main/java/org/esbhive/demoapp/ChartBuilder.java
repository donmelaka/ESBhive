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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is the class that creates the URL for the chart and saves it
 * @author pubudu
 */
public class ChartBuilder {

  private int throughputChartMultiplyBy = 0;
  private int throughputChartDivideBy = 0;
  private String[] throughputChartLabels = null;
  private int rtChartMultiplyBy = 0;
  private int rtChartDivideBy = 0;
  private String[] rtChartLabels = null;
  private Queue<Color> colorQueue = new LinkedList<Color>();
  HashMap<Integer, HashMap<Integer, ArrayList<ResponseDataCalculator>>> dataMap =
          new HashMap<Integer, HashMap<Integer, ArrayList<ResponseDataCalculator>>>();

  /**
   *
   * @param tcmb
   * @param tcdb
   * @param tcl
   * @param rcmb
   * @param rcdb
   * @param rcl
   */
  public ChartBuilder(int tcmb, int tcdb, String[] tcl, int rcmb, int rcdb, String[] rcl) {
    this.throughputChartDivideBy = tcdb;
    this.throughputChartMultiplyBy = tcmb;
    this.throughputChartLabels = tcl;
    this.rtChartDivideBy = rcdb;
    this.rtChartMultiplyBy = rcmb;
    this.rtChartLabels = rcl;
  }

  /**
   * Use this method to add a calculated data item
   * @param rdc the calculated data item to be added
   */
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
      entry.put(rdc.getNumESBs(), list);
      dataMap.put(rdc.getRequestsPerClient(), entry);
    }
  }

  public void createChart() throws MalformedURLException, IOException {
    for (Iterator<Integer> iter = dataMap.keySet().iterator(); iter.hasNext();) {
      Integer requestPerClient = iter.next();
      try {
        saveChart(createThroughputChartString(requestPerClient, dataMap.get(requestPerClient)),
                "Requests-" + requestPerClient + "-Throughput.png");
      } catch (NullPointerException ex) {
        Logger.getLogger(ChartBuilder.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
        saveChart(createResponseTimeChartString(requestPerClient, dataMap.get(requestPerClient)),
                "Requests-" + requestPerClient + "-ResponseTime.png");
      } catch (NullPointerException ex) {
        Logger.getLogger(ChartBuilder.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private String createThroughputChartString(Integer requestPerClient, HashMap<Integer, ArrayList<ResponseDataCalculator>> chartData) {
    // Defining lines
    List<Line> lines = new ArrayList<Line>();
    List<String> xAxisValues = new ArrayList<String>();
    boolean firstRun = true;
    for (Iterator<Integer> iter = chartData.keySet().iterator(); iter.hasNext();) {
      Integer numESBs = iter.next();
      ArrayList<ResponseDataCalculator> lineData = chartData.get(numESBs);
      TreeMap<Integer, Double> linePoints = new TreeMap<Integer, Double>();
      for (Iterator<ResponseDataCalculator> rdcIter = lineData.iterator(); rdcIter.hasNext();) {
        ResponseDataCalculator rdc = rdcIter.next();
        linePoints.put(rdc.getNumClients(), (double) rdc.getThroughput());

      }
      double[] points = new double[linePoints.values().size()];
      int i = 0;
      System.out.println("Throughput Chart Data");

      for (Iterator<Double> pointIter = linePoints.values().iterator(); pointIter.hasNext();) {
        points[i] = pointIter.next().doubleValue() * throughputChartMultiplyBy / throughputChartDivideBy;
        System.out.println(points[i]);
        i++;
      }
      for (Iterator xAxisValIter = linePoints.keySet().iterator(); xAxisValIter.hasNext() && firstRun;) {
        xAxisValues.add(xAxisValIter.next().toString());
      }
      firstRun = false;
      Random numGen = new Random();
      Color color = Color.newColor("" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10));
      Line line = Plots.newLine(Data.newData(points),
              color,
              "For " + numESBs + " ESB(s)");
      colorQueue.add(color);
      line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
      line.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 4);
      lines.add(line);

    }

    LineChart chart = GCharts.newLineChart(lines);
    chart.setSize(600, 450);
    chart.setTitle("Throughput per second with " + requestPerClient + " requests per client.", Color.BLACK, 14);

    AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 12, AxisTextAlignment.CENTER);
    AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(xAxisValues);
    xAxis.setAxisStyle(axisStyle);
    AxisLabels yAxis = AxisLabelsFactory.newAxisLabels(throughputChartLabels);
    yAxis.setAxisStyle(axisStyle);
    AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Throughput", 50.0);
    yAxis2.setAxisStyle(axisStyle);
    AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Number of Clients", 50.0);
    xAxis3.setAxisStyle(axisStyle);



    // Adding axis info to chart.
    chart.addXAxisLabels(xAxis);
    chart.addYAxisLabels(yAxis);
    chart.addYAxisLabels(yAxis2);
    chart.addXAxisLabels(xAxis3);

    // Defining background and chart fills.
    chart.setBackgroundFill(Fills.newSolidFill(Color.ALICEBLUE));
    LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.LIGHTGREY, 100);
    chart.setAreaFill(fill);
    String url = chart.toURLString();

    System.out.println(url);
    return url;
  }

  private String createResponseTimeChartString(Integer requestPerClient, HashMap<Integer, ArrayList<ResponseDataCalculator>> chartData) {
    // Defining lines
    List<Line> lines = new ArrayList<Line>();
    List<String> xAxisValues = new ArrayList<String>();
    boolean firstRun = true;
    for (Iterator<Integer> iter = chartData.keySet().iterator(); iter.hasNext();) {
      Integer numESBs = iter.next();
      ArrayList<ResponseDataCalculator> lineData = chartData.get(numESBs);
      TreeMap<Integer, Double> linePoints = new TreeMap<Integer, Double>();
      for (Iterator<ResponseDataCalculator> rdcIter = lineData.iterator(); rdcIter.hasNext();) {
        ResponseDataCalculator rdc = rdcIter.next();
        linePoints.put(rdc.getNumClients(), (double) rdc.getResponseTime());

      }
      double[] points = new double[linePoints.values().size()];
      int i = 0;

      System.out.println("Response Time Chart Data");
      for (Iterator<Double> pointIter = linePoints.values().iterator(); pointIter.hasNext();) {
        points[i] = pointIter.next().doubleValue() * rtChartMultiplyBy / rtChartDivideBy;
        System.out.println(points[i]);
        i++;
      }
      for (Iterator xAxisValIter = linePoints.keySet().iterator(); xAxisValIter.hasNext() && firstRun;) {
        xAxisValues.add(xAxisValIter.next().toString());
      }
      firstRun = false;
      Line line = Plots.newLine(Data.newData(points),
              colorQueue.poll(),
              "For " + numESBs + " ESB(s)");
      line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
      line.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 4);
      lines.add(line);

    }

    LineChart chart = GCharts.newLineChart(lines);
    chart.setSize(600, 450);
    chart.setTitle("Response Time (in milliseconds) with " + requestPerClient + " requests per client.", Color.BLACK, 14);

    AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 12, AxisTextAlignment.CENTER);
    AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(xAxisValues);
    xAxis.setAxisStyle(axisStyle);
    AxisLabels yAxis = AxisLabelsFactory.newAxisLabels(rtChartLabels);
    yAxis.setAxisStyle(axisStyle);
    AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("ResponseTime", 50.0);
    yAxis2.setAxisStyle(axisStyle);
    AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Number of Clients", 50.0);
    xAxis3.setAxisStyle(axisStyle);



    // Adding axis info to chart.
    chart.addXAxisLabels(xAxis);
    chart.addYAxisLabels(yAxis);
    chart.addYAxisLabels(yAxis2);
    chart.addXAxisLabels(xAxis3);

    // Defining background and chart fills.
    chart.setBackgroundFill(Fills.newSolidFill(Color.ALICEBLUE));
    LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.LIGHTGREY, 100);
    chart.setAreaFill(fill);
    String url = chart.toURLString();

    System.out.println(url);
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
      if (bytesRead == -1) {
        break;
      }
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
