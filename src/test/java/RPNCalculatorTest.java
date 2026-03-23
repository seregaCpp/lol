import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RPNCalculatorTest {

    @Test
    void testToRPNSimple() {
        List<String> rpn = RPNCalculator.toRPN("3+4*2");
        assertEquals(List.of("3", "4", "2", "*", "+"), rpn);
    }

    @Test
    void testToRPNWithBrackets() {
        List<String> rpn = RPNCalculator.toRPN("(1+2)*3");
        assertEquals(List.of("1", "2", "+", "3", "*"), rpn);
    }

    @Test
    void testEvaluateRPN() {
        double result = RPNCalculator.evaluateRPN(List.of("3", "4", "2", "*", "+"));
        assertEquals(11.0, result);
    }

    @Test
    void testFullExpression() {
        List<String> rpn = RPNCalculator.toRPN("3+4*2/(1-5)^2");
        double result = RPNCalculator.evaluateRPN(rpn);
        assertEquals(3.5, result);
    }

    @Test
    void testDegrees() {
        List<String> rpn = RPNCalculator.toRPN("2^4^3");
        double result = RPNCalculator.evaluateRPN(rpn);
        assertEquals(Math.pow(2, 64), result);
    }

    @Test
    void testDivisionByZero() {
        List<String> rpn = List.of("5", "0", "/");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            RPNCalculator.evaluateRPN(rpn);
        });
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    void testInvalidBrackets() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            RPNCalculator.toRPN("(1+2");
        });
        assertEquals("Mismatched brackets", ex.getMessage());
    }

    @Test
    void testInvalidNumbers() {
        List<String> rpn = List.of("1", "-");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            RPNCalculator.evaluateRPN(rpn);
        });
        assertEquals("There are not enough operands for the operation", ex.getMessage());
    }
}