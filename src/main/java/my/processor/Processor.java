package my.processor;

import my.command.Invoker;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Processor
{
    private Process process;
    
    public Invoker<Processor> createInvoker()
    {
        return new Invoker<>(this);
    }
}
