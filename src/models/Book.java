/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author hieu
 */


public class Book {

    public Book(String bookID, String bookName, String author, int quantity) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.author = author;
        this.quantity = quantity;
    }

    public Book(String bookID, String bookName, int issuedQuantity) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.issuedQuantity = issuedQuantity;
    }
    
    
    
    public Book(String bookID, String bookName, String category, String author, String publisher, int price, int quantity) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    private String bookID;
    private String bookName;
    private String category;
    private String author;
    private String publisher;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    private int price;
    private int quantity;
    private int issuedQuantity;

    public int getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(int issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }
    
    
}
