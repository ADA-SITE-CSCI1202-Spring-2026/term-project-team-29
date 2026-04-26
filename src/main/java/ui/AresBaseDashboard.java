package ui;

import processors.*;
import resources.Resource;
import resources.ResourceManager;
import tasks.ColonyTask;
import tasks.TaskLibrary;
import main.SimulationEngine;
import fileio.SaveLoadManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AresBaseDashboard extends JFrame {
	private SimulationEngine engine;
	private SaveLoadManager saveLoadManager;
	private DefaultListModel<String> taskListModel;

	// UI Components
	private JTextArea logArea;
	private JLabel timeLabel;
	private Map<String, JLabel> resourceLabels;

	private JButton runSimulationButton;
	private JButton saveButton;
	private JButton loadButton;

	public AresBaseDashboard() {
		// Initialize backend
		engine = new SimulationEngine(() -> refreshData());
		saveLoadManager = new SaveLoadManager();
		resourceLabels = new HashMap<>();
		taskListModel = new DefaultListModel<>();

		setupUI();
		refreshData();
		engine.start();
		appendLog("🏭 Ares Base Control System Initialized");
		appendLog("📋 " + TaskLibrary.getAllTasks().size() + " tasks loaded into simulation");
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

		timeLabel = new JLabel("Base Time: 00:00", SwingConstants.CENTER);
		timeLabel.setFont(new Font("Arial", Font.BOLD, 18));

		JPanel controlButtons = new JPanel(new FlowLayout());
		runSimulationButton = new JButton("⚡ Execute Next Task");
		JButton pauseButton = new JButton("⏸ Pause");
		JButton fastForwardButton = new JButton("⏩ Fast Forward");
		saveButton = new JButton("💾 Save Game");
		loadButton = new JButton("📂 Load Game");

		runSimulationButton.addActionListener(e -> {
			String result = engine.executeNextTask();
			appendLog(result);
			refreshData();
		});

		pauseButton.addActionListener(e -> {
			if (engine.isPaused()) {
				engine.resume();
				pauseButton.setText("⏸ Pause");
				appendLog("▶ Simulation resumed");
			} else {
				engine.pause();
				pauseButton.setText("▶ Resume");
				appendLog("⏸ Simulation paused");
			}
		});

		fastForwardButton.addActionListener(e -> {
			if (engine.getSpeed() == 1) {
				engine.fastForward();
				fastForwardButton.setText("🐢 Normal Speed");
				appendLog("⏩ Fast forward activated");
			} else {
				engine.normalSpeed();
				fastForwardButton.setText("⏩ Fast Forward");
				appendLog("🐢 Normal speed restored");
			}
		});

		saveButton.addActionListener(e -> saveGame());
		loadButton.addActionListener(e -> loadGame());

		controlButtons.add(runSimulationButton);
		controlButtons.add(pauseButton);
		controlButtons.add(fastForwardButton);
		controlButtons.add(saveButton);
		controlButtons.add(loadButton);

		topPanel.add(timeLabel, BorderLayout.CENTER);
		topPanel.add(controlButtons, BorderLayout.SOUTH);

		// Center: Processor Panels
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new TitledBorder("Task Queue"));

		
		JList<String> taskQueueList = new JList<>(taskListModel);
		taskQueueList.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane taskScroll = new JScrollPane(taskQueueList);
		centerPanel.add(taskScroll, BorderLayout.CENTER);

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

		JPanel restockPanel = new JPanel(new FlowLayout());
		restockPanel.setBorder(new TitledBorder("Cargo Replicator"));
		JComboBox<Resource> resourceCombo = new JComboBox<>(Resource.values());
		JButton restockButton = new JButton("🔄 Synthesize");
		restockButton.addActionListener(e -> {
			Resource selected = (Resource) resourceCombo.getSelectedItem();
			String result = engine.restock(selected);
			appendLog(result);
			refreshData();
		});
		restockPanel.add(new JLabel("Resource:"));
		restockPanel.add(resourceCombo);
		restockPanel.add(restockButton);
		bottomPanel.add(restockPanel, BorderLayout.NORTH);

		// Adding to main frame
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	private JPanel createResourcePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Resources"));
		panel.setPreferredSize(new Dimension(250, 0));

		JPanel resourcesPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		resourcesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Resource types
		String[] resourceNames = { "🫁 Oxygen", "🔧 Parts", "👔 Suits", "👷 Crew", "🔬 Lab Eq.", "⚡ Power",
				"💰 Credits" };
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

	private void refreshData() {
		// update clock
		timeLabel.setText("Base Time: " + engine.getFormattedGameTime());

		// update resource labels
		ResourceManager rm = engine.getResourceManager();
		updateResourceLabel("🫁 Oxygen", rm.getAmount(Resource.OXYGEN));
		updateResourceLabel("🔧 Parts", rm.getAmount(Resource.SPARE_PARTS));
		updateResourceLabel("👔 Suits", rm.getAmount(Resource.SPACE_SUITS));
		updateResourceLabel("👷 Crew", rm.getAmount(Resource.CREW_MEMBERS));
		updateResourceLabel("🔬 Lab Eq.", rm.getAmount(Resource.LAB_EQUIPMENTS));
		updateResourceLabel("⚡ Power", rm.getAmount(Resource.POWER_UNITS));
		updateResourceLabel("💰 Credits", rm.getCredits());

		// update task queue display
		taskListModel.clear();
		for (ColonyTask task : engine.getTaskQueue()) {
			taskListModel.addElement("[" + task.getTaskType() + "] " + task.getName() + " | Parts: "
					+ task.getRequiredParts() + " | Time: " + task.getTimeToFix() + " mins");
		}
	}

	private void updateResourceLabel(String key, int value) {
		JLabel label = resourceLabels.get(key);
		if (label != null) {
			label.setText(String.valueOf(value));
			if (value < 5) {
				label.setForeground(Color.RED);
			} else if (value < 15) {
				label.setForeground(Color.ORANGE);
			} else {
				label.setForeground(new Color(0, 100, 0));
			}
		}
	}

	private void saveGame() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Game");
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			try {
				engine.saveGame(path);
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
				engine.loadGame(path);
				appendLog("📂 Game loaded from " + path);
				refreshData();
			} catch (Exception ex) {
				appendLog("❌ Load failed: " + ex.getMessage());
			}
		}
	}

	private void emergencyProtocol() {
		int confirm = JOptionPane.showConfirmDialog(this,
				"⚠️ EMERGENCY PROTOCOL ⚠️\n\n" + "This will clear the task queue!\n\n" + "Are you sure?",
				"EMERGENCY PROTOCOL", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			appendLog("🚨 EMERGENCY PROTOCOL ACTIVATED!");
			engine.getTaskQueue().clear();
			appendLog("✓ Task queue cleared");
			appendLog("🔄 Emergency systems engaged");
			refreshData();
		}
	}

	private void appendLog(String message) {
		String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
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