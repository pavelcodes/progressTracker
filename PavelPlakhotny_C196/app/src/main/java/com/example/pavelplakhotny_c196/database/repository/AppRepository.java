package com.example.pavelplakhotny_c196.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.pavelplakhotny_c196.database.AppDatabase;
import com.example.pavelplakhotny_c196.database.entity.Assessment;
import com.example.pavelplakhotny_c196.database.entity.Course;
import com.example.pavelplakhotny_c196.database.entity.Term;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {

    private static AppRepository instance;
    private AppDatabase appDatabase;
    public LiveData<List<Term>> mTerms;
    public LiveData<List<Course>> mCourses;
    public LiveData<List<Assessment>> mAssessments;
    public LiveData<List<Course>> termCourses;
    public LiveData<List<Assessment>> assessments_in_course;
    public int courseCount;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    private AppRepository(Context context) {
        appDatabase = AppDatabase.getDatabase(context);
        mTerms = getAllTerms();
        mCourses = getAllCourses();
        mAssessments = getAllAssessments();
    }

    //Terms
    public LiveData<List<Term>> getAllTerms() {
        return appDatabase.termDao().getTermListAll();
    }

    public Term getTermForId(int termId) {
        return appDatabase.termDao().getTermForId(termId);
    }

    public void insertTerm(final Term term) {
        executor.execute(() -> appDatabase.termDao().insertTerm(term));
    }

    public void updateTerm(final Term term) {
        executor.execute(() -> {
            appDatabase.termDao().updateTerm(term);
        });
    }


    public int checkIfCoursesAssignedToTerm(final Integer termId) {
        courseCount = appDatabase.termDao().checkIfCoursesAssignedToTerm(termId);
        return courseCount;
    }


    public void deleteTerm(final Term term) {
        executor.execute(() -> appDatabase.termDao().deleteTerm(term));
    }

    public void unlinkCourseFromTerm(final Integer termIdNull, int id) {
        executor.execute(() -> appDatabase.courseDao().unlinkCourseFromTerm(termIdNull, id));
    }
    public void addCourseToTerm(final Integer id, final String title) {
        executor.execute(() -> appDatabase.courseDao().addCourseToTerm(id, title));
    }

    //courses

    public LiveData<List<Course>> getAllCourses() {
        return appDatabase.courseDao().getCoursesListAll();
    }

    public void updateCourse(final Course course) {
        executor.execute(() -> {
            appDatabase.courseDao().updateCourse(course);
        });
    }

    public LiveData<List<Course>> getUnassignedCourses() {
        return appDatabase.courseDao().getUnassignedCourses();
    }

    public LiveData<List<Course>> getCourseListForTerm(Integer termIdFK) {
        termCourses = appDatabase.courseDao().getCourseListForTerm(termIdFK);
        return termCourses;
    }

    public void insertCourse(final Course course) {
        executor.execute(() -> appDatabase.courseDao().insertCourse(course));
    }

    public void deleteCourse(final Course course) {
        executor.execute(() -> appDatabase.courseDao().deleteCourse(course));
    }

    //assessments
    public LiveData<List<Assessment>> getAllAssessments() {
        return appDatabase.assessmentDao().getAllAssessments();
    }

    public void updateAssessment(final Assessment assessment) {
        executor.execute(() -> {
            appDatabase.assessmentDao().updateAssessment(assessment);
        });
    }

    public void insertAssessment(final Assessment assessment) {
        executor.execute(() -> appDatabase.assessmentDao().insertAssessment(assessment));
    }

    public void deleteAssessment(final Assessment assessment) {
        executor.execute(() -> appDatabase.assessmentDao().deleteAssessment(assessment));
    }

    public void unlinkAssessmentFromCourse(final Integer courseIdNull, int id) {
        executor.execute(() -> appDatabase.assessmentDao().unlinkAssessmentFromCourse(courseIdNull, id));
    }

    public void addAssessmentToCourse(final Integer id, final String title) {
        executor.execute(() -> appDatabase.assessmentDao().addAssessmentToCourse(id, title));
    }

    public LiveData<List<Assessment>> getUnassignedAssessments() {
        return appDatabase.assessmentDao().getUnassignedAssessments();
    }

    public LiveData<List<Assessment>> getAssessmentListForCurrentCourse(Integer courseIdFK) {
        assessments_in_course = appDatabase.assessmentDao().getAssessmentListForCurrentCourse(courseIdFK);
        return assessments_in_course;
    }
}

