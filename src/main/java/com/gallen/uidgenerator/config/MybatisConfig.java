package com.gallen.uidgenerator.config;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * 描述
 *
 * @author Gallen - 2020/4/1
 */
@Configuration
@MapperScan("com.baidu.fsg.uid.worker.dao")
public class MybatisConfig {

    /**
     * 注意这里是uid-generator jar中的mapper，所以需要使用 classpath*
     */
    private static final String MAPPER_LOCATION = "classpath*:META-INF/mybatis/mapper/*.xml";

    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
            return sqlSessionFactoryBean.getObject();
        } catch (IOException e) {
            throw new RuntimeException("sqlSessionFactoryBean error", e);
        } catch (Exception e) {
            throw new RuntimeException("sqlSessionFactoryBean getObject error", e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        return new SqlSessionTemplate(sqlSessionFactoryBean(), ExecutorType.BATCH);
    }
}
