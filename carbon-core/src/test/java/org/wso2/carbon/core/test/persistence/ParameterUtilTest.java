package org.wso2.carbon.core.test.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.apache.axis2.description.Parameter;
import org.wso2.carbon.core.util.ParameterUtil;

public class ParameterUtilTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateParameter1() throws Exception {
        Parameter p = ParameterUtil.createParameter("name", "value");
        String s = p.getValue().toString();
        assertTrue(s.equals("value"));
    }

    public void testCreateParameter2() throws Exception {
        Parameter p = null;
        try {
            p = ParameterUtil.createParameter(null, "value");
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Parameter name is madatory."));
        }
        assertNull(p);
    }

    public static Test suite() {
        return new TestSuite(ParameterUtilTest.class);
    }
}