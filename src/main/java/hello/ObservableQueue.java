package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Implementation of {@link TextObservable} which maps to a JMS queue.
 * Created by ton on 23/02/16.
 */
@Component
public class ObservableQueue implements TextObservable {

    private final DefaultMessageListenerContainer jmsContainer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservableQueue.class);

    @Autowired
    public ObservableQueue(DefaultMessageListenerContainer jmsContainer) {
        this.jmsContainer = jmsContainer;
        LOGGER.info("Initialized");
    }

    @Override
    public Observable<String> getTextStream() {
        LOGGER.info("getTextStream()");
        return Observable.create(observer -> {
            LOGGER.info("observer:" + observer.getClass().getName());
            MessageListener listener = message -> {
                LOGGER.info("ObservableQueue received " + message);
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        observer.onNext(textMessage.getText());
                    } catch (JMSException e) {
                        observer.onError(new RuntimeException(e));
                    }
                }
            };
            jmsContainer.setMessageListener(listener);
        });
    }
}
