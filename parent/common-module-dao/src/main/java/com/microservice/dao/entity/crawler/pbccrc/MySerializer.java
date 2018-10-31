package com.microservice.dao.entity.crawler.pbccrc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by zmy on 2017/12/29.
 */
public class MySerializer extends JsonSerializer<CreditCardRecordDetail> {
    @Override
    public void serialize(CreditCardRecordDetail value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("mapping_id", value.getMapping_id());
        jgen.writeStringField("report_no", value.getReport_no());
        jgen.writeNumberField("recordDetail_autoId", value.getCreditRecordDetail().getAuto_id());
        jgen.writeEndObject();
    }
}