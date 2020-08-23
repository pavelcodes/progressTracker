package com.example.pavelplakhotny_c196.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course", indices = {@Index(name = "term_id_FK_Index", value = {"term_id_FK"})},
        foreignKeys = {@ForeignKey(entity = Term.class, parentColumns = "term_id", childColumns = "term_id_FK", onDelete = CASCADE)})
public class Course implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int course_id;
    @ColumnInfo(name = "course_title")
    private String course_title;
    @ColumnInfo(name = "course_notes")
    private String course_notes;
    @ColumnInfo(name = "term_id_FK")
    private Integer term_id_FK;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "course_start")
    private String course_start;
    @ColumnInfo(name = "course_end")
    private String course_end;
    @ColumnInfo(name = "mentor_name")
    private String mentor_name;
    @ColumnInfo(name = "mentor_email")
    private String mentor_email;
    @ColumnInfo(name = "mentor_phone")
    private String mentor_phone;

    public Course(String course_title, String course_start, String course_end, Integer term_id_FK, String status, String course_notes, String mentor_name, String mentor_email, String mentor_phone) {
        // this.course_id = course_id;
        this.course_title = course_title;
        this.course_notes = course_notes;
        this.term_id_FK = term_id_FK;
        this.status = status;
        this.course_start = course_start;
        this.course_end = course_end;
        this.mentor_name = mentor_name;
        this.mentor_email = mentor_email;
        this.mentor_phone = mentor_phone;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    public String getMentor_email() {
        return mentor_email;
    }

    public void setMentor_email(String mentor_email) {
        this.mentor_email = mentor_email;
    }

    public String getMentor_phone() {
        return mentor_phone;
    }

    public void setMentor_phone(String mentor_phone) {
        this.mentor_phone = mentor_phone;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourse_notes() {
        return course_notes;
    }

    public void setCourse_notes(String course_notes) {
        this.course_notes = course_notes;
    }

    public Integer getTerm_id_FK() {
        return term_id_FK;
    }

    public void setTerm_id_FK(Integer term_id_FK) {
        this.term_id_FK = term_id_FK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourse_start() {
        return course_start;
    }

    public void setCourse_start(String course_start) {
        this.course_start = course_start;
    }

    public String getCourse_end() {
        return course_end;
    }

    public void setCourse_end(String course_end) {
        this.course_end = course_end;
    }
}

