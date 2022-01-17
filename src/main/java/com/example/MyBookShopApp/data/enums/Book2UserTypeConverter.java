package com.example.MyBookShopApp.data.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class Book2UserTypeConverter implements AttributeConverter<Book2UserType, String> {
    @Override
    public String convertToDatabaseColumn(Book2UserType type) {
        if (type == null) {
            return null;
        }
        return type.toString();
    }

    @Override
    public Book2UserType convertToEntityAttribute(String typeValue) {
        if (typeValue == null) {
            return null;
        }

        return Stream.of(Book2UserType.values())
                .filter(c -> c.toString().equals(typeValue))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
