package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by ton on 23/02/16.
 */
public class Listener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                LOGGER.info("((TextMessage) message).getText() = " + ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Received message is not a TextMessage");
        }
    }
}
