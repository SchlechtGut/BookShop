package com.example.MyBookShopApp.data.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class Book2UserTypeConverter implements AttributeConverter<Book2UserType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Book2UserType type) {
        if (type == null) {
            return null;
        }
        return type.ordinal() + 1;
    }

    @Override
    public Book2UserType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return Stream.of(Book2UserType.values())
                .filter(c -> value.equals(c.ordinal() + 1))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
