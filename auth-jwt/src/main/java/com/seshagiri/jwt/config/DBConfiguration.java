package com.seshagiri.jwt.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration()
public class DBConfiguration {
	
	//@Value("")
	
	@Bean
	public DataSource dataSource() {
	    HikariDataSource dataSource = new HikariDataSource();
	    dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/auth_retaildb_user");
	    dataSource.setUsername("root");
	    dataSource.setPassword("Seshagiri#21");
	    // set other properties here
	    return dataSource;
	}

}
