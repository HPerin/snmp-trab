package ufrgs.network.manager.model;

import ufrgs.network.manager.data.ClientService;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 12/3/16.
 */
public class ServiceTableModel implements TableModel {

    private List<ClientService> clientServiceList;

    public ServiceTableModel(List<ClientService> clientServiceList) {
        if (clientServiceList == null)
            this.clientServiceList = new ArrayList<>();
        else
            this.clientServiceList = clientServiceList;
    }

    @Override
    public int getRowCount() {
        return clientServiceList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Service";
            case 1: return "State";
            case 2: return "Path";
            case 3: return "UpTime";
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
            case 0: return clientServiceList.get(rowIndex).getName();
            case 1: return clientServiceList.get(rowIndex).getStatus();
            case 2: return clientServiceList.get(rowIndex).getPath();
            case 3: return clientServiceList.get(rowIndex).getUptime();
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
