package Logics;

import DataType.Admin;
import DataType.Train;
import DataType.User;
import WorkForm.DefaultUserGUI;
import WorkForm.LoginDialog;
import WorkForm.StuffUserGUI;
import WorkForm.WindowTools;

import javax.swing.*;

public class TicketSystem {
    private User user;

    public void login() {

        LoginDialog login = new LoginDialog();
        login.pack();
        WindowTools.moveToCenter(login);
        login.setVisible(true);
        user = login.getUser();
    }

    public void showFrame() {
        JFrame frame = null;
        if (user instanceof Admin) {
            frame = new StuffUserGUI();
            StuffUserGUI f = (StuffUserGUI) frame;
            User[] us = DataBase.getUsers();
            Train[] tr = DataBase.getTrains();
            if (us != null) {
                f.drawUserList(DataBase.usersToTable(us));
            }
            if (tr != null) {
                f.drawUserList(DataBase.trainsToTable(tr));
            }

        } else {
            frame = new DefaultUserGUI();
        }

        frame.pack();
        WindowTools.moveToCenter(frame);
        frame.setVisible(true);
    }
}
