package processors;

import tasks.ColonyTask;

public class Hydroponics implements IProcessor {
    @Override
    public boolean canProcess(ColonyTask task) {
        return task.getTaskType().equals("LIFE_SUPPORT") ||
                task.getTaskType().equals("RESEARCH_TASK");
    }

    @Override
    public String processTask(ColonyTask task) {
        return "🌱 Hydroponics completed: " + task.getName();
    }
}
