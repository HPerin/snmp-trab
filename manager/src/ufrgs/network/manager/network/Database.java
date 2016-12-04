package ufrgs.network.manager.network;

import com.google.gson.Gson;
import ufrgs.network.manager.data.Client;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by lucas on 12/3/16.
 */
public class Database {

    private List<Client> clientList;
    private String lastUpdate;

    public Database() {
        clientList = new ArrayList<>();
    }

    private synchronized void insertClient(Client newClient) {
        for (Client client : clientList) {
            if (client.getAddress().equals(newClient.getAddress())) {
                clientList.remove(client);
                break;
            }
        }

        clientList.add(newClient);
    }

    public synchronized void addClients(List<Client> newClientList) {
        for (Client client : newClientList) {
            insertClient(client);
        }

        lastUpdate = getCurrentTimeStamp();
        saveDatabase();
    }

    public synchronized String getLastUpdate() {
        return lastUpdate;
    }

    public synchronized List<Client> getClientList() {
        return new ArrayList<>(clientList);
    }

    public synchronized Client getClient(String address) {
        for (Client client : clientList) {
            if (address.equals(client.getAddress())) {
                return client;
            }
        }

        return null;
    }

    private void saveDatabase() {
        try {
            PrintWriter printWriter = new PrintWriter("clients.db");
            printWriter.print(new Gson().toJson(this));
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Database loadFromFile() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("clients.db"));
            String data = new String(encoded, Charset.defaultCharset());
            return new Gson().fromJson(data, Database.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Database();
        }
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }

}
