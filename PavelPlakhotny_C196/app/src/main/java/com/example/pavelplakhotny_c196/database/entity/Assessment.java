package com.example.pavelplakhotny_c196.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "assessment", indices = {@Index(name = "course_id_FK_IndexAssessment", value = {"course_id_FK"})},
        foreignKeys = {@ForeignKey(entity = Course.class, parentColumns = "course_id", childColumns = "course_id_FK", onDelete = CASCADE)})
public class Assessment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int assessment_id;
    @ColumnInfo(name = "assessment_title")
    private String assessment_title;
    @ColumnInfo(name = "type")
    private String assessment_type;
    @ColumnInfo(name = "start_date")
    private String start_date;
    @ColumnInfo(name = "due_date")
    private String due_date;
    @ColumnInfo(name = "course_id_FK")
    private Integer course_id_FK;

    public Assessment(String assessment_title, String assessment_type, String due_date, Integer course_id_FK, String start_date) {
        this.assessment_title = assessment_title;
        this.assessment_type = assessment_type;
        this.due_date = due_date;
        this.course_id_FK = course_id_FK;
        this.start_date = start_date;

    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public String getAssessment_title() {
        return assessment_title;
    }

    public void setAssessment_title(String assessment_title) {
        this.assessment_title = assessment_title;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public Integer getCourse_id_FK() {
        return course_id_FK;
    }

    public void setCourse_id_FK(Integer course_id_FK) {
        this.course_id_FK = course_id_FK;
    }
}
