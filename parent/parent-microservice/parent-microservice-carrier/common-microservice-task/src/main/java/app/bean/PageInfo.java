package app.bean;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zmy on 2018/1/19.
 */
public class PageInfo implements Serializable{
    private int number;
    private int size;
    private List<TaskMobile> content;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<TaskMobile> getContent() {
        return content;
    }

    public void setContent(List<TaskMobile> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "number=" + number +
                ", size=" + size +
                ", content=" + content +
                '}';
    }
}
