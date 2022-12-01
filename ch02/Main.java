public class Main {


    public static void main(String[] args) throws Exception {


        final Publisher flowExample = new Publisher();

        flowExample.flowDelivery(10000L, 20000L, 30000L, 40000L, 50000L, 600L, 700L, 800L, 900L, 1000L, 100);

//nano seconds that this execution takes
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("The execution takes " + totalTime+ "  nanoseconds");
    }
}
