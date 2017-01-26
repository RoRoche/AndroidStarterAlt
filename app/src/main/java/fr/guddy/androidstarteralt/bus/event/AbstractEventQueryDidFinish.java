package fr.guddy.androidstarteralt.bus.event;

import fr.guddy.androidstarteralt.rest.queries.AbstractQuery;

public abstract class AbstractEventQueryDidFinish<QueryType extends AbstractQuery> extends AbstractEvent {
    //region Fields
    public final QueryType query;
    public final boolean success;
    public final ErrorType errorType;
    public final Throwable throwable;
    //endregion

    //region Constructor
    public AbstractEventQueryDidFinish(final QueryType poQuery, final boolean pbSuccess, final ErrorType poErrorType, final Throwable poThrowable) {
        query = poQuery;
        success = pbSuccess;
        errorType = poErrorType;
        throwable = poThrowable;
    }
    //endregion

    public enum ErrorType {
        UNKNOWN,
        NETWORK_UNREACHABLE
    }
}
