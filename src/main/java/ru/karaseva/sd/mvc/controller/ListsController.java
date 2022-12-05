package ru.karaseva.sd.mvc.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.karaseva.sd.mvc.dao.ToDoListDao;

@Controller
public class ListsController {
	private final ToDoListDao dao;

	public ListsController(@Qualifier("toDoListInMemoryDao") ToDoListDao dao) {
		this.dao = dao;
	}

	@GetMapping("/manageLists")
	public String manageLists(ModelMap modelMap) {
		modelMap.addAttribute("lists", dao.showLists());
		return "lists";
	}

	@PostMapping("/deleteList")
	public String deleteList(@RequestParam("listId") int listId) {
		dao.deleteList(listId);
		return "redirect:/manageLists";
	}

	@PostMapping("/addList")
	public String addList(@RequestParam("listName") String listName) {
		dao.addList(listName);
		return "redirect:/manageLists";
	}
}
