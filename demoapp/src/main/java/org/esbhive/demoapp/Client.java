/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import samples.services.SimpleStockQuoteServiceCallbackHandler;
import samples.services.SimpleStockQuoteServiceStub;
import samples.services.xsd.GetQuote;

/**
 * This is the class that sends the requests to the StockQouteProxy service and
 * measures the time taken for the response to arrive.
 *
 * @author pubudu
 */
public class Client extends Thread {

  int numReqestsPerClient = 0;
  CountDownLatch doneSignal = null;
  List timeList = Collections.synchronizedList(new ArrayList());
  File dir = null;
  File file = null;
  Long totalTime = null;
  private AtomicInteger totalResponded = new java.util.concurrent.atomic.AtomicInteger();
/**
 *
 * @param numReqestsPerClient the number of asynchronous requests to send on a per
 * client basis
 * @param doneSignal
 * @param dir the directory in which to save the data file
 */
  public Client(int numReqestsPerClient, CountDownLatch doneSignal, File dir) {
    this.numReqestsPerClient = numReqestsPerClient;
    this.doneSignal = doneSignal;
    this.dir = dir;
    file = new File(dir, (new Double(Math.random())).toString()+".properties");
    try {
      file.createNewFile();
    } catch (IOException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void run() {
    final Long startTime = System.nanoTime();
    GetQuote gq = new GetQuote();
    gq.setSymbol("IBM");
    for (int i = 0; i < numReqestsPerClient; i++) {
      try {
        Long curTime = System.nanoTime();
        MyClientData mcd = new MyClientData(curTime, i, numReqestsPerClient);
        SimpleStockQuoteServiceCallbackHandler ssqscbh = new SimpleStockQuoteServiceCallbackHandler(mcd) {

          @Override
          public void receiveResultgetQuote(samples.services.xsd.GetQuoteResponse result) {
            MyClientData mcd = (MyClientData) this.clientData;
            long time = System.nanoTime() - mcd.getCurTime();
            timeList.add(time);
            totalResponded.incrementAndGet();
            if (totalResponded.intValue() == numReqestsPerClient) {
              totalTime = System.nanoTime() - startTime;
            }
            doneSignal.countDown();
          }
        };
        int next = (int) Math.rint(Math.random() * (App.url_list.length - 1));
        SimpleStockQuoteServiceStub sqps = new SimpleStockQuoteServiceStub(App.url_list[next] + "/services/StockQuoteProxy.StockQuoteProxyHttpSoap12Endpoint");
        sqps.startgetQuote(gq, ssqscbh);
      } catch (RemoteException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        totalResponded.incrementAndGet();
        if (totalResponded.intValue() == numReqestsPerClient) {
          totalTime = System.nanoTime() - startTime;
        }
        doneSignal.countDown();
      }
    }
    FileWriter fstream = null;
    PrintWriter out = null;
    try {

      doneSignal.await();
      long additionOfTimes = 0;
      fstream = new FileWriter(file);
      out = new PrintWriter(file);
      for (Iterator<Long> i = timeList.iterator(); i.hasNext();) {
        additionOfTimes = additionOfTimes + i.next();
      }
      out.println("total.time = " + totalTime);
      out.println("response.time.sum = " + additionOfTimes);
      out.println("total.requests = " + numReqestsPerClient);
      out.println("requests.served = " + timeList.size());
    } catch (IOException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        fstream.close();
        out.close();
      } catch (IOException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
