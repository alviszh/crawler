/*
 * JNativeLogger.java
 *
 * Created on 25. April 2007, 09:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.xvolks.jnative.logging;

import java.text.SimpleDateFormat;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.logging.JNativeLogger.SEVERITY;

/**
 *
 * @author osthop
 */
public class ConsoleLogger extends AbstractLogger {
    
    private ConsoleLogger() {}
    
    public static JNativeLogger getInstance(Class clazz) {
        return getInstance(clazz.getName());
    }
    
    public static JNativeLogger getInstance(String name) {
        JNativeLogger logger = null;
        if(!loggers.containsKey(name)) {
            logger = new ConsoleLogger();
            logger.setName(name);
            loggers.put(name, logger);
        } else {
            logger = (JNativeLogger)loggers.get(name);
        }
        return logger;
    }
   
    public void log(SEVERITY severity, Object message) {
        
        if(!JNative.isLogginEnabled())
            return;
        
        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis()));
        strBuf.append(",");
        if(severity != null)
            strBuf.append(" "+severity.getName());
        
        final String[] str = inferCaller();
        if(str != null) {
            strBuf.append(" ["+str[0]+"]");
            strBuf.append(" ["+str[1]+"]: ");
        }
        if(message instanceof Throwable) {
            Throwable err = (Throwable)message;
            strBuf.append(err.getMessage());
            for(int i = 0; i < err.getStackTrace().length; i++) {
                strBuf.append(System.getProperty( "line.separator" ));
                strBuf.append(err.getStackTrace()[i].toString());
            }
        } else {
            strBuf.append(message);
        }
        
        System.err.println(strBuf.toString());
    }       
    // Protected method to infer the caller's class and method names
    private String[] inferCaller() {
        // Get the stack trace.
        final StackTraceElement stack[] = (new Throwable()).getStackTrace();
        // First, search back to a method in the Logger class.
        int ix = 0;
        while (ix < stack.length) {
            final StackTraceElement frame = stack[ix];
            final String cname = frame.getClassName();
            if (cname.equals("xvolks.jnative.logging.ConsoleLogger")) {
            break;
            }
            ix++;
        }
        // Now search for the first frame before the "Logger" class.
        while (ix < stack.length) {
            final StackTraceElement frame = stack[ix];
            final String cname = frame.getClassName();
            if (!cname.equals("xvolks.jnative.logging.ConsoleLogger")) {
                // We've found the relevant frame.
                final String[] str = new String[2];
                str[0] = cname;
                str[1] = frame.getMethodName();
                return str;
            }
            ix++;
        }
        return null;
        // We haven't found a suitable frame, so just punt.  This is
        // OK as we are only committed to making a "best effort" here.
    }

	public void log(Object message) {
		log(SEVERITY.INFO, message);
	}
}
