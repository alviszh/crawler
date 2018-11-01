package app.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_commerceBasicUser;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceBasicUserRepository;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.e_commerce.basic"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.e_commerce.basic"})
public class E_CommerceTaskService {

    @Autowired
    private TracerLog tracer;

    @Autowired
    private E_CommerceTaskRepository taskE_CommerceRepository;

    @Autowired
    private E_CommerceBasicUserRepository e_commerceBasicUserRepository;

    /**
     * @param e_commerceJsonBean
     * @return TaskInsurance
     * @Description: 创建用户及生成taskID
     */
    public E_CommerceTask createTask(E_CommerceJsonBean e_commerceJsonBean) {
        E_CommerceTask e_commerceTask = new E_CommerceTask();
        if (null == e_commerceJsonBean) {
            tracer.addTag("E_CommerceTaskService createTask", "E_CommerceJsonBean is null !");
            throw new RuntimeException("E_CommerceJsonBean is null");
        } else if (null == e_commerceJsonBean.getUsername()) {
            tracer.addTag("E_CommerceTaskService createTask", "E_CommerceJsonBean username is null !");
            throw new RuntimeException("E_CommerceJsonBean field(username) is null");
        } else {
            E_commerceBasicUser basicE_commerceUser = e_commerceBasicUserRepository.findByNameAndIdnum(e_commerceJsonBean.getUsername(), e_commerceJsonBean.getIdnum());
            if (null == basicE_commerceUser) {
                basicE_commerceUser = new E_commerceBasicUser();
                basicE_commerceUser.setIdnum(e_commerceJsonBean.getIdnum());
                basicE_commerceUser.setName(e_commerceJsonBean.getUsername());
                e_commerceBasicUserRepository.save(basicE_commerceUser);
                tracer.addTag("用户不存在   :", basicE_commerceUser.toString());

                String uuid = UUID.randomUUID().toString();
                e_commerceTask.setTaskid(uuid);
                e_commerceTask.setBasicUser(basicE_commerceUser);
                e_commerceTask.setWebsiteType(e_commerceJsonBean.getLogintype());
                e_commerceTask.setBasicUser(basicE_commerceUser);
                e_commerceTask = taskE_CommerceRepository.save(e_commerceTask);
                tracer.addTag("taskInsurance 生成taskid  :", e_commerceTask.getTaskid());
            } else {
                tracer.addTag("用户已存在   :", basicE_commerceUser.toString());
                String uuid = UUID.randomUUID().toString();
                e_commerceTask.setTaskid(uuid);
                e_commerceTask.setBasicUser(basicE_commerceUser);
                e_commerceTask.setWebsiteType(e_commerceJsonBean.getLogintype());
                e_commerceTask = taskE_CommerceRepository.save(e_commerceTask);
                tracer.addTag("taskInsurance 生成taskid  :", e_commerceTask.getTaskid());
            }
        }
        return e_commerceTask;
    }

    public E_CommerceTask getE_CommerceTask(String taskid) {
        tracer.addTag("InsuranceTaskService getTaskInsurance", taskid);
        E_CommerceTask task = taskE_CommerceRepository.findByTaskid(taskid);
        return task;
    }
}
