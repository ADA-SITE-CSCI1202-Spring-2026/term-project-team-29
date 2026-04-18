package ui;

import tasks.ColonyTask;

public interface BaseModule {
    boolean canProcess(ColonyTask task);
    boolean processTask(ColonyTask task, ResourceManager resourceManager);
    String getModuleName();
}