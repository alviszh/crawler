package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月20日 下午5:55:17 
 */
public class ProcessTest {
	 /**
     * 显示当前机器的所有进程
	 * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
//	  try {
//          Process process = Runtime.getRuntime().exec("taskList");
//          Scanner in = new Scanner(process.getInputStream());
//          int count = 0;
//          while (in.hasNextLine()) {
//              count++;
//              System.out.println(count + ":" + in.nextLine());
//          }
//      } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
    	String currentPid = getCurrentPid();
    	
    	System.out.println(getPidNameByPid(currentPid));
    	
	}
    
 // 通过获取当前运行主机的pidName，截取获得他的pid  
    public static String getCurrentPid() throws Exception {  
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
        String pidName = runtime.getName();// 5296@dell-PC  
        String pid = pidName.substring(0, pidName.indexOf("@"));  
        return pid;  
    } 
 // 通过Pid获取PidName  
    public static String getPidNameByPid(String pid) throws Exception {  
        String pidName = null;  
        InputStream is = null;  
        InputStreamReader ir = null;  
        BufferedReader br = null;  
        String line = null;  
        String[] array = (String[]) null;  
        try {  
            Process p = Runtime.getRuntime().exec("TASKLIST /NH /FO CSV /FI \"PID EQ " + pid + "\"");  
            is = p.getInputStream(); // "javaw.exe","3856","Console","1","72,292  
                                        // K"从这个进程中获取对应的PidName  
            ir = new InputStreamReader(is);  
            br = new BufferedReader(ir);  
            while ((line = br.readLine()) != null) {  
                if (line.indexOf(pid) != -1) {  
                    array = line.split(",");  
                    line = array[0].replaceAll("\"", "");  
                    line = line.replaceAll(".exe", "");// 考虑pidName后缀为exe或者EXE  
                    line = line.replaceAll(".exe".toUpperCase(), "");  
                    pidName = line;  
                }  
            }  
        } catch (IOException localIOException) {  
            throw new Exception("获取进程名称出错！");  
        } finally {  
            if (br != null) {  
                br.close();  
            }  
            if (ir != null) {  
                ir.close();  
            }  
            if (is != null) {  
                is.close();  
            }  
        }  
        return pidName;  
    }  
}
