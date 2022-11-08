package com.example.budget.services.EntityConverter;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import java.sql.Date;

public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(
            YearMonth attribute) {
        if (attribute != null) {
            return Date.valueOf(
                    attribute.atDay(1));
        }
        return null;
    }

    @Override
    public YearMonth convertToEntityAttribute(
            Date dbData) {
        if (dbData != null) {
            return YearMonth.from(
                    Instant
                            .ofEpochMilli(dbData.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
        }
        return null;
    }
}
