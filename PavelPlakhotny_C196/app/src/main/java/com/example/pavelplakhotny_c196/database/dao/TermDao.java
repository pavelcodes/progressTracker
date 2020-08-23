package com.example.pavelplakhotny_c196.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pavelplakhotny_c196.database.entity.Term;

import java.util.List;

@Dao
public interface TermDao {
    @Query("SELECT * FROM term ORDER BY term_start")
    LiveData<List<Term>> getTermListAll();

    @Query("SELECT * FROM term WHERE term_id = :termId")
    Term getTermForId(int termId);

    @Query("SELECT COUNT(*) from course WHERE term_id_FK =:id")
    int checkIfCoursesAssignedToTerm(Integer id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTerm(Term term);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);


}
