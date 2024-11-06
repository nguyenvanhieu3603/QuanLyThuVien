/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import javax.swing.table.DefaultTableModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;
import models.Student;

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

public class ManageStudents extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    String studentID, studentName, gender, studentEmail, major, birthday;
    DefaultTableModel model;

    public ManageStudents() {
        initComponents();
        setStudentDetailsToTable();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    // to set the student details into table
    public void setStudentDetailsToTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlythuvien", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from student_details");

            while (rs.next()) {
                int serialID = rs.getInt("serialID");
                String studentID = rs.getString("studentID");
                String studentName = rs.getString("StudentName");
                String gender = rs.getString("gender");
                java.sql.Date birthday = rs.getDate("birthday");
                String studentEmail = rs.getString("studentEmail");
                String major = rs.getString("major");

                // Định dạng ngày sinh thành chuỗi yyyy-MM-dd
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedBirthday = dateFormat.format(birthday);

                Object[] obj = { serialID, studentID, studentName, gender, formattedBirthday, studentEmail, major };
                model = (DefaultTableModel) tbl_studentDetails.getModel();
                model.addRow(obj);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // to add book to book_details table
    public boolean addStudent() {
        boolean isAdded = false;

        // Lấy thông tin sinh viên từ các trường nhập liệu
        String studentID = txtStudentID.getText();
        String studentName = txtStudentName.getText();
        String gender = txtGender.getText();
        String birthdayString = txtBirthday.getText();
        String studentEmail = txtStudentEmail.getText();
        String major = comboMajor.getSelectedItem().toString();

        // Chuyển đổi chuỗi ngày sinh thành kiểu java.sql.Date
        Date birthday = parseDate(birthdayString);

        // Thực hiện thêm sinh viên vào cơ sở dữ liệu
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

            // Thêm sinh viên mới vào cơ sở dữ liệu với giá trị serialID mới
            String sql = "INSERT INTO student_details (serialID, studentID, StudentName, gender, birthday, studentEmail, major) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, newSerialID);
            pst.setString(2, studentID);
            pst.setString(3, studentName);
            pst.setString(4, gender);
            pst.setDate(5, birthday);
            pst.setString(6, studentEmail);
            pst.setString(7, major);

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                // Đổ dữ liệu vào table
                Object[] obj = { newSerialID, studentID, studentName, gender, birthdayString, studentEmail, major };
                DefaultTableModel model = (DefaultTableModel) tbl_studentDetails.getModel();
                model.addRow(obj);
                isAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // Phương thức chuyển đổi chuỗi ngày thành java.sql.Date
    private Date parseDate(String dateString) {
        SimpleDateFormat[] formats = {
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("MM/dd/yyyy")
        };

        for (SimpleDateFormat format : formats) {
            try {
                format.setLenient(false);
                return new Date(format.parse(dateString).getTime());
            } catch (ParseException e) {
                // Không làm gì cả, thử định dạng tiếp theo
            }
        }

        // Nếu không có định dạng nào phù hợp
        throw new IllegalArgumentException("Ngày không hợp lệ: " + dateString);
    }

    // to update book details
    public boolean updateStudent() {
        boolean isUpdated = false;
        String studentID = txtStudentID.getText();
        String studentName = txtStudentName.getText();
        String gender = txtGender.getText();
        String birthdayString = txtBirthday.getText();
        String studentEmail = txtStudentEmail.getText();
        String major = comboMajor.getSelectedItem().toString();

        Date birthday = parseDate(birthdayString);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "update student_details set studentName = ?, gender = ?, birthday = ?, studentEmail = ?, major = ?   where studentID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, studentName);
            pst.setString(2, gender);
            pst.setDate(3, birthday);
            pst.setString(4, studentEmail);
            pst.setString(5, major);
            pst.setString(6, studentID);

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

    // method to delete student detail
    public boolean deleteStudent() {
        boolean isDeleted = false;
        studentID = txtStudentID.getText(); // Lấy giá trị studentID từ textfield

        try {
            Connection con = DBConnection.getConnection();
            // Lấy serialID của sinh viên được chọn
            String serialIDSql = "SELECT serialID FROM student_details WHERE studentID = ?";
            PreparedStatement serialIDStatement = con.prepareStatement(serialIDSql);
            serialIDStatement.setString(1, studentID);
            ResultSet rs = serialIDStatement.executeQuery();
            int serialID = 0;
            if (rs.next()) {
                serialID = rs.getInt("serialID");
            }
            rs.close();

            // Xóa sinh viên có student_id được chỉ định
            String deleteSql = "DELETE FROM student_details WHERE studentID = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteSql);
            deleteStatement.setString(1, studentID);
            int deleteRowCount = deleteStatement.executeUpdate();

            // Cập nhật lại số thứ tự của các sinh viên còn lại
            String updateSql = "UPDATE student_details SET serialID = serialID - 1 WHERE serialID > ?";
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

    // method to find student based on studentID and major
    public Student searchByID(String studentID) {
        String sql = "SELECT * FROM student_details WHERE studentID = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, studentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int serialID = rs.getInt("serialID");
                String id = rs.getString("studentID");
                String name = rs.getString("StudentName");
                String gender = rs.getString("gender");
                Date birthday = rs.getDate("birthday");
                String email = rs.getString("studentEmail");
                String studentMajor = rs.getString("major");

                // Tạo đối tượng sinh viên từ dữ liệu được trả về từ cơ sở dữ liệu
                Student student = new Student(id, name, gender, birthday, email, studentMajor);
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy sinh viên
    }

    public List<Student> searchByMajor(String major) {
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM student_details WHERE major = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, major);
            ResultSet rs = pst.executeQuery();

            // Duyệt qua các dòng kết quả và thêm vào danh sách
            while (rs.next()) {
                String id = rs.getString("studentID");
                String name = rs.getString("StudentName");
                String gender = rs.getString("gender");
                Date birthday = rs.getDate("birthday");
                String email = rs.getString("studentEmail");
                String studentMajor = rs.getString("major");

                // Định dạng ngày sinh thành chuỗi yyyy-MM-dd
                // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // String formattedBirthday = dateFormat.format(birthday);
                Student st = new Student(id, name, gender, birthday, email, studentMajor);
                studentList.add(st);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList; // Trả về null nếu không tìm thấy sinh viên
    }

    public void displayTable() {

    }

    // method to clear table
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) tbl_studentDetails.getModel();
        model.setRowCount(0);
    }

    public void clearFormInput() {
        txtStudentID.setText("");
        txtStudentName.setText("");
        txtGender.setText("");
        txtBirthday.setText("");
        txtStudentEmail.setText("");
    }

    // Phương thức để sắp xếp lại dữ liệu trong bảng theo serialID tăng dần
    private void sortDataBySerialID() {
        DefaultTableModel model = (DefaultTableModel) tbl_studentDetails.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        tbl_studentDetails.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // Sắp xếp theo cột serialID
        sorter.setSortKeys(sortKeys);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtStudentID = new app.bolivia.swing.JCTextField();
        txtStudentName = new app.bolivia.swing.JCTextField();
        txtGender = new app.bolivia.swing.JCTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblQLSV = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtBirthday = new app.bolivia.swing.JCTextField();
        txtStudentEmail = new app.bolivia.swing.JCTextField();
        comboMajor = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_studentDetails = new rojeru_san.complementos.RSTableMetro();
        jPanel2 = new javax.swing.JPanel();
        lblThongTinSV = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtFindID = new javax.swing.JTextField();
        txtFindMajor = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnHienThi = new rojeru_san.complementos.RSButtonHover();
        btnLamMoi = new rojeru_san.complementos.RSButtonHover();
        btnSua = new rojeru_san.complementos.RSButtonHover();
        btnXoa = new rojeru_san.complementos.RSButtonHover();
        btnThem = new rojeru_san.complementos.RSButtonHover();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(100, 136, 234));
        jPanel4.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("MSSV");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        txtStudentID.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtStudentID.setPlaceholder("Nhập mã số sinh viên");
        txtStudentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStudentIDActionPerformed(evt);
            }
        });
        jPanel4.add(txtStudentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 280, 40));

        txtStudentName.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtStudentName.setPlaceholder("Nhập họ và tên sinh viên");
        txtStudentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStudentNameActionPerformed(evt);
            }
        });
        jPanel4.add(txtStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 280, 40));

        txtGender.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtGender.setPlaceholder("Nhập giới tính");
        txtGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGenderActionPerformed(evt);
            }
        });
        jPanel4.add(txtGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 410, 280, 40));

        jLabel11.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Giới tính");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, -1));

        jLabel2.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Chuyên ngành");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 650, -1, -1));

        jLabel7.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Họ và tên");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, -1));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/51516_arrow_back_left_icon.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 50));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblQLSV.setBackground(new java.awt.Color(153, 153, 255));
        lblQLSV.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        lblQLSV.setForeground(new java.awt.Color(102, 102, 102));
        lblQLSV.setText("Quản lý sinh viên");
        jPanel6.add(lblQLSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 340, 50));

        jLabel4.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Ngày sinh");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, -1, -1));

        jLabel5.setFont(new java.awt.Font("Montserrat SemiBold", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Địa chỉ email");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 560, -1, -1));

        txtBirthday.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtBirthday.setPlaceholder("Nhập ngày sinh");
        txtBirthday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBirthdayActionPerformed(evt);
            }
        });
        jPanel4.add(txtBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 500, 280, 40));

        txtStudentEmail.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        txtStudentEmail.setPlaceholder("Nhập email");
        txtStudentEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStudentEmailActionPerformed(evt);
            }
        });
        jPanel4.add(txtStudentEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 590, 280, 40));

        comboMajor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboMajor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CNTT", "HTTT", "KHMT", " " }));
        jPanel4.add(comboMajor, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 680, 280, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/male_user_50px.png"))); // NOI18N
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 50, 60));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 830));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_studentDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "STT", "Mssv", "Họ và tên", "Giới tính", "Ngày sinh", "Địa chỉ email", "Chuyên ngành"
                }));
        tbl_studentDetails.setColorBordeFilas(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setColorBordeHead(new java.awt.Color(255, 255, 255));
        tbl_studentDetails.setRowHeight(30);
        tbl_studentDetails.setShowGrid(false);
        tbl_studentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_studentDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_studentDetails);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 990, 380));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblThongTinSV.setFont(new java.awt.Font("Montserrat", 1, 48)); // NOI18N
        lblThongTinSV.setText("Thông tin sinh viên");
        jPanel2.add(lblThongTinSV);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 990, 80));

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel1.setText("Tìm kiếm theo mã sinh viên");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 250, 40));

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setText("Tìm kiếm theo chuyên ngành");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 250, 40));

        txtFindID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtFindID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindIDActionPerformed(evt);
            }
        });
        jPanel1.add(txtFindID, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 310, 40));

        txtFindMajor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtFindMajor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindMajorActionPerformed(evt);
            }
        });
        jPanel1.add(txtFindMajor, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 310, 40));

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

        btnHienThi.setText("Hiển thị");
        btnHienThi.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnHienThi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiActionPerformed(evt);
            }
        });
        jPanel1.add(btnHienThi, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 690, 150, 50));

        btnLamMoi.setText("Làm mới");
        btnLamMoi.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        jPanel1.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 690, 150, 50));

        btnSua.setText("Sửa");
        btnSua.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        jPanel1.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 690, 150, 50));

        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        jPanel1.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 690, 150, 50));

        btnThem.setText("Thêm");
        btnThem.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel1.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 690, 160, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 1030, 830));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel8MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }// GEN-LAST:event_jLabel8MouseClicked

    private void txtStudentIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtStudentIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtStudentIDActionPerformed

    private void txtStudentNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtStudentNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtStudentNameActionPerformed

    private void txtGenderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtGenderActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtGenderActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        if (deleteStudent() == true) {
            JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công");
            clearTable();
            setStudentDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sinh viên không thành công");
        }
    }// GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        if (addStudent() == true) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công");
            clearTable();
            sortDataBySerialID();
            setStudentDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên không thành công");
        }
    }// GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaActionPerformed
        if (updateStudent() == true) {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công");
            clearTable();
            setStudentDetailsToTable();
            clearFormInput();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên không thành công");
        }
    }// GEN-LAST:event_btnSuaActionPerformed

    private void tbl_studentDetailsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tbl_studentDetailsMouseClicked
        int rowNo = tbl_studentDetails.getSelectedRow();
        TableModel model = tbl_studentDetails.getModel();

        txtStudentID.setText(model.getValueAt(rowNo, 1).toString());
        txtStudentName.setText(model.getValueAt(rowNo, 2).toString());
        comboMajor.setSelectedItem(model.getValueAt(rowNo, 3).toString());

    }// GEN-LAST:event_tbl_studentDetailsMouseClicked

    private void txtBirthdayActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtBirthdayActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtBirthdayActionPerformed

    private void txtStudentEmailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtStudentEmailActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtStudentEmailActionPerformed

    private void txtFindIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFindIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFindIDActionPerformed

    private void txtFindMajorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFindMajorActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFindMajorActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String masv = txtFindID.getText();
        String nganh = txtFindMajor.getText();

        if (!masv.isEmpty() && nganh.isEmpty()) {
            // người dùng nhập mã sinh viên
            Student sv = searchByID(masv);
            if (sv != null) {
                model.setRowCount(0);
                Object[] row = { 0, sv.getStudentID(), sv.getName(), sv.getGender(), sv.getBirthday(), sv.getEmail(),
                        sv.getMajor() };
                model.addRow(row);
                JOptionPane.showMessageDialog(rootPane, "Tìm kiếm sinh viên thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sinh viên có mã " + masv);
                txtFindID.setText("");
            }
        } else if (!nganh.isEmpty() && masv.isEmpty()) {
            List<Student> danhSachSV = searchByMajor(nganh);
            if (!danhSachSV.isEmpty()) {
                int i = 0;
                model.setRowCount(0);
                for (Student sv : danhSachSV) {
                    Object[] row = { i + 1, sv.getStudentID(), sv.getName(), sv.getGender(), sv.getBirthday(),
                            sv.getEmail(), sv.getMajor() };
                    model.addRow(row);
                    i++;
                }
                JOptionPane.showMessageDialog(rootPane, "Tìm kiếm sinh viên theo chuyên ngành thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sinh viên có ngành là " + nganh);
            }
        }
    }// GEN-LAST:event_jButton1ActionPerformed

    private void btnHienThiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHienThiActionPerformed
        setStudentDetailsToTable();
    }// GEN-LAST:event_btnHienThiActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLamMoiActionPerformed
        clearTable();
    }// GEN-LAST:event_btnLamMoiActionPerformed

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
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStudents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.complementos.RSButtonHover btnHienThi;
    private rojeru_san.complementos.RSButtonHover btnLamMoi;
    private rojeru_san.complementos.RSButtonHover btnSua;
    private rojeru_san.complementos.RSButtonHover btnThem;
    private rojeru_san.complementos.RSButtonHover btnXoa;
    private javax.swing.JComboBox<String> comboMajor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private rojeru_san.complementos.RSTableMetro tbl_studentDetails;
    private app.bolivia.swing.JCTextField txtBirthday;
    private javax.swing.JTextField txtFindID;
    private javax.swing.JTextField txtFindMajor;
    private app.bolivia.swing.JCTextField txtGender;
    private app.bolivia.swing.JCTextField txtStudentEmail;
    private app.bolivia.swing.JCTextField txtStudentID;
    private app.bolivia.swing.JCTextField txtStudentName;
    // End of variables declaration//GEN-END:variables
}
