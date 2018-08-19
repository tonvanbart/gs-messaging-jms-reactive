## Reactive JMS with Spring

[![Build Status](https://travis-ci.com/tonvanbart/gs-messaging-jms-reactive.svg?branch=master)](https://travis-ci.com/tonvanbart/gs-messaging-jms-reactive)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=gs-messaging-jms-reactive&metric=alert_status)](https://sonarcloud.io/api/project_badges/measure?project=gs-messaging-jms-reactive&metric=alert_status)

This is a modified version of the gs-messaging-jms project (see http://spring.io/guides/gs/messaging-jms/).
Differences with the version from the Spring guide:

* uses explicit MessageListenerContainer configuration instead of `@MessageListener` annotated method.
* adds `application.properties` to talk to external JMS broker (in this case Dockered broker instance) - delete 
this file to go back to the internal provider.
* shows how to create an Rx Observable from a JMS queue.

### Creating an Observable queue
The `ObservableQueue` gets the `ListenerContainer` injected. Once a client requests the stream via `getTextStream()`,
the code creates a `MessageListener` and registers it with the container. Incoming text messages are passed on to 
subscribers `onNext()` method and any exceptions to `onError()`.
The `Subscriber` acts as the client and in this case simply logs any messages it observes.

To run the project, issue `./gradlew bootRun` in the project root. If everything goes well, in the logging you should
see something like the following (some messages snipped for brevity):

    2016-02-24 21:09:24.447  INFO 20739 --- [           main] hello.ObservableQueue                    : Initialized
    2016-02-24 21:09:24.449  INFO 20739 --- [           main] hello.Subscriber                         : initializing Subscriber(hello.ObservableQueue@2d0399f4)
    2016-02-24 21:09:24.450  INFO 20739 --- [           main] hello.Subscriber                         : starting subscription...
    2016-02-24 21:09:24.450  INFO 20739 --- [           main] hello.ObservableQueue                    : getTextStream()
    2016-02-24 21:09:24.494  INFO 20739 --- [           main] hello.ObservableQueue                    : observer:rx.observers.SafeSubscriber
    2016-02-24 21:09:24.776  INFO 20739 --- [           main] hello.Application                        : Started Application in 0.975 seconds (JVM running for 1.234)
    2016-02-24 21:09:24.776  INFO 20739 --- [           main] hello.Application                        : Sending a new message.
    2016-02-24 21:09:24.817  INFO 20739 --- [ jmsContainer-1] hello.Subscriber                         : Observed: 'ping!'
    2016-02-24 21:09:24.820  INFO 20739 --- [           main] hello.Application                        : Sending a second message.
    2016-02-24 21:09:24.832  INFO 20739 --- [ jmsContainer-1] hello.Subscriber                         : Observed: 'pong!'
    2016-02-24 21:09:25.839  INFO 20739 --- [           main] o.s.c.support.DefaultLifecycleProcessor  : Stopping beans in phase 2147483647
    2016-02-24 21:09:26.841  INFO 20739 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
    
    BUILD SUCCESSFUL
    
For a Dockered ActiveMQ instance, see https://github.com/aterreno/activemq-dockerfile, or delete application.properties to
use an embedded broker.