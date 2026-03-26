import java.util.List;


public class Main {
    public static void main(String[] args) {
        String expression = "3+(2+5*4)";

        try {
            List<String> rpn = RPNCalculator.toRPN(expression);
            System.out.println("RPN: " + String.join(" ", rpn));

            double result = RPNCalculator.evaluateRPN(rpn);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}