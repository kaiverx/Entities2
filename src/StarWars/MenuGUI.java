package StarWars;

import StarWars.Entities.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuGUI {
    private JFrame frame;
    private JPanel rightPanel;
    private DefaultListModel<String> listModel;
    private JList<String> entityList;
    private JScrollPane scrollPane;

    public MenuGUI() {
        frame = new JFrame("Меню Star Wars");
        frame.setSize(1280, 720); // Устанавливаем разрешение окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Основной макет окна

        // Левый панель с кнопками
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Вертикальное расположение кнопок
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Отступ слева

        // Добавляем кнопки
        panel.add(createButton("Показать всех", e -> showEntitiesList()));
        panel.add(Box.createVerticalStrut(10)); // Отступ между кнопками
        panel.add(createButton("Добавить", e -> EntityManager.addEntity()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Изменить", e -> updateEntityInfo()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Удалить", e -> deleteEntity()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Другие действия", e -> interactWithEntity()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Загрузить", e -> loadEntities()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Вывести параметры", e -> showEntitiesByParameters()));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButton("Выход", e -> System.exit(0)));

        // Добавляем панель с кнопками в левую часть
        frame.add(panel, BorderLayout.WEST);

        // Правая панель для списка с прокруткой
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        frame.add(rightPanel, BorderLayout.CENTER);

        // Кнопка для скрытия/показа списка
        JButton toggleTableButton = new JButton("Закрыть таблицу");
        toggleTableButton.addActionListener(e -> toggleTableVisibility());

        // Добавляем кнопку в правый нижний угол
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(toggleTableButton, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null); // Центрируем окно на экране
        frame.setVisible(true); // Показываем окно
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Фиксированный размер кнопки
        button.setMaximumSize(new Dimension(200, 50)); // Ограничиваем максимальный размер
        button.setAlignmentX(Component.LEFT_ALIGNMENT); // Выравнивание кнопки по левому краю
        button.addActionListener(action);
        return button;
    }

    private void updateEntityInfo() {
        String name = JOptionPane.showInputDialog("Введите имя персонажа:");
        if (name != null) EntityManager.update(1, name);
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
        String type = JOptionPane.showInputDialog("Введите тип сущностей (или оставьте пустым для всех):");
        EntityManager.loadEntitiesFromFile(type != null && !type.isEmpty() ? type : null);
    }

    private void showEntitiesByParameters() {
        String choice = JOptionPane.showInputDialog("Введите параметр (1-Тип, 2-Фракция, 3-Возраст, 4-Сила, 5-Планета):");
        if (choice != null) EntityManager.show(Integer.parseInt(choice));
    }

    // Метод для отображения списка сущностей
    private void showEntitiesList() {
        List<Entity> entities = EntityManager.getEntities();
        List<String> entityNames = new ArrayList<>();

        for (Entity entity : entities) {
            entityNames.add(entity.toString());
        }

        // Создаем модель списка и добавляем элементы
        listModel = new DefaultListModel<>();
        for (String name : entityNames) {
            listModel.addElement(name);
        }

        // Создаем JList и JScrollPane для прокрутки
        entityList = new JList<>(listModel);
        scrollPane = new JScrollPane(entityList);

        // Добавляем прокручиваемый список в правую панель
        rightPanel.removeAll();
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Обновляем окно
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // Метод для переключения видимости списка
    private void toggleTableVisibility() {
        if (rightPanel.getComponentCount() > 0) {
            // Если список отображается, скрываем его
            rightPanel.removeAll();
        } else {
            // Если список скрыт, показываем его
            showEntitiesList();
        }

        // Обновляем правую панель
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuGUI::new);
    }
}
