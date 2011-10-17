/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoapp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author pubudu
 *
 */
public class ResponseData {

  private long totalTime;
  private long responseTimeSum;
  private int totalRequests;
  private int requestsServed;

  public ResponseData(File dataFile) {
    Configuration data = null;
    try {
      data = new PropertiesConfiguration(dataFile);
    } catch (ConfigurationException ex) {
      Logger.getLogger(ResponseData.class.getName()).log(Level.SEVERE, null, ex);
    }
    try{
    this.totalTime = data.getLong("total.time");
    this.responseTimeSum = data.getLong("response.time.sum");
    this.totalRequests = data.getInt("total.requests");
    this. requestsServed = data.getInt("requests.served");
    }catch(Exception ex){
      Logger.getLogger(ResponseData.class.getName()).log(Level.SEVERE, dataFile.getAbsolutePath(), ex);
    }
  }

  public ResponseData(long totalTime, long responseTime, int totalRequests, int requestsServed) {
    this.totalTime = totalTime;
    this.responseTimeSum = responseTime;
    this.totalRequests = totalRequests;
    this.requestsServed = requestsServed;
  }

  public int getRequestsServed() {
    return requestsServed;
  }

  public void setRequestsServed(int requestsServed) {
    this.requestsServed = requestsServed;
  }

  public long getResponseTimeSum() {
    return responseTimeSum;
  }

  public void setResponseTime(long responseTime) {
    this.responseTimeSum = responseTime;
  }

  public int getTotalRequests() {
    return totalRequests;
  }

  public void setTotalRequests(int totalRequests) {
    this.totalRequests = totalRequests;
  }

  public long getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(long totalTime) {
    this.totalTime = totalTime;
  }
}
