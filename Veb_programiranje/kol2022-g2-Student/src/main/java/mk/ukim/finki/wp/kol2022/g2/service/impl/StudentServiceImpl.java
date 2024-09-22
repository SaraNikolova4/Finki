package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.CourseRepository;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    private final CourseRepository courseRepository;
    @Override
    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(InvalidStudentIdException :: new);
    }

    @Override
    public Student create(String name, String email, String password, StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        List<Course> courseList = courseRepository.findAllById(courseId);
        return studentRepository.save(new Student(name, email,password,type,courseList, enrollmentDate));
    }

    @Override
    public Student update(Long id, String name, String email, String password, StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        List<Course> courseList = courseRepository.findAllById(coursesId);
        Student s = studentRepository.findById(id).orElseThrow(InvalidStudentIdException :: new);
        s.setName(name);
        s.setEmail(email);
        s.setPassword(password);
        s.setEnrollmentDate(enrollmentDate);
        s.setCourses(courseList);
        s.setType(type);
        studentRepository.save(s);
        return  s;
    }

    @Override
    public Student delete(Long id) {
        Student s = studentRepository.findById(id).orElseThrow(InvalidStudentIdException :: new);
        studentRepository.delete(s);
        return s;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {

        return null;
    }
}
