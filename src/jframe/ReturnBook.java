/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.*;
import javax.swing.JFrame;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import java.text.SimpleDateFormat;



/**
 *
 * @author hieu
 */
public class ReturnBook extends javax.swing.JFrame {

    /**
     * Creates new form IsssueBook
     */
    public ReturnBook() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //to fetch the issue book details from db and display it to panel
    public void getIssueBookDetails(){
        String bookId = txt_bookId.getText();
        String studentId = txt_studentId.getText();
        
        try {
            Connection con = DBConnection.getConnection();
            String sql = "select * from issue_book_details where bookID = ? and studentID = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);
            pst.setString(2, studentId);
            pst.setString(3, "đang mượn");
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                lbl_issueId.setText(rs.getString("id"));
                lbl_bookName.setText(rs.getString("bookName"));
                lbl_studentName.setText(rs.getString("studentName"));
                lbl_issueDate.setText(rs.getString("issueDate"));
                lbl_dueDate.setText(rs.getString("dueDate"));
                lbl_bookError.setText("");
            } else {
                lbl_issueId.setText("");
                lbl_bookName.setText("");
                lbl_studentName.setText("");
                lbl_issueDate.setText("");
                lbl_dueDate.setText("");
                lbl_bookError.setText("Không tồn tại thông tin mượn sách!");

            }
        } catch(Exception e){
            e.printStackTrace();  
        }
    }

    //return the book
    public boolean returnBook1() {
        boolean isReturned = false;
        String bookId = txt_bookId.getText();
        String studentId = txt_studentId.getText();
        String returnDate = txt_returnDate.getText();
        
        try {
            Connection con = DBConnection.getConnection();
            String sql = "update issue_book_details set returnDate = ?, status = ? where bookID = ? and studentID = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, returnDate);
            //String status = checkDate(java.sql.Date.valueOf(returnDate)); 
            //pst.setString(2, status); // Setting status
        
            pst.setString(3, bookId);
            pst.setString(4, studentId);
            pst.setString(5, "đang mượn");
            
            int rowCount = pst.executeUpdate();
            if ( rowCount > 0){
                isReturned = true;
            } else {
                isReturned = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return isReturned;
    }
    
    public boolean returnBook() {
    boolean isReturned = false;
    String bookId = txt_bookId.getText();
    String studentId = txt_studentId.getText();
    String returnDateStr = txt_returnDate.getText(); // Assuming this is in 'yyyy-MM-dd' format

    try {
        // Get the connection
        Connection con = DBConnection.getConnection();

        // Fetch the due date from the database
        String fetchDueDateSql = "SELECT dueDate FROM issue_book_details WHERE bookID = ? AND studentID = ? AND status = ?";
        PreparedStatement fetchDueDatePst = con.prepareStatement(fetchDueDateSql);
        fetchDueDatePst.setString(1, bookId);
        fetchDueDatePst.setString(2, studentId);
        fetchDueDatePst.setString(3, "đang mượn");

        ResultSet rs = fetchDueDatePst.executeQuery();
        Date dueDate = null;
        if (rs.next()) {
            dueDate = rs.getDate("dueDate");
        }

        if (dueDate == null) {
            return false;
        }

        // Convert returnDateStr to java.util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = sdf.parse(returnDateStr);

        // Determine the status based on the due date and return date
        String status = checkDate(dueDate, returnDate);

        // Update the return date and status in the database
        String sql = "UPDATE issue_book_details SET returnDate = ?, status = ? WHERE bookID = ? AND studentID = ? AND status = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, returnDateStr); // Setting returnDate
        pst.setString(2, status); // Setting status
        pst.setString(3, bookId);
        pst.setString(4, studentId);
        pst.setString(5, "đang mượn");

        int rowCount = pst.executeUpdate();
        if (rowCount > 0) {
            isReturned = true;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return isReturned;
}
   
    public String checkDate(Date dueDate, Date returnDate) {
        if (returnDate.after(dueDate)) {
            return "trả quá hạn";
        } else {
            return "đã trả";
        }
    }
    
    public boolean lostBook() {
        boolean isReturned = false;
        String bookId = txt_bookId.getText();
        String studentId = txt_studentId.getText();
        
        try {
            Connection con = DBConnection.getConnection();
            String sql = "update issue_book_details set status = ? where bookID = ? and studentID = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "đã mất");
            pst.setString(2, bookId);
            pst.setString(3, studentId);
            pst.setString(4, "đang mượn");
            
            int rowCount = pst.executeUpdate();
            if ( rowCount > 0){
                isReturned = true;
            } else {
                isReturned = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return isReturned;
    }
    
   
    public void updateBookCount() {
        String bookId = txt_bookId.getText();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "update book_details set quantity = quantity + 1 where bookId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);

            int rowCount = pst.executeUpdate();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng sách thành công");
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
        jLabel15 = new javax.swing.JLabel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        txt_studentId = new app.bolivia.swing.JCTextField();
        jLabel17 = new javax.swing.JLabel();
        rSMaterialButtonRectangle1 = new rojerusan.RSMaterialButtonRectangle();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lbl_issueId = new app.bolivia.swing.JCTextField();
        jLabel21 = new javax.swing.JLabel();
        lbl_bookName = new app.bolivia.swing.JCTextField();
        jLabel22 = new javax.swing.JLabel();
        lbl_studentName = new app.bolivia.swing.JCTextField();
        lbl_dueDate = new app.bolivia.swing.JCTextField();
        jLabel23 = new javax.swing.JLabel();
        lbl_bookError = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lbl_issueDate = new app.bolivia.swing.JCTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        rSMaterialButtonRectangle2 = new rojerusan.RSMaterialButtonRectangle();
        jLabel2 = new javax.swing.JLabel();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        txt_returnDate = new app.bolivia.swing.JCTextField();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 153, 204));
        jLabel15.setText("Mã sách");
        panel_main.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 250, 80, -1));

        txt_bookId.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
        panel_main.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 240, 250, 40));

        txt_studentId.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
        panel_main.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 310, 250, 40));

        jLabel17.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 153, 204));
        jLabel17.setText("Mã sinh viên");
        panel_main.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 320, 120, -1));

        rSMaterialButtonRectangle1.setBackground(new java.awt.Color(100, 136, 234));
        rSMaterialButtonRectangle1.setText("Kiểm tra");
        rSMaterialButtonRectangle1.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        rSMaterialButtonRectangle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle1ActionPerformed(evt);
            }
        });
        panel_main.add(rSMaterialButtonRectangle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 380, 210, -1));

        jPanel3.setBackground(new java.awt.Color(100, 136, 234));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Thông tin mượn sách");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabel20.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Mã phiếu mượn");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 150, -1));

        lbl_issueId.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_issueId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_issueIdActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_issueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 360, 40));

        jLabel21.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Tên sách");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 150, -1));

        lbl_bookName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_bookName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_bookNameActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 360, 40));

        jLabel22.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Tên sinh viên");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 140, -1));

        lbl_studentName.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_studentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_studentNameActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 360, 40));

        lbl_dueDate.setFont(new java.awt.Font("Montserrat", 0, 14)); // NOI18N
        lbl_dueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_dueDateActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 360, 40));

        jLabel23.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Hạn trả");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, 150, -1));

        lbl_bookError.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        lbl_bookError.setForeground(new java.awt.Color(255, 51, 51));
        jPanel3.add(lbl_bookError, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 620, 360, 110));

        jLabel24.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Ngày mượn");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 150, -1));

        lbl_issueDate.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        lbl_issueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_issueDateActionPerformed(evt);
            }
        });
        jPanel3.add(lbl_issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 360, 40));

        panel_main.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 440, 800));

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Trả sách");
        jPanel1.add(jLabel1);

        panel_main.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 90, 350, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel11);

        panel_main.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 50, 40));

        rSMaterialButtonRectangle2.setBackground(new java.awt.Color(100, 136, 234));
        rSMaterialButtonRectangle2.setText("Trả sách");
        rSMaterialButtonRectangle2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        rSMaterialButtonRectangle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle2ActionPerformed(evt);
            }
        });
        panel_main.add(rSMaterialButtonRectangle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 560, 210, -1));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/library-2.png"))); // NOI18N
        panel_main.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 80, 550, 700));

        rSMaterialButtonRectangle3.setBackground(new java.awt.Color(100, 136, 234));
        rSMaterialButtonRectangle3.setText("Mất sách");
        rSMaterialButtonRectangle3.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle3ActionPerformed(evt);
            }
        });
        panel_main.add(rSMaterialButtonRectangle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 660, 210, -1));

        txt_returnDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_returnDate.setForeground(new java.awt.Color(102, 102, 102));
        txt_returnDate.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txt_returnDate.setPhColor(new java.awt.Color(102, 102, 102));
        txt_returnDate.setPlaceholder("Nhập ngày trả");
        txt_returnDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_returnDateFocusLost(evt);
            }
        });
        txt_returnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_returnDateActionPerformed(evt);
            }
        });
        panel_main.add(txt_returnDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 490, 250, 40));

        jLabel18.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 153, 204));
        jLabel18.setText("Ngày trả");
        panel_main.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 500, 120, -1));

        getContentPane().add(panel_main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1700, 800));

        setSize(new java.awt.Dimension(1658, 810));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookIdActionPerformed

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void rSMaterialButtonRectangle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle1ActionPerformed
        getIssueBookDetails();
    }//GEN-LAST:event_rSMaterialButtonRectangle1ActionPerformed

    private void txt_bookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookIdFocusLost
        
    }//GEN-LAST:event_txt_bookIdFocusLost

    private void txt_studentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentIdFocusLost
        
    }//GEN-LAST:event_txt_studentIdFocusLost

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void rSMaterialButtonRectangle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle2ActionPerformed
        if (returnBook() ==  true){
            JOptionPane.showMessageDialog(this, "Trả sách thành công!");
            updateBookCount();
        } else{
            JOptionPane.showMessageDialog(this, "Trả sách thất bại!");
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle2ActionPerformed

    private void lbl_issueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_issueDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_issueDateActionPerformed

    private void lbl_dueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_dueDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_dueDateActionPerformed

    private void lbl_studentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_studentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_studentNameActionPerformed

    private void lbl_bookNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_bookNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_bookNameActionPerformed

    private void lbl_issueIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_issueIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_issueIdActionPerformed

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        if (lostBook() ==  true){
            JOptionPane.showMessageDialog(this, "Báo mất sách thành công!");
        } else{
            JOptionPane.showMessageDialog(this, "Báo mất sách thất bại!");
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void txt_returnDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_returnDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_returnDateFocusLost

    private void txt_returnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_returnDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_returnDateActionPerformed

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
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReturnBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbl_bookError;
    private app.bolivia.swing.JCTextField lbl_bookName;
    private app.bolivia.swing.JCTextField lbl_dueDate;
    private app.bolivia.swing.JCTextField lbl_issueDate;
    private app.bolivia.swing.JCTextField lbl_issueId;
    private app.bolivia.swing.JCTextField lbl_studentName;
    private javax.swing.JPanel panel_main;
    private rojeru_san.componentes.RSDateChooserBeanInfo rSDateChooserBeanInfo1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle1;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle2;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_returnDate;
    private app.bolivia.swing.JCTextField txt_studentId;
    // End of variables declaration//GEN-END:variables
}
