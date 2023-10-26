package com.equator.linker.model.dto;


import com.equator.core.dynamic.config.CommaSeparatedItem;
import com.equator.core.dynamic.config.ModelTransformerField;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Author: Equator
 * @Date: 2022/8/14 22:49
 **/
@Data
public class DynamicAppConfiguration {
    @ModelTransformerField(nullable = true)
    private long version;

    /**
     * 是否允许用户自行注册
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean allowRegister;

}
