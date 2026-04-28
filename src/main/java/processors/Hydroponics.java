package processors;
 
import tasks.ColonyTask;
 
// Hydroponics Bay handles all RESEARCH_TASK crises.
// MedicalWard owns LIFE_SUPPORT; EngineeringBay owns ENGINEERING_TASK.
// Each processor has exactly one unique domain so canProcess() never produces ambiguous matches.
public class Hydroponics implements IProcessor {
    @Override
    public boolean canProcess(ColonyTask task) {
        return task.getTaskType().equals("RESEARCH_TASK");
    }
 
    @Override
    public String processTask(ColonyTask task) {
        return "🌱 Hydroponics completed: " + task.getName();
    }
}
