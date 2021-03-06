package Logics;

import DataType.Admin;
import DataType.DataTools;
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

public class DataBase {
    public static final String SQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String SQL_HOST = "jdbc:mysql://192.168.108.128:3306/ticketSys?useSSL=false";
    public static final String SQL_USER = "root";
    public static final String SQL_PASSWORD = "A15604559612";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String formatCalendar(Calendar calendar) {
        return new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime());
    }
    public static String formatCalendar(Calendar calendar,String format) {
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static void main(String[] args) {
        final String INSERT_PRE = "insert into testTable(calendar) values ('";
        final String INSERT_END = "')";

        try {
            var stm = getConnection();
            var result = stm.executeQuery("select * from testTable");
            Vector<Vector<Object>> tableVector = new Vector<>();
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);
            while (result.next()) {
                Calendar cc = Calendar.getInstance();
                int id = result.getInt("id");
                String tim = result.getString("calendar");
                Date dt = result.getDate("calendar");
                Time resultTime = result.getTime("calendar");
                Vector<Object> line = new Vector<>();
                line.add(id);
                // line.add(tim);
                // line.add(dt);
                // line.add(resultTime);
                cc.setTime(fmt.parse(tim));
                line.add(fmt.format(cc.getTime()));
                tableVector.add(line);
                // JOptionPane.showMessageDialog(null, "id:" + id + ",tim:" + tim);
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
            while (JOptionPane.showConfirmDialog(null, "?????????", "????????????",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
            var result = stm.executeQuery("select * from users where name='" + name + "'");
            if (result.next() && result.getString("name").equals(name)) {
                var pass = result.getString("password");
                User user;
                if (result.getBoolean("admin")) {
                    user = new Admin(name, pass);
                } else {
                    user = new User(name, pass);
                }
                user.setMoney(result.getInt("money"));
                closeConnect(stm);
                return user;
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
                User tmp;
                if (result.getBoolean("admin")) {
                    tmp = new Admin(result.getString("name"), result.getString("password"));
                } else {
                    tmp = new User(result.getString("name"), result.getString("password"));
                }
                tmp.setMoney(result.getInt("money"));
                list.add(tmp);
            }
            closeConnect(stm);
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
                User tmp;
                if (result.getBoolean("admin")) {
                    tmp = new Admin(result.getString("name"), result.getString("password"));
                } else {
                    tmp = new User(result.getString("name"), result.getString("password"));
                }
                tmp.setMoney(result.getInt("money"));
                list.add(tmp);
            }
            closeConnect(stm);
            if (!list.isEmpty()) {
                return list.toArray(new User[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void updateUserPassword(String user, char[] newPass) throws MyCustomMessageException {
        if (DataType.DataTools.isNameLegal(user) && DataType.DataTools.isPasswordLegal(newPass)) {
            try {
                Statement stm = getConnection();
                int result = stm.executeUpdate(
                        "update  users set password=" + String.valueOf(newPass) + " where name='" + user + "'");

                closeConnect(stm);
                if (result == 0) {
                    throw new MyCustomMessageException("??????????????????");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateUserMoney(String user, int money) throws MyCustomMessageException {
        if (DataType.DataTools.isNameLegal(user) && money >= 0) {
            try {
                Statement stm = getConnection();
                int result = stm.executeUpdate(
                        "update  users set money=" + money + " where name='" + user + "'");

                closeConnect(stm);
                if (result == 0) {
                    throw new MyCustomMessageException("??????????????????");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateUserPower(String user, boolean power) throws MyCustomMessageException {
        if (DataType.DataTools.isNameLegal(user)) {
            try {
                Statement stm = getConnection();
                int result = stm.executeUpdate("update  users set admin=" + power + " where name='" + user + "'");

                closeConnect(stm);
                if (result == 0) {
                    throw new MyCustomMessageException("????????????");
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

            while (result.next()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fmt.parse(result.getString("launch")));
                list.add(new Train(result.getInt("No"), result.getString("name"), result.getInt("from"),
                        result.getInt("to"), calendar, result.getInt("minute")));
            }
            closeConnect(stm);
            if (!list.isEmpty()) {
                return list.toArray(new Train[0]);
            }
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Train[] getBoughtTicket(String user) {
        if (!DataTools.isNameLegal(user)) return null;
        try {
            Statement stm = getConnection();
            ResultSet result = stm.executeQuery("select train from ticketHistory where `user`='" + user + "'");
            Vector<Train> list = new Vector<>();
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);

            Calendar calendar = Calendar.getInstance();
            while (result.next()) {
                int id = result.getInt("train");
                Train[] tmp = getTrains("No=" + id);
                if (tmp != null)
                    list.addAll(Arrays.asList(tmp));
            }
            closeConnect(stm);
            if (!list.isEmpty()) {
                return list.toArray(new Train[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Train[] getTrains(String condition) {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from trains where " + condition);
            Vector<Train> list = new Vector<>();
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);

            while (result.next()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fmt.parse(result.getString("launch")));
                list.add(new Train(result.getInt("No"), result.getString("name"), result.getInt("from"),
                        result.getInt("to"), calendar, result.getInt("minute")));
            }
            closeConnect(stm);
            if (!list.isEmpty()) {
                return list.toArray(new Train[0]);
            }
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Train[] getTrains(String condition, Calendar day) {
        try {
            Statement stm = getConnection();
            var result = stm.executeQuery("select * from trains where " + condition+" and `launch` like '"+formatCalendar(day,"yyyy-MM-dd")+"%'");
            Vector<Train> list = new Vector<>();
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            fmt.setLenient(false);

            while (result.next()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fmt.parse(result.getString("launch")));
                if (calendar.get(Calendar.YEAR) == day.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)) {
                    Train tr = new Train(
                            result.getInt("No"),
                            result.getString("name"),
                            result.getInt("from"),
                            result.getInt("to"),
                            calendar,
                            result.getInt("minute")
                    );
                    list.add(tr);
                }
            }
            closeConnect(stm);
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
        if (list == null)
            return null;
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
        if (list == null)
            return null;
        String[] p = getPlaces();
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        fmt.setLenient(false);
        for (Train train : list) {
            Vector<Object> line = new Vector<>();
            line.add(String.valueOf(train.getUid()));
            line.add(train.getName());
            line.add(p[train.getFrom() - 1]);
            line.add(p[train.getTo() - 1]);
            line.add(fmt.format(train.getLaunchTime().getTime()));
            line.add(String.valueOf(train.getMinuteCost()));
            line.add(String.valueOf(train.getMoneyCost()));
            tab.add(line);
        }
        return tab.isEmpty() ? null : tab;
    }

    public static String[] getPlaces() {

        try {
            Statement stm = getConnection();
            ResultSet result = stm.executeQuery("select * from provinces");
            Vector<String> list = new Vector<>();
            while (result.next()) {
                list.add(result.getString("name"));
            }
            closeConnect(stm);
            if (!list.isEmpty()) {
                return list.toArray(new String[0]);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getPlace(String name) {

        int r = 0;
        try {
            Statement stm = getConnection();
            ResultSet result = stm.executeQuery("select * from provinces where `name`='" + name + "'");
            if (result.next()) {
                r = result.getInt("id");
            }
            closeConnect(stm);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String getPlace(int id) {

        String r = null;
        try {
            Statement stm = getConnection();
            ResultSet result = stm.executeQuery("select * from provinces where `id`=" + id);
            if (result.next()) {
                r = result.getString("name");
            }
            closeConnect(stm);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static void insertUser(User user) throws SQLException, ClassNotFoundException {
        if (!DataType.DataTools.isNameLegal(user.getName())
                || !DataType.DataTools.isPasswordLegal(user.getPassword())) {
            throw new IllegalArgumentException("??????????????????");
        }
        Statement stm = getConnection();
        boolean power = user instanceof Admin;
        int rst = stm.executeUpdate("insert into users (name, password, admin) value ('" + user.getName() + "','"
                + user.getPassword() + "'," + power + ")");
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }
    }

    public static void insertTrain(Train train) throws SQLException, ClassNotFoundException {

        Statement stm = getConnection();

        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        int rst = stm.executeUpdate("insert into trains (name, `from`, `to`, launch, minute, money) value ('"
                + train.getName() + "',"
                + train.getFrom() + ","
                + train.getTo() + ",'"
                + fmt.format(train.getLaunchTime().getTime()) + "',"
                + train.getMinuteCost() + ","
                + String.format("%.2f", train.getMoneyCost())
                + ")");
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }

    }

    public static void insertBuyTicket(String userName, int ticket) throws SQLException, ClassNotFoundException {

        Statement stm = getConnection();
        int rst = stm.executeUpdate("insert into ticketHistory (`user`, train) value ('"
                + userName + "'," + ticket + ")"
        );
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }
    }

    public static void removeBoughtTicket(String userName, int ticket) throws SQLException, ClassNotFoundException {

        Statement stm = getConnection();
        int rst = stm.executeUpdate("delete from ticketHistory where `user`='" + userName + "' and train=" + ticket);
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }
    }

    public static void removeTrain(int id) throws SQLException, ClassNotFoundException {

        Statement stm = getConnection();
        int rst = stm.executeUpdate("delete from trains where No=" + id);
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }
    }

    public static void removeUser(String user) throws SQLException, ClassNotFoundException {

        Statement stm = getConnection();
        int rst = stm.executeUpdate("delete from users where `name`='" + user + "'");
        closeConnect(stm);
        if (rst != 1) {
            throw new SQLException("SQL????????????");
        }
    }
}
