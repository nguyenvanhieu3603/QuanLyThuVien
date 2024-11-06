/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.*;
import javax.swing.JFrame;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author hieu
 */
public class IssueBook extends javax.swing.JFrame {

    /**
     * Creates new form IsssueBook
     */
    public IssueBook() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        date_dueDate.setEnabled(false);
        
    }

    public void getBookDetails() {
        String bookId = txt_bookId.getText();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from book_details where bookID = ?");
            pst.setString(1, bookId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lbl_bookId.setText(rs.getString("bookID"));
                lbl_bookName.setText(rs.getString("bookName"));
                lbl_category.setText(rs.getString("category"));
                lbl_author.setText(rs.getString("author"));
                lbl_publisher.setText(rs.getString("publisher"));
                lbl_quantity.setText(rs.getString("quantity"));
                lbl_bookError.setText("");
            } else {
                lbl_bookError.setText("Sách không tồn tại");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStudentDetails() {
        String studentId = txt_studentId.getText();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from student_details where studentID = ?");
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lbl_studentId.setText(rs.getString("studentID"));
                lbl_studentName.setText(rs.getString("studentName"));
                lbl_email.setText(rs.getString("studentEmail"));
                lbl_major.setSelectedItem(rs.getString("major"));
                lbl_studentError.setText("");
            } else {
                lbl_studentError.setText("Sinh viên không tồn tại");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    insert isssue book details to db
    public boolean issueBook() {
        boolean isIssued = false;
        String bookId = txt_bookId.getText();
        String studentId = txt_studentId.getText();
        String bookName = lbl_bookName.getText();
        String studentName = lbl_studentName.getText();

        Date uIssueDate = date_issueDate.getDatoFecha();
        Date uDueDate = date_dueDate.getDatoFecha();

        long l1 = uIssueDate.getTime();
        long l2 = uDueDate.getTime();

        if (l1 > l2) {
            JOptionPane.showMessageDialog(this, "Ngày trả sách phải sau ngày mượn sách");
            return false;
        }

        java.sql.Date sIssueDate = new java.sql.Date(l1);
        java.sql.Date sDueDate = new java.sql.Date(l2);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "insert into issue_book_details(bookID, bookName, studentID, studentName, issueDate, dueDate, status)"
                    + "values(?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);
            pst.setString(2, bookName);
            pst.setString(3, studentId);
            pst.setString(4, studentName);
            pst.setDate(5, sIssueDate);
            pst.setDate(6, sDueDate);
            pst.setString(7, "Đang mượn");

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                isIssued = true;
            } else {
                isIssued = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isIssued;
    }

//    update book quantity
    public void updateBookQuantity() {
        String bookId = txt_bookId.getText();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "update book_details set quantity = quantity - 1 where bookId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng sách thành công");
                int initialCount = Integer.parseInt(lbl_quantity.getText());

                lbl_quantity.setText(Integer.toString(initialCount - 1));
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng sách không thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    Check if a book is already issued by a student
    public boolean isAlreadyIssued() {
        boolean isAlreadyIssued = false;

        String bookId = txt_bookId.getText();
        String studentId = txt_studentId.getText();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "select * from issue_book_details where bookID = ? and studentID = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);
            pst.setString(2, studentId);
            pst.setString(3, "Đang mượn");

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                isAlreadyIssued = true;
            } else {
                isAlreadyIssued = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAlreadyIssued;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rSDateChooserBeanInfo1 = new rojeru_san.componentes.RSDateChooserBeanInfo();
        panel_main = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lbl_studentId = new app.bolivia.swing.JCTextField();
        jLabel7 = new javax.swing.JLabel();
        lbl_studentName = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        lbl_email = new app.bolivia.swing.JCTextField();
        jLabel2 = new javax.swing.JLabel();
        lbl_major = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        lbl_studentError = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        txt_studentId = new app.bolivia.swing.JCTextField();
        jLabel16 = new javax.swing.JLabel();
        date_issueDate = new rojeru_san.componentes.RSDateChooser();
        date_dueDate = new rojeru_san.componentes.RSDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        rSMaterialButtonRectangle1 = new rojerusan.RSMaterialButtonRectangle();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lbl_bookId = new app.bolivia.swing.JCTextField();
        jLabel21 = new javax.swing.JLabel();
        lbl_bookName = new app.bolivia.swing.JCTextField();
        jLabel22 = new javax.swing.JLabel();
        lbl_author = new app.bolivia.swing.JCTextField();
        lbl_publisher = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        lbl_bookError1 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lbl_bookError = new javax.swing.JLabel();
        lbl_quantity = new app.bolivia.swing.JCTextField();
        jLabel24 = new javax.swing.JLabel();
        lbl_category = new app.bolivia.swing.JCTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("MSSV");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 120, -1));

        lbl_studentId.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_studentId.setPlaceholder("Nhập mã số sinh viên");
        lbl_studentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_studentIdActionPerformed(evt);
            }
        });
        jPanel1.add(lbl_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 340, 40));

        jLabel7.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Họ và tên");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 150, -1));

        lbl_studentName.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_studentName.setPlaceholder("Nhập họ và tên sinh viên");
        lbl_studentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_studentNameActionPerformed(evt);
            }
        });
        jPanel1.add(lbl_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 340, 40));

        jLabel6.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Địa chỉ email");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 190, -1));

        lbl_email.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_email.setPlaceholder("Nhập email");
        lbl_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_emailActionPerformed(evt);
            }
        });
        jPanel1.add(lbl_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 340, 40));

        jLabel2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Chuyên ngành");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 200, -1));

        lbl_major.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_major.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CNTT", "HTTT", "KHMT", " " }));
        jPanel1.add(lbl_major, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 460, 340, 40));

        jLabel8.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Thông tin sinh viên");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        lbl_studentError.setFont(new java.awt.Font("Montserrat SemiBold", 0, 24)); // NOI18N
        lbl_studentError.setForeground(new java.awt.Color(255, 51, 51));
        jPanel1.add(lbl_studentError, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 550, 340, 60));

        panel_main.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 440, 800));

        jLabel15.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 153, 204));
        jLabel15.setText("Mã sách");
        panel_main.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 250, 80, -1));

        txt_bookId.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 112, 192), null));
        txt_bookId.setForeground(new java.awt.Color(102, 102, 102));
        txt_bookId.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txt_bookId.setPhColor(new java.awt.Color(102, 102, 102));
        txt_bookId.setPlaceholder("Nhập mã sách");
        txt_bookId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_bookIdFocusLost(evt);
            }
        });
        txt_bookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bookIdActionPerformed(evt);
            }
        });
        panel_main.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 240, 250, 40));

        txt_studentId.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 112, 192), null));
        txt_studentId.setForeground(new java.awt.Color(102, 102, 102));
        txt_studentId.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txt_studentId.setPhColor(new java.awt.Color(102, 102, 102));
        txt_studentId.setPlaceholder("Nhập mã sinh viên");
        txt_studentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_studentIdFocusLost(evt);
            }
        });
        txt_studentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_studentIdActionPerformed(evt);
            }
        });
        panel_main.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 310, 250, 40));

        jLabel16.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 153, 204));
        jLabel16.setText("Ngày trả");
        panel_main.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 540, 90, -1));

        date_issueDate.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        date_issueDate.setFuente(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        date_issueDate.setPlaceholder("Chọn ngày mượn");
        date_issueDate.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                date_issueDateCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                date_issueDateInputMethodTextChanged(evt);
            }
        });
        date_issueDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                date_issueDatePropertyChange(evt);
            }
        });
        panel_main.add(date_issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 460, 250, -1));

        date_dueDate.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        date_dueDate.setFuente(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        date_dueDate.setPlaceholder("Chọn ngày trả");
        panel_main.add(date_dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 540, 250, -1));

        jLabel17.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 153, 204));
        jLabel17.setText("Mã sinh viên");
        panel_main.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 320, 120, -1));

        jLabel18.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 153, 204));
        jLabel18.setText("Ngày mượn");
        panel_main.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 470, 120, -1));

        rSMaterialButtonRectangle1.setBackground(new java.awt.Color(100, 136, 234));
        rSMaterialButtonRectangle1.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        rSMaterialButtonRectangle1.setLabel("Hoàn tất");
        rSMaterialButtonRectangle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle1ActionPerformed(evt);
            }
        });
        panel_main.add(rSMaterialButtonRectangle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 640, 210, -1));

        jPanel3.setBackground(new java.awt.Color(100, 136, 234));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Thông tin sách");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        jLabel20.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Mã sách");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 150, -1));

        lbl_bookId.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_bookId.setPlaceholder("Nhập mã sách");
        lbl_bookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_bookIdActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 360, 40));

        jLabel21.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Tên sách");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 150, -1));

        lbl_bookName.setFont(new java.awt.Font("Montserrat", 0, 14)); // NOI18N
        lbl_bookName.setPlaceholder("Nhập tên sách");
        lbl_bookName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_bookNameActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 360, 40));

        jLabel22.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Tác giả");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 140, -1));

        lbl_author.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_author.setPlaceholder("Nhập tác giả");
        lbl_author.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_authorActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_author, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 360, 40));

        lbl_publisher.setFont(new java.awt.Font("Montserrat", 0, 14)); // NOI18N
        lbl_publisher.setPlaceholder("Nhập nhà xuất bản");
        lbl_publisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_publisherActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_publisher, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 360, 40));

        jLabel23.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Nhà xuất bản");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, 150, -1));

        lbl_bookError1.setFont(new java.awt.Font("Montserrat SemiBold", 0, 24)); // NOI18N
        lbl_bookError1.setForeground(new java.awt.Color(255, 51, 51));
        jPanel3.add(lbl_bookError1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 700, 360, 60));

        jLabel19.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Số lượng");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 600, 150, -1));

        lbl_bookError.setFont(new java.awt.Font("Montserrat SemiBold", 0, 24)); // NOI18N
        lbl_bookError.setForeground(new java.awt.Color(255, 51, 51));
        jPanel3.add(lbl_bookError, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 670, 360, 60));

        lbl_quantity.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_quantity.setPlaceholder("Nhập số lượng");
        lbl_quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_quantityActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 630, 360, 40));

        jLabel24.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Thể loại");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 150, -1));

        lbl_category.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_category.setPlaceholder("Nhập thể loại");
        lbl_category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_categoryActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 360, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel4.add(jLabel11);

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 40));

        panel_main.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 800));

        jPanel2.setBackground(new java.awt.Color(51, 102, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mượn sách");
        jPanel2.add(jLabel3);

        panel_main.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 110, 350, 50));

        getContentPane().add(panel_main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1410, 800));

        setSize(new java.awt.Dimension(1425, 810));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_quantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_quantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_quantityActionPerformed

    private void lbl_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_studentIdActionPerformed

    private void lbl_studentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_studentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_studentNameActionPerformed

    private void lbl_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_emailActionPerformed

    private void txt_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookIdActionPerformed

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void rSMaterialButtonRectangle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle1ActionPerformed
        if (lbl_quantity.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Sách này tạm hết vui lòng chọn sách khác");
        } else {

            if (isAlreadyIssued() == false) {
                if (issueBook() == true) {
                    JOptionPane.showMessageDialog(this, "Mượn sách thành công");
                    updateBookQuantity();
                } else {
                    JOptionPane.showMessageDialog(this, "Mượn sách không thành công");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sinh viên đã mượn quyển sách này");
            }
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle1ActionPerformed

    private void txt_bookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookIdFocusLost
        if (!txt_bookId.getText().equals("")) {
            getBookDetails();
        }
    }//GEN-LAST:event_txt_bookIdFocusLost

    private void txt_studentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentIdFocusLost
        if (!txt_studentId.getText().equals("")) {
            getStudentDetails();
        }
    }//GEN-LAST:event_txt_studentIdFocusLost

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void lbl_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_bookIdActionPerformed

    private void lbl_bookNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_bookNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_bookNameActionPerformed

    private void lbl_authorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_authorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_authorActionPerformed

    private void lbl_publisherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_publisherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_publisherActionPerformed

    private void lbl_categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_categoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_categoryActionPerformed

    private void date_issueDateInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_date_issueDateInputMethodTextChanged
        date_dueDate.setDatoFecha(date_issueDate.getDatoFecha());
    }//GEN-LAST:event_date_issueDateInputMethodTextChanged

    private void date_issueDateCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_date_issueDateCaretPositionChanged
    }//GEN-LAST:event_date_issueDateCaretPositionChanged

    private void date_issueDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_date_issueDatePropertyChange
        date_dueDate.setDatoFecha(date_issueDate.getDatoFecha());
    }//GEN-LAST:event_date_issueDatePropertyChange
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IssueBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.componentes.RSDateChooser date_dueDate;
    private rojeru_san.componentes.RSDateChooser date_issueDate;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private app.bolivia.swing.JCTextField lbl_author;
    private javax.swing.JLabel lbl_bookError;
    private javax.swing.JLabel lbl_bookError1;
    private app.bolivia.swing.JCTextField lbl_bookId;
    private app.bolivia.swing.JCTextField lbl_bookName;
    private app.bolivia.swing.JCTextField lbl_category;
    private app.bolivia.swing.JCTextField lbl_email;
    private javax.swing.JComboBox<String> lbl_major;
    private app.bolivia.swing.JCTextField lbl_publisher;
    private app.bolivia.swing.JCTextField lbl_quantity;
    private javax.swing.JLabel lbl_studentError;
    private app.bolivia.swing.JCTextField lbl_studentId;
    private app.bolivia.swing.JCTextField lbl_studentName;
    private javax.swing.JPanel panel_main;
    private rojeru_san.componentes.RSDateChooserBeanInfo rSDateChooserBeanInfo1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle1;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_studentId;
    // End of variables declaration//GEN-END:variables
}
