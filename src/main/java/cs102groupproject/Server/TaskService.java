package cs102groupproject.Server;

import cs102groupproject.SharedObjects.Task;
/**
 * responsible for handling server-side task operations
 * and database interactions.
 * @author Begüm Göktaş
 * Date: 27/12/2025
 */
public class TaskService {

    private final DBManager db;

    public TaskService(DBManager db) {
        this.db = db;
    }
    
    /**
     * updates the given task
     */
    public void updateTask(Task task) {
        boolean success = db.updateTaskStatus(task.getId(), task.getIsCompleted());
        
        if (success) {
            System.out.println("Task ID " + task.getId() + " is updated.");
        } else {
            System.err.println("Task ID " + task.getId() + " NOT UPDATED");
        }
    }

    /**
     * saves the given task to the database 
     * @return saved Task
     */
    public Task createTask(Task task){
        int id = db.addTask(task.getName(), task.getColor(),task.getImportance(), task.getUserId(), task.getGoogleCaldendarID(), task.getDueDate());
        return new Task(task.getName(), task.getColor(), task.getImportance(), task.getUserId(), id, task.getDueDate(), task.getGoogleCaldendarID(), false);
    }
    
}
