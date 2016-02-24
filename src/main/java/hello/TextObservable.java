package hello;

import rx.Observable;

/**
 * An interface that will return an {@link rx.Observable} stream of strings.
 * Created by ton on 23/02/16.
 */
public interface TextObservable {

    public Observable<String> getTextStream();

}
