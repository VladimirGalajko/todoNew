package com.example.todoNew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;



@SpringBootApplication
public class TodoNewApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoNewApplication.class, args);

		Scanner scanner = new Scanner(System.in);

		TaskManager taskManager = new TaskManager();

		while (true) {
			System.out.println("Выберите действие:");
			System.out.println("1. Просмотр задач");
			System.out.println("2. Создание задачи");
			System.out.println("3. Удаление задачи");
			System.out.println("4. Редактирование задачи");
			System.out.println("5. Изменение статуса выполнения задачи");
			System.out.println("0. Выход");

			int choice = scanner.nextInt();

			switch (choice) {
				case 1:
					taskManager.viewTasks();
					break;
				case 2:
					taskManager.createTask();
					break;
				case 3:
					taskManager.deleteTask();
					break;
				case 4:
					taskManager.editTask();
					break;
				case 5:
					taskManager.changeTaskStatus();
					break;
				case 0:
					taskManager.saveTasks();
					System.exit(0);
					break;
				default:
					System.out.println("Неверный ввод. Пожалуйста, выберите корректное действие.");
			}
		}
	}
}
