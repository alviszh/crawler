package app.bean;

import com.microservice.dao.entity.crawler.pbccrc.*;

import java.io.Serializable;
import java.util.List;

/**
 * 公共记录
 * 包含：最近5年内的欠税记录、民事判决记录、强制执行记录、行政处罚记录及电信欠费记录
 * Created by zmy on 2018/1/3.
 */
public class PublicRecord implements Serializable {

    private static final long serialVersionUID = -4757836919670006412L;

    public List<PublicAdministrativePunishmen> publicAdministrativePunishmens; //行政处罚记录

    public List<PublicTaxesOwed> publicTaxesOweds;  //欠税记录

    public List<PublicCivilJudgment> publicCivilJudgments;  //民事判决记录

    public List<PublicEnforcedRecord> publicEnforcedRecords;  //强制执行记录

    public List<PublicTeleArrearsRecord> publicTeleArrearsRecords;  //电信欠费记录

    public List<PublicAdministrativePunishmen> getPublicAdministrativePunishmens() {
        return publicAdministrativePunishmens;
    }

    public void setPublicAdministrativePunishmens(List<PublicAdministrativePunishmen> publicAdministrativePunishmens) {
        this.publicAdministrativePunishmens = publicAdministrativePunishmens;
    }

    public List<PublicTaxesOwed> getPublicTaxesOweds() {
        return publicTaxesOweds;
    }

    public void setPublicTaxesOweds(List<PublicTaxesOwed> publicTaxesOweds) {
        this.publicTaxesOweds = publicTaxesOweds;
    }

    public List<PublicCivilJudgment> getPublicCivilJudgments() {
        return publicCivilJudgments;
    }

    public void setPublicCivilJudgments(List<PublicCivilJudgment> publicCivilJudgments) {
        this.publicCivilJudgments = publicCivilJudgments;
    }

    public List<PublicEnforcedRecord> getPublicEnforcedRecords() {
        return publicEnforcedRecords;
    }

    public void setPublicEnforcedRecords(List<PublicEnforcedRecord> publicEnforcedRecords) {
        this.publicEnforcedRecords = publicEnforcedRecords;
    }

    public List<PublicTeleArrearsRecord> getPublicTeleArrearsRecords() {
        return publicTeleArrearsRecords;
    }

    public void setPublicTeleArrearsRecords(List<PublicTeleArrearsRecord> publicTeleArrearsRecords) {
        this.publicTeleArrearsRecords = publicTeleArrearsRecords;
    }

    @Override
    public String toString() {
        return "PublicRecord{" +
                "publicAdministrativePunishmens=" + publicAdministrativePunishmens +
                ", publicTaxesOweds=" + publicTaxesOweds +
                ", publicCivilJudgments=" + publicCivilJudgments +
                ", publicEnforcedRecords=" + publicEnforcedRecords +
                ", publicTeleArrearsRecords=" + publicTeleArrearsRecords +
                '}';
    }
}
