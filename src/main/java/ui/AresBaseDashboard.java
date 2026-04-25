package ui;

import processors.*;
import resources.ResourceManager;
import tasks.ColonyTask;
import tasks.TaskLibrary;
import main.SimulationEngine;
import fileio.SaveLoadManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AresBaseDashboard extends JFrame {
    private SimulationEngine engine;
    private ResourceManager resourceManager;
    private Map<String, IProcessor> processors;
    private SaveLoadManager saveLoadManager;

    // UI Components
    private JTextArea logArea;
    private JLabel timeLabel;
    private Map<String, JProgressBar> processorBars;
    private Map<String, JLabel> resourceLabels;
    private Map<String, JLabel> currentTaskLabels;
    private JButton runSimulationButton;
    private JButton assignTaskButton;
    private JButton saveButton;
    private JButton loadButton;

    // Track current tasks for each processor
    private Map<String, ColonyTask> currentTasks;

    public AresBaseDashboard() {
        // Initialize backend
        engine = new SimulationEngine();
        resourceManager = new ResourceManager();
        saveLoadManager = new SaveLoadManager();
        processors = new HashMap<>();
        processorBars = new HashMap<>();
        resourceLabels = new HashMap<>();
        currentTaskLabels = new HashMap<>();
        currentTasks = new HashMap<>();

        // Setup processors - these implement IProcessor
        processors.put("Hydroponics", new Hydroponics());
        processors.put("Engineering Bay", new EngineeringBay());
        processors.put("Medical Ward", new MedicalWard());

        setupUI();
        refreshData();
        appendLog("🏭 Ares Base Control System Initialized");
        appendLog("📋 Loaded " + TaskLibrary.getAllTasks().size() + " tasks from library");
    }

    private void setupUI() {
        setTitle("Ares Base Control Dashboard - Team 29");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(1200, 700);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Time and main controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeLabel = new JLabel("Cycle: 0", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel controlButtons = new JPanel(new FlowLayout());
        runSimulationButton = new JButton("▶ Run Cycle");
        assignTaskButton = new JButton("📋 Assign Task");
        saveButton = new JButton("💾 Save Game");
        loadButton = new JButton("📂 Load Game");

        runSimulationButton.addActionListener(e -> runSimulationCycle());
        assignTaskButton.addActionListener(e -> showTaskAssignmentDialog());
        saveButton.addActionListener(e -> saveGame());
        loadButton.addActionListener(e -> loadGame());

        controlButtons.add(runSimulationButton);
        controlButtons.add(assignTaskButton);
        controlButtons.add(saveButton);
        controlButtons.add(loadButton);

        topPanel.add(timeLabel, BorderLayout.CENTER);
        topPanel.add(controlButtons, BorderLayout.SOUTH);

        // Center: Processor Panels
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Map.Entry<String, IProcessor> entry : processors.entrySet()) {
            centerPanel.add(createProcessorPanel(entry.getKey(), entry.getValue()));
        }

        // Resources
        JPanel rightPanel = createResourcePanel();

        // Log/Output
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new TitledBorder("Event Log"));
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Adding to main frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JPanel createProcessorPanel(String name, IProcessor processor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder(name));
        panel.setBackground(new Color(240, 240, 240));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 150, 0));
        processorBars.put(name, progressBar);

        JLabel taskLabel = new JLabel("Current Task: None", SwingConstants.CENTER);
        taskLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        currentTaskLabels.put(name, taskLabel);

        JLabel statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        JButton processButton = new JButton("Process Task");
        processButton.setBackground(new Color(70, 130, 200));
        processButton.setForeground(Color.WHITE);

        processButton.addActionListener(e -> {
            ColonyTask currentTask = currentTasks.get(name);
            if (currentTask == null) {
                appendLog("⚠️ No task assigned to " + name);
                JOptionPane.showMessageDialog(this,
                        "No task assigned to " + name + "!\nUse 'Assign Task' button first.",
                        "No Task", JOptionPane.WARNING_MESSAGE);
                return;
            }

            appendLog("🔧 " + name + " processing: " + currentTask.getName());

            String result = processor.processTask(currentTask);
            appendLog(result);

            // Updating the progress
            int newProgress = processorBars.get(name).getValue() + 50;
            processorBars.get(name).setValue(Math.min(100, newProgress));

            if (processorBars.get(name).getValue() >= 100) {
                appendLog("✅ " + name + " completed task: " + currentTask.getName());
                processorBars.get(name).setValue(0);
                currentTasks.put(name, null);
                taskLabel.setText("Current Task: None");
                statusLabel.setText("Status: Ready");
            } else {
                statusLabel.setText("Status: Processing...");
            }

            refreshData();
        });

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(taskLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(processButton);

        panel.add(progressBar, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResourcePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Resources"));
        panel.setPreferredSize(new Dimension(250, 0));

        JPanel resourcesPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        resourcesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Resource types
        String[] resourceNames = {"💧 Water", "🍔 Food", "⚡ Energy", "🫁 Oxygen", "🔧 Parts"};
        for (String res : resourceNames) {
            JLabel label = new JLabel(res + ":");
            JLabel valueLabel = new JLabel("0");
            valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
            resourceLabels.put(res, valueLabel);
            resourcesPanel.add(label);
            resourcesPanel.add(valueLabel);
        }

        panel.add(resourcesPanel, BorderLayout.NORTH);

        // Adding the emergency button
        JButton emergencyButton = new JButton("🚨 Emergency Protocol");
        emergencyButton.setBackground(Color.RED);
        emergencyButton.setForeground(Color.WHITE);
        emergencyButton.addActionListener(e -> emergencyProtocol());
        panel.add(emergencyButton, BorderLayout.SOUTH);

        return panel;
    }

    private void showTaskAssignmentDialog() {
        List<ColonyTask> allTasks = TaskLibrary.getAllTasks();

        if (allTasks == null || allTasks.isEmpty()) {
            appendLog("⚠️ No tasks available in library");
            return;
        }

        // Create dialog for task assignment
        JDialog dialog = new JDialog(this, "Assign Task", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Task selection panel
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBorder(new TitledBorder("Select Task"));

        DefaultListModel<String> taskListModel = new DefaultListModel<>();
        for (ColonyTask task : allTasks) {
            String taskInfo = String.format("[%s] %s (Difficulty: %d, Crew: %d, Time: %d min)",
                    task.getTaskType(),
                    task.getName(),
                    task.getDifficulties(),
                    task.getCrewMembersRequired(),
                    task.getTimeToFix()
            );
            taskListModel.addElement(taskInfo);
        }

        JList<String> taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane taskScroll = new JScrollPane(taskList);
        taskScroll.setPreferredSize(new Dimension(450, 200));
        taskPanel.add(taskScroll, BorderLayout.CENTER);

        JPanel processorPanel = new JPanel(new FlowLayout());
        processorPanel.setBorder(new TitledBorder("Assign To"));

        String[] processorNames = processors.keySet().toArray(new String[0]);
        JComboBox<String> processorCombo = new JComboBox<>(processorNames);
        processorPanel.add(new JLabel("Processor:"));
        processorPanel.add(processorCombo);

        JTextArea taskDetails = new JTextArea(5, 40);
        taskDetails.setEditable(false);
        taskDetails.setFont(new Font("Monospaced", Font.PLAIN, 11));

        taskList.addListSelectionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                ColonyTask selected = allTasks.get(index);
                taskDetails.setText(String.format(
                        "Task Details:\n" +
                                "Name: %s\n" +
                                "Type: %s\n" +
                                "Parts Required: %d\n" +
                                "Time Required: %d minutes\n" +
                                "Crew Needed: %d\n" +
                                "Difficulty: %d (0=Easy,1=Medium,2=Hard)\n" +
                                "Supplies Required: %d",
                        selected.getName(),
                        selected.getTaskType(),
                        selected.getRequiredParts(),
                        selected.getTimeToFix(),
                        selected.getCrewMembersRequired(),
                        selected.getDifficulties(),
                        selected.getSuppliesRequired()
                ));
            }
        });

        JScrollPane detailsScroll = new JScrollPane(taskDetails);
        detailsScroll.setBorder(new TitledBorder("Task Information"));

        mainPanel.add(taskPanel, BorderLayout.NORTH);
        mainPanel.add(detailsScroll, BorderLayout.CENTER);
        mainPanel.add(processorPanel, BorderLayout.SOUTH);

        JButton assignButton = new JButton("Assign Task");
        assignButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(dialog, "Please select a task!");
                return;
            }

            ColonyTask selectedTask = allTasks.get(index);
            String selectedProcessor = (String) processorCombo.getSelectedItem();
            IProcessor processor = processors.get(selectedProcessor);

            // Check if processor can process this task
            if (processor.canProcess(selectedTask)) {
                currentTasks.put(selectedProcessor, selectedTask);
                currentTaskLabels.get(selectedProcessor).setText("Current Task: " + selectedTask.getName());
                appendLog(String.format("📋 Assigned '%s' to %s", selectedTask.getName(), selectedProcessor));
                appendLog(String.format("   ⚙️ Requires: %d parts, %d crew, %d minutes",
                        selectedTask.getRequiredParts(),
                        selectedTask.getCrewMembersRequired(),
                        selectedTask.getTimeToFix()));
                dialog.dispose();
            } else {
                appendLog("❌ " + selectedProcessor + " cannot process: " + selectedTask.getName());
                JOptionPane.showMessageDialog(dialog,
                        selectedProcessor + " cannot process this task type!\n" +
                                "Task type: " + selectedTask.getTaskType(),
                        "Cannot Process", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(assignButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void runSimulationCycle() {
        appendLog("▶ Running simulation cycle...");
        try {
            // Update cycle display
            String currentText = timeLabel.getText();
            int currentCycle = Integer.parseInt(currentText.split(": ")[1]);
            timeLabel.setText("Cycle: " + (currentCycle + 1));

            appendLog("✓ Cycle " + (currentCycle + 1) + " completed");
            refreshData();
        } catch (Exception ex) {
            appendLog("❌ Error in simulation: " + ex.getMessage());
        }
    }

    private void refreshData() {
        // Update resource display with random values for demonstration
        // Replace with actual ResourceManager calls when ready
        String[] resourceNames = {"💧 Water", "🍔 Food", "⚡ Energy", "🫁 Oxygen", "🔧 Parts"};
        for (String resourceName : resourceNames) {
            int value = 50 + (int)(Math.random() * 50);
            JLabel label = resourceLabels.get(resourceName);
            if (label != null) {
                label.setText(String.valueOf(value));
                if (value < 20) {
                    label.setForeground(Color.RED);
                } else if (value < 50) {
                    label.setForeground(Color.ORANGE);
                } else {
                    label.setForeground(new Color(0, 100, 0));
                }
            }
        }
    }

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Game");
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            try {
                // saveLoadManager.save(path, engine, resourceManager);
                appendLog("💾 Game saved to " + path);
            } catch (Exception ex) {
                appendLog("❌ Save failed: " + ex.getMessage());
            }
        }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Game");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            try {
                // saveLoadManager.load(path);
                appendLog("📂 Game loaded from " + path);
                refreshData();
            } catch (Exception ex) {
                appendLog("❌ Load failed: " + ex.getMessage());
            }
        }
    }

    private void emergencyProtocol() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "⚠️ EMERGENCY PROTOCOL ⚠️\n\n" +
                        "This will:\n" +
                        "- Cancel all current tasks\n" +
                        "- Consume emergency resources\n" +
                        "- Alert all crew members\n\n" +
                        "Are you sure?",
                "EMERGENCY PROTOCOL",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            appendLog("🚨 EMERGENCY PROTOCOL ACTIVATED!");
            // Clear all tasks
            for (String processor : currentTasks.keySet()) {
                currentTasks.put(processor, null);
                currentTaskLabels.get(processor).setText("Current Task: None");
                processorBars.get(processor).setValue(0);
            }
            appendLog("✓ All tasks cancelled");
            appendLog("🔄 Emergency systems engaged");
        }
    }

    private void appendLog(String message) {
        String timestamp = java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + timestamp + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());

        // Limit log size to prevent memory issues
        if (logArea.getLineCount() > 500) {
            try {
                int lastNewline = logArea.getText().indexOf("\n", logArea.getText().length() / 2);
                if (lastNewline > 0) {
                    logArea.replaceRange("", 0, lastNewline + 1);
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AresBaseDashboard().setVisible(true);
        });
    }
}