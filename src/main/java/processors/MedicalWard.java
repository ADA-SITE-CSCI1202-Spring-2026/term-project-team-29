package processors;

import tasks.ColonyTask;

public class MedicalWard implements IProcessor {
    @Override
    public boolean canProcess(ColonyTask task) {
        return task.getTaskType().equals("LIFE_SUPPORT");
    }

    @Override
    public String processTask(ColonyTask task) {
        return "🏥 Medical Ward completed: " + task.getName();
    }
}
