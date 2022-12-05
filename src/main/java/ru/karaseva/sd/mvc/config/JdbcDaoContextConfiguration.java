package ru.karaseva.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.karaseva.sd.mvc.dao.ToDoListJdbcDao;

@Configuration
public class JdbcDaoContextConfiguration {
	@Bean
	public ToDoListJdbcDao toDoListJdbcDao(){
		return new ToDoListJdbcDao();
	}
}
