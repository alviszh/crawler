package com.microservice.dao.entity.crawler.e_commerce.taobao;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_tb_user_info")
public class TbUserInfo extends IdEntity implements Serializable {

    private String taskid;
    private String loginName;
    private String nickName;
    private String realname;
    private String gender;
    private String birthday;
    private String divisionCode;//居住地行政区划代码
    private String liveDivisionCode;//家乡行政区划代码

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getLiveDivisionCode() {
		return liveDivisionCode;
	}

	public void setLiveDivisionCode(String liveDivisionCode) {
		this.liveDivisionCode = liveDivisionCode;
	}

	@Override
	public String toString() {
		return "TbUserInfo [taskid=" + taskid + ", loginName=" + loginName + ", nickName=" + nickName + ", realname="
				+ realname + ", gender=" + gender + ", birthday=" + birthday + ", divisionCode=" + divisionCode
				+ ", liveDivisionCode=" + liveDivisionCode + "]";
	}
    
}
