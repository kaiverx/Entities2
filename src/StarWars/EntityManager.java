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
        panel.add(createButton("Ситх", e -> {dialog.dispose();createSith(); }));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Клон", e -> {dialog.dispose();createClone(); }));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Дроид", e -> {dialog.dispose();createDroid(); }));
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
                writer.write(((Jedi) entity).isMaster() + "\n");  // Гранд-мастер
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
            JFrame frame = new JFrame("Взаимодействие с " + entity.getName());
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 1));

            JButton btn1 = new JButton("Сыграть в Голошахматы");
            btn1.addActionListener(e -> outputArea.append("Вы сыграли в Голошахматы с " + entity.getName() + "\n"));

            JButton btn2 = new JButton("Вызвать на поединок");
            btn2.addActionListener(e -> outputArea.append("Вы сразились с " + entity.getName() + "\n"));

            JButton btn3 = new JButton("Дать 10 кредитов");
            btn3.addActionListener(e -> outputArea.append("Вы дали 10 кредитов " + entity.getName() + "\n"));

            JButton btn4 = new JButton("Отправить выполнять задание");
            btn4.addActionListener(e -> outputArea.append(entity.takeCare() + "\n"));

            JButton btn5 = new JButton("Закрыть");
            btn5.addActionListener(e -> frame.dispose());

            buttonPanel.add(btn1);
            buttonPanel.add(btn2);
            buttonPanel.add(btn3);
            buttonPanel.add(btn4);
            buttonPanel.add(btn5);

            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Персонаж не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Джедае");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);

        JPanel form = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField lightsaberColorField = new JTextField();
        JTextField forceLevelField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JCheckBox isMasterCheckBox = new JCheckBox("Гранд-мастер");

        JButton submitButton = new JButton("Создать Джедая");

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

        frame.add(form, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            // Читаем данные
            String name = nameField.getText().trim();
            String fraction = fractionField.getText().trim();
            String planet = planetField.getText().trim();
            String lightsaberColor = lightsaberColorField.getText().trim();
            int forceLevel = getValidIntInput(forceLevelField.getText(), "Уровень владения силой");
            int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
            int age = getValidIntInput(ageField.getText(), "Возраст");
            boolean isMaster = isMasterCheckBox.isSelected();

            // Проверяем корректность ввода
            if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || lightsaberColor.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Заполните все текстовые поля!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (forceLevel < 0 || powerLevel < 0 || age < 0) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Числовые поля должны содержать корректные положительные значения!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Создаем Джедая
            Jedi newJedi = new Jedi(name, fraction, age, planet, powerLevel, lightsaberColor, forceLevel, isMaster);
            writeEntityToFile(newJedi, "Jedi");

            JOptionPane.showMessageDialog(frame, "Джедай " + name + " успешно создан!");
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);

        return null;
    }

    private static int getValidIntInput(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value < 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка: " + fieldName + " должно быть положительным числом!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private static Sith createSith() {
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Ситхе");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);

        JPanel form = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField lightsaberColorField = new JTextField();
        JTextField forceLevelField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JCheckBox isMasterCheckBox = new JCheckBox("Гранд-мастер");

        JButton submitButton = new JButton("Создать Ситха");

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

        frame.add(form, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            // Читаем данные
            String name = nameField.getText().trim();
            String fraction = fractionField.getText().trim();
            String planet = planetField.getText().trim();
            String lightsaberColor = lightsaberColorField.getText().trim();
            int forceLevel = getValidIntInput(forceLevelField.getText(), "Уровень владения силой");
            int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
            int age = getValidIntInput(ageField.getText(), "Возраст");
            boolean isMaster = isMasterCheckBox.isSelected();

            // Проверяем корректность ввода
            if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || lightsaberColor.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Заполните все текстовые поля!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (forceLevel < 0 || powerLevel < 0 || age < 0) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Числовые поля должны содержать корректные положительные значения!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Создаем Ситха
            Sith newSith = new Sith(name, fraction, age, planet, powerLevel, lightsaberColor, forceLevel, isMaster);
            writeEntityToFile(newSith, "Sith");

            JOptionPane.showMessageDialog(frame, "Ситх " + name + " успешно создан!");
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);

        return null;
    }

    private static CloneTrooper createClone() {
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Клоне");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField cloneNumberField = new JTextField();

        JButton submitButton = new JButton("Создать Клона");

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
        form.add(new JLabel("Введите серийный номер Клона:"));
        form.add(cloneNumberField);
        form.add(submitButton);

        frame.add(form, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            // Читаем данные
            String name = nameField.getText().trim();
            String fraction = fractionField.getText().trim();
            String planet = planetField.getText().trim();
            String cloneNumber = cloneNumberField.getText().trim();
            int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
            int age = getValidIntInput(ageField.getText(), "Возраст");

            if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || cloneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Заполните все текстовые поля!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (powerLevel < 0 || age < 0) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Числовые поля должны содержать корректные положительные значения!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CloneTrooper newCloneTrooper = new CloneTrooper(name, fraction, age, planet, powerLevel, cloneNumber);
            writeEntityToFile(newCloneTrooper, "CloneTrooper");

            JOptionPane.showMessageDialog(frame, "Клон " + name + " успешно создан!");
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);

        return null;
    }

    private static Droid createDroid() {
        JDialog frame = new JDialog();
        frame.setTitle("Форма ввода данных о Дроиде");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField fractionField = new JTextField();
        JTextField planetField = new JTextField();
        JTextField powerLevelField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField batteryLevelField = new JTextField();

        JButton submitButton = new JButton("Создать Дроида");

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
        form.add(new JLabel("Введите модель Дроида:"));
        form.add(modelField);
        form.add(new JLabel("Введите функцию Дроида:"));
        form.add(typeField);
        form.add(new JLabel("Введите уровень заряда Дроида:"));
        form.add(batteryLevelField);
        form.add(submitButton);

        frame.add(form, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            // Читаем данные
            String name = nameField.getText().trim();
            String fraction = fractionField.getText().trim();
            String planet = planetField.getText().trim();
            String model = modelField.getText().trim();
            String type = typeField.getText().trim();
            int powerLevel = getValidIntInput(powerLevelField.getText(), "Уровень силы");
            int age = getValidIntInput(ageField.getText(), "Возраст");
            int batteryLevel = getValidIntInput(batteryLevelField.getText(), "Уровень заряда");

            if (name.isEmpty() || fraction.isEmpty() || planet.isEmpty() || model.isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Заполните все текстовые поля!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (powerLevel < 0 || age < 0 || batteryLevel < 0) {
                JOptionPane.showMessageDialog(frame, "Ошибка: Числовые поля должны содержать корректные положительные значения!", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Droid newDroid = new Droid(name, fraction, age, planet, powerLevel, model, type, batteryLevel);
            writeEntityToFile(newDroid, "Droid");

            JOptionPane.showMessageDialog(frame, "Дроид " + name + " успешно создан!");
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);

        return null;
    }

    public static void show() {
        JDialog frame = new JDialog();
        frame.setTitle("Фильтрация персонажей");
        frame.setSize(700, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1)); // Каждый фильтр будет на новой строке

        JButton typeButton = new JButton("Фильтровать по типу");
        JButton fractionButton = new JButton("Фильтровать по фракции");
        JButton ageButton = new JButton("Фильтровать по возрасту");
        JButton powerButton = new JButton("Фильтровать по уровню силы");
        JButton planetButton = new JButton("Фильтровать по планете");
        JButton backButton = new JButton("Вернуться в главное меню");

        typeButton.addActionListener(e -> {
            String type = JOptionPane.showInputDialog(frame, "Введите тип сущности (например, Droid, CloneTrooper):");
            List<Entity> filteredEntities = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity.getClass().getSimpleName().equalsIgnoreCase(type)) {
                    filteredEntities.add(entity);
                }
            }

            updateResults(resultArea, filteredEntities);
        });

        fractionButton.addActionListener(e -> {
            String fraction = JOptionPane.showInputDialog(frame, "Введите фракцию:");
            List<Entity> filteredEntities = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity.getFraction().equalsIgnoreCase(fraction)) {
                    filteredEntities.add(entity);
                }
            }

            updateResults(resultArea, filteredEntities);
        });

        ageButton.addActionListener(e -> {
            String minAgeStr = JOptionPane.showInputDialog(frame, "Введите минимальный возраст:");
            String maxAgeStr = JOptionPane.showInputDialog(frame, "Введите максимальный возраст:");
            try {
                int minAge = Integer.parseInt(minAgeStr);
                int maxAge = Integer.parseInt(maxAgeStr);
                List<Entity> filteredEntities = new ArrayList<>();
                for (Entity entity : entities) {
                    if (entity.getAge() >= minAge && entity.getAge() <= maxAge) {
                        filteredEntities.add(entity);
                    }
                }

                updateResults(resultArea, filteredEntities);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите корректные значения для возраста.");
            }
        });

        powerButton.addActionListener(e -> {
            String minPowerStr = JOptionPane.showInputDialog(frame, "Введите минимальный уровень силы:");
            String maxPowerStr = JOptionPane.showInputDialog(frame, "Введите максимальный уровень силы:");
            try {
                int minPower = Integer.parseInt(minPowerStr);
                int maxPower = Integer.parseInt(maxPowerStr);
                List<Entity> filteredEntities = new ArrayList<>();
                for (Entity entity : entities) {
                    if (entity.getPowerLevel() >= minPower && entity.getPowerLevel() <= maxPower) {
                        filteredEntities.add(entity);
                    }
                }

                updateResults(resultArea, filteredEntities);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите корректные значения для уровня силы.");
            }
        });

        planetButton.addActionListener(e -> {
            String planet = JOptionPane.showInputDialog(frame, "Введите планету:");
            List<Entity> filteredEntities = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity.getPlanet().equalsIgnoreCase(planet)) {
                    filteredEntities.add(entity);
                }
            }

            updateResults(resultArea, filteredEntities);
        });

        backButton.addActionListener(e -> {
            frame.dispose(); // Закрытие окна
        });

        buttonPanel.add(typeButton);
        buttonPanel.add(fractionButton);
        buttonPanel.add(ageButton);
        buttonPanel.add(powerButton);
        buttonPanel.add(planetButton);
        buttonPanel.add(backButton);

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Метод для обновления текстовой области с результатами поиска
    private static void updateResults(JTextArea resultArea, List<Entity> filteredEntities) {
        if (filteredEntities.isEmpty()) {
            resultArea.setText("Не найдено персонажей, соответствующих критериям.");
        } else {
            StringBuilder resultText = new StringBuilder("Найденные персонажи:\n");
            filteredEntities.forEach(entity -> resultText.append(entity.toString()).append("\n"));
            resultArea.setText(resultText.toString());
        }
    }

    public static void update(String name) {
        Entity entity = EntityManager.findEntityByName(name);
        if (entity == null) {
            JOptionPane.showMessageDialog(null, "Сущность не найдена!");
            return;
        }

        JDialog frame = new JDialog();
        frame.setTitle("Форма изменения данных");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setResizable(false);

        JPanel form = new JPanel();
        form.setLayout(new GridLayout(9, 2, 10, 10));

        // Поля ввода
        JTextField nameField = new JTextField(entity.getName());
        JTextField fractionField = new JTextField(entity.getFraction());
        JTextField planetField = new JTextField(entity.getPlanet());
        JTextField ageField = new JTextField(String.valueOf(entity.getAge()));
        JTextField powerLevelField = new JTextField(String.valueOf(entity.getPowerLevel()));

        // Поля, специфичные для Jedi/Sith
        JTextField lightsaberColorField = new JTextField(entity instanceof Jedi || entity instanceof Sith ? entity.getLightsaberColor() : "");
        JTextField forceLevelField = new JTextField(entity instanceof Jedi || entity instanceof Sith ? String.valueOf(entity.getForceLevel()) : "");
        JCheckBox isMasterCheckBox = new JCheckBox("Гранд-мастер", entity instanceof Jedi && ((Jedi) entity).isMaster());

        // Поля для CloneTrooper и Droid
        JTextField cloneNumberField = new JTextField(entity instanceof CloneTrooper ? ((CloneTrooper) entity).getCloneNumber() : "");
        JTextField modelField = new JTextField(entity instanceof Droid ? ((Droid) entity).getModelType() : "");
        JTextField batteryLevelField = new JTextField(entity instanceof Droid ? String.valueOf(((Droid) entity).getBatteryLevel()) : "");

        JButton submitButton = new JButton("Изменить данные");

        // Добавляем поля на форму
        form.add(new JLabel("Имя:"));
        form.add(nameField);
        form.add(new JLabel("Фракция:"));
        form.add(fractionField);
        form.add(new JLabel("Возраст:"));
        form.add(ageField);
        form.add(new JLabel("Планета:"));
        form.add(planetField);
        form.add(new JLabel("Уровень силы:"));
        form.add(powerLevelField);

        if (entity instanceof Jedi || entity instanceof Sith) {
            form.add(new JLabel("Цвет светового меча:"));
            form.add(lightsaberColorField);
            form.add(new JLabel("Уровень владения силой:"));
            form.add(forceLevelField);
            form.add(new JLabel("Гранд-мастер:"));
            form.add(isMasterCheckBox);
        }

        if (entity instanceof CloneTrooper) {
            form.add(new JLabel("Номер клона:"));
            form.add(cloneNumberField);
        }

        if (entity instanceof Droid) {
            form.add(new JLabel("Модель дроида:"));
            form.add(modelField);
            form.add(new JLabel("Уровень заряда:"));
            form.add(batteryLevelField);
        }

        form.add(submitButton);
        frame.add(form, BorderLayout.CENTER);

        // Обработчик кнопки
        submitButton.addActionListener(e -> {
            entity.setName(nameField.getText());
            entity.setFraction(fractionField.getText());
            entity.setPlanet(planetField.getText());
            entity.setAge(getValidIntInput(ageField.getText(), "Возраст"));
            entity.setPowerLevel(getValidIntInput(powerLevelField.getText(), "Уровень силы"));

            if (entity instanceof Jedi jedi) {
                jedi.setLightsaberColor(lightsaberColorField.getText());
                jedi.setForceLevel(getValidIntInput(forceLevelField.getText(), "Уровень владения силой"));
                jedi.setIsMaster(isMasterCheckBox.isSelected());
            } else if (entity instanceof Sith sith) {
                sith.setLightsaberColor(lightsaberColorField.getText());
                sith.setForceLevel(getValidIntInput(forceLevelField.getText(), "Уровень владения силой"));
                sith.setIsMaster(isMasterCheckBox.isSelected());
            } else if (entity instanceof CloneTrooper clone) {
                clone.setCloneNumber(cloneNumberField.getText());
            } else if (entity instanceof Droid droid) {
                droid.setModelType(modelField.getText());
                droid.setBatteryLevel(getValidIntInput(batteryLevelField.getText(), "Уровень заряда"));
            }

            JOptionPane.showMessageDialog(frame, "Данные успешно обновлены!");
            frame.dispose();
        });

        frame.pack();
        frame.setVisible(true);
    }
    }


