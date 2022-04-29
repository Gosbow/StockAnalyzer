package stockanalyzer.ctrl;

import yahooApi.YahooFinance;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;

import java.util.*;
import java.util.stream.Collectors;



public class Controller {
		
	public void process(String ticker) throws YahooException, getAverageException {
		System.out.println("Start process");

		//TODO implement Error handling


		try {
			QuoteResponse response = (QuoteResponse) getData(ticker);
			long x = response.getResult().stream().
					map(Result::getAsk).count();
			System.out.println("\nRegular Market Day High from asked Stocks: " + String.format("%.2f", getMaxval(response)));
			System.out.println("\nAverage of asked Stocks : "+ String.format("%.2f", getAverageCourseoflastDays(response,x)));

		}
		catch(YahooException e){
			throw new YahooException("Error while fetching Data.");
		}


		//TODO implement methods for
		//1) Daten laden
		//2) Daten Analyse

	}

	public double getMaxval(QuoteResponse response) throws YahooException {


		List<Double> input = new ArrayList<>();

		try{
			response.getResult().stream().forEach(s -> input.add(s.getRegularMarketDayHigh()));
			return Collections.max(input);
		}catch (Exception e){
			throw new YahooException(e.getMessage());
		}

	}

	public double getAverageCourseoflastDays(QuoteResponse response, long cnt) throws getAverageException  {

		String symbol = response.getResult().stream().
				map(Result::getSymbol).collect(Collectors.joining());
		String getLongName = response.getResult().stream().
				map(Result::getLongName).collect(Collectors.joining());

		if(symbol.isEmpty() || symbol.isBlank() || getLongName.contains("null")){
			throw new getAverageException("\nStock Information is not available!");
		} else {
			List<Double> input = new ArrayList<>();
			double sum=0;
			response.getResult().stream().forEach(s -> input.add(s.getRegularMarketDayHigh()));
			for(int i=0; i<input.size();i++){
				sum += input.get(i);
			}
			response.getResult().stream().forEach(result -> System.out.println("\n"+result.getLongName() + " (" + result.getSymbol() + "): Regular Market High: "+ result.getRegularMarketDayHigh() + "\tAverage of the last 50 days: " + String.format("%.2f",result.getFiftyDayAverage()) + " " + result.getCurrency()));

			return sum/cnt;
		}



	}



		public Object getData (String searchString) throws YahooException {

			List<String> searchStrings = Arrays.asList(searchString);
			YahooFinance yahooFinance = new YahooFinance();
			try {
				YahooResponse response = yahooFinance.getCurrentData(searchStrings);
				QuoteResponse quotes = response.getQuoteResponse();
				return quotes;
			} catch (Exception e) {
				throw new YahooException("Error while fetching Data");
			}

		}


	public void closeConnection() {
		
	}
}
