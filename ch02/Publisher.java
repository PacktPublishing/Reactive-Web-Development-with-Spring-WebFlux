import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Publisher {

    private static final long MAX_SECONDS_TO_PICK_UP = 3;

    void flowDelivery(final long sleepOrGoOutTimeclient1,
                      final long sleepOrGoOutTimeclient2,
                      final long sleepOrGoOutTimeclient3,
                      final long sleepOrGoOutTimeclient4,
                      final long sleepOrGoOutTimeclient5,
                      final long sleepOrGoOutTimeclient6,
                      final long sleepOrGoOutTimeclient7,
                      final long sleepOrGoOutTimeclient8,
                      final long sleepOrGoOutTimeclient9,
                      final long sleepOrGoOutTimeclient10,
                      final int maxItems) throws Exception {
        final SubmissionPublisher<Integer> orderPublisher =
                new SubmissionPublisher<>(ForkJoinPool.commonPool(), maxItems);


        final Subscriber client1 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_1
        );

        final Subscriber client2 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_2
        );

        final Subscriber client3 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_3
        );

        final Subscriber client4 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_4
        );


        final Subscriber client5 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_5
        );


        final Subscriber client6 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_6
        );


        final Subscriber client7 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_7
        );


        final Subscriber client8 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_8
        );

        final Subscriber client9 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_9
        );

        final Subscriber client10 = new Subscriber(
                sleepOrGoOutTimeclient1,
                Subscriber.CLIENT_10
        );

        orderPublisher.subscribe(client1);
        orderPublisher.subscribe(client2);
        orderPublisher.subscribe(client3);
        orderPublisher.subscribe(client4);
        orderPublisher.subscribe(client5);
        orderPublisher.subscribe(client6);
        orderPublisher.subscribe(client7);
        orderPublisher.subscribe(client8);
        orderPublisher.subscribe(client9);
        orderPublisher.subscribe(client10);


        System.out.println("Sending the items already produced by the store: " + maxItems + "you have  " + MAX_SECONDS_TO_PICK_UP + "seconds to receive them");
        IntStream.rangeClosed(1, 100).forEach((number) -> {
            System.out.println("delivering the item " + number + " to customer");
            final int deliverLag = orderPublisher.offer(
                    number,
                    MAX_SECONDS_TO_PICK_UP,
                    TimeUnit.SECONDS,
                    (customer, response) -> {
                        customer.onError(
                                new RuntimeException("The " + ((Subscriber) customer)
                                        .clientSubscriberName() + "unable to receive in the desired time" + response));
                        return false;
                    });
            if (deliverLag < 0) {
                //failed items
                System.out.println("There was an error with this delivery, we will try again soon " + -deliverLag);
            } else {
                System.out.println("still left  " + deliverLag +
                        " to be removed");
            }
        });

        //blocks the thread until all Subscribers have completed
        while (orderPublisher.estimateMaximumLag() > 0) {
            Thread.sleep(500L);


        }

        // call onComplete() from every customer, and wait some time to the slower ends
        orderPublisher.close();
        Thread.sleep(2000L);

    }


}