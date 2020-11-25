package com.sargam.college.notes;

public class NoteValues {

     private String title;
    private String description;
    private String priority;
    private String startDate;
    private String lastDate;

    public NoteValues() {
        //empty constructor required
    }

    public  NoteValues(String title,String description,String priority,String startDate,String lastDate){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.startDate = startDate;
        this.lastDate = lastDate;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
