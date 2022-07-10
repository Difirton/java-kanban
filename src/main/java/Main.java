import controller.HttpTaskServer;
import controller.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException { //TODO добавить обработчик
        new KVServer().start();
        new HttpTaskServer().start();

    }
}
