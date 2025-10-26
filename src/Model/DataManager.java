package Model;

import java.io.*;

public
    class DataManager {

    private static final String FILE_PATH = "app_data.ser";

    public static void save(AppState state) {
        try (ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(FILE_PATH))) {
            out.writeObject(state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppState load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (AppState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
