package service;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements Serializable {
    private final long serialVersionUID = 1L;
    private File file;

    FileBackedTasksManager() {
        file = new File("resources" + File.separator + "data" + File.separator + "data.bin");
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)) ) {
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileBackedTasksManager read() {
        File fileToRead = new File("resources" + File.separator + "data" + File.separator + "data.bin");
        FileBackedTasksManager fileBackedTasksManager = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToRead)) ) {
            fileBackedTasksManager = (FileBackedTasksManager) ois.readObject();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileBackedTasksManager{");
        sb.append("Epics=").append(this.getAllEpics());
        sb.append(", Subtasks=").append(this.getAllSubtasks());
        sb.append(", History=").append(this.getHistory());
        sb.append('}');
        return sb.toString();
    }
}
