package processors;

import tasks.ColonyTask;

public class EngineeringBay implements IProcessor {
    @Override
    public boolean canProcess(ColonyTask task) {
        return task.getTaskType().equals("ENGINEERING_TASK");
    }

    @Override
    public String processTask(ColonyTask task) {
        return "🔧 Engineering Bay completed: " + task.getName();
    }
}
