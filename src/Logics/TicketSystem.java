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
        JFrame frame;
        if (user instanceof Admin) {
            StuffUserGUI f = new StuffUserGUI();
            frame = f;
            User[] us = DataBase.getUsers();
            Train[] tr = DataBase.getTrains();
            if (us != null) {
                f.drawUserList(DataBase.usersToTable(us));
            }
            if (tr != null) {
                f.drawTrainList(DataBase.trainsToTable(tr));
            }
        } else {
            DefaultUserGUI df = new DefaultUserGUI();
            df.setLoginUser(user);
            frame = df;
        }
        frame.pack();
        WindowTools.moveToCenter(frame);
        frame.setVisible(true);
    }

}
