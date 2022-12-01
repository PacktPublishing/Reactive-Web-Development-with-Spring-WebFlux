import java.util.concurrent.Flow;
import java.util.stream.IntStream;

public class Subscriber implements Flow.Subscriber<Integer> {

    public static final String CLIENT_1 = "Client 1";
    public static final String CLIENT_2 = "Client 2";
    public static final String CLIENT_3 = "Client 3";
    public static final String CLIENT_4 = "Client 4";
    public static final String CLIENT_5 = "Client 5";
    public static final String CLIENT_6 = "Client 6";
    public static final String CLIENT_7 = "Client 7";
    public static final String CLIENT_8 = "Client 8";
    public static final String CLIENT_9 = "Client 9";
    public static final String CLIENT_10 = "Client 10";


    private final Long timeOutOfHomeOrSleeping;
    private final String clientSubscriberName;
    private int nextOrderIDItemExpected;
    private int totalItems;
    private Flow.Subscription clientSubscription;


    public Subscriber(Long timeOutOfHomeOrSleeping, String clientSubscriberName) {
        this.timeOutOfHomeOrSleeping = timeOutOfHomeOrSleeping;
        this.clientSubscriberName = clientSubscriberName;
        this.nextOrderIDItemExpected = 1;
        this.totalItems = 0;
    }


    @Override
    public void onSubscribe(Flow.Subscription clientSubscription) {
        this.clientSubscription = clientSubscription;
        clientSubscription.request(1);
    }

    @Override
    public void onNext(final Integer itemNumber) {
        if (itemNumber != nextOrderIDItemExpected) {
            IntStream.range(nextOrderIDItemExpected, itemNumber).forEach(
                    (orderItemNumber) ->
                            System.out.println("customer ended up leaving the case and missed the delivery " + orderItemNumber)
            );
            nextOrderIDItemExpected = itemNumber;
        }
        System.out.println("I managed to remove the item, thank you " + itemNumber);
        goOutOrSleep();
        nextOrderIDItemExpected++;
        totalItems++;

        System.out.println("I'll get another item now, next one should be: " +
                nextOrderIDItemExpected);
        clientSubscription.request(1);
    }


    @Override
    public void onError(Throwable throwable) {
        System.out.println("Logistics company has a problem with delivery: " + throwable.getMessage());

    }


    @Override
    public void onComplete() {
        System.out.println("I received all items in my order, thank you! Were" +
                totalItems + " in total");
    }


    private void goOutOrSleep() {
        try {
            Thread.sleep(timeOutOfHomeOrSleeping);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String clientSubscriberName() {
        return clientSubscriberName;
    }
}


