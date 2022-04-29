package stockanalyzer.ctrl;

import yahooApi.YahooFinance;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Controller {
		
	public void process(String ticker) throws YahooException, getAverageException {
		System.out.println("Start process");

		//TODO implement Error handling


		try {
			QuoteResponse response = (QuoteResponse) getData(ticker);
		/*String map = response.getResult().stream().
				map(Result::getSymbol).collect(Collectors.joining());*/
			getAverageCourseoflastDays(response);
			List<String> test = Arrays.asList(ticker);
			long x = response.getResult().stream().
					map(Result::getAsk).count();
		}
		catch(YahooException e){
			throw new YahooException("Error while fetching Data.");
		}


		//Integer y = response.getResult().stream().
		//		map(Result::getAsk).mapToInt();

		List<Integer> val = new ArrayList<>();



			//val.stream().filter(p -> response.getResult
			//getMaxval()


		//TODO implement methods for
		//1) Daten laden
		//2) Daten Analyse

	}

	public double getMaxval(List<Double> input){


		return Collections.max(input);
	}

	public void getAverageCourseoflastDays(QuoteResponse response) throws getAverageException  {

		String symbol = response.getResult().stream().
				map(Result::getSymbol).collect(Collectors.joining());
		String getLongName = response.getResult().stream().
				map(Result::getLongName).collect(Collectors.joining());

		if(symbol.isEmpty() || symbol.isBlank() || getLongName.contains("null")){
			throw new getAverageException("Stock Information is not available!");
		} else {

			response.getResult().stream().forEach(result -> System.out.println("\n"+result.getLongName() + " (" + result.getSymbol() + "): " + "\tAverage of the last 50 days: " + result.getFiftyDayAverage() + " " + result.getCurrency()));
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

		//	return null;
		}


	public void closeConnection() {
		
	}
}
