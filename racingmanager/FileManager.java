package racingmanager;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FileManager {

    /**
     * Сохраняет данные таблицы гонщиков в CSV-файл.
     *
     * @param parent главное окно приложения
     * @param model модель таблицы
     */
    public void save(JFrame parent, DefaultTableModel model) {

        FileDialog save = new FileDialog(
            parent,
            "Сохранение данных",
            FileDialog.SAVE
        );

        save.setFile("racers.csv");
        save.setVisible(true);

        if (save.getFile() == null) {
            return;
        }

        String fileName =
            save.getDirectory() + save.getFile();

        try {
            BufferedWriter writer =
                new BufferedWriter(
                    new FileWriter(fileName)
                );

            for (int i = 0; i < model.getRowCount(); i++) {

                String racer =
                    model.getValueAt(i, 0).toString();

                String team =
                    model.getValueAt(i, 1).toString();

                String points =
                    model.getValueAt(i, 2).toString();

                writer.write(
                    racer + ";" + team + ";" + points
                );

                writer.newLine();
            }

            writer.close();

            JOptionPane.showMessageDialog(
                parent,
                "Данные сохранены"
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                parent,
                "Ошибка сохранения файла"
            );

            ex.printStackTrace();
        }
    }
}