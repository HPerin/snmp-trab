package ufrgs.network.manager.data;

import java.util.List;

/**
 * Created by lucas on 12/3/16.
 */
public class Client {

    private String address;
    private String systemDescription;
    private List<ClientService> clientServiceList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription(String systemDescription) {
        this.systemDescription = systemDescription;
    }

    public List<ClientService> getClientServiceList() {
        return clientServiceList;
    }

    public void setClientServiceList(List<ClientService> clientServiceList) {
        this.clientServiceList = clientServiceList;
    }

    public int getServiceDownCount() {
        int count = 0;
        if (clientServiceList == null) return -1;
        for (ClientService clientService : clientServiceList) {
            if (clientService.getStatus().equals("stopped"))
                count++;
        }
        return count;
    }
}
