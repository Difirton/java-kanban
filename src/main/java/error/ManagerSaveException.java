package main.java.error;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) { //Я как раз думал и выбирал из двух вариантов: записать сразу
        // месседж(поскольку исключение бросается в одном методе или передать из конкретного места. Не угадал...))
        super(message);
    }
}
