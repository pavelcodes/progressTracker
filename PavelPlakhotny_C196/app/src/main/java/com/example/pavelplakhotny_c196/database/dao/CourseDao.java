package com.example.pavelplakhotny_c196.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pavelplakhotny_c196.database.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    LiveData<List<Course>> getCoursesListAll();

    @Query("SELECT * from course WHERE term_id_FK is null")
    LiveData<List<Course>> getUnassignedCourses();

    @Query("SELECT * from course WHERE term_id_FK = :termIdFK")
    LiveData<List<Course>> getCourseListForTerm(Integer termIdFK);

    @Query("SELECT * FROM course WHERE course_id = :id")
    Course getCourseById(int id);

    @Query("UPDATE course SET term_id_FK = :termIdNull WHERE course_id = :id")
    void unlinkCourseFromTerm(Integer termIdNull, int id);

    @Query("UPDATE course SET term_id_FK = :id WHERE course_title = :title")
    void addCourseToTerm(Integer id, String title);

    @Query("SELECT count(*) from course where status = :status")
    Integer courseInProgressCount(String status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);


}
