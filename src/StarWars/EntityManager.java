package StarWars;


import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.InputMismatchException;
import java.util.*;
import StarWars.Entities.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Entity> entities = new ArrayList<>();


//    public static void menu() {
//        // Создание персонажей
//        Jedi obiWan = new Jedi("Оби-Ван Кеноби", "Республика", 57, "Стижон", 9000, "Синий", 95, true);
//        Sith darthVader = new Sith("Дарт Вейдер", "Империя", 46, "Татуин", 9500, "Тёмный разряд", 95, true);
//        CloneTrooper rex = new CloneTrooper("Капитан Рекс", "Республика", 12, "Эндор", 2000, "CT-7567");
//        Droid r2d2 = new Droid("R2-D2", "Республика", 66, "Набу", 50, "Астромех", "Поддержка", 100);
//
//        // Добавление персонажей в список
//        entities.add(obiWan);
//        entities.add(darthVader);
//        entities.add(rex);
//        entities.add(r2d2);
//        Menu.showMenu();
//    }

//    public static void showAllEntities() {
//        if (entities.isEmpty()) {
//            System.out.println("Нет персонажей в списке.");
//        } else {
//            for (Entity entity : entities) {
//                System.out.println(entity.toString());
//            }
//        }
//    }

    public static List<Entity> getEntities() {
        return new ArrayList<>(entities); // Возвращаем копию списка
    }

    public static void addEntity() {
        // Создаём модальное окно для выбора
        JDialog dialog = new JDialog();
        dialog.setTitle("Выберите тип персонажа, которого хотите добавить");
        dialog.setSize(400, 300);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null); // Окно будет по центру экрана
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Устанавливаем модальность
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Вертикальное расположение кнопок
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Отступы

        // Добавляем кнопки
        panel.add(createButton("Джедай", e -> { dialog.dispose();createJedi();  }));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Ситх", e -> {createSith(); dialog.dispose();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Клон", e -> {createClone(); dialog.dispose();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Дроид", e -> {createDroid(); dialog.dispose();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Вернуться в главное меню", e -> dialog.dispose()));

        // Добавляем панель в окно
        dialog.add(panel, BorderLayout.CENTER); // Центрируем панель в окне

        // Обновляем окно, чтобы кнопки отобразились
        dialog.revalidate();
        dialog.repaint();

        // Теперь показываем окно
        dialog.setVisible(true);
    }

    private static JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Фиксированный размер кнопки
        button.setMaximumSize(new Dimension(200, 50)); // Ограничиваем максимальный размер
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Выравнивание кнопки по центру
        button.addActionListener(action);
        return button;
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
        // Используем JDialog для модального окна
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Джедае");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Устанавливаем модальность
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Запрещаем изменение размера окна
        frame.setResizable(false);

        // Создаем панель с GridLayout для ввода данных
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(9, 2, 10, 10)); // Добавим отступы для элементов

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField lightsaberColorField = new JTextField();
        JTextField forceLevelField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JCheckBox isMasterCheckBox = new JCheckBox("Гранд-мастер");

        // Устанавливаем предпочтительный размер для полей ввода
        Dimension fieldSize = new Dimension(100, 1);  // Ширина 200, высота 30
        nameField.setPreferredSize(fieldSize);
        fractionField.setPreferredSize(fieldSize);
        planetField.setPreferredSize(fieldSize);
        lightsaberColorField.setPreferredSize(fieldSize);
        forceLevelField.setPreferredSize(fieldSize);
        powerLevelField.setPreferredSize(fieldSize);
        ageField.setPreferredSize(fieldSize);

        JButton submitButton = new JButton("Создать Джедая");

        // Добавляем компоненты на панель
        form.add(new JLabel("Имя Джедая:"));
        form.add(nameField);
        form.add(new JLabel("Фракция Джедая:"));
        form.add(fractionField);
        form.add(new JLabel("Возраст Джедая:"));
        form.add(ageField);
        form.add(new JLabel("Планета Джедая:"));
        form.add(planetField);
        form.add(new JLabel("Уровень силы Джедая:"));
        form.add(powerLevelField);
        form.add(new JLabel("Цвет светового меча:"));
        form.add(lightsaberColorField);
        form.add(new JLabel("Уровень владения силой:"));
        form.add(forceLevelField);
        form.add(new JLabel("Гранд-мастер?"));
        form.add(isMasterCheckBox);
        form.add(submitButton);

        // Добавляем панель с формой на окно
        frame.add(form, BorderLayout.CENTER);

        // Обработчик события для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Проверка введенных данных
                String name = nameField.getText();
                String fraction = fractionField.getText();
                String planet = planetField.getText();
                String lightsaberColor = lightsaberColorField.getText();
                int forceLevel = getValidIntInput(forceLevelField.getText(), "Уровень владения силой");
                int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
                int age = getValidIntInput(ageField.getText(), "Возраст");

                boolean isMaster = isMasterCheckBox.isSelected();

                if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || lightsaberColor.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, заполните все поля!");
                } else {
                    Jedi newJedi = new Jedi(name, fraction, age, planet, powerLevel, lightsaberColor, forceLevel, isMaster);
                    // Здесь можно добавить логику для сохранения или вывода информации о Ситхе
                    JOptionPane.showMessageDialog(frame, "Джедай " + name + " успешно создан!");
                    frame.dispose(); // Закрытие окна после создания Ситха
                }
            }
        });

        // Размер окна под компоненты
        frame.pack();
        frame.setVisible(true); // Показываем окно после настройки всех компонентов

        return null; // Возвращает null, потому что вы не используете возвращаемое значение
    }

    private static int getValidIntInput(String input, String fieldName) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Пожалуйста, введите корректное число для " + fieldName);
            return -1; // Возвращаем -1, если ввод некорректен
        }
    }

    private static Sith createSith() {
        // Используем JDialog для модального окна
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Ситхе");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Устанавливаем модальность
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Запрещаем изменение размера окна
        frame.setResizable(false);

        // Создаем панель с GridLayout для ввода данных
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(9, 2, 10, 10)); // Добавим отступы для элементов

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField lightsaberColorField = new JTextField();
        JTextField forceLevelField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JCheckBox isMasterCheckBox = new JCheckBox("Гранд-мастер");

        // Устанавливаем предпочтительный размер для полей ввода
        Dimension fieldSize = new Dimension(100, 1);  // Ширина 200, высота 30
        nameField.setPreferredSize(fieldSize);
        fractionField.setPreferredSize(fieldSize);
        planetField.setPreferredSize(fieldSize);
        lightsaberColorField.setPreferredSize(fieldSize);
        forceLevelField.setPreferredSize(fieldSize);
        powerLevelField.setPreferredSize(fieldSize);
        ageField.setPreferredSize(fieldSize);

        JButton submitButton = new JButton("Создать Ситха");

        // Добавляем компоненты на панель
        form.add(new JLabel("Имя Ситха:"));
        form.add(nameField);
        form.add(new JLabel("Фракция Ситха:"));
        form.add(fractionField);
        form.add(new JLabel("Возраст Ситха:"));
        form.add(ageField);
        form.add(new JLabel("Планета Ситха:"));
        form.add(planetField);
        form.add(new JLabel("Уровень силы Ситха:"));
        form.add(powerLevelField);
        form.add(new JLabel("Цвет светового меча:"));
        form.add(lightsaberColorField);
        form.add(new JLabel("Уровень владения силой:"));
        form.add(forceLevelField);
        form.add(new JLabel("Гранд-мастер?"));
        form.add(isMasterCheckBox);
        form.add(submitButton);

        // Добавляем панель с формой на окно
        frame.add(form, BorderLayout.CENTER);

        // Обработчик события для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Проверка введенных данных
                String name = nameField.getText();
                String fraction = fractionField.getText();
                String planet = planetField.getText();
                String lightsaberColor = lightsaberColorField.getText();
                int forceLevel = getValidIntInput(forceLevelField.getText(), "Уровень владения силой");
                int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
                int age = getValidIntInput(ageField.getText(), "Возраст");

                boolean isMaster = isMasterCheckBox.isSelected();

                if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || lightsaberColor.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, заполните все поля!");
                } else {
                    Sith newSith = new Sith(name, fraction, age, planet, powerLevel, lightsaberColor, forceLevel, isMaster);
                    // Здесь можно добавить логику для сохранения или вывода информации о Ситхе
                    JOptionPane.showMessageDialog(frame, "Ситх " + name + " успешно создан!");
                    frame.dispose(); // Закрытие окна после создания Ситха
                }
            }
        });

        // Размер окна под компоненты
        frame.pack();
        frame.setVisible(true); // Показываем окно после настройки всех компонентов

        return null; // Возвращает null, потому что вы не используете возвращаемое значение
    }

    private static CloneTrooper createClone() {
        // Используем JDialog для модального окна
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Клоне");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Устанавливаем модальность
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Запрещаем изменение размера окна
        frame.setResizable(false);


        // Создаем панель с GridLayout для ввода данных
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField cloneNumberField = new JTextField();

        // Устанавливаем предпочтительный размер для полей ввода
        Dimension fieldSize = new Dimension(100, 1);

        nameField.setPreferredSize(fieldSize);
        fractionField.setPreferredSize(fieldSize);
        planetField.setPreferredSize(fieldSize);
        ageField.setPreferredSize(fieldSize);
        powerLevelField.setPreferredSize(fieldSize);
        cloneNumberField.setPreferredSize(fieldSize);

        JButton submitButton = new JButton("Создать Клона");

        // Добавляем компоненты на панель
        form.add(new JLabel("Имя Клона:"));
        form.add(nameField);
        form.add(new JLabel("Фракция Клона:"));
        form.add(fractionField);
        form.add(new JLabel("Возраст Клона:"));
        form.add(ageField);
        form.add(new JLabel("Планета Клона:"));
        form.add(planetField);
        form.add(new JLabel("Уровень силы Клона:"));
        form.add(powerLevelField);
        form.add (new JLabel("Введите серийный номер Клона"));
        form.add (cloneNumberField);

        form.add(submitButton);

        // Добавляем панель с формой на окно
        frame.add(form, BorderLayout.CENTER);

        // Обработчик события для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Проверка введенных данных
                String name = nameField.getText();
                String fraction = fractionField.getText();
                String planet = planetField.getText();
                String cloneNumber = cloneNumberField.getText();
                int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
                int age = getValidIntInput(ageField.getText(), "Возраст");

                if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || cloneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, заполните все поля!");
                } else {
                    CloneTrooper newCloneTrooper = new CloneTrooper(name, fraction, age, planet, powerLevel, cloneNumber);
                    // Здесь можно добавить логику для сохранения или вывода информации о Ситхе
                    JOptionPane.showMessageDialog(frame, "Клон " + name + " успешно создан!");
                    frame.dispose(); // Закрытие окна после создания Ситха
                }
            }
        });

        // Размер окна под компоненты
        frame.pack();
        frame.setVisible(true); // Показываем окно после настройки всех компонентов

        return null; // Возвращает null, потому что вы не используете возвращаемое значение
    }

    private static Droid createDroid() {
        // Используем JDialog для модального окна
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Дроиде");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Устанавливаем модальность
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // Запрещаем изменение размера окна
        frame.setResizable(false);


        // Создаем панель с GridLayout для ввода данных
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField batteryLevelField = new JTextField();

        // Устанавливаем предпочтительный размер для полей ввода
        Dimension fieldSize = new Dimension(100, 1);

        nameField.setPreferredSize(fieldSize);
        fractionField.setPreferredSize(fieldSize);
        planetField.setPreferredSize(fieldSize);
        ageField.setPreferredSize(fieldSize);
        powerLevelField.setPreferredSize(fieldSize);
        modelField.setPreferredSize(fieldSize);
        typeField.setPreferredSize(fieldSize);
        batteryLevelField.setPreferredSize(fieldSize);

        JButton submitButton = new JButton("Создать Дроида");

        // Добавляем компоненты на панель
        form.add(new JLabel("Имя Дроида:"));
        form.add(nameField);
        form.add(new JLabel("Фракция Дроида:"));
        form.add(fractionField);
        form.add(new JLabel("Возраст Дроида:"));
        form.add(ageField);
        form.add(new JLabel("Планета Дроида:"));
        form.add(planetField);
        form.add(new JLabel("Уровень силы Дроида:"));
        form.add(powerLevelField);
        form.add (new JLabel("Введите модель Дроида"));
        form.add (modelField);
        form.add (new JLabel("Введите функцию Дроида"));
        form.add (modelField);
        form.add (new JLabel("Введите уровень заряда Дроида"));
        form.add (modelField);

        form.add(submitButton);

        // Добавляем панель с формой на окно
        frame.add(form, BorderLayout.CENTER);

        // Обработчик события для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Проверка введенных данных
                String name = nameField.getText();
                String fraction = fractionField.getText();
                String planet = planetField.getText();
                String model = modelField.getText();
                String type = typeField.getText();
                int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
                int age = getValidIntInput(ageField.getText(), "Возраст");
                int batteryLevel = getValidIntInput(batteryLevelField.getText(), "Возраст");

                if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || model.isEmpty() || type.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Пожалуйста, заполните все поля!");
                } else {
                    Droid newDroid = new Droid(name, fraction, age, planet, powerLevel, model, type, batteryLevel);
                    // Здесь можно добавить логику для сохранения или вывода информации о Ситхе
                    JOptionPane.showMessageDialog(frame, "Клон " + name + " успешно создан!");
                    frame.dispose(); // Закрытие окна после создания Ситха
                }
            }
        });

        // Размер окна под компоненты
        frame.pack();
        frame.setVisible(true); // Показываем окно после настройки всех компонентов

        return null; // Возвращает null, потому что вы не используете возвращаемое значение
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

