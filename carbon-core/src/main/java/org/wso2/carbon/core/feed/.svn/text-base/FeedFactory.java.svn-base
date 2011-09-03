/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.core.feed;

import org.wso2.carbon.core.feed.atom.AtomFeed;
import org.wso2.carbon.core.feed.rss.RSSFeed;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;

import java.util.Hashtable;
import java.util.Map;
/*
 * 
 */

public class FeedFactory {

    private static Map<String, RSSFeed> rssFeedsMap = new Hashtable<String, RSSFeed>();
    private static Map<String, AtomFeed> atomFeedsMap = new Hashtable<String, AtomFeed>();

    public static RSSFeed getRSSFeed(String rssSource, AxisConfiguration axisConfig) {
        RSSFeed rssFeed = rssFeedsMap.get(rssSource);
        if (rssFeed == null) {
            rssFeed = new RSSFeed(axisConfig);
            rssFeedsMap.put(rssSource, rssFeed);
        }
        return rssFeed;
    }

    public static AtomFeed getAtomFeed(String atomSource, AxisConfiguration axisConfig) {
        AtomFeed atomFeed = atomFeedsMap.get(atomSource);
        if (atomFeed == null) {
            atomFeed = new AtomFeed(axisConfig);
            atomFeedsMap.put(atomSource, atomFeed);
        }
        return atomFeed;
    }
}
