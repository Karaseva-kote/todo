package ru.karaseva.sd.mvc.dao;

import ru.karaseva.sd.mvc.model.Case;
import ru.karaseva.sd.mvc.model.ToDoList;

import java.util.List;

public interface ToDoListDao {
	List<ToDoList> showLists();
	void addList(String name);
	void deleteList(int id);

	List<Case> showCasesOfList(int listId);
	void addCase(String caseDescription, int listId);
	void done(int id);
}
