package view;

import modeles.Course;


import java.util.List;

public interface ICourse {

 Course findById(int id);
    List<Course> findAll();
    void save(Course course);
    void update(Course course);
    void delete(int id);
}
