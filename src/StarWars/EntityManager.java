package StarWars;

import java.io.*;
import java.util.InputMismatchException;
import java.util.*;
import StarWars.Entities.*;
import java.util.ArrayList;
import java.util.List;


public class EntityManager {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Entity> entities = new ArrayList<>();

    public static void menu() {
        // Создание персонажей
        Jedi obiWan = new Jedi("Оби-Ван Кеноби", "Республика", 57, "Стижон", 9000, "Синий", 95, true);
        Sith darthVader = new Sith("Дарт Вейдер", "Империя", 46, "Татуин", 9500, "Тёмный разряд", 95, true);
        CloneTrooper rex = new CloneTrooper("Капитан Рекс", "Республика", 12, "Эндор", 2000, "CT-7567");
        Droid r2d2 = new Droid("R2-D2", "Республика", 66, "Набу", 50, "Астромех", "Поддержка", 100);

        // Добавление персонажей в список
        entities.add(obiWan);
        entities.add(darthVader);
        entities.add(rex);
        entities.add(r2d2);
        Menu.showMenu();
    }

    public static void showAllEntities() {
        if (entities.isEmpty()) {
            System.out.println("Нет персонажей в списке.");
        } else {
            for (Entity entity : entities) {
                System.out.println(entity.toString());
            }
        }
    }
    // Добавление сущности
    public static void addEntity() {
        System.out.println("Выберите тип персонажа которого хотите добавить");
        System.out.println("1. Джедай");
        System.out.println("2. Ситх");
        System.out.println("3. Клон");
        System.out.println("4. Дроид");
        System.out.println("5. Вернуться в главное меню");
        System.out.println("Введите нужный пункт:");

        int type = getValidIntegerInput();
        String entityType = "";
        Entity newEntity = null;
        switch (type) {
            case 1:
                newEntity = createJedi();
                if (isNameExists(newEntity.getName())){
                    entities.remove(newEntity);
                }
                else{
                    entityType = "Jedi";
                }
                break;
            case 2:
                newEntity = createSith();
                if (isNameExists(newEntity.getName())){
                    entities.remove(newEntity);
                }
                else{
                    entityType = "Sith";
                }

                break;
            case 3:
                newEntity = createClone();
                if (isNameExists(newEntity.getName())){
                    entities.remove(newEntity);
                }
                else{
                    entityType = "Clone";
                }

                break;
            case 4:
                newEntity = createDroid();
                if (isNameExists(newEntity.getName())){
                    entities.remove(newEntity);
                }
                else{
                    entityType = "Droid";
                }
            case 5:

                break;
            default:
                System.out.println("Неверный выбор.");
        }

        if (newEntity != null) {
            entities.add(newEntity);
            System.out.println(newEntity.getName() + " добавлен(а).");
            // Запись персонажа в файл
            writeEntityToFile(newEntity, entityType);
        }
    }

    private static void writeEntityToFile(Entity entity, String entityType) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("entities.txt", true))) {
            // Записываем только значения параметров, без пояснений

            writer.write(entityType + "\n");  // Записываем тип сущности
            writer.write(entity.getName() + "\n");  // Имя
            writer.write(entity.getFraction() + "\n");  // Фракция
            writer.write(entity.getAge() + "\n");  // Возраст
            writer.write(entity.getPlanet() + "\n");  // Планета
            writer.write(entity.getPowerLevel() + "\n");  // Уровень силы

            // Дополнительные данные в зависимости от типа сущности
            if (entity instanceof Jedi) {
                writer.write(((Jedi) entity).getLightsaberColor() + "\n");  // Цвет светового меча
                writer.write(((Jedi) entity).getForceLevel() + "\n");  // Уровень владения силой
                writer.write(((Jedi) entity).isGrandMaster() + "\n");  // Гранд-мастер
            } else if (entity instanceof Sith) {
                writer.write(((Sith) entity).getLightsaberColor() + "\n");  // Цвет светового меча
                writer.write(((Sith) entity).getForceLevel() + "\n");  // Уровень владения темной стороной
                writer.write(((Sith) entity).isMaster() + "\n");  // Мастер ситхов
            } else if (entity instanceof CloneTrooper) {
                writer.write(((CloneTrooper) entity).getCloneNumber() + "\n");  // Номер клона
            } else if (entity instanceof Droid) {
                writer.write(((Droid) entity).getModelType() + "\n");  // Модель дроида
                writer.write(((Droid) entity).getFunction() + "\n");  // Функция
                writer.write(((Droid) entity).getBatteryLevel() + "\n");  // Уровень заряда батареи
            }

            writer.write("\n"); // Добавляем пустую строку после каждого персонажа
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static void loadEntitiesFromFile(String filterType) {
        if (filterType != null && !isValidEntityType(filterType)) {
            System.out.println("Неизвестный тип сущности: " + filterType);
            return; // Выход из метода, если тип некорректный
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("entities.txt"))) {
            String line;
            Entity entity = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Убираем лишние пробелы

                if (line.isEmpty()) continue; // Пропускаем пустые строки

                if (entity == null) {
                    String entityType = line.toLowerCase();
                    if (filterType != null && !entityType.equals(filterType.toLowerCase())) {
                        continue; // Пропускаем ненужные типы
                    }

                    // Создаём объект соответствующего типа
                    switch (entityType) {
                        case "jedi":
                            entity = new Jedi("", "", 0, "", 0, "", 0, false);
                            break;
                        case "sith":
                            entity = new Sith("", "", 0, "", 0, "", 0, false);
                            break;
                        case "clone":
                            entity = new CloneTrooper("", "", 0, "", 0, "");
                            break;
                        case "droid":
                            entity = new Droid("", "", 0, "", 0, "", "", 0);
                            break;
                        default:
                            System.out.println("Неизвестный тип сущности: " + entityType);
                            entity = null; // Сбрасываем некорректный объект
                    }
                } else {
                    // Заполняем данные для текущей сущности
                    if (fillEntityData(entity, line)) {
                        if (!isNameExists(entity.getName())) {
                            entities.add(entity); // Добавляем в список только уникальных персонажей
                        }
                        entity = null; // Сбрасываем объект для следующей сущности
                    }
                }
            }

            System.out.println("Персонажи успешно загружены из файла.");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static boolean fillEntityData(Entity entity, String data) {
        if (entity instanceof Jedi) {
            Jedi jedi = (Jedi) entity;
            if (jedi.getName().isEmpty()) {
                jedi.setName(data);
            } else if (jedi.getFraction().isEmpty()) {
                jedi.setFraction(data);
            } else if (jedi.getAge() == 0) {
                jedi.setAge(Integer.parseInt(data));
            } else if (jedi.getPlanet().isEmpty()) {
                jedi.setPlanet(data);
            } else if (jedi.getPowerLevel() == 0) {
                jedi.setPowerLevel(Integer.parseInt(data));
            } else if (jedi.getLightsaberColor().isEmpty()) {
                jedi.setLightsaberColor(data);
            } else if (jedi.getForceLevel() == 0) {
                jedi.setForceLevel(Integer.parseInt(data));
            } else {
                jedi.setIsMaster(Boolean.parseBoolean(data));
                return true; // Объект полностью заполнен
            }
        } else if (entity instanceof Sith) {
            Sith sith = (Sith) entity;
            if (sith.getName().isEmpty()) {
                sith.setName(data);
            } else if (sith.getFraction().isEmpty()) {
                sith.setFraction(data);
            } else if (sith.getAge() == 0) {
                sith.setAge(Integer.parseInt(data));
            } else if (sith.getPlanet().isEmpty()) {
                sith.setPlanet(data);
            } else if (sith.getPowerLevel() == 0) {
                sith.setPowerLevel(Integer.parseInt(data));
            } else if (sith.getLightsaberColor().isEmpty()) {
                sith.setLightsaberColor(data);
            } else if (sith.getForceLevel() == 0) {
                sith.setForceLevel(Integer.parseInt(data));
            } else {
                sith.setIsMaster(Boolean.parseBoolean(data));
                return true; // Объект полностью заполнен
            }
        } else if (entity instanceof CloneTrooper) {
            CloneTrooper clone = (CloneTrooper) entity;
            if (clone.getName().isEmpty()) {
                clone.setName(data);
            } else if (clone.getFraction().isEmpty()) {
                clone.setFraction(data);
            } else if (clone.getAge() == 0) {
                clone.setAge(Integer.parseInt(data));
            } else if (clone.getPlanet().isEmpty()) {
                clone.setPlanet(data);
            } else if (clone.getPowerLevel() == 0) {
                clone.setPowerLevel(Integer.parseInt(data));
            } else {
                clone.setCloneNumber(data);
                return true; // Объект полностью заполнен
            }
        } else if (entity instanceof Droid) {
            Droid droid = (Droid) entity;
            if (droid.getName().isEmpty()) {
                droid.setName(data);
            } else if (droid.getFraction().isEmpty()) {
                droid.setFraction(data);
            } else if (droid.getAge() == 0) {
                droid.setAge(Integer.parseInt(data));
            } else if (droid.getPlanet().isEmpty()) {
                droid.setPlanet(data);
            } else if (droid.getPowerLevel() == 0) {
                droid.setPowerLevel(Integer.parseInt(data));
            } else if (droid.getModelType().isEmpty()) {
                droid.setModelType(data);
            } else if (droid.getFunction().isEmpty()) {
                droid.setFunction(data);
            } else {
                droid.setBatteryLevel(Integer.parseInt(data));
                return true; // Объект полностью заполнен
            }
        }

        return false; // Данные ещё не завершены
    }

    // Удаление сущности
    public static void removeEntity(String input) {
        if (input.equalsIgnoreCase("jedi") ||
                input.equalsIgnoreCase("sith") ||
                input.equalsIgnoreCase("clone") ||
                input.equalsIgnoreCase("droid")) {
            String entityType = input.toLowerCase();
            boolean removed = entities.removeIf(entity -> {
                switch (entityType) {
                    case "jedi" -> {
                        return entity instanceof Jedi;
                    }
                    case "sith" -> {
                        return entity instanceof Sith;
                    }
                    case "clone" -> {
                        return entity instanceof CloneTrooper;
                    }
                    case "droid" -> {
                        return entity instanceof Droid;
                    }
                }
                return false;
            });

            if (removed) {
                System.out.println("Все персонажи типа " + entityType + " удалены.");
            } else {
                System.out.println("Персонажи типа " + entityType + " не найдены.");
            }
        } else {
            Entity entity = findEntityByName(input);
            if (entity != null) {
                entities.remove(entity);
                System.out.println(entity.getName() + " удален.");
            } else {
                System.out.println("Персонаж не найден.");
            }
        }
    }

    // Метод взаимодействия
    public static void interactWithEntity(String name) {
        Entity entity = findEntityByName(name);
        if (entity != null) {
            System.out.println("Что вы хотите сделать?");
            System.out.println("1. Сыграть в Голошахматы");
            System.out.println("2. Вызвать персонажа на поединок");
            System.out.println("3. Дать 10 кредитов");
            System.out.println("4. Отправить выполнять задание");
            System.out.println("5. Вернуться в главное меню");
            int choice = getValidIntegerInput();
            switch (choice) {
                case 1 -> System.out.println("Вы сыграли в Голошахматы с " + entity.getName());
                case 2 -> System.out.println("Вы сразились с " + entity.getName());
                case 3 -> System.out.println("Вы дали 10 кредитов " + entity.getName());
                case 4 -> System.out.println(entity.takeCare());
                case 5 -> {
                }
                default -> System.out.println("Неверный выбор.");
            }
        } else {
            System.out.println("Персонаж не найден.");
        }

    }

    public static boolean isNameExists(String name) {
        for (Entity entity : entities) {
            if (entity.getName().equalsIgnoreCase(name)) {
                return true; // Имя уже существует
            }
        }
        return false; // Имя не найдено
    }

    public static Entity findEntityByName(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private static boolean isValidEntityType(String type) {
        return type.equalsIgnoreCase("jedi") ||
                type.equalsIgnoreCase("sith") ||
                type.equalsIgnoreCase("clone") ||
                type.equalsIgnoreCase("droid");
    }

    private static Jedi createJedi() {

        scanner.nextLine(); // Очистка буфера перед вводом строк
        System.out.println("Введите имя Джедая: ");
        String name = scanner.nextLine();

        System.out.println("Введите фракцию Джедая: ");
        String fraction = scanner.nextLine();

        int age = getValidIntInput("Введите возраст Джедая: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите планету Джедая: ");
        String planet = scanner.nextLine();

        int powerLevel = getValidIntInput("Введите уровень силы Джедая: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите цвет светового меча Джедая: ");
        String lightsaberColor = scanner.nextLine();

        int forceLevel = getValidIntInput("Введите уровень владения силой Джедая: ");

        boolean isMaster = getValidBooleanInput("Является ли Джедай гранд-мастером? (true/false): ");

        if (!isNameExists(name)){
            return new Jedi(name, fraction, age, planet, (int) powerLevel, lightsaberColor, forceLevel, isMaster);
        }
        return null;
    }

    private static Sith createSith() {
        scanner.nextLine(); // Очистка буфера перед вводом строк
        System.out.println("Введите имя Ситха: ");
        String name = scanner.nextLine();

        System.out.println("Введите фракцию Ситха: ");
        String fraction = scanner.nextLine();

        int age = getValidIntInput("Введите возраст Ситха: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите планету Ситха: ");
        String planet = scanner.nextLine();

        int powerLevel = getValidIntInput("Введите уровень силы Ситха: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите цвет светового меча Ситха: ");
        String lightsaberColor = scanner.nextLine();

        int darkSideLevel = getValidIntInput("Введите уровень владения темной стороной: ");

        boolean isMaster = getValidBooleanInput("Является ли Ситх мастером ситхов? (true/false): ");

        if (!isNameExists(name)){
            return new Sith(name, fraction, age, planet, (int) powerLevel, lightsaberColor, darkSideLevel, isMaster);
        }
        return null;
    }

    private static CloneTrooper createClone() {
        scanner.nextLine(); // Очистка буфера перед вводом строк
        System.out.println("Введите имя клона: ");
        String name = scanner.nextLine();

        System.out.println("Введите фракцию клона: ");
        String fraction = scanner.nextLine();

        int age = getValidIntInput("Введите возраст клона: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите планету клона: ");
        String planet = scanner.nextLine();

        int powerLevel = getValidIntInput("Введите уровень силы клона: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите номер клона: ");
        String cloneNumber = scanner.nextLine();

        if (!isNameExists(name)){
            return new CloneTrooper(name, fraction, age, planet, (int) powerLevel, cloneNumber);
        }
        return null;
    }

    private static Droid createDroid() {
        scanner.nextLine(); // Очистка буфера перед вводом строк
        System.out.println("Введите имя дроида: ");
        String name = scanner.nextLine();

        System.out.println("Введите фракцию дроида: ");
        String fraction = scanner.nextLine();

        int age = getValidIntInput("Введите возраст дроида: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите планету дроида: ");
        String planet = scanner.nextLine();

        int powerLevel = getValidIntInput("Введите уровень силы дроида: ");
        scanner.nextLine(); // Очистка буфера

        System.out.println("Введите модель дроида: ");
        String model = scanner.nextLine();

        System.out.println("Введите тип дроида: ");
        String type = scanner.nextLine();

        int batteryLevel = getValidIntInput("Введите уровень заряда батареи дроида: ");
        if (!isNameExists(name)){
            return new Droid(name, fraction, age, planet, powerLevel, model, type, batteryLevel);
        }
        return null;
    }

    private static int getValidIntInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите целое число.");
                scanner.nextLine(); // Очистка буфера
            }
        }
    }

    private static boolean getValidBooleanInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return scanner.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите true или false.");
                scanner.nextLine(); // Очистка буфера
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

    public static void show(int choice){
        List<Entity> filteredEntities = new ArrayList<>();


        switch (choice) {
            case 1 -> {
                System.out.println("Введите тип сущности:");
                String type = new Scanner(System.in).nextLine().trim().toLowerCase();
                filteredEntities = entities.stream()
                        .filter(entity -> entity.getClass().getSimpleName().equalsIgnoreCase(type))
                        .toList();
            }
            case 2 -> {
                System.out.println("Введите фракцию:");
                String fraction = new Scanner(System.in).nextLine().trim();
                filteredEntities = entities.stream()
                        .filter(entity -> entity.getFraction().equalsIgnoreCase(fraction))
                        .toList();
            }
            case 3 -> {
                System.out.println("Введите минимальный возраст:");
                int minAge = getValidIntegerInput();
                System.out.println("Введите максимальный возраст:");
                int maxAge = getValidIntegerInput();
                filteredEntities = entities.stream()
                        .filter(entity -> entity.getAge() >= minAge && entity.getAge() <= maxAge)
                        .toList();
            }
            case 4 -> {
                System.out.println("Введите минимальный уровень силы:");
                int minPower = getValidIntegerInput();
                System.out.println("Введите максимальный уровень силы:");
                int maxPower = getValidIntegerInput();
                filteredEntities = entities.stream()
                        .filter(entity -> entity.getPowerLevel() >= minPower && entity.getPowerLevel() <= maxPower)
                        .toList();
            }
            case 5 -> {
                System.out.println("Введите планету:");
                String planet = new Scanner(System.in).nextLine().trim();
                filteredEntities = entities.stream()
                        .filter(entity -> entity.getPlanet().equalsIgnoreCase(planet))
                        .toList();
            }
            case 6 -> {
                System.out.println("Возвращение в главное меню.");
                return;
            }
            default -> {
                System.out.println("Неверный выбор. Попробуйте снова.");
                return;
            }
        }
        if (filteredEntities.isEmpty()) {
            System.out.println("Нет персонажей, соответствующих выбранным параметрам.");
        } else {
            System.out.println("\nНайденные персонажи:");
            filteredEntities.forEach(System.out::println);
        }

    }

    public static void update(int choice,String name){
        Entity entity = EntityManager.findEntityByName(name);
        switch (choice) {
            case 1 -> {
                System.out.println("Введите новое имя: ");
                String newName = scanner.nextLine();
                entity.setName(newName);
            }
            case 2 -> {
                System.out.println("Введите новую фракцию: ");
                String newFraction = scanner.nextLine();
                entity.setFraction(newFraction);
            }
            case 3 -> {
                System.out.println("Введите новый возраст: ");
                int newAge = getValidIntegerInput();
                entity.setAge(newAge);
            }
            case 4 -> {
                System.out.println("Введите новую планету: ");
                String newPlanet = scanner.nextLine();
                entity.setPlanet(newPlanet);
            }
            case 5 -> {
                System.out.println("Введите новый уровень силы: ");
                int newPowerLevel = getValidIntegerInput();
                entity.setPowerLevel(newPowerLevel);
            }
            case 6 -> {
                if (entity instanceof Jedi || entity instanceof Sith) {
                    System.out.println("Введите новый цвет светового меча: ");
                    String newLightsaberColor = scanner.nextLine();
                    entity.setLightsaberColor(newLightsaberColor);
                } else {
                    System.out.println("Операция не поддерживается для данного типа сущности.");
                }
            }
            case 7 -> {
                if (entity instanceof Droid) {
                    System.out.println("Введите новую модель дроида: ");
                    String newModel = scanner.nextLine();
                    ((Droid) entity).setModelType(newModel);
                } else if (entity instanceof Jedi || entity instanceof Sith) {
                    System.out.println("Введите новый уровень владения силой: ");
                    int newForceLevel = getValidIntegerInput();
                    entity.setForceLevel(newForceLevel);
                } else {
                    System.out.println("Операция не поддерживается для данного типа сущности.");
                }
            }
            case 8 -> {
                if (entity instanceof Droid) {
                    System.out.println("Введите новый уровень заряда батареи: ");
                    int newBatteryLevel = getValidIntegerInput();
                    ((Droid) entity).setBatteryLevel(newBatteryLevel);
                } else if (entity instanceof CloneTrooper) {
                    System.out.println("Введите новый номер клона: ");
                    String newCloneNumber = scanner.nextLine();
                    ((CloneTrooper) entity).setCloneNumber(newCloneNumber);
                } else if (entity instanceof Jedi) {
                    System.out.println("Гранд-мастер (true/false): ");
                    boolean isGrandMaster = Boolean.parseBoolean(scanner.nextLine());
                    ((Jedi) entity).setIsMaster(isGrandMaster);
                } else if (entity instanceof Sith) {
                    System.out.println("Мастер ситхов (true/false): ");
                    boolean isSithMaster = Boolean.parseBoolean(scanner.nextLine());
                    ((Sith) entity).setIsMaster(isSithMaster);
                } else {
                    System.out.println("Операция не поддерживается для данного типа сущности.");
                }
            }
            default -> System.out.println("Неверный выбор.");
        }
    }
}

