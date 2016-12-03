package ufrgs.network.manager.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by lucas on 12/3/16.
 */
public class Database {

    private Map<String, String> clientMap;

    public Database() {
        clientMap = new HashMap<>();
    }

    public void addClients(Map<String, String> clientMap) {
        for (Map.Entry<String, String> entry : clientMap.entrySet()) {
            if (this.clientMap.containsKey(entry.getKey())) {
                this.clientMap.remove(entry.getKey());
            }
            this.clientMap.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, String> getClientMap() {
        return clientMap;
    }
}
