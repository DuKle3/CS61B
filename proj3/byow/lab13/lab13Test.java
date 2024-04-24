package byow.lab13;

public class lab13Test {
    public static void main(String[] args) {
        MemoryGame g = new MemoryGame(40, 40, 123);
        System.out.println(g.generateRandomString(10));

        // g.flashSequence("12345");
        g.solicitNCharsInput(3);
    }
}
