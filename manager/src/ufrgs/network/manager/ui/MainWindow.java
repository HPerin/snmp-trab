package ufrgs.network.manager.ui;

import ufrgs.network.manager.data.Client;
import ufrgs.network.manager.data.ClientService;
import ufrgs.network.manager.data.NetworkInterface;
import ufrgs.network.manager.model.ClientTableModel;
import ufrgs.network.manager.model.NetworkInterfaceTableModel;
import ufrgs.network.manager.model.ServiceTableModel;
import ufrgs.network.manager.network.Database;
import ufrgs.network.manager.network.Discover;
import ufrgs.network.manager.network.InfoUpdater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 12/3/16.
 */
public class MainWindow {
    private JPanel mainPanel;
    private JTextField searchStart;
    private JButton searchButton;
    private JTextField clientsFoundField;
    private JTable clientTable;
    private JTable servicesTable;
    private JTextField curAddress;
    private JTextField curSystemDescription;
    private JTextField searchPort;
    private JTextField lastUpdateField;
    private JTextField curSystemLocation;
    private JButton updateCurSystemLocation;
    private JTable networkInterfacesTable;
    private JSlider searchTimeoutSlider;
    private JFrame mainFrame;
    private Database database;

    public MainWindow() {
        mainFrame = new JFrame("Manager");
        mainFrame.setContentPane(mainPanel);

        database = Database.loadFromFile();
        clientTable.setModel(new ClientTableModel(database.getClientList()));
        servicesTable.setModel(new ServiceTableModel(new ArrayList<ClientService>()));
        networkInterfacesTable.setModel(new NetworkInterfaceTableModel(new ArrayList<NetworkInterface>()));

        clientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = clientTable.rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && row != -1) {
                    Client client = database.getClient(String.valueOf(clientTable.getModel().getValueAt(row, 0)));
                    curAddress.setText(client.getAddress());
                    curSystemDescription.setText(client.getSystemDescription());
                    curSystemLocation.setText(client.getSystemLocation());
                    servicesTable.setModel(new ServiceTableModel(client.getClientServiceList()));
                    networkInterfacesTable.setModel(new NetworkInterfaceTableModel(client.getNetworkInterfaceList()));
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButton.setEnabled(false);
                try {
                    database.addClients(new Discover().searchClients(searchStart.getText(), searchPort.getText(), searchTimeoutSlider.getValue()));
                    clientTable.setModel(new ClientTableModel(database.getClientList()));
                    clientsFoundField.setText(String.valueOf(database.getClientList().size()));
                    lastUpdateField.setText(database.getLastUpdate());
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
                searchButton.setEnabled(true);
            }
        });

        updateCurSystemLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!new InfoUpdater().updateSystemLocation(curAddress.getText(), curSystemLocation.getText())) {
                        curSystemLocation.setText(database.getClient(curAddress.getText()).getSystemLocation());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void updateDatabase() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Client> updatedClientList = new ArrayList<>();
                    Discover discover = new Discover();
                    for (Client client : database.getClientList()) {
                        updatedClientList.add(discover.getClient(client.getAddress()));
                    }
                    database.addClients(updatedClientList);

                    clientTable.setModel(new ClientTableModel(database.getClientList()));
                    clientsFoundField.setText(String.valueOf(database.getClientList().size()));
                    lastUpdateField.setText(database.getLastUpdate());
                    if (!curAddress.getText().equals("")) {
                        Client client = database.getClient(curAddress.getText());
                        servicesTable.setModel(new ServiceTableModel(client.getClientServiceList()));
                        networkInterfacesTable.setModel(new NetworkInterfaceTableModel(client.getNetworkInterfaceList()));
                        curAddress.setText(client.getAddress());
                        //curSystemLocation.setText(client.getSystemLocation());
                        curSystemDescription.setText(client.getSystemDescription());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void run() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
