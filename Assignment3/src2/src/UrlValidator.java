import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {
    public static boolean isValid(String urlToValidate) {
        String regexPattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(regexPattern);
        if (urlToValidate == null) {
            return false;
        }
        Matcher m = p.matcher(urlToValidate);
        return m.matches();
    }
}

