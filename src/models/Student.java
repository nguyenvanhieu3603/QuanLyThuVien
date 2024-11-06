/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import java.util.Date;

/**
 *
 * @author hieu
 */
public class Student {
    private String studentID;
    private String name;
    private String gender;
    private Date birthday;
    private String email;
    private String major;
    private int issuedBooks;
    
    public Student() {
    }

    public Student(String studentID, String name, int issuedBooks) {
        this.studentID = studentID;
        this.name = name;
        this.issuedBooks = issuedBooks;
    }

    
    
    // Constructor
    public Student(String studentID, String name, String gender, Date birthday, String email, String major) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.major = major;
    }

    public Student(String studentID, String name, String email, String major) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.major = major;
    }

    
    
    // Getters
    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public int getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(int issuedBooks) {
        this.issuedBooks = issuedBooks;
    }
    
    
}
