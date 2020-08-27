package com.agmtopy.shardingsphere.example.core;

import com.agmtopy.shardingsphere.example.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * springBoot start 注入方式
 */
@Component
public class SpringBootTemplate {
    @Resource
    private DataSource dataSource;

    @PostConstruct
    public void initMethod() throws SQLException {
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
}
