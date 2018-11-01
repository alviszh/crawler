package app.service;

import app.commontracerlog.TracerLog;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class CrawlerStatusStandaloneService {

    @Autowired
    TaskStandaloneRepository taskStandaloneRepository;
    @Autowired
    private TracerLog tracerLog;

    /**
     * @Description 爬取完成后更新TaskStandalone
     * @param taskId
     */
    public TaskStandalone updateTaskFinished(String taskId) {
        tracerLog.addTag("updateTaskFinished", "CrawlerStatusStandaloneService updateTaskFinished");
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(taskId);

        tracerLog.addTag("updateTaskFinished taskStandalone to String", taskStandalone.toString());
        if (null != taskStandalone) {
            taskStandalone.setPhase(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhase());
            taskStandalone.setPhase_status(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhasestatus());
            taskStandalone.setDescription(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getDescription());
            taskStandalone.setCode(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getCode());
            taskStandalone.setFinished(true);
            taskStandalone = taskStandaloneRepository.save(taskStandalone);
            tracerLog.addTag("updateTaskFinished", "updateTaskFinished success");

//            crawlerImpl.getAllDataDone(taskId);

        }else{
            tracerLog.addTag("updateTaskFinished", "此时还有数据没有爬取完成，故暂未更新最终爬取状态");
        }
        return taskStandalone;
    }

    public TaskStandalone changeStatus(String phase, String phasestatus, String description, Integer code,
                                       boolean finished, String taskid){
        tracerLog.addTag("change task status", description);
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(taskid);
        tracerLog.addTag("changeStatus.taskStandalone", taskStandalone.toString());
        taskStandalone.setPhase(phase);
        taskStandalone.setPhase_status(phasestatus);
        taskStandalone.setDescription(description);
        taskStandalone.setCode(code);
        taskStandalone.setFinished(finished);
        taskStandaloneRepository.save(taskStandalone);

        return taskStandalone;
    }


}
