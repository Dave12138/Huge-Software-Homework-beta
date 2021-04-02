package Logics;

import DataType.Admin;
import DataType.Train;
import DataType.User;
import exception.MyCustomMessageException;

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
    public static final String SQL_HOST = "jdbc:mysql://192.168.108.128:3306/ticketSys?useSSL=false";
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
            var result = stm.executeQuery("select `name`,`password`,`admin` from users where name='" + name + "'");
            if (result.next() && result.getString("name").equals(name)) {
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

    public static User[] getUsers() {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from users");
            Vector<User> list = new Vector<>();
            while (result.next()) {
                if (result.getBoolean("admin")) {
                    list.add(new Admin(result.getString("name"), result.getString("password")));
                } else {
                    list.add(new User(result.getString("name"), result.getString("password")));
                }
            }
            if (!list.isEmpty()) {
                return list.toArray(new User[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User[] getUsers(String key) {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from users where name like '%" + key + "%'");
            Vector<User> list = new Vector<>();
            while (result.next()) {
                if (result.getBoolean("admin")) {
                    list.add(new Admin(result.getString("name"), result.getString("password")));
                } else {
                    list.add(new User(result.getString("name"), result.getString("password")));
                }
            }
            if (!list.isEmpty()) {
                return list.toArray(new User[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUserPassword(String user, char[] newPass) throws MyCustomMessageException {
        if (isNameLegal(user) && isPasswordLegal(newPass)) {
            try {
                Statement stm = getConnection();
                int result = stm.executeUpdate("update  users set password=" + String.valueOf(newPass) + " where name='" + user + "'");
                if (result == 0) {
                    throw new MyCustomMessageException("密码更新失败");
                }


            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Train[] getTrains() {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from trains");
            Vector<Train> list = new Vector<>();
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);

            Calendar calendar = Calendar.getInstance();
            while (result.next()) {
                calendar.setTime(fmt.parse(result.getString("launch")));
                list.add(new Train(
                        result.getInt("No"),
                        result.getString("name"),
                        result.getInt("from"),
                        result.getInt("to"),
                        calendar,
                        result.getInt("minute")
                ));
            }
            if (!list.isEmpty()) {
                return list.toArray(new Train[0]);
            }
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vector<Vector<Object>> usersToTable(User[] list) {
        Vector<Vector<Object>> tab = new Vector<>();
        if (list == null) return null;
        for (User user : list) {
            Vector<Object> line = new Vector<>();
            line.add(user.getName());
            line.add(user.getPassword());
            line.add(String.valueOf(user instanceof Admin));
            tab.add(line);
        }
        return tab.isEmpty() ? null : tab;
    }

    public static Vector<Vector<Object>> trainsToTable(Train[] list) {
        Vector<Vector<Object>> tab = new Vector<>();
        if (list == null) return null;
        String[] p = getPlaces();
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        fmt.setLenient(false);
        for (Train train : list) {
            Vector<Object> line = new Vector<>();
            line.add(String.valueOf(train.getUid()));
            line.add(train.getName());
            line.add(p[train.getFrom() - 1]);
            line.add(p[train.getTo() - 1]);
            line.add(fmt.format(train.getLaunchTime()));
            line.add(String.valueOf(train.getMinuteCost()));
            tab.add(line);
        }
        return tab.isEmpty() ? null : tab;
    }

    public static String[] getPlaces() {

        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from provinces");
            Vector<String> list = new Vector<>();
            while (result.next()) {
                list.add(result.getString("name"));
            }
            if (!list.isEmpty()) {
                return list.toArray(new String[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertUser(User user) throws SQLException, ClassNotFoundException {
        if (!isNameLegal(user.getName()) || !isPasswordLegal(user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码");
        }
        Statement stm = getConnection();
        boolean power = user instanceof Admin;
        int rst = stm.executeUpdate("insert into users (name, password, admin) value ('" + user.getName() + "','" + user.getPassword() + "'," + power + ")");
        if (rst != 1) {
            throw new SQLException("SQL未知错误");
        }


    }


}
