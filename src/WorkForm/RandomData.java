package WorkForm;

import DataType.Train;
import Logics.DataBase;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public abstract class RandomData {
    public static void main(String[] args) {
        Random rand = new Random();
        for (int fr = 0; fr < 27; fr++) {
            Calendar pointer = Calendar.getInstance();
            for (int i = 0; i < 30; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) ('A' + rand.nextInt(26)));
                for (int j = 0; j < 4; j++) {
                    sb.append(rand.nextInt(10));
                }
                pointer.set(Calendar.HOUR_OF_DAY, rand.nextInt(22) + 1);
                pointer.set(Calendar.MINUTE, rand.nextInt(60));
                int f = rand.nextInt(27) + 1;
                int t = rand.nextInt(27) + 1;
                while (f == t) {
                    t = rand.nextInt(27) + 1;
                }
                Train train = new Train(
                        0,
                        sb.toString(),
                        f,
                        t,
                        pointer,
                        rand.nextInt(300),
                        20
                );
                try {
                    DataBase.insertTrain(train);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                pointer.add(Calendar.DATE, 1);
            }
        }
    }
}
