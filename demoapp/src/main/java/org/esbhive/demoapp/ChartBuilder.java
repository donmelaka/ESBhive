/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoapp;

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
      entry.put(rdc.getRequestsPerClient(),list);
    }
  }

  String createChart(){
    return null;
  }
}
