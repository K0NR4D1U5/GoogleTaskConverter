package googletask.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class FileTools {
    public static String readFileToString(String path) {
        String content = null;
        try {
            content = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Tasks.json file not found.");
        }
        return content;
    }

    public static void saveTasksToFile(String fileName, LinkedHashMap<String, LinkedHashSet<String>> tasks) {
        Path filePath = Paths.get(fileName.trim().replace('/', '-') + ".csv");
        StringBuilder stringBuilder = new StringBuilder();

        for (var entry : tasks.entrySet()) {
            stringBuilder.append("task,").append(entry.getKey()).append(",4,1,,,,,\n,,,,,,,,\n");
            for (String task : entry.getValue()) {
                stringBuilder.append(task).append("\n,,,,,,,,\n");
            }
        }
        String taskList = stringBuilder.toString().trim();

        try {
            Files.writeString(filePath, taskList, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
