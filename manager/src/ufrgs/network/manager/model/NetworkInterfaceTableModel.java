package ufrgs.network.manager.model;

import ufrgs.network.manager.data.NetworkInterface;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lucas on 12/4/16.
 */
public class NetworkInterfaceTableModel implements TableModel {

    private List<NetworkInterface> networkInterfaceList;

    public NetworkInterfaceTableModel(List<NetworkInterface> networkInterfaceList) {
        this.networkInterfaceList = networkInterfaceList;
    }

    @Override
    public int getRowCount() {
        return networkInterfaceList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Description";
            case 1: return "Speed";
            case 2: return "InOctets";
            case 3: return "OutOctets";
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

    public String getUnitBytes(int exp) {
        switch (exp) {
            case 0:
                return "B";
            case 3:
                return "KB";
            case 6:
                return "MB";
            case 9:
                return "GB";
            case 12:
                return "TB";
            default:
                return "10 ^ " + String.valueOf(exp);
        }
    }

    public String getUnitBits(int exp) {
        switch (exp) {
            case 0:
                return "b";
            case 3:
                return "Kb";
            case 6:
                return "Mb";
            case 9:
                return "Gb";
            case 12:
                return "Tb";
            default:
                return "10 ^ " + String.valueOf(exp);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        double speed = networkInterfaceList.get(rowIndex).getSpeed();
        int speedExp = 0;
        while(speed > 1000) {
            speed /= 1000;
            speedExp += 3;
        }
        speed = new BigDecimal(speed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        double inOctets = networkInterfaceList.get(rowIndex).getInOctets();
        int inOctetsExp = 0;
        while(inOctets > 1024) {
            inOctets /= 1024;
            inOctetsExp += 3;
        }
        inOctets = new BigDecimal(inOctets).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        double outOctets = networkInterfaceList.get(rowIndex).getOutOctets();
        int outOctetsExp = 0;
        while(outOctets > 1024) {
            outOctets /= 1024;
            outOctetsExp += 3;
        }
        outOctets = new BigDecimal(outOctets).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        switch (columnIndex) {
            case 0: return networkInterfaceList.get(rowIndex).getDescription();
            case 1: return String.valueOf(speed) + " " + getUnitBits(speedExp);
            case 2: return String.valueOf(inOctets) + " " + getUnitBytes(inOctetsExp);
            case 3: return String.valueOf(outOctets) + " " + getUnitBytes(outOctetsExp);
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
