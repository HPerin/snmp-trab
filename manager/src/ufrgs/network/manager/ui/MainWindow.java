package ufrgs.network.manager.ui;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import ufrgs.network.manager.network.Database;
import ufrgs.network.manager.network.Discover;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.function.BiConsumer;

/**
 * Created by lucas on 12/3/16.
 */
public class MainWindow {
    private JPanel mainPanel;
    private JTabbedPane summaryPane;
    private JTextField searchStart;
    private JButton searchButton;
    private JTextField clientsFoundField;
    private JTable clientTable;
    private JFrame mainFrame;
    private Database database;

    public MainWindow() {
        mainFrame = new JFrame("Manager");
        mainFrame.setContentPane(mainPanel);

        database = new Database();

        clientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = clientTable.rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && row != -1) {

                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Map<String, String> map = new Discover().searchClients(searchStart.getText());
                    System.out.println(map.size());
                    database.addClients(map);
                    System.out.println(database.getClientMap().size());

                    DefaultTableModel defaultTableModel = new DefaultTableModel();
                    defaultTableModel.addColumn("Address");
                    defaultTableModel.addColumn("sysDescr");
                    for (Map.Entry<String, String> entry : database.getClientMap().entrySet()) {
                        String row[] = new String[2];
                        row[0] = entry.getKey();
                        row[1] = entry.getValue();
                        defaultTableModel.addRow(row);
                    }
                    clientTable.setModel(defaultTableModel);
                    clientsFoundField.setText(String.valueOf(database.getClientMap().size()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void run() {
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
