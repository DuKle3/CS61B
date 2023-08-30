package tester;
import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.introcs.StdRandom;
public class TestArrayDequeEC {
    @Test
    public void addMethodAndRemoveMethod() {
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        String methodCalled = "\n";
        for (int i = 0; i < 100; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                student.addFirst(randVal);
                sol.addFirst(randVal);
                methodCalled += "addFirst(" + randVal + ")\n";
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                student.addLast(randVal);
                sol.addLast(randVal);
                methodCalled += "addLast(" + randVal + ")\n";
            } else if (operationNumber == 2 && !student.isEmpty() && !sol.isEmpty()) {
                Integer x1 = sol.removeFirst();
                Integer x2 = student.removeFirst();
                methodCalled += "removeFirst()\n";
                assertEquals(methodCalled + "\n", x1, x2);
            } else if (operationNumber == 3 && !student.isEmpty() && !sol.isEmpty()) {
                Integer x1 = sol.removeLast();
                Integer x2 = student.removeLast();
                methodCalled += "removeLast()\n";
                assertEquals(methodCalled + "\n", x1, x2);
            }
        }
    }
}

