package com.equator.linker.service.util;


import com.equator.inf.core.model.exception.VerifyException;

public class EnumUtil {
    public static Enum findEnumByVal(Enum[] enumArr, int val) {
        for (Enum anEnum : enumArr) {
            if (anEnum.ordinal() == val) {
                return anEnum;
            }
        }
        throw new VerifyException("找不到对应的枚举值");
    }
}
