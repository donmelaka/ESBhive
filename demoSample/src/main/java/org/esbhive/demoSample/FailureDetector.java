/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * @author guest
 */
public class FailureDetector implements Watcher {

	private ZooKeeper zk;
	private String zkConnectionString = null;
	private UIInterface ui;

	public FailureDetector(UIInterface ui) {
		this.ui = ui;
		String jarFilePath =
			FailureDetector.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String propertiesFile = new File(jarFilePath).getParent() + File.separator + "options.properties";
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propertiesFile);
		} catch (ConfigurationException ex) {
			Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
		}
		zkConnectionString = config.getString("zk.con.string");
		try {
			this.zk = new ZooKeeper(zkConnectionString, 3000, this);
		} catch (IOException ex) {
			Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			zk.getChildren("/nodes", this);
		} catch (KeeperException ex) {
			Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void process(WatchedEvent event) {
		EventType type = event.getType();
		List<String> nodes = null;
		if (type == EventType.NodeChildrenChanged) {
			try {
				nodes = zk.getChildren("/nodes", true);
			} catch (KeeperException ex) {
				Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InterruptedException ex) {
				Logger.getLogger(FailureDetector.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (nodes.size() > 0) {
				String[] nodeArray = nodes.toArray(new String[0]);
				Arrays.sort(nodeArray);
				ui.currentLeader(nodeArray[0]);
				
			}
		}



	}
}
