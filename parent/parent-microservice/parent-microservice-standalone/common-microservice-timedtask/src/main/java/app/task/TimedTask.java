package app.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zmy on 2018/6/19.
 */
@Component
public class TimedTask {
    @Autowired
    private TracerLog tracer;
    public final static long TIMEINTERVAL = 60 * 1000;

	@Scheduled(fixedDelay = TIMEINTERVAL)
	public void pbccrcTask(){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = f.format(new Date());
        System.out.println( date +" >>fixedDelay执行....");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
