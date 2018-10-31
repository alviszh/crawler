/*
 * Logger.java
 *
 * Created on 25. April 2007, 09:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.xvolks.jnative.logging;


/**
 *
 * @author osthop
 */
public interface JNativeLogger {
    
    public enum SEVERITY {
        DEBUG(1, "[DEBUG]"),
        FATAL(2, "[FATAL]"),
        ERROR(3, "[ERROR]"),
        WARN(4, "[WARN]"),
        INFO(5, "[INFO]");

        private int value;
        private String name;

        private SEVERITY(int value, String name) {
            this.value = value;
            this.name = name;
        }
        
        public int getValue() {
        	return value;
        }
        public String getName() {
            return name;
        }
    }
    public String getName();
    public void setName(String name);
    public void log(SEVERITY severity, Object message);
    public void log(Object message);
}
