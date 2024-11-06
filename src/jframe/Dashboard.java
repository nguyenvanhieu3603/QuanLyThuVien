/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import models.Book;
import models.Student;

/**
 *
 * @author hieu
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    DefaultTableModel model;

    public Dashboard() {
        initComponents();
        showPieChart();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setStudentToTable();
        setBookToTable();
        setMissingBooksToTable();

    }

    public void showPieChart() {

        // create dataset
        DefaultPieDataset barDataset = new DefaultPieDataset();

        try {
            Connection connect = DBConnection.getConnection();
            String sql = "select bookName, count(*) as issue_count from issue_book_details group by bookId";
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                barDataset.setValue(rs.getString("bookName"), Double.valueOf(rs.getDouble("issue_count")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create chart
        JFreeChart piechart = ChartFactory.createPieChart("Biểu đồ sách được mượn", barDataset, false, true, false);// explain

        PiePlot piePlot = (PiePlot) piechart.getPlot();

        // changing pie chart blocks colors
        piePlot.setSectionPaint("IPhone 5s", new Color(255, 255, 102));
        piePlot.setSectionPaint("SamSung Grand", new Color(102, 255, 102));
        piePlot.setSectionPaint("MotoG", new Color(255, 102, 153));
        piePlot.setSectionPaint("Nokia Lumia", new Color(0, 204, 204));

        piePlot.setBackgroundPaint(Color.white);

        // create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select studentID, studentName, count(*) as issuedBooks from issue_book_details group by studentID");

            while (rs.next()) {
                String studentID = rs.getString("studentID");
                String studentName = rs.getString("studentName");
                int issuedBooks = rs.getInt("issuedBooks");

                students.add(new Student(studentID, studentName, issuedBooks));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select bookID, bookName, count(*) as issuedBooks from issue_book_details group by bookID");

            while (rs.next()) {
                String bookID = rs.getString("bookID");
                String bookName = rs.getString("bookName");
                int issuedQuantity = rs.getInt("issuedBooks");

                books.add(new Book(bookID, bookName, issuedQuantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getMissingBooks() {
        List<Book> books = new ArrayList<>();

        try {
             Connection conn = DBConnection.getConnection();
            String sql = "select bookID, bookName, count(*) as issuedBooks from issue_book_details where status = ? group by bookID";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "đã mất");
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String bookID = rs.getString("bookID");
                String bookName = rs.getString("bookName");
                int issuedQuantity = rs.getInt("issuedBooks");

                books.add(new Book(bookID, bookName, issuedQuantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public void setStudentToTable() {
        List<Student> students = new ArrayList<>();
        int max = 0;

        for (Student st : getStudents()) {
            if (st.getIssuedBooks() > max) {
                max = st.getIssuedBooks();
            }
        }

        for (Student st : getStudents()) {
            if (st.getIssuedBooks() == max) {
                students.add(st);
            }
        }

        for (Student st : students) {
            Object[] obj = {st.getStudentID(), st.getName(), st.getIssuedBooks()};
            model = (DefaultTableModel) tbl_studentDetails.getModel();
            model.addRow(obj);
        }
    }

    public void setBookToTable() {
        List<Book> books = new ArrayList<>();
        int max = 0;

        for (Book b : getBooks()) {
            if (b.getIssuedQuantity() > max) {
                max = b.getIssuedQuantity();
            }
        }

        for (Book b : getBooks()) {
            if (b.getIssuedQuantity() == max) {
                books.add(b);
            }
        }

        for (Book b : books) {
            Object[] obj = {b.getBookID(), b.getBookName(), b.getIssuedQuantity()};
            model = (DefaultTableModel) tbl_bookDetails.getModel();
            model.addRow(obj);
        }
    }

    public void setMissingBooksToTable() {
        for (Book b : getMissingBooks()) {
            Object[] obj = {b.getBookID(), b.getBookName(), b.getIssuedQuantity()};
            model = (DefaultTableModel) tbl_bookDetails2.getModel();
            model.addRow(obj);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        piePlot1 = new org.jfree.chart.plot.PiePlot();
        year1 = new org.jfree.data.time.Year();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelPieChart = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_studentDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_bookDetails2 = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(102, 102, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thống kê");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 160, 50));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 400, 10));

        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 80, 50));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelPieChart.setPreferredSize(new java.awt.Dimension(540, 450));
        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel2.add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 80, 470, 430));

        tbl_studentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MSSV", "Họ và tên", "Số sách"
            }
        ));
        tbl_studentDetails.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setRowHeight(40);
        tbl_studentDetails.setShowGrid(false);
        tbl_studentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_studentDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_studentDetails);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 990, 150));

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel3.setText("Sinh viên mượn sách nhiều nhất");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 320, 40));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel4.setText("Sách đã mất");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 480, 380, 40));

        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sách", "Tên sách", "Tình trạng"
            }
        ));
        tbl_bookDetails.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setRowHeight(40);
        tbl_bookDetails.setShowGrid(false);
        tbl_bookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bookDetailsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_bookDetails);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 990, 150));

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jLabel5.setText("Sách được sinh viên mượn nhiều nhất");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 380, 40));

        tbl_bookDetails2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sách", "Tên sách", "Số lượng"
            }
        ));
        tbl_bookDetails2.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tbl_bookDetails2.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tbl_bookDetails2.setRowHeight(40);
        tbl_bookDetails2.setShowGrid(false);
        tbl_bookDetails2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bookDetails2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_bookDetails2);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, 990, 150));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 2367, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }//GEN-LAST:event_jPanel4MouseClicked

    private void tbl_studentDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_studentDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_studentDetailsMouseClicked

    private void tbl_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bookDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_bookDetailsMouseClicked

    private void tbl_bookDetails2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bookDetails2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_bookDetails2MouseClicked

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelPieChart;
    private org.jfree.chart.plot.PiePlot piePlot1;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails2;
    private rojeru_san.complementos.RSTableMetro tbl_studentDetails;
    private org.jfree.data.time.Year year1;
    // End of variables declaration//GEN-END:variables
}
