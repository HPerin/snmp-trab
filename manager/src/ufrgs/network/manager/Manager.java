package ufrgs.network.manager;

import sun.applet.Main;
import ufrgs.network.manager.data.ClientService;
import ufrgs.network.manager.network.InfoProvider;
import ufrgs.network.manager.ui.MainWindow;

import java.io.IOException;

/**
 * Created by lucas on 12/3/16.
 */
public class Manager {

    public static void main(String args[]) throws IOException, InterruptedException {
        MainWindow mainWindow = new MainWindow();
        mainWindow.run();

        while(true) {
            mainWindow.updateDatabase();
            Thread.sleep(10000);
        }
    }
}
