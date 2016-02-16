package com.bluecatcode.common.base;

import javax.annotation.Nullable;

public interface RichEnumTrait<T extends Enum<T> & RichEnumTrait<T>> extends RichEnum<T> {

    T self();

    default RichEnumInstance<T> instance() {
        return RichEnumInstance.richEnum(self());
    }

    @Override
    default boolean nameEquals(@Nullable String that) {
        return instance().nameEquals(that);
    }

    @Override
    default boolean nameEqualsIgnoreCase(@Nullable String that) {
        return instance().nameEqualsIgnoreCase(that);
    }

    @Override
    default boolean nameEqualsIgnoreCaseAndUnderscore(@Nullable String that) {
        return instance().nameEqualsIgnoreCaseAndUnderscore(that);
    }
}
