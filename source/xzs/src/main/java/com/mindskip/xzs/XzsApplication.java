package com.mindskip.xzs;

import com.mindskip.xzs.configuration.property.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 武汉思维跳跃科技有限公司
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(value = { SystemConfig.class})
@EnableCaching
public class XzsApplication {

    public static void main(String[] args) {
        SpringApplication.run(XzsApplication.class, args);
    }
}


//        增加 **考试----用户**关联表，关系为多对多。
//        考虑到**指定参加考试的用户**时，用户信息可能还没有录入进系统，所以关联表中用户的唯一标识为**手机号**。
//        要求用户在注册账号时一定要填写手机号，并且后台要对手机号进行**唯一性**认证。
//        前端数据