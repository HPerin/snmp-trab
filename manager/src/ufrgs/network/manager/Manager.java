package ufrgs.network.manager;

import com.alee.laf.WebLookAndFeel;
import sun.applet.Main;
import ufrgs.network.manager.data.ClientService;
import ufrgs.network.manager.network.InfoProvider;
import ufrgs.network.manager.ui.MainWindow;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by lucas on 12/3/16.
 */
public class Manager {

    public static void main(String args[]) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WebLookAndFeel.install();
            }
        });

        MainWindow mainWindow = new MainWindow();
        mainWindow.run();

        while(true) {
            mainWindow.updateDatabase();
            Thread.sleep(10000);
        }
    }
}
