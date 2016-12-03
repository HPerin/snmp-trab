package ufrgs.network.manager;

import ufrgs.network.manager.network.Discover;
import ufrgs.network.manager.network.InfoProvider;
import ufrgs.network.manager.ui.MainWindow;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by lucas on 12/3/16.
 */
public class Manager {

    public static void main(String args[]) throws IOException {
        new MainWindow().run();
    }
}
