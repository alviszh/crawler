package app.entity.contact;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zmy on 2018/1/19.
 */
public class PageInfo<T> implements Serializable{
    private int number; //当前页
    private int size;   //页大小
    private long totalElements; //全部记录数
    private List<T> content;

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

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

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "number=" + number +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", content=" + content +
                '}';
    }
}
