
package hello;

import java.io.File;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
@EnableJms
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Bean
    DefaultMessageListenerContainer jmsContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("mailbox-destination");
        // normally we would set a listener here, for ex.:
//        container.setMessageListener(new Listener());
        return container;
    }

    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        // Send messages
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("ping!");
            }
        };
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        LOGGER.info("Sending a new message.");
        jmsTemplate.send("mailbox-destination", messageCreator);

        messageCreator = (session) -> { return session.createTextMessage("pong!"); };
        LOGGER.info("Sending a second message.");
        jmsTemplate.send("mailbox-destination", messageCreator);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted", e);
        }

        context.close();
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

}
