package app.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ETest {
	public static void main(String[] args) {
		try {
//			int i=1/0;
			String str="org.springframework.dao.InvalidDataAccessResourceUsageException: could not execute statement; SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not execute statement at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:241) at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:224) at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:499) at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:59) at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:216) at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:153) at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)";
			String[] split = str.split("\\)");  //需要转义
			StringBuffer buf=new StringBuffer();
			buf.append("<!DOCTYPE html><html><head></head><body>");
			for (String string : split) {
				buf.append(string+")"+"<br/>");
			}
			buf.append("</body></html>");
			System.out.println(buf.toString());
		} catch (Exception e) {
			ExceptionUtils.getStackTrace(e);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			String eToString = sw.toString(); 
			String[] split = eToString.split("\\)");  //需要转义
			StringBuffer buf=new StringBuffer();
			buf.append("<!DOCTYPE html><html><head></head><body>");
			for (String string : split) {
				buf.append(string+")"+"<br/>");
			}
			buf.append("</body></html>");
			System.out.println(buf.toString());
		}
		
	}
	
}
