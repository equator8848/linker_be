package com.equator.linker.service.version;

import com.equator.core.model.exception.PreCondition;
import com.equator.core.model.exception.VerifyException;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbInstance;
import org.apache.commons.lang3.StringUtils;

public class ImageVersionComparableVersionGenerator implements ImageVersionGenerator {
    @Override
    public Integer getImageVersionType() {
        return ModelStatus.ImageVersionType.COMPARABLE_VERSION;
    }

    private static final String VERSION_PATTERN = "\\d+\\.\\d+\\.\\d+";

    @Override
    public void validate(String initVersion) {
        if (StringUtils.isBlank(initVersion)) {
            throw new VerifyException("初始镜像版本不能为空");
        }
        PreCondition.isTrue(initVersion.matches(VERSION_PATTERN), "版本必须是a.b.c形式，其中a、b、c都必须是数字");
    }

    @Override
    public String genNextVersion(TbInstance tbInstance) {
        return incrementVersion(tbInstance.getImageVersion());
    }


    public static String incrementVersion(String version) {
        long nextVersionNumber = dottedDecimalNotationToNumber(version) + 1;
        PreCondition.isTrue(nextVersionNumber <= 999999999, "版本超出最大值");
        return numberToDottedDecimalNotation(nextVersionNumber);
    }

    /**
     * 点分十进制版本转整数
     *
     * @param input
     * @return
     */
    public static long dottedDecimalNotationToNumber(String input) {
        String[] nums = input.split("\\.");
        long res = 0;
        for (int i = 0; i < 3; i++) {
            res = res * 1000 + Integer.parseInt(nums[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(dottedDecimalNotationToNumber("999.999.999"));
    }

    /**
     * 整数转点分十进制版本
     *
     * @param input
     * @return
     */
    public static String numberToDottedDecimalNotation(Long input) {
        long num = input;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            res.insert(0, num % 1000 + ".");
            num /= 1000;
        }
        return res.substring(0, res.length() - 1);
    }


}
