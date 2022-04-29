package stockanalyzer.ctrl;

import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;

import java.util.stream.Collectors;

public class getAverageException extends Throwable {
    public getAverageException(String s) {
        super(s);
    }
}
