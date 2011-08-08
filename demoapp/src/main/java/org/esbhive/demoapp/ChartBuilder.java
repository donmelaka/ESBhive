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
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

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
			entry.put(rdc.getNumESBs(), list);
			dataMap.put(rdc.getRequestsPerClient(), entry);
		}
	}

	public void createChart() throws MalformedURLException, IOException {
		for (Iterator<Integer> iter = dataMap.keySet().iterator(); iter.hasNext();) {
			Integer requestPerClient = iter.next();
			saveChart(createThroughputChartString(requestPerClient, dataMap.get(requestPerClient)),
				"Requests-" + requestPerClient + "-Throughput.png");
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
			for (Iterator<Double> pointIter = linePoints.values().iterator(); pointIter.hasNext();) {
				points[i] = pointIter.next().doubleValue();
				i++;
			}
			for (Iterator xAxisValIter = linePoints.keySet().iterator(); xAxisValIter.hasNext() && firstRun;) {
				xAxisValues.add(xAxisValIter.next().toString());
			}
			firstRun = false;
			Random numGen = new Random();
			Line line = Plots.newLine(Data.newData(points),
				Color.newColor("" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10) + "" + numGen.nextInt(10)),
				"For " + numESBs + " ESB(s)");
			line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
			line.addShapeMarkers(Shape.DIAMOND, Color.newColor("CA3D05"), 12);
			line.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
			lines.add(line);

		}

		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(600, 450);
		chart.setTitle("Throughput with " + requestPerClient + " requests per client.", Color.WHITE, 14);
		chart.setGrid(1, 25, 3, 2);

		AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.WHITE, 12, AxisTextAlignment.CENTER);
		AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(xAxisValues);
		xAxis.setAxisStyle(axisStyle);
		AxisLabels yAxis = AxisLabelsFactory.newAxisLabels("", "25", "50", "75", "100");
		yAxis.setAxisStyle(axisStyle);
		AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Number of Clients", 50.0);
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Throughput", 50.0);



		// Adding axis info to chart.
		chart.addXAxisLabels(xAxis);
		chart.addYAxisLabels(yAxis);
		chart.addYAxisLabels(yAxis2);
		chart.addXAxisLabels(xAxis3);

		// Defining background and chart fills.
		chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
		fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
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
