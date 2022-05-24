package stockanalyzer.downloader;


import java.io.FileNotFoundException;
import java.util.List;

import java.util.concurrent.*;

public class ParallelDownloader extends Downloader {
    Future task;
    long starttime = System.nanoTime();

    @Override
    public int process(List<String> tickers){
        System.out.println("Starting Parallel...");
        ExecutorService executor = Executors.newCachedThreadPool();
        for(String ticker : tickers){
            try{
                 task = executor.submit(()->saveJson2File(ticker));
            } catch (Exception e){
                //throw YahooException("Fehler");
            }
        }
        try{
            task.get();
        }catch (InterruptedException | ExecutionException e){

        } catch (Exception e){
            System.out.println("Fehler beim Downloaden, zu langee");
        }
      //  Executors Executor = new ExecutorService(saveJson2File("ABC"));
        long endtime = System.nanoTime() - starttime;
        System.out.println("Time for Paralleling Thing: "+ endtime/1000 + " ms.");
        return 0;
    }
}
