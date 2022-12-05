package ru.karaseva.sd.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
	private final int id;
	private final String name;
	private final List<Case> cases = new ArrayList<>();

	public ToDoList(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Case> getCases() {
		return cases;
	}

	public void addCase(Case newCase) {
		cases.add(newCase);
	}

	public void deleteCase(int id) {
		cases.removeIf(c -> c.getId() == id);
	}
}
