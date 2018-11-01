package app.bean.mobsec;

/**
 * Created by Administrator on 2018/9/11.
 */
public class Area {
    String city; //城市

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Area{" +
                "city='" + city + '\'' +
                '}';
    }
}
