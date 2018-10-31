/*
 * AbstractLogger.java
 *
 * Created on 25. April 2007, 12:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.xvolks.jnative.logging;

import java.util.HashMap;

/**
 *
 * @author osthop
 */
public abstract class AbstractLogger implements JNativeLogger  {
    
    protected static HashMap<String, JNativeLogger> loggers = new HashMap<String, JNativeLogger>();
    protected String loggerName = null;
    
    public String getName() {
        return loggerName;
    }
    public void setName(String name) {
        loggerName = name;
    }
}
