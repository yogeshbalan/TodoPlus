package work.innov8.todoplus.model;

import java.util.Date;

/**
 * Created by yogesh on 11/3/17.
 */

public class Task {

    private int _id;
    private String taskTitle;
    private String taskDescription;
    private Date taskDate;
    private Date taskDateCreated;
    private boolean taskDone;

    public Task() {
    }

    public Task(String taskTitle, String taskDescription, Date taskDate, Date taskDateCreated, boolean taskDone) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskDateCreated = taskDateCreated;
        this.taskDone = taskDone;
    }

    public Task(int _id, String taskTitle, String taskDescription, Date taskDate, Date taskDateCreated, boolean taskDone) {
        this._id = _id;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskDateCreated = taskDateCreated;
        this.taskDone = taskDone;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Date getTaskDateCreated() {
        return taskDateCreated;
    }

    public void setTaskDateCreated(Date taskDateCreated) {
        this.taskDateCreated = taskDateCreated;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
