package processors;
import tasks.ColonyTask;

public interface IProcessor {
	boolean canProcess(ColonyTask task);
	String processTask(ColonyTask task); // to return log message
	
}
