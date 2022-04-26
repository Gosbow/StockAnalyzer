package stockanalyzer.ctrl;

import yahooApi.YahooFinance;
import yahooApi.beans.Quote;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.YahooResponse;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


public class Controller {
		
	public void process(String ticker) {
		System.out.println("Start process");


		//TODO implement Error handling 

		try{
			QuoteResponse response = (QuoteResponse)getData(ticker);

			response.getResult().stream().forEach(quote -> System.out.println(quote.getLongName() + " (" + quote.getSymbol() + ")" + ":"));
			response.getResult().stream().forEach(quote -> System.out.println(" Current BID: " + quote.getAsk() + " " + quote.getCurrency() + " Average of the last 10 days: " + quote.getAverageDailyVolume10Day()));
		}
		catch (NullPointerException e){
			System.err.println("Something went wrong, while fetching Data. Error:  " + e.toString());
		}
		//TODO implement methods for
		//1) Daten laden
		//2) Daten Analyse

	}
	

	public Object getData(String searchString) {

		List<String> searchStrings = Arrays.asList(searchString);
		YahooFinance yahooFinance = new YahooFinance();
		try {
			YahooResponse response = yahooFinance.getCurrentData(searchStrings);
			QuoteResponse quotes = response.getQuoteResponse();
			return quotes;
		} catch (NullPointerException e){
			System.out.println("Couldn't get Data from " + YahooFinance.URL_YAHOO);
			e.printStackTrace();
		} catch (Exception e){
			System.err.println("Something went wrong, while fetching Data. Error:  " + e.toString());
		}

		return null;
	}


	public void closeConnection() {
		
	}
}
