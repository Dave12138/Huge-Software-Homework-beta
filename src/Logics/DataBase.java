package Logics;

import DataType.Admin;
import DataType.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import static DataType.DataTools.isNameLegal;
import static DataType.DataTools.isPasswordLegal;

public class DataBase {
    public static final String SQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String SQL_HOST = "jdbc:mysql://192.168.0.102:3306/ticketSys?useSSL=false";
    public static final String SQL_USER = "root";
    public static final String SQL_PASSWORD = "A15604559612";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static void main(String[] args) {
        final String INSERT_PRE = "insert into testTable(calendar) values ('";
        final String INSERT_END = "')";

        try {
            var stm = getConnection();
            var result = stm.executeQuery("select * from testTable");
            Vector<Vector<Object>> tableVector = new Vector<>();
            var fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);
            while (result.next()) {
                Calendar cc = Calendar.getInstance();
                int id = result.getInt("id");
                String tim = result.getString("calendar");
                Date dt = result.getDate("calendar");
                Time resultTime = result.getTime("calendar");
                Vector<Object> line = new Vector<>();
                line.add(id);
//                line.add(tim);
//                line.add(dt);
//                line.add(resultTime);
                cc.setTime(fmt.parse(tim));
                line.add(fmt.format(cc.getTime()));
                tableVector.add(line);
//                JOptionPane.showMessageDialog(null, "id:" + id + ",tim:" + tim);
            }

            String[] header = {"id", "Calendar"};
            DefaultTableModel model = new DefaultTableModel(tableVector, new Vector<>(Arrays.asList(header))) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JOptionPane.showMessageDialog(null, new JScrollPane(new JTable(model)));
            Calendar createdNewOne = Calendar.getInstance();
            var rand = new Random(createdNewOne.getTimeInMillis());
            while (JOptionPane.showConfirmDialog(null, "继续？", "插入新值", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                createdNewOne.add(Calendar.HOUR, rand.nextInt() % 500);
                stm.executeUpdate(INSERT_PRE + fmt.format(createdNewOne.getTime()) + INSERT_END);
            }
            closeConnect(stm);
        } catch (SQLException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static Statement getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(SQL_DRIVER);
        var connection = DriverManager.getConnection(SQL_HOST, SQL_USER, SQL_PASSWORD);
        return connection.createStatement();
    }

    private static void closeConnect(Statement statement) throws SQLException {
        Connection connection = statement.getConnection();
        statement.close();
        connection.close();
    }

    public static User getUserByName(String name) {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select (password,admin) from users where name=" + name);
            if (result.next()) {
                if (result.getBoolean("admin")) {
                    return new Admin(name, result.getString("password"));
                }
                return new User(name, result.getString("password"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertUser(User user) {
        try {
            if (!isNameLegal(user.getName()) || !isPasswordLegal(user.getPassword())) {
                throw new IllegalArgumentException("用户名或密码");
            }
            Statement stm = getConnection();
            boolean power = user instanceof Admin;
            int rst = stm.executeUpdate("insert into users (name, password, admin) value ('" + user.getName() + "','" + user.getPassword() + "'," + power + ")");
            if (rst != 1) {
                throw new SQLException("SQL未知错误");
            }

        } catch (SQLException | IllegalArgumentException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
