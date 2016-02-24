package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Subscriber for a {@link TextObservable}.
 * Created by ton on 23/02/16.
 */
@Component
public class Subscriber {

    private final TextObservable textObservable;

    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);

    @Autowired
    public Subscriber(TextObservable textObservable) {
        LOGGER.info("initializing Subscriber({})", textObservable);
        this.textObservable = textObservable;
        LOGGER.info("starting subscription...");
        textObservable.getTextStream()
                .subscribe(msgtext -> LOGGER.info("Observed: '{}'", msgtext));
    }
}
