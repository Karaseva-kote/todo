package ru.karaseva.sd.mvc.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.karaseva.sd.mvc.dao.ToDoListDao;
import ru.karaseva.sd.mvc.model.ToDoList;

@Controller
class CasesController {
	private final ToDoListDao dao;
	private int currentListId;

	public CasesController(@Qualifier("toDoListInMemoryDao") ToDoListDao dao) {
		this.dao = dao;
	}

	@GetMapping("/")
	public String cases(ModelMap modelMap) {
		ToDoList currentList = dao.showLists().stream()
				.filter(list -> list.getId() == currentListId)
				.findFirst()
				.orElse(null);
		modelMap.addAttribute("cases", dao.showCasesOfList(currentListId));
		modelMap.addAttribute("currList", currentList);
		modelMap.addAttribute("lists", dao.showLists());
		return "cases";
	}

	@PostMapping("/setCase")
	public String cases(@RequestParam("listId") int listId) {
		currentListId = listId;
		return "redirect:/";
	}

	@PostMapping("/updateCase")
	public String update(@RequestParam("caseId") int caseId) {
		dao.done(caseId);
		return "redirect:/";
	}

	@RequestMapping(value = "/addCase", method = RequestMethod.POST)
	public String add(@RequestParam("description") String description) {
		dao.addCase(description, currentListId);
		return "redirect:/";
	}
}
