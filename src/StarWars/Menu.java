package StarWars;

import StarWars.Entities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Показать всех персонажей");
            System.out.println("2. Добавить персонажа");
            System.out.println("3. Изменить информацию о персонаже");
            System.out.println("4. Удалить персонажа из списка");
            System.out.println("5. Другие действия с персонажами");
            System.out.println("6. Загрузить персонажей из файла");
            System.out.println("7. Вывести персонажей по параметрам");
            System.out.println("8. Выход");
            System.out.println("Выберите нужный пункт меню: ");

            int choice = getValidIntegerInput();
            switch (choice) {
                case 1 -> EntityManager.showAllEntities();
                case 2 -> EntityManager.addEntity();
                case 3 -> updateEntityInfo();
                case 4 ->{
                    scanner.nextLine();
                    System.out.println("Введите имя персонажа для удаления или тип персонажа (jedi, sith, clone, droid и т.д.) для массового удаления: ");
                    String input = scanner.nextLine().trim(); // Убираем лишние пробелы из ввода
                    EntityManager.removeEntity(input);
                }

                case 5 -> {
                    scanner.nextLine();
                    System.out.println("Введите имя персонажа для взаимодействия: ");
                    String name = scanner.nextLine();
                    EntityManager.interactWithEntity(name);
                }
                case 6 -> {
                    System.out.println("Введите тип сущностей для загрузки (jedi, sith, clone, droid) или оставьте пустым для загрузки всех:");
                    scanner.nextLine();
                    String filterType = scanner.nextLine().trim();
                    if (filterType.isEmpty()) filterType = null;
                    EntityManager.loadEntitiesFromFile(filterType);
                }
                case 7 -> showEntitiesByParameters();

                case 8 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static int getValidIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка ввода! Пожалуйста, введите целое число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void updateEntityInfo() {
        scanner.nextLine(); // Очистка буфера
        System.out.println("Введите имя персонажа для обновления информации: ");
        String name = scanner.nextLine();

        Entity entity = EntityManager.findEntityByName(name);
        if (entity == null) {
            System.out.println("Персонаж не найден.");
            return;
        }

        System.out.println("Что хотите изменить?");
        if (entity instanceof Jedi) {
            System.out.println("1. Имя");
            System.out.println("2. Фракцию");
            System.out.println("3. Возраст");
            System.out.println("4. Планету");
            System.out.println("5. Уровень силы");
            System.out.println("6. Цвет светового меча");
            System.out.println("7. Уровень владения силой");
            System.out.println("8. Гранд-мастер");
        } else if (entity instanceof Sith) {
            System.out.println("1. Имя");
            System.out.println("2. Фракцию");
            System.out.println("3. Возраст");
            System.out.println("4. Планету");
            System.out.println("5. Уровень силы");
            System.out.println("6. Цвет светового меча");
            System.out.println("7. Уровень владения темной стороной");
            System.out.println("8. Мастер ситхов");
        } else if (entity instanceof CloneTrooper) {
            System.out.println("1. Имя");
            System.out.println("2. Фракцию");
            System.out.println("3. Возраст");
            System.out.println("4. Планету");
            System.out.println("5. Уровень силы");
            System.out.println("6. Номер клона");
        } else if (entity instanceof Droid) {
            System.out.println("1. Имя");
            System.out.println("2. Фракцию");
            System.out.println("3. Возраст");
            System.out.println("4. Планету");
            System.out.println("5. Уровень силы");
            System.out.println("6. Модель дроида");
            System.out.println("7. Функцию");
            System.out.println("8. Уровень заряда батареи");
        }

        int choice = getValidIntegerInput(); // Используем защищенный метод для выбора пункта меню
        scanner.nextLine(); // Очистка буфера

        EntityManager.update(choice,name);
    }

    private static void showEntitiesByParameters() {
        System.out.println("\nВыберите параметр для фильтрации:");
        System.out.println("1. Тип сущности (Jedi, Sith, Clone, Droid)");
        System.out.println("2. Фракция");
        System.out.println("3. Возраст");
        System.out.println("4. Уровень силы");
        System.out.println("5. Планета");
        System.out.println("6. Вернуться в главное меню");

        int choice = getValidIntegerInput();
        EntityManager.show(choice);
    }
}