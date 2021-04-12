package DataType;

import exception.MyCustomMessageException;

public class User {
    private String name;
    private String password;
    private int money;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPassword(char[] password) {
        this.password = String.valueOf(password) ;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean couldAfford(int cost) {
        return money > cost;
    }

    public void consume(int cost) throws MyCustomMessageException {
        if (couldAfford(cost))
            money -= cost;
        else throw new MyCustomMessageException("余额不足");
    }

    public void recharge(int amount) {
        money += amount;
    }

}
