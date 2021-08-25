package com.bwee.webit.stats.db.converter;

import com.bwee.webit.stats.model.UserAction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class IntToUserActionConverter implements Converter<Integer, UserAction> {

    @Override
    public UserAction convert(final Integer code) {
        if (code == null) {
            return null;
        }

        return UserAction.findByCode(code);
    }
}
