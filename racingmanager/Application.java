package racingmanager;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Экранная форма менеджера соревнований автогонщиков.
 * Предназначена для отображения списка гонщиков,
 * информации об их командах и набранных очках.
 *
 * @author Бутаков Святослав 5316
 * @version 3.0 (Обработка исключений)
 */
public class Application {

    /**
     * Главное окно приложения.
     */
    private JFrame racingManager;

    /**
     * Модель таблицы со сведениями о гонщиках.
     */
    private DefaultTableModel model;

    /**
     * Таблица со списком гонщиков.
     */
    private JTable racersTable;

    /**
     * Область прокрутки таблицы.
     */
    private JScrollPane scroll;

    /**
     * Панель инструментов с кнопками управления гонщиками.
     */
    private JToolBar toolBar;

    /**
     * Надпись для поля поиска.
     */
    private JLabel searchLabel;

    /**
     * Поле ввода поискового запроса.
     */
    private JTextField searchField;

    /**
     * Кнопка выполнения поиска.
     */
    private JButton searchButton;

    /**
     * Кнопка добавления гонщика.
     */
    private JButton addButton;

    /**
     * Кнопка изменения данных гонщика.
     */
    private JButton editButton;

    /**
     * Кнопка удаления гонщика.
     */
    private JButton deleteButton;

    /**
     * Кнопка перехода к списку команд.
     */
    private JButton teamsButton;

    /**
     * Кнопка перехода к списку трасс.
     */
    private JButton tracksButton;

    /**
     * Кнопка просмотра графика соревнований.
     */
    private JButton scheduleButton;

    /**
     * Кнопка просмотра результатов соревнований.
     */
    private JButton resultsButton;

    /**
     * Кнопка выхода из приложения.
     */
    private JButton exitButton;

    /**
     * Панель поиска гонщиков.
     */
    private JPanel searchPanel;

    /**
     * Панель навигационных кнопок.
     */
    private JPanel navigationPanel;

    /**
     * Нижняя панель экранной формы.
     */
    private JPanel bottomPanel;
    
    /**
     * Собственное исключение для некорректного имени гонщика.
     * Возникает, если введенное имя содержит цифры.
     */
    private class MyException extends Exception {

        /**
         * Создает исключение с сообщением об ошибке.
         */
        public MyException() {
            super("Имя гонщика не должно содержать цифры");
        }
    }
    
    /**
     * Проверяет правильность введенного имени гонщика.
     *
     * @param field поле ввода имени гонщика
     * @throws MyException если имя содержит цифры
     * @throws NullPointerException если поле пустое
     */
    private void checkName(JTextField field)
            throws MyException, NullPointerException {

        String name = field.getText();

        if (name.length() == 0) {
            throw new NullPointerException("Имя гонщика не введено");
        }

        if (name.matches(".*\\d.*")) {
            throw new MyException();
        }
    }
    
    private FileManager fileManager;
    
    /**
     * Создает и отображает экранную форму приложения.
     * Создает главное окно, панель инструментов, таблицу гонщиков,
     * панель поиска и навигационные кнопки.
     *
     * Подключает слушателей к нескольким компонентам:
     * <ul>
     *     <li>JButton: при нажатии кнопки поиска выводится сообщение.</li>
     *     <li>JTextField: при нажатии Enter выводится введенный текст.</li>
     *     <li>JButton: при нажатии кнопки выхода главное окно закрывается.</li>
     * </ul>
     */
    public void buildWindow() {
    	
    	fileManager = new FileManager();

        // Создание главного окна приложения.
        racingManager = new JFrame("Менеджер соревнований");
        racingManager.setSize(900, 600);
        racingManager.setLocationRelativeTo(null);
        racingManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Установка компоновщика главного окна.
        racingManager.setLayout(new BorderLayout());

        // Создание кнопок управления гонщиками.
        addButton = new JButton("Добавить");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");

        // Настройка подсказок для кнопок.
        addButton.setToolTipText("Добавить нового гонщика");
        editButton.setToolTipText("Изменить данные выбранного гонщика");
        deleteButton.setToolTipText("Удалить выбранного гонщика");

        // Создание панели инструментов.
        toolBar = new JToolBar("Панель инструментов");

        // Добавление кнопок на панель инструментов.
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);

        // Размещение панели инструментов в верхней части окна.
        racingManager.add(toolBar, BorderLayout.NORTH);

        // Создание названий столбцов таблицы.
        String[] columns = {
            "Гонщик",
            "Команда",
            "Очки"
        };

        // Создание исходных данных таблицы.
        Object[][] data = {
        	    {"Кими Антонелли", "Mercedes", 340},
        	    {"Джордж Расселл", "Mercedes", 238},
        	    {"Льюис Хэмилтон", "Ferrari", 229}
        	};

        // Создание модели таблицы.
        model = new DefaultTableModel(data, columns);

        // Создание таблицы.
        racersTable = new JTable(model);

        // Создание области прокрутки таблицы.
        scroll = new JScrollPane(racersTable);

        // Размещение таблицы в центре окна.
        racingManager.add(scroll, BorderLayout.CENTER);

        // Создание компонентов поиска.
        searchLabel = new JLabel("Поиск:");
        searchField = new JTextField(20);
        searchButton = new JButton("Найти");
        
        /**
         * Обрабатывает нажатие Enter в поле поиска.
         * Проверяет введенное имя гонщика
         * и обрабатывает возможные исключения.
         */
        searchField.addActionListener(e -> {

            try {
                checkName(searchField);

                JOptionPane.showMessageDialog(
                    racingManager,
                    "Поиск гонщика: " + searchField.getText()
                );
            }

            catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(
                    racingManager,
                    ex.getMessage()
                );
            }

            catch (MyException ex) {
                JOptionPane.showMessageDialog(
                    racingManager,
                    ex.getMessage()
                );
            }
        });
        
        /**
         * Обрабатывает нажатие кнопки поиска.
         * Проверяет правильность введенного имени гонщика
         * и обрабатывает возможные исключения.
         */
        searchButton.addActionListener(e -> {

            try {
                checkName(searchField);

                JOptionPane.showMessageDialog(
                    racingManager,
                    "Поиск гонщика: " + searchField.getText()
                );
            }

            catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(
                    racingManager,
                    ex.getMessage()
                );
            }

            catch (MyException ex) {
                JOptionPane.showMessageDialog(
                    racingManager,
                    ex.getMessage()
                );
            }
        });

        searchButton.setToolTipText("Выполнить поиск гонщика");

        // Создание панели поиска.
        searchPanel = new JPanel();

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Создание навигационных кнопок.
        teamsButton = new JButton("Команды");
        tracksButton = new JButton("Трассы");
        scheduleButton = new JButton("График соревнований");
        resultsButton = new JButton("Результаты");
        exitButton = new JButton("Выход");
        
        /**
         * Обрабатывает нажатие кнопки выхода.
         * Закрывает главное окно приложения.
         */
        exitButton.addActionListener(e -> {
            racingManager.dispose();
        });

        // Создание панели навигации.
        navigationPanel = new JPanel();

        navigationPanel.add(teamsButton);
        navigationPanel.add(tracksButton);
        navigationPanel.add(scheduleButton);
        navigationPanel.add(resultsButton);
        navigationPanel.add(exitButton);

        // Создание общей нижней панели.
        bottomPanel = new JPanel(new BorderLayout());

        // Размещение поиска и навигации внутри нижней панели.
        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(navigationPanel, BorderLayout.SOUTH);

        // Размещение нижней панели в окне.
        racingManager.add(bottomPanel, BorderLayout.SOUTH);

        // Отображение экранной формы.
        racingManager.setVisible(true);
    }

    /**
     * Создает приложение и отображает экранную форму.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        new Application().buildWindow();
    }
}