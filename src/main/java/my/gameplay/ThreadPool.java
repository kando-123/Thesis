package my.gameplay;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kay Jay O'Nail
 */
public class ThreadPool
{
    private final Set<Thread> threads;
    
    private ThreadPool()
    {
        threads = new HashSet<>();
    }
    
    private static ThreadPool instance = null;
    
    public static ThreadPool getInstance()
    {
        if (instance == null)
        {
            instance = new ThreadPool();
        }
        return instance;
    }
    
    public void fork(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        threads.add(thread);
        thread.start();
    }
    
    public void joinAll()
    {
        for (var thread : threads)
        {
            thread.interrupt();
            try
            {
                thread.join();
            }
            catch (InterruptedException i)
            {
                System.err.println(i);
            }
        }
    }
}
