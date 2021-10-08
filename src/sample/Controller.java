package sample;

public class Controller {

    private final Model model ;

    public Controller(Model model) {
        this.model = model ;
    }

    public void updateX(String x) {
        model.setX(convertStringToInt(x));
    }

    public void updateY(String y) {
        model.setY(convertStringToInt(y));
    }

    private int convertStringToInt(String s) {
        if (s == null || s.isEmpty()) {
            return 0 ;
        }
        if ("-".equals(s)) {
            return 0 ;
        }
        return Integer.parseInt(s);
    }
}