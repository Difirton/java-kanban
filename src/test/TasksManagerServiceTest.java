package test;

import junit.framework.TestCase;
import services.TasksManagerService;

import java.util.ArrayList;
import java.util.List;

public class TasksManagerServiceTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        TasksManagerService taskManagerService = new TasksManagerService();
        List<String> examplesNamesEpics = new ArrayList<>(List.of("Изучить Java-core", "Изучить PostgreSQL",
                "Изучить Spring-boot", "Изучить Java-EE", "Изучить Spring-security", "Изучить Spring-Data",
                "Изучить Spring-Cloud", "Изучить Hibernate", "Изучить Kafka", "Изучить MySQL", "Изучить патеерны"));
        List<String> examplesNamesSubtasks = new ArrayList<>(List.of("Изучить классы", "Изучить инкапсуляцию",
                "Изучить наслудование", "Изучить уровни доступа", "Изучить полиморфизм", "Изучить интерфесы",
                "Изучить исключения", "Изучить коллекции", "Изучить Generic", "Изучить создание потоков",
                "Изучить синхронизацию поток", "Изучить классы синхронизации", "Изучить лямбда-выражения",
                "Изучить Stream API", "Изучить Reflection API", "Изучить JDBC", "Изучить связи таблиц",
                "Изучить запросы SQL", "Изучить параллельный доступ", "Изучить конфигурирование bean через XML",
                "Изучить внедрение зависимостей", "Изучить области видимости бинов", "Изучить Thymeleaf",
                "Изучить Maven", "Изучить JPA", "Изучить AOP", "Изучить Zuul", "Изучить Service Discovery"));
        String examplesDescription = "Главное упорство)";

        for (int i = 0; i < 5; i++) {
            taskManagerService.createNewEpic(examplesNamesEpics.get((int)(examplesNamesEpics.size() * Math.random())),
                    examplesDescription);
        }
        for (int i = 0; i < 6; i++) {
            taskManagerService.createNewSubtask(examplesNamesSubtasks.get((int)(examplesNamesEpics.size() *
                    Math.random())), examplesDescription, (long)(5 * Math.random()));
        }
    }
}
