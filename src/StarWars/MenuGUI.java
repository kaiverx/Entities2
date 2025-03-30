package StarWars;

import StarWars.Entities.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuGUI {
    private JFrame frame;
    private JPanel rightPanel;
    private JButton showAllButton;

    public MenuGUI() {
        frame = new JFrame("Меню Star Wars");
        frame.setSize(1110, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Основной макет окна

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Добавляем кнопки
        showAllButton = createButton("Показать всех", e -> showEntitiesTable());
        panel.add(showAllButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Добавить", e -> {EntityManager.addEntity();showEntitiesTable();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Изменить", e -> {updateEntityInfo();showEntitiesTable();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Удалить", e -> {deleteEntity();showEntitiesTable();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Другие действия", e -> interactWithEntity()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Загрузить", e -> {loadEntities();showEntitiesTable();}));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Вывести параметры", e -> EntityManager.show()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Выход", e -> System.exit(0)));

        // Добавляем панель с кнопками в левую часть
        frame.add(panel, BorderLayout.WEST);

        // Правая панель для списка
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null); // Центрируем окно на экране
        frame.setVisible(true); // Показываем окно
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }

    private void updateEntityInfo() {
        String name = JOptionPane.showInputDialog("Введите имя персонажа:");
        if (name != null) EntityManager.update(name);
    }

    private void deleteEntity() {
        String name = JOptionPane.showInputDialog("Введите имя или тип персонажа для удаления:");
        if (name != null) EntityManager.removeEntity(name);
    }

    private void interactWithEntity() {
        String name = JOptionPane.showInputDialog("Введите имя персонажа для взаимодействия:");
        if (name != null) EntityManager.interactWithEntity(name);
    }

    private void loadEntities() {
        String type = JOptionPane.showInputDialog("Введите тип сущностей (jedi, sith, droid, clone) или оставьте пустым для всех:");
        EntityManager.loadEntitiesFromFile(type != null && !type.isEmpty() ? type : null);
    }

    private void showEntitiesTable() {
        List<Entity> entities = EntityManager.getEntities();


        // Заголовки таблицы
        String[] columnNames = {
                "Имя", "Фракция", "Возраст", "Планета", "Сила",
                "Цвет меча", "Уровень силы", "Мастер?", "Тип модели", "Функция", "Батарея", "Номер клона"
        };

        // Данные для таблицы
        Object[][] data = new Object[entities.size()][columnNames.length];

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Object[] row = new Object[columnNames.length];

            row[0] = entity.getName();
            row[1] = entity.getFraction();
            row[2] = entity.getAge();
            row[3] = entity.getPlanet();
            row[4] = entity.getPowerLevel();

            // Заполняем уникальные параметры
            if (entity instanceof Jedi) {
                Jedi jedi = (Jedi) entity;
                row[5] = jedi.getLightsaberColor();
                row[6] = jedi.getForceLevel();
                row[7] = jedi.isMaster();
            } else if (entity instanceof Sith) {
                Sith sith = (Sith) entity;
                row[5] = sith.getLightsaberColor();
                row[6] = sith.getForceLevel();
                row[7] = sith.isMaster();
            } else if (entity instanceof Droid) {
                Droid droid = (Droid) entity;
                row[8] = droid.getModelType();
                row[9] = droid.getFunction();
                row[10] = droid.getBatteryLevel();
            } else if (entity instanceof CloneTrooper) {
                CloneTrooper clone = (CloneTrooper) entity;
                row[11] = clone.getCloneNumber();
            }

            // Заменяем null на "-"
            for (int j = 0; j < row.length; j++) {
                if (row[j] == null) row[j] = "-";
            }

            data[i] = row;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);

        rightPanel.removeAll();
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    //класс модели таблицы унаследованый от абстракт tadle model
}
