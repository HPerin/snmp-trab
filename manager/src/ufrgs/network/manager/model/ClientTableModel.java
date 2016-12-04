package ufrgs.network.manager.model;

import ufrgs.network.manager.data.Client;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 12/3/16.
 */
public class ClientTableModel implements TableModel {

    private List<Client> clientList;

    public ClientTableModel(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public int getRowCount() {
        return clientList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Address";
            case 1: return "System Location";
            case 2: return "Services Down";
        }

        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return clientList.get(rowIndex).getAddress();
            case 1: return clientList.get(rowIndex).getSystemLocation();
            case 2: return clientList.get(rowIndex).getServiceDownCount();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
