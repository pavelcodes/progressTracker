package com.example.pavelplakhotny_c196.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "term")
public class Term implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int term_id;
    @ColumnInfo(name = "term_name")
    private String term_name;
    @ColumnInfo(name = "term_start")
    private String term_start;
    @ColumnInfo(name = "term_end")
    private String term_end;


    @Ignore
    public Term(int term_id, String term_name, String term_start, String term_end) {
        this.term_id = term_id;
        this.term_name = term_name;
        this.term_start = term_start;
        this.term_end = term_end;
    }

    public Term(String term_name, String term_start, String term_end) {
        this.term_name = term_name;
        this.term_start = term_start;
        this.term_end = term_end;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public void setTerm_start(String term_start) {
        this.term_start = term_start;
    }

    public void setTerm_end(String term_end) {
        this.term_end = term_end;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public String getTerm_start() {
        return term_start;
    }

    public String getTerm_end() {
        return term_end;
    }


}
