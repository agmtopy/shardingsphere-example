package com.agmtopy.shardingsphere.example.javacore;

import com.agmtopy.shardingsphere.example.entity.User;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * java api设置shardingsphere配置
 */
public class JavaTemplate {

    public static void main(String[] args) throws SQLException {
        JavaTemplate javaTemplate = new JavaTemplate();
        DataSource dataSource = javaTemplate.dataSource();
        Connection connection = dataSource.getConnection();
        String sql = "SELECT * FROM t_user WHERE id = 74";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user = User.builder()
                    .id(resultSet.getInt(1))
                    .vender_id(resultSet.getString(2))
                    .user_name(resultSet.getString(3))
                    .pass_word(resultSet.getString(4))
                    .build();
            System.out.println(user);
        }
    }

    /**
     * 创建DataSourceHelper
     */
    static class DataSourceHelper {
        public static DataSource createDataSource(final String dataSourceName) {
            String url = String.format("jdbc:mysql://127.0.0.1:3306/%s?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT", dataSourceName);
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername("root");
            hikariConfig.setPassword("111111");
            return new HikariDataSource(hikariConfig);
        }
    }

    /**
     * 创建DataSource
     */
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = Maps.newHashMap();
        result.put("ds0", DataSourceHelper.createDataSource("ds0"));
        result.put("ds1", DataSourceHelper.createDataSource("ds1"));
        return result;
    }

    public DataSource dataSource() throws SQLException {
        //创建分片规则配置类
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        //创建分表规则配置类
        TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration("t_user", "ds${0..1}.t_user");

        //创建分布式主键生成配置类
        Properties properties = new Properties();
        properties.setProperty("worker.id", "33");
        KeyGeneratorConfiguration keyGeneratorConfig = new KeyGeneratorConfiguration("SNOWFLAKE", "id", properties);
        tableRuleConfig.setKeyGeneratorConfig(keyGeneratorConfig);
        shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);

        //根据性别分库
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds${id%2}"));

        //创建具体的DataSource
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }
}
