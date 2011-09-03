/*
 * Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.core.test.persistence;

import junit.framework.TestCase;

import java.io.File;

public class BaseTestCase extends TestCase {

    public void setUp() {

        if (System.getProperty("carbon.home") == null) {
            File file = new File("distribution/carbon-home");
            if (file.exists()) {
                System.setProperty("carbon.home", file.getAbsolutePath());
            } else {
                file = new File("../../distribution/3.0.1/carbon-home");
                if (file.exists()) {
                    System.setProperty("carbon.home", file.getAbsolutePath());
                }
            }
        }

    }
}