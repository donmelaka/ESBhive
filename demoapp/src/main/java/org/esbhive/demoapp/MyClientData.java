/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.demoapp;

/**
 *
 * @author pubudu
 */
public class MyClientData {
  static int totalResponded = 0;
  Long curTime;
  int requestNumber;
  int numRequestsPerClient;

  public Long getCurTime() {
    return curTime;
  }

  public void setCurTime(Long curTime) {
    this.curTime = curTime;
  }

  public int getNumRequestsPerClient() {
    return numRequestsPerClient;
  }

  public void setNumRequestsPerClient(int numRequestsPerClient) {
    this.numRequestsPerClient = numRequestsPerClient;
  }

  public int getRequestNumber() {
    return requestNumber;
  }

  public void setRequestNumber(int requestNumber) {
    this.requestNumber = requestNumber;
  }

  public MyClientData(Long curTime, int requestNumber, int numRequestsPerClient) {
    this.curTime = curTime;
    this.requestNumber = requestNumber;
    this.numRequestsPerClient = numRequestsPerClient;
  }


}
