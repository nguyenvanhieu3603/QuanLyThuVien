/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import javax.swing.table.DefaultTableModel;
import models.Book;

/**
 *
 * @author hieu
 */
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class ManageBooks extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    String bookID, bookName, category, author, publisher;
    int price, quantity;
    DefaultTableModel model;

    public ManageBooks() {
        initComponents();
        setBookDetailsToTable();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    // to set the student details into table
    public void setBookDetailsToTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlythuvien", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from book_details");

            while (rs.next()) {
                int serialID = rs.getInt("serialID");
                String bookID = rs.getString("bookID");
                String bookName = rs.getString("bookName");
                String category = rs.getString("category");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");

                Object[] obj = { serialID, bookID, bookName, category, author, publisher, price, quantity };
                model = (DefaultTableModel) tbl_bookDetails.getModel();
                model.addRow(obj);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // to add book to book_details table
    public boolean addBook() {
        boolean isAdded = false;

        // Lấy thông tin sách từ các trường nhập liệu
        String bookID = txtBookID.getText();
        String bookName = txtBookName.getText();
        String category = comboCategory.getSelectedItem().toString();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        int price = Integer.parseInt(txtPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());

        // Thực hiện thêm sách vào cơ sở dữ liệu
        try {
            Connection conn = DBConnection.getConnection();

            // Lấy giá trị serialID lớn nhất hiện có
            String getMaxSerialIDSql = "SELECT MAX(serialID) AS max_serialID FROM student_details";
            Statement getMaxSerialIDStatement = conn.createStatement();
            ResultSet rs = getMaxSerialIDStatement.executeQuery(getMaxSerialIDSql);

            int maxSerialID = 0;
            if (rs.next()) {
                maxSerialID = rs.getInt("max_serialID");
            }

            // Tăng giá trị serialID lên một đơn vị
            int newSerialID = maxSerialID + 1;

            String sql = "INSERT INTO book_details (serialID, bookID, bookName, category, author, publisher, price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, newSerialID);
            pst.setString(2, bookID);
            pst.setString(3, bookName);
            pst.setString(4, category);
            pst.setString(5, author);
            pst.setString(6, publisher);
            pst.setInt(7, price);
            pst.setInt(8, quantity);

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                // Lấy giá trị của serialID đã được tự động tăng
                ResultSet generatedKeys = pst.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int serialID = generatedKeys.getInt(1);
                    // Đổ dữ liệu vào table
                    Object[] obj = { newSerialID, bookID, bookName, category, author, publisher, price, quantity };
                    DefaultTableModel model = (DefaultTableModel) tbl_bookDetails.getModel();
                    model.addRow(obj);
                }
                isAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // to update book details
    public boolean updateBook() {
        boolean isUpdated = false;
        String bookID = txtBookID.getText();
        String bookName = txtBookName.getText();
        String category = comboCategory.getSelectedItem().toString();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        int price = Integer.parseInt(txtPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());

        try {
            Connection con = DBConnection.getConnection();
            String sql = "update book_details set bookName = ?, category = ?, author = ?, publisher = ?, price = ?, quantity = ?   where bookID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookID);
            pst.setString(2, bookName);
            pst.setString(3, category);
            pst.setString(4, author);
            pst.setString(5, publisher);
            pst.setInt(6, price);
            pst.setInt(7, quantity);

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    // method to delete book detail
    public boolean deleteBook() {
        boolean isDeleted = false;
        bookID = txtBookID.getText();

        try {
            Connection con = DBConnection.getConnection();
            // Lấy serialID của sinh viên được chọn
            String serialIDSql = "SELECT serialID FROM student_details WHERE studentID = ?";
            PreparedStatement serialIDStatement = con.prepareStatement(serialIDSql);
            serialIDStatement.setString(1, bookID);
            ResultSet rs = serialIDStatement.executeQuery();
            int serialID = 0;
            if (rs.next()) {
                serialID = rs.getInt("serialID");
            }
            // Xóa sách có book_id được chỉ định
            String deleteSql = "DELETE FROM book_details WHERE bookID = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteSql);
            deleteStatement.setString(1, bookID);
            int deleteRowCount = deleteStatement.executeUpdate();

            // Cập nhật lại số thứ tự của các sách còn lại
            String updateSql = "UPDATE book_details SET serialID = serialID - 1 WHERE serialID > ?";
            PreparedStatement updateStatement = con.prepareStatement(updateSql);
            updateStatement.setInt(1, serialID);
            int updateRowCount = updateStatement.executeUpdate();

            // Kiểm tra nếu sinh viên đã được xóa và số thứ tự đã được cập nhật
            if (deleteRowCount > 0 && updateRowCount > 0) {
                isDeleted = true;
            } else {
                isDeleted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    // method to find book based on bookID and category
    public Book searchByID(String bookID) {
        String sql = "SELECT * FROM book_details WHERE bookID = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, bookID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String bookName = rs.getString("bookName");
                String category = rs.getString("category");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int price = Integer.valueOf(rs.getString("price"));
                int quantity = Integer.valueOf(rs.getString("quantity"));

                // Tạo đối tượng sách từ dữ liệu được trả về từ cơ sở dữ liệu
                Book book = new Book(bookID, bookName, category, author, publisher, price, quantity);
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy sách
    }

    public List<Book> searchByCategory(String category) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM book_details WHERE category = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, category);
            ResultSet rs = pst.executeQuery();

            // Duyệt qua các dòng kết quả và thêm vào danh sách
            while (rs.next()) {
                String bookID = rs.getString("bookID");
                String bookName = rs.getString("bookName");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int price = Integer.valueOf(rs.getString("price"));
                int quantity = Integer.valueOf(rs.getString("quantity"));

                Book book = new Book(bookID, bookName, category, author, publisher, price, quantity);
                bookList.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList; // Trả về null nếu không tìm thấy sách
    }

    // method to clear table
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) tbl_bookDetails.getModel();
        model.setRowCount(0);
    }

    public void clearFormInput() {
        txtBookID.setText("");
        txtBookName.setText("");
        txtQuantity.setText("");
        txtAuthor.setText("");
        txtPublisher.setText("");
        txtPrice.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtBookID = new app.bolivia.swing.JCTextField();
        txtBookName = new app.bolivia.swing.JCTextField();
        txtQuantity = new app.bolivia.swing.JCTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblQLSV = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAuthor = new app.bolivia.swing.JCTextField();
        txtPublisher = new app.bolivia.swing.JCTextField();
        comboCategory = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtPrice = new app.bolivia.swing.JCTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jPanel2 = new javax.swing.JPanel();
        lblThongTinSV = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtFindID = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnLamMoi = new rojeru_san.complementos.RSButtonHover();
        btnSua = new rojeru_san.complementos.RSButtonHover();
        btnXoa = new rojeru_san.complementos.RSButtonHover();
        btnThem = new rojeru_san.complementos.RSButtonHover();
        rbtGiaoTrinh = new javax.swing.JRadioButton();
        rbtThamKhao = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(100, 136, 234));
        jPanel4.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 30, -1));

        jLabel9.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Mã sách");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        txtBookID.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtBookID.setPlaceholder("Nhập mã sách");
        txtBookID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBookIDActionPerformed(evt);
            }
        });
        jPanel4.add(txtBookID, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 320, 40));

        txtBookName.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtBookName.setPlaceholder("Nhập tên sách");
        txtBookName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBookNameActionPerformed(evt);
            }
        });
        jPanel4.add(txtBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 320, 40));

        txtQuantity.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtQuantity.setPlaceholder("Nhập số lượng");
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });
        jPanel4.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 320, 40));

        jLabel11.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Thể loại");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        jLabel2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Số lượng");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 660, -1, -1));

        jLabel7.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tên sách");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, -1, -1));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 50));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblQLSV.setBackground(new java.awt.Color(153, 153, 255));
        lblQLSV.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        lblQLSV.setForeground(new java.awt.Color(102, 102, 102));
        lblQLSV.setText("Quản lý sách");
        jPanel6.add(lblQLSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 300, 50));

        jLabel4.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tác giả");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, -1, -1));

        jLabel5.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nhà xuất bản");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 480, -1, -1));

        txtAuthor.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtAuthor.setPlaceholder("Nhập tác giả");
        txtAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuthorActionPerformed(evt);
            }
        });
        jPanel4.add(txtAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 320, 40));

        txtPublisher.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtPublisher.setPlaceholder("Nhập nhà xuất bản");
        txtPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPublisherActionPerformed(evt);
            }
        });
        jPanel4.add(txtPublisher, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, 320, 40));

        comboCategory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giáo trình", "Sách tham khảo", " " }));
        jPanel4.add(comboCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 320, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Book_Shelf_50px.png"))); // NOI18N
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, -1, 60));

        txtPrice.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtPrice.setPlaceholder("Nhập giá sách");
        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });
        jPanel4.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 600, 320, 40));

        jLabel13.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Đơn giá");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 570, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 830));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sách", "Tên sách", "Thể loại", "Tác giả", "Nhà xuất bản", "Giá", "Số lượng"
            }
        ));
        tbl_bookDetails.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setRowHeight(30);
        tbl_bookDetails.setShowGrid(false);
        tbl_bookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bookDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_bookDetails);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 1090, 360));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblThongTinSV.setFont(new java.awt.Font("Montserrat", 1, 48)); // NOI18N
        lblThongTinSV.setText("Thông tin sách");
        jPanel2.add(lblThongTinSV);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1090, 80));

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel1.setText("Tìm kiếm theo mã sách");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 250, 40));

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setText("Tìm kiếm theo thể loại sách");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 250, 40));

        txtFindID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtFindID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindIDActionPerformed(evt);
            }
        });
        jPanel1.add(txtFindID, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 310, 40));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 190, -1, 40));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 153, 255));
        jButton1.setText("Tìm kiếm");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 160, 110, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kinhLup.png"))); // NOI18N
        jLabel6.setText("jLabel6");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 160, 50, 40));

        btnLamMoi.setText("Làm mới");
        btnLamMoi.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        jPanel1.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 670, 140, 60));

        btnSua.setText("Sửa");
        btnSua.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        jPanel1.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 670, 120, 60));

        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        jPanel1.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 670, 130, 60));

        btnThem.setText("Thêm");
        btnThem.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel1.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 670, 140, 60));

        buttonGroup1.add(rbtGiaoTrinh);
        rbtGiaoTrinh.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rbtGiaoTrinh.setText("Giáo trình");
        rbtGiaoTrinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtGiaoTrinhActionPerformed(evt);
            }
        });
        jPanel1.add(rbtGiaoTrinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 310, 40));

        buttonGroup1.add(rbtThamKhao);
        rbtThamKhao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rbtThamKhao.setText("Sách tham khảo");
        jPanel1.add(rbtThamKhao, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 310, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 1110, 830));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel8MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }// GEN-LAST:event_jLabel8MouseClicked

    private void txtBookIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtBookIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtBookIDActionPerformed

    private void txtBookNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtBookNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtBookNameActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtQuantityActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        if (deleteBook() == true) {
            JOptionPane.showMessageDialog(this, "Xóa sách thành công");
            clearTable();
            setBookDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sách không thành công");
        }
    }// GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        if (addBook() == true) {
            JOptionPane.showMessageDialog(this, "Thêm sách thành công");
            clearTable();
            setBookDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sách không thành công");
        }
    }// GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaActionPerformed
        if (updateBook() == true) {
            JOptionPane.showMessageDialog(this, "Cập nhật sách thành công");
            clearTable();
            setBookDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sách không thành công");
        }
    }// GEN-LAST:event_btnSuaActionPerformed

    private void tbl_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbl_bookDetailsMouseClicked
        int rowNo = tbl_bookDetails.getSelectedRow();
        TableModel model = tbl_bookDetails.getModel();

        txtBookID.setText(model.getValueAt(rowNo, 1).toString());
        txtBookName.setText(model.getValueAt(rowNo, 2).toString());
        comboCategory.setSelectedItem(model.getValueAt(rowNo, 3).toString());

    }// GEN-LAST:event_tbl_bookDetailsMouseClicked

    private void txtAuthorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAuthorActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtAuthorActionPerformed

    private void txtPublisherActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtPublisherActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtPublisherActionPerformed

    private void txtFindIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFindIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFindIDActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String ma = txtFindID.getText();

        if (!ma.isEmpty()) {
            // người dùng nhập mã sinh viên
            Book bk = searchByID(ma);
            if (bk != null) {
                model.setRowCount(0);
                Object[] row = { 0, bk.getBookID(), bk.getBookName(), bk.getCategory(), bk.getAuthor(),
                        bk.getPublisher(), bk.getQuantity() };
                model.addRow(row);
                JOptionPane.showMessageDialog(rootPane, "Tìm kiếm sách thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sách có mã " + ma);
                txtFindID.setText("");
            }
        } else if (rbtGiaoTrinh.isSelected()) {
            String loai = rbtGiaoTrinh.getText();
            List<Book> danhSach = searchByCategory(loai);
            if (!danhSach.isEmpty()) {
                model.setRowCount(0);
                int i = 0;
                for (Book bk : danhSach) {
                    Object[] row = { i + 1, bk.getBookID(), bk.getBookName(), bk.getCategory(), bk.getAuthor(),
                            bk.getPublisher(), bk.getQuantity() };
                    model.addRow(row);
                    i++;
                }
                JOptionPane.showMessageDialog(rootPane, "Tìm kiếm sách theo thể loại thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sách có thể loại là " + loai);
            }
        } else if (rbtThamKhao.isSelected()) {
            String loai = rbtThamKhao.getText();
            List<Book> danhSach = searchByCategory(loai);
            if (!danhSach.isEmpty()) {
                model.setRowCount(0);
                int i = 0;
                for (Book bk : danhSach) {
                    Object[] row = { i + 1, bk.getBookID(), bk.getBookName(), bk.getCategory(), bk.getAuthor(),
                            bk.getPublisher(), bk.getQuantity() };
                    model.addRow(row);
                    i++;
                }
                JOptionPane.showMessageDialog(rootPane, "Tìm kiếm sách theo thể loại thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sách có thể loại là " + loai);
            }
        }
    }// GEN-LAST:event_jButton1ActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLamMoiActionPerformed
        clearTable();
        clearFormInput();
        setBookDetailsToTable();
    }// GEN-LAST:event_btnLamMoiActionPerformed

    private void rbtGiaoTrinhActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rbtGiaoTrinhActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_rbtGiaoTrinhActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBooks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.complementos.RSButtonHover btnLamMoi;
    private rojeru_san.complementos.RSButtonHover btnSua;
    private rojeru_san.complementos.RSButtonHover btnThem;
    private rojeru_san.complementos.RSButtonHover btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboCategory;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblQLSV;
    private javax.swing.JLabel lblThongTinSV;
    private javax.swing.JRadioButton rbtGiaoTrinh;
    private javax.swing.JRadioButton rbtThamKhao;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private app.bolivia.swing.JCTextField txtAuthor;
    private app.bolivia.swing.JCTextField txtBookID;
    private app.bolivia.swing.JCTextField txtBookName;
    private javax.swing.JTextField txtFindID;
    private app.bolivia.swing.JCTextField txtPrice;
    private app.bolivia.swing.JCTextField txtPublisher;
    private app.bolivia.swing.JCTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
