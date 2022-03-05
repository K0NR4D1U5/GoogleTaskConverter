package googletask.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.GoogleTasks;
import models.Task;
import models.TaskItems;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class Main {
    private static final boolean SKIP_COMPLETED = true;
    private static final boolean SKIP_EMPTY = true;
    private static final boolean SKIP_HIDDEN = true;
    private static final boolean SKIP_DELETED = true;
    private static final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {
        String json = FileTools.readFileToString(System.getProperty("user.dir") + "/Tasks.json");
        GoogleTasks googleTasks = gson.fromJson(json, GoogleTasks.class);
        
        LinkedHashMap<String, LinkedHashSet<String>> uncompletedTasksListMap = new LinkedHashMap<>();
        List<TaskItems> taskItems = googleTasks.getTasks();

        for (TaskItems taskItem : taskItems) {
            List<Task> tasks = taskItem.getItems();
            LinkedHashSet<String> uncompletedTasks = new LinkedHashSet<>();

            for (Task task : tasks) {
                boolean deleted = task.getDeleted() && SKIP_DELETED;
                boolean hidden = task.getHidden() && SKIP_HIDDEN;
                boolean completed = task.getStatus().equals("completed") && SKIP_COMPLETED;
                boolean empty = task.getTitle().trim().equals("") && SKIP_EMPTY;

                if (!deleted && !hidden && !completed && !empty) {
                    if (!task.getTitle().trim().equals("")) {
                        //TODO fix due date and repeatabiliy ("notes": "RO(2,2)",
                        //                                    "due": "2022-03-05T00:00:00Z")
                        uncompletedTasks.add("task," + task.getTitle().trim() + "," + "4," + "2,,," + task.getDue() + ",,");
                    }
                }
            }
            uncompletedTasksListMap.put(taskItem.getTitle(), uncompletedTasks);
        }

        FileTools.saveTasksToFile("Tasks", uncompletedTasksListMap);
    }
}