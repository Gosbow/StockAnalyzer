package stockanalyzer.ctrl;

import stockanalyzer.downloader.ParallelDownloader;
import stockanalyzer.downloader.SequentialDownloader;
import yahooApi.YahooFinance;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;

import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;



public class Controller {
		
	public void process(String ticker) throws YahooException, getAverageException {
		System.out.println("Start process");

		//TODO implement Error handling

		try {
			QuoteResponse response = (QuoteResponse) getData(ticker);
			long count = response.getResult().stream().
					map(Result::getAsk).count();
			System.out.println("\n Number of found Data: " + getNumberofStocks(count));
			System.out.println("\nRegular Market Day High from asked Stocks: " + String.format("%.2f", getMaxval(response)));
			System.out.println("\nAverage of asked Stocks : "+ String.format("%.2f", getAverageCourseoflastDays(response,getNumberofStocks(count))));

		}
		catch(YahooException e){
			System.err.println(e.getMessage());
		}catch (Exception e){
			System.err.println(e.getMessage());
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

	public long getNumberofStocks(long nr) throws YahooException {
		if(nr == 0){
			throw new YahooException("\nNo Data found.");
		}else{
			return nr;
		}
	}
	public double getAverageCourseoflastDays(QuoteResponse response, long cnt) throws getAverageException  {

		String symbol = response.getResult().stream().
				map(Result::getSymbol).collect(Collectors.joining());
		String getLongName = response.getResult().stream().
				map(Result::getLongName).collect(Collectors.joining());
		double sum = 0;
		if(symbol.isEmpty() || symbol.isBlank() || getLongName.contains("null")){
			throw new getAverageException("\nStock Information is not available!");
		} else {
			try {
				List<Double> input = new ArrayList<>();

				response.getResult().stream().forEach(s -> input.add(s.getRegularMarketDayHigh()));
				for (int i = 0; i < input.size(); i++) {
					sum += input.get(i);
				}
				response.getResult().stream().forEach(result -> System.out.println("\n" + result.getLongName() + " (" + result.getSymbol() + "): Regular Market High: " + result.getRegularMarketDayHigh() + "\tAverage of the last 50 days: " + String.format("%.2f", result.getFiftyDayAverage()) + " " + result.getCurrency()));

				if(cnt == 0){
					throw new ArithmeticException("Problem with the Calculation");
				}else {
					return sum / cnt;
				}
			}catch (Exception e){
				System.err.println(e.getMessage());
			}
			return 0;
		}

	//return 0;

	}



		public Object getData (String searchString) throws YahooException, UnknownHostException {

			List<String> searchStrings = Arrays.asList(searchString);
			YahooFinance yahooFinance = new YahooFinance();
			try {
				YahooResponse response = yahooFinance.getCurrentData(searchStrings);
				QuoteResponse quotes = response.getQuoteResponse();
				return quotes;
			}catch (UnknownHostException e){
				throw new UnknownHostException(e.getMessage());
			}catch (Exception e) {
				throw new YahooException("\nError while fetching Data");
			}

		}

		public void downloadTickers(){
		List<String> seqList = Arrays.asList("AAPL", "MSFT", "ABC");
			List<String> paraList = Arrays.asList("MAN", "TWTR", "FB", "GOOG");

			SequentialDownloader test = new SequentialDownloader();
			test.process(seqList);
			ParallelDownloader parallel = new ParallelDownloader();
			parallel.process(paraList);
		}

	public void closeConnection() {
		
	}
}
