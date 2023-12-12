package xyz.equator8848.linker.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Equator
 * @Date: 2021/11/21 23:36
 **/
@Data
@Configuration
public class SecurityConfiguration {
    /**
     * DES 对称加密密钥
     */
    @Value("${linker.security.des-key}")
    private String desKey;

}
