package Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTime {

	
	    public static void main(String[] args) {  
	        Timer timer = new Timer();  
	        int delay = 2*1000; //2秒后开始  
	        int period = 3*1000; //每3秒执行一次  
	        timer.scheduleAtFixedRate(new MyTimerTask(), delay, period);  
	    }  
	public static class MyTimerTask extends TimerTask {  
	    @Override  
	    public void run() {  
	        System.out.println("定时器开始执行任务……" + new Date());  
	    }  
	}  
}
