import RoomModel.Room;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class JView extends JFrame {

    private CalculateResults results;
    InitInfo info = new InitInfo();
    private JButton button1;
    private JPanel mainPanel;
    private JButton button2;
    private JPanel panel2;
    private JLabel costLabel;
    private JLabel timeLabel;
    private JLabel collectiveDoseLabel;
    private JLabel personalDoseLabel;
    private JTree tree1;
    private JButton button3;
    private JButton collectiveHistogramButton;
    private JButton personalHistogramButton;
    private final JFileChooser chooser = new JFileChooser();

    public JView() throws HeadlessException {

        setContentPane(mainPanel);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("<no data>");
        DefaultTreeModel model = new DefaultTreeModel(top);
        tree1.setModel(model);
        setTitle("Exam var 2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            InputStream in = getClass().getResourceAsStream("RoomsInformation.xlsx");
            info.setRooms(ExcelReader.readRoomsInfo(in));
            JOptionPane.showMessageDialog(JView.this, "Информация о помещениях успешно загружена");
            button1.setEnabled(true);
        } catch (InvalidFormatException | IOException | InvalidOperationException exception) {
            JOptionPane.showMessageDialog(JView.this, "Ошибка при загрузке информации о помещениях");
            System.exit(0);
        }
        button1.addActionListener(e -> {
            try {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("", "xlsx");
                chooser.setFileFilter(filter);
                chooser.showDialog(null, "Choose file:");
                File file = chooser.getSelectedFile();
                info.setWorks(ExcelReader.readWorksInfo(file));
                JOptionPane.showMessageDialog(JView.this, "Информация о работах загружена");
                button2.setEnabled(true);
                button3.setEnabled(true);
                DefaultTreeModel model2 = new DefaultTreeModel(top);
                tree1.setModel(model2);
                costLabel.setText("<no data>");
                timeLabel.setText("<no data>");
                collectiveDoseLabel.setText("<no data>");
                personalDoseLabel.setText("<no data>");
            } catch (IOException | InvalidFormatException |IllegalStateException| NullPointerException ex) {
                JOptionPane.showMessageDialog(JView.this, "Ошибка при загрузке файла");
            } catch (IllegalArgumentException exe) {
                JOptionPane.showMessageDialog(JView.this, "Файл не был выбран");
            }
        });
        button2.addActionListener(e -> {
            try {
                int priceL = Integer.parseInt(JOptionPane.showInputDialog(JView.this, "Введите ставку рабочего:"));
                if (priceL < 0) throw new NumberFormatException("Отрицательное число");
                results = Calculate.calculates(info, priceL);
                DecimalFormat formatter = new DecimalFormat("#0.00");
                costLabel.setText(formatter.format(results.getProjectCost()));
                timeLabel.setText(formatter.format(results.getProjectTime()));
                collectiveDoseLabel.setText(formatter.format(results.getCollectiveDose()));
                personalDoseLabel.setText(formatter.format(results.getPersonalDose()));
                collectiveHistogramButton.setEnabled(true);
                personalHistogramButton.setEnabled(true);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(JView.this, "Необходимо ввести целое число");
            }
        });
        button3.addActionListener(e -> {
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Объект");
            for (Room room : info.getRooms()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(room.getRoomName() + ", " + room.getFloor());
                for (OngoingWork work : info.getWorks()) {
                    if (work.getCode() == (room.getRoomCode())) {
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(work);
                        node.add(childNode);
                    }
                }
                parentNode.add(node);
            }
            DefaultTreeModel infoModel = new DefaultTreeModel(parentNode);
            tree1.setModel(infoModel);
        });

        collectiveHistogramButton.addActionListener(e -> {
            createChart();
        });
        personalHistogramButton.addActionListener(e -> {
            createPersonalChart();
        });
    }

    private void createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0;
        for (int i = 0; i < 1000; i++) {
            int res = (int) Calculate.calculates(info, 1).getCollectiveDose();
            if (res < 4100) {
                count1++;
            } else if (res < 4350) {
                count2++;
            } else if (res < 4600) {
                count3++;
            } else if (res < 4850) {
                count4++;
            } else if (res < 5100) {
                count5++;
            } else count6++;
        }
        dataset.addValue(count1, "<4100", "<4100");
        dataset.addValue(count2, "4100-4350", "4100-4350");
        dataset.addValue(count3, "4350-4600", "4350-4600");
        dataset.addValue(count4, "4600-4850", "4600-4850");
        dataset.addValue(count5, "4850-5100", "4850-5100");
        dataset.addValue(count6, ">5100", ">5100");
        JFreeChart chart = ChartFactory.createBarChart(
                "Коллективная полученная доза",
                null,
                "Значений в диапазоне",
                dataset);
        chart.addSubtitle(new TextTitle("Значения полученные в ходе 10000 экспериментальных расчетов"));
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        JFrame f = new JFrame("Коллективная доза");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(800, 500);
        f.add(new ChartPanel(chart));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void createPersonalChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0;
        for (int i = 0; i < 1000; i++) {
            int res = (int) Calculate.calculates(info, 1).getPersonalDose();
            if (res < 60) {
                count1++;
            } else if (res < 64) {
                count2++;
            } else if (res < 68) {
                count3++;
            } else if (res < 72) {
                count4++;
            } else if (res < 76) {
                count5++;
            } else count6++;
        }
        dataset.addValue(count1, "<60", "<60");
        dataset.addValue(count2, "60-64", "60-64");
        dataset.addValue(count3, "64-68", "64-68");
        dataset.addValue(count4, "68-72", "68-72");
        dataset.addValue(count5, "72-76", "72-76");
        dataset.addValue(count6, ">76", ">76");
        JFreeChart chart = ChartFactory.createBarChart(
                "Средняя индивидуальная полученная доза",
                null,
                "Значений в диапазоне",
                dataset);
        chart.addSubtitle(new TextTitle("Значения полученные в ходе 10000 экспериментальных расчетов"));
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        JFrame f = new JFrame("Индивидуальная доза");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(800, 500);
        f.add(new ChartPanel(chart));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}