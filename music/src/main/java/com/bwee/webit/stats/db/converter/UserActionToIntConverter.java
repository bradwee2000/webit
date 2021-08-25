package com.bwee.webit.stats.db.converter;

import com.bwee.webit.stats.model.UserAction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class UserActionToIntConverter implements Converter<UserAction, Integer> {

    @Override
    public Integer convert(UserAction userAction) {
        if (userAction == null) {
            return null;
        }
        return userAction.getCode();
    }
}
