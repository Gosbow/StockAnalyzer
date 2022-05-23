package stockanalyzer.downloader;

import org.w3c.dom.ls.LSOutput;
import stockanalyzer.ctrl.YahooException;

import java.io.FileNotFoundException;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDownloader extends Downloader {


    @Override
    public int process(List<String> tickers){
        System.out.println("Starting Parallel...");
        ExecutorService executor = Executors.newCachedThreadPool();
        for(String ticker : tickers){
            try{
                Future<?> task = executor.submit(()->saveJson2File(ticker));
            } catch (Exception e){
                //throw YahooException("Fehler");
            }
        }
      //  Executors Executor = new ExecutorService(saveJson2File("ABC"));
    return 0;
    }
}
