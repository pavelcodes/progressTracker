package com.example.pavelplakhotny_c196.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pavelplakhotny_c196.database.entity.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessment")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * from assessment WHERE course_id_FK = :courseId ORDER BY due_date DESC")
    LiveData<List<Assessment>> getAssessmentsByCourse(int courseId);

    @Query("SELECT * FROM assessment WHERE assessment_id = :assessmentId")
    Assessment getAssessmentById(int assessmentId);

    @Query("SELECT * from assessment WHERE course_id_FK is null")
    LiveData<List<Assessment>> getUnassignedAssessments();

    @Query("SELECT * from assessment WHERE course_id_FK = :termIdFK")
    LiveData<List<Assessment>> getAssessmentListForCurrentCourse(Integer termIdFK);

    @Query("UPDATE assessment SET course_id_FK = :courseIdNull WHERE assessment_id = :id")
    void unlinkAssessmentFromCourse(Integer courseIdNull, int id);

    @Query("UPDATE assessment SET course_id_FK = :id WHERE assessment_title = :title")
    void addAssessmentToCourse(Integer id, String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

}
