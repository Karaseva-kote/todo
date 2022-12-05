package ru.karaseva.sd.mvc.dao;

import ru.karaseva.sd.mvc.model.Case;
import ru.karaseva.sd.mvc.model.ToDoList;

import java.util.ArrayList;
import java.util.List;

public class ToDoListInMemoryDao implements ToDoListDao {
	private int lastListId = 0;
	private int lastCaseId = 0;
	private final List<ToDoList> lists = new ArrayList<>();

	@Override
	public List<ToDoList> showLists() {
		return lists;
	}

	@Override
	public void addList(String name) {
		lists.add(new ToDoList(lastListId, name));
		lastListId++;
	}

	@Override
	public void deleteList(int id) {
		lists.removeIf(list -> list.getId() == id);
	}

	@Override
	public List<Case> showCasesOfList(int listId) {
		ToDoList list = lists.stream().filter(l -> l.getId() == listId).findFirst().orElse(null);
		return list == null? null : list.getCases();
	}

	@Override
	public void addCase(String caseDescription, int listId) {
		lists.stream()
				.filter(l -> l.getId() == listId)
				.findFirst()
				.ifPresent(list -> list.addCase(new Case(lastCaseId,caseDescription)));
		lastCaseId++;
	}

	@Override
	public void done(int id) {
		lists.forEach(list -> list.deleteCase(id));
	}
}
