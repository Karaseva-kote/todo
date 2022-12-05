package ru.karaseva.sd.mvc.dao;

import org.apache.commons.text.StringSubstitutor;
import ru.karaseva.sd.mvc.model.Case;
import ru.karaseva.sd.mvc.model.ToDoList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ToDoListJdbcDao implements ToDoListDao {
	private static final String dbUrl = "jdbc:sqlite:test.db";
	private static final SqlCompanion SQL = SqlCompanion.forClass(ToDoListJdbcDao.class);
	private static final String createListsQuery = SQL.getRequiredQuery("CREATE_LISTS");
	private static final String createCasesQuery = SQL.getRequiredQuery("CREATE_CASES");
	private static final String addListQuery = SQL.getRequiredQuery("ADD_LIST");
	private static final String addCaseQuery = SQL.getRequiredQuery("ADD_CASE");
	private static final String deleteListQuery = SQL.getRequiredQuery("DELETE_LIST");
	private static final String deleteCaseQuery = SQL.getRequiredQuery("DELETE_CASE");
	private static final String deleteCaseByListQuery = SQL.getRequiredQuery("DELETE_CASE_BY_LIST");
	private static final String showListsQuery = SQL.getRequiredQuery("SHOW_LISTS");
	private static final String showCasesOfListQuery = SQL.getRequiredQuery("SHOW_CASES_OF_LIST");

	public ToDoListJdbcDao() {
		try {
			doActionDB(statement -> {
				statement.executeUpdate(createListsQuery);
				statement.executeUpdate(createCasesQuery);
			});
		} catch (SQLException e) {
			throw new RuntimeException("Exception during creating tables", e);
		}
	}

	@Override
	public List<ToDoList> showLists() {
		List<ToDoList> lists = new ArrayList<>();
		try {
			doActionDB(statement -> {
				ResultSet result = statement.executeQuery(showListsQuery);

				while (result.next()) {
					int id = result.getInt("id");
					String name = result.getString("name");

					lists.add(new ToDoList(id, name));
				}
			});
		} catch (SQLException e) {
			throw new RuntimeException("Exception during getting lists", e);
		}
		return lists;
	}

	@Override
	public void addList(String name) {
		Map<String, Object> substitutions = Map.of("name", name);
		String query = new StringSubstitutor(substitutions, "{{", "}}").replace(addListQuery);
		try {
			doActionDB(statement -> statement.executeUpdate(query));
		} catch (SQLException e) {
			throw new RuntimeException("Exception during adding list", e);
		}
	}

	@Override
	public void deleteList(int id) {
		Map<String, Object> substitutions = Map.of("id", id);
		String toDoListQuery = new StringSubstitutor(substitutions, "{{", "}}").replace(deleteListQuery);
		String caseQuery = new StringSubstitutor(substitutions, "{{", "}}").replace(deleteCaseByListQuery);
		try {
			doActionDB(statement -> {
				statement.executeUpdate(toDoListQuery);
				statement.executeUpdate(caseQuery);
			});
		} catch (SQLException e) {
			throw new RuntimeException("Exception during deleting list", e);
		}
	}

	@Override
	public List<Case> showCasesOfList(int listId) {
		List<Case> cases = new ArrayList<>();
		Map<String, Object> substitutions = Map.of("id", listId);
		String query = new StringSubstitutor(substitutions, "{{", "}}").replace(showCasesOfListQuery);
		try {
			doActionDB(statement -> {
				ResultSet result = statement.executeQuery(query);

				while (result.next()) {
					int id = result.getInt("id");
					String description = result.getString("description");

					cases.add(new Case(id, description));
				}
			});
		} catch (SQLException e) {
			throw new RuntimeException("Exception during getting lists", e);
		}
		return cases;
	}

	@Override
	public void addCase(String caseDescription, int listId) {
		Map<String, Object> substitutions = Map.of(
				"description", caseDescription,
				"listId", listId
		);
		String query = new StringSubstitutor(substitutions, "{{", "}}").replace(addCaseQuery);
		try {
			doActionDB(statement -> statement.executeUpdate(query));
		} catch (SQLException e) {
			throw new RuntimeException("Exception during adding case", e);
		}
	}

	@Override
	public void done(int id) {
		Map<String, Object> substitutions = Map.of("id", id);
		String query = new StringSubstitutor(substitutions, "{{", "}}").replace(deleteCaseQuery);
		try {
			doActionDB(statement -> statement.executeUpdate(query));
		} catch (SQLException e) {
			throw new RuntimeException("Exception during deleting case", e);
		}
	}

	private static void doActionDB(ThrowingConsumer<Statement> consumer) throws SQLException {
		try (Connection c = DriverManager.getConnection(dbUrl)) {
			try (Statement statement = c.createStatement()) {
				consumer.accept(statement);
			}
		}
	}

	@FunctionalInterface
	public interface ThrowingConsumer<T> extends Consumer<T> {

		@Override
		default void accept(final T elem) {
			try {
				acceptThrows(elem);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}

		void acceptThrows(T elem) throws Exception;
	}
}
