package stockanalyzer.ctrl;

import stockanalyzer.downloader.ParallelDownloader;
import stockanalyzer.downloader.SequentialDownloader;
import yahooApi.YahooFinance;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;
import yahoofinance.Stock;

import java.math.BigDecimal;
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
			if(!ticker.contains(",")) {
				System.out.println("\nGet the Historical Average: " + String.format("%.2f",getlogHistoricalQuotes(ticker)));
			}
			System.out.println("\n Number of Data: " + getNumberofStocks(count));

				System.out.println("\nRegular Market Day High from asked Stocks: " + String.format("%.2f", getMaxval(response)));
				System.out.println("\nAverage of asked Stocks : " + String.format("%.2f", getAverageCourseoflastDays(response, getNumberofStocks(count))));

		}
		catch(YahooException | Exception | getAverageException e){
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
		}catch (NullPointerException e){
			throw new YahooException("Stock not found.");
		}
		catch (Exception e){
			throw new YahooException(e.getMessage());
		}

	}

	public float getlogHistoricalQuotes(String ticker) throws YahooException, getAverageException {
		List<String> tickerList = Arrays.asList(ticker);
		Stock stock = null;
		float result = 0;

		/*String ticker = response.getResult().stream().
				map(Result::getSymbol).collect(Collectors.joining());*/
		try {
			long count;

			List<BigDecimal> adjValue = new ArrayList<>();
			List<Calendar> calendar = new ArrayList<>();
			for(int i = 0; i < tickerList.size(); i++) {
				stock = yahoofinance.YahooFinance.get(tickerList.get(i));
			}
		//	stock.getHistory().forEach(System.out::println);
			stock.getHistory().stream().forEach(s -> calendar.add(s.getDate()));
			count = stock.getHistory().stream().count();
			stock.getHistory().stream().forEach(s -> adjValue.add(s.getAdjClose()));
		//	System.out.println("Shows the Average of the Dates: ");
			for(int i = 0; i < count;i++){

				result += adjValue.listIterator(i).next().floatValue();
		//		System.out.print(" " + calendar.get(i).getTime());
			}
			result /= count;
		} catch (ArithmeticException e){
			throw new getAverageException(e.getMessage());
		}catch (NullPointerException e){
			throw new YahooException("Error while fetching data.");
		}
		catch (Exception e) {
			throw new YahooException(e.getMessage());
		}
		return result;
	}
	public String getSymbol(QuoteResponse response) throws YahooException {
		String symbol = null;
		try {
			symbol = response.getResult().stream().
					map(Result::getSymbol).collect(Collectors.joining());
			return symbol;
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
	public double getAverageCourseoflastDays(QuoteResponse response, long cnt) throws getAverageException {

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

		public void downloadTickers() throws YahooException, Exception {

			try{
				List<String> StockList = Arrays.asList("AAPL", "MSFT", "ABC", "MAN", "TWTR", "FB", "GOOG");
				/*List<String> seqList = Arrays.asList("AAPL", "MSFT", "ABC");*/
				//	List<String> paraList = Arrays.asList("MAN", "SVNDY", "WMT", "OMV");

				long sequentialstartTime = System.nanoTime();
			SequentialDownloader seqDownload = new SequentialDownloader();
			seqDownload.process(StockList);
			long seqendtime = (System.nanoTime() -sequentialstartTime)/1000;
			System.out.println("Time of Sequential Download: " + seqendtime + " ms.");
			ParallelDownloader parallel = new ParallelDownloader();
			int paratime = parallel.process(StockList);
			System.out.println("Time Difference between Sequential and Parallel Download: " + (seqendtime-paratime) + " ms.");
			if(paratime < (int)seqendtime){
				System.out.println("Parallel is faster.");
			}else
			{
				System.out.println("Sequential is faster");
			}
		}catch (Exception e){
			throw new YahooException("Error while doing the downloadTicker");
			}
		}

	public void closeConnection() {
		
	}
}
