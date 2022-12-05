package ru.karaseva.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.karaseva.sd.mvc.dao.ToDoListInMemoryDao;

@Configuration
public class InMemoryDaoContextConfiguration {
	@Bean
	public ToDoListInMemoryDao toDoListInMemoryDao(){
		return new ToDoListInMemoryDao();
	}
}
