package stockanalyzer.downloader;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;

public class ParallelDownloader extends Downloader {
    List<Future> futureList = new ArrayList<>();
    Future task;
    long starttime = System.nanoTime();

    @Override
    public int process(List<String> tickers){
        System.out.println("Starting Parallel Downloading...");
        ExecutorService executor = Executors.newCachedThreadPool();
        for(String ticker : tickers){
            try{
                 task = executor.submit(()->saveJson2File(ticker));
                 futureList.add(task);
            } catch (Exception e){
                e.getMessage();
            }
        }
        try{
            //task.get();
            for(int i = 0; i < futureList.size();i++) {
                futureList.get(i);
            }
        }catch (Exception e){
       e.getMessage();
        }
      //  Executors Executor = new ExecutorService(saveJson2File("ABC"));
        long endtime = System.nanoTime() - starttime;
        System.out.println("Time for Paralleling Thing: "+ endtime/1000 + " ms.");

        return ((int)endtime/1000);
    }
}
