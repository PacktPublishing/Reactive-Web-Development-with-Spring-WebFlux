package com.packt.ch04;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FluxSubscriberTest {
    @Test
    public void whenReceiveAFluxTheContentShouldBeTheSameAsItWasIssued() {
        //operator just: create an Observable that emits a particular item
        Flux<String> fluxString = Flux.just("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .log();

        //We create a verification step and indicate what is expected
        StepVerifier.create(fluxString)
                .expectNext("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .verifyComplete();
    }

    @Test
    public void whenReceiveAListOfStringShouldBeTheSameAsItWasIssued() {
        //from iterable: creates a Flux that emits the items contained in the provided Iterable
        Flux<String> flux = Flux.fromIterable(List.of("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono"))
                .log();

        flux.subscribe();

        StepVerifier.create(flux)
                .expectNext("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .verifyComplete();
    }

    @Test
    public void whenReceiveAFluxTheContentShouldBeTheSameAsItWasIssuedAndLimitRateAs50() {
        Flux<Integer> flux = Flux.range(1, 50)
                .log()
                // limit the rate of the incoming or outgoing messages through our reactive channel
                // we can control the number of messages by sending specific demand request to the upstream. In this case, we request only 5.
                .limitRate(5);

        flux.subscribe();

        StepVerifier.create(flux)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                        21, 22, 23, 24, 25, 26, 27, 28, 29, 30
                        , 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                        41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
                .verifyComplete();
    }

    @Test
    public void whenReceiveAFluxTheContentShouldBeTheSameAsItWasIssuedAndControlBackPressure() {

        //range operator : generate itens from 1 to 50
        Flux<Integer> flux = Flux.range(1, 50)
                .log();

        //create a new Base Subscriber
        flux.subscribe(new BaseSubscriber<>() {
            private int count = 0;
            //request only 10 itens each time
            private final int requestLimit = 10;


            //implement a specific action to do when subscribe
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(requestLimit);
            }


            //implement a specific action to do when onNext()
            @Override
            protected void hookOnNext(Integer value) {
                count++;
                if (count >= requestLimit) {
                    count = 0;
                    request(requestLimit);
                }
            }
        });


        // verify if even controlling the backpressure, we receive the same itens
        StepVerifier.create(flux)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                        21, 22, 23, 24, 25, 26, 27, 28, 29, 30
                        , 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                        41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
                .verifyComplete();
    }


    @Test
    public void whenCreateAnObservableShouldReceive2EachTime() throws Exception {
        // interval : create an Observable that emits a sequence of integers spaced by a given time interval
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200))
                //take 10 each time
                .take(2)
                .log();

        interval.subscribe();

        // inactivates the used thread for the indicated time
        Thread.sleep(5000);
    }

    @Test
    public void shouldInterruptWhenOnError() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5,6,7,8,9)
                // emit the emissions from two or more Observables without interleaving them
                .concatWith(Flux.error(new RuntimeException("Error")))
                // will not be issued
                .concatWith(Flux.just(10));
        flux.subscribe(
                (number) -> System.out.println("Number " + number),
                //onError
                Throwable::printStackTrace,                    
                () -> System.out.println("subscriber Completed") 
        );
        StepVerifier
                .create(flux)
                .expectNext(1, 2, 3, 4, 5,6,7,8,9)
                // checks if it throws an exception
                .expectError(RuntimeException.class);
    }


    @Test
    public void shouldValidateOnErrorResume() {
        Flux<Integer> flux = Flux.just(1, 2,3,4,5,6,7,8,9,10)
                .concatWith(Flux.error(new RuntimeException("My Exception")))
                .concatWith(Flux.just(11))
                .onErrorResume(e->{
                    System.out.println("Exception occured " + e.getMessage());
                    return Flux.just(12,13);
                });

        StepVerifier
                .create(flux)
                .expectNext(1, 2,3,4,5,6,7,8,9,10)
                .expectNext(12,13)
                .verifyComplete();

    }

    @Test
    public void shouldValidateOnErrorContinue() {

        Flux<Integer> fluxFromJust = Flux.just(1, 2,3,4,5,6,7,8,9,10,11,12,13,14,15)
                //execute a map function
                .map(this::map)
                //even after an error, continue the sequence
                .onErrorContinue((e,i)-> System.out.println("Error in item+" + i ));
        StepVerifier
                .create(fluxFromJust)
                .expectNext(1, 2,4,5,6,7,8,9,10,11,12,13,14,15)
                .verifyComplete();

    }

    private int map(Integer i) {
        if( i==3)
            throw new RuntimeException("Exception");
        return i;
    }


    @Test
    public void shouldValidateDoOnError() throws InterruptedException {

        Flux<String> flux = Flux.just("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .concatWith(Flux.error(new RuntimeException("Exception")))
                .doOnError(e -> System.out.println("What do on Error ?"));

        StepVerifier
                .create(flux)
                .expectNext("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .expectError()
                .verify();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void shouldValidateDoFinally() throws InterruptedException {
        Flux<String> flux = Flux.just("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .concatWith(Flux.error(new RuntimeException("Exception")))
                .doFinally( i->{
                    if (SignalType.ON_ERROR.equals(i)) {
                        System.out.println(" Error ");
                    }
                    if (SignalType.ON_COMPLETE.equals(i)) {
                        System.out.println("Success");
                    }
                });

        StepVerifier
                .create(flux)
                .expectNext("Packt", "Reactor", "Webflux", "Spring", "Netty", "Reactive", "Backpressure", "Flux", "Mono")
                .expectError()
                .verify();
        TimeUnit.SECONDS.sleep(10);
    }


}
