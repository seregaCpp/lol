import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter expression:");
        String expression = scanner.nextLine();

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