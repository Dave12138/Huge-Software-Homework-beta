package DataType;

public abstract class DataTools {
    public static boolean isNameLegal(String name) {
        if (name == null) return false;
        char[] arr = name.toCharArray();
        if (arr.length < 3 || arr.length > 20) return false;
        for (char c : arr) {
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    public static boolean isPasswordLegal(String pass) {
        if (pass == null) return false;
        return isPasswordLegal(pass.toCharArray());
    }

    public static boolean isPasswordLegal(char[] pass) {
        if (pass == null) return false;
        if (pass.length < 6 || pass.length > 20) return false;
        for (char c : pass) {
            if (Character.isDigit(c) || Character.isLetter(c)) continue;
            if (Character.isSpaceChar(c) || Character.isWhitespace(c)) return false;
            switch (c) {
                case '@':
                case '.':
                case '*':
                case '_':
                case '$':
                    continue;
                default:
                    return false;
            }
        }
        return true;
    }
}
