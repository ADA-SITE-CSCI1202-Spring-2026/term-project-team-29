package ui;

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

	private static final long serialVersionUID = 1L;
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
	private JProgressBar hpBar;

	public AresBaseDashboard() {
		applyTheme();
		engine = new SimulationEngine(() -> refreshData());
		saveLoadManager = new SaveLoadManager();
		resourceLabels = new HashMap<>();
		taskListModel = new DefaultListModel<>();

		setupUI();
		refreshData();
		engine.start();
		appendLog("[INIT] Ares Base Control System Initialized");
		appendLog("[INFO] " + TaskLibrary.getAllTasks().size() + " tasks loaded into simulation");
	}

	private JButton createStyledButton(String text, Color bg, Color fg) {
		JButton btn = new JButton(text);
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(new Font("Consolas", Font.BOLD, 12));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setOpaque(true);
		return btn;
	}

	private TitledBorder createStyledBorder(String title) {
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(30, 215, 96), 1),
				title);
		border.setTitleColor(new Color(30, 215, 96));
		border.setTitleFont(new Font("Consolas", Font.BOLD, 12));
		return border;
	}

	private void applyTheme() {
		UIManager.put("Panel.background", new Color(13, 17, 23));
		UIManager.put("Label.foreground", new Color(200, 210, 220));
		UIManager.put("TextArea.background", new Color(22, 27, 34));
		UIManager.put("TextArea.foreground", new Color(139, 233, 253));
		UIManager.put("List.background", new Color(22, 27, 34));
		UIManager.put("List.foreground", new Color(139, 233, 253));
		UIManager.put("ScrollPane.background", new Color(13, 17, 23));
		UIManager.put("ComboBox.background", new Color(22, 27, 34));
		UIManager.put("ComboBox.foreground", new Color(200, 210, 220));
		UIManager.put("Button.background", new Color(30, 215, 96));
		UIManager.put("Button.foreground", Color.BLACK);
		UIManager.put("Button.font", new Font("Consolas", Font.BOLD, 12));
	}

	private void setupUI() {
		setTitle("Ares Base Control Dashboard - Team 29");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));
		setSize(1300, 750);
		getContentPane().setBackground(new Color(13, 17, 23));

		// Top: time + controls
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		timeLabel = new JLabel("BASE TIME: 00:00", SwingConstants.CENTER);
		timeLabel.setFont(new Font("Consolas", Font.BOLD, 22));
		timeLabel.setForeground(new Color(30, 215, 96));
		timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		JPanel controlButtons = new JPanel(new FlowLayout());
		runSimulationButton = createStyledButton(">> Execute Next Task", new Color(30, 215, 96), Color.BLACK);
		JButton pauseButton = createStyledButton("|| Pause", new Color(255, 165, 0), Color.BLACK);
		JButton fastForwardButton = createStyledButton(">> Fast Forward", new Color(0, 180, 255), Color.BLACK);
		saveButton = createStyledButton("[S] Save", new Color(80, 80, 80), Color.WHITE);
		loadButton = createStyledButton("[L] Load", new Color(80, 80, 80), Color.WHITE);

		runSimulationButton.addActionListener(e -> {
			String result = engine.executeNextTask();
			appendLog(result);
			refreshData();
		});

		pauseButton.addActionListener(e -> {
			if (engine.isPaused()) {
				engine.resume();
				pauseButton.setText("|| Pause");
				appendLog("[INFO] Simulation resumed");
			} else {
				engine.pause();
				pauseButton.setText("> Resume");
				appendLog("[INFO] Simulation paused");
			}
		});

		fastForwardButton.addActionListener(e -> {
			if (engine.getSpeed() == 1) {
				engine.fastForward();
				fastForwardButton.setText("= Normal Speed");
				appendLog("[INFO] Fast forward activated");
			} else {
				engine.normalSpeed();
				fastForwardButton.setText(">> Fast Forward");
				appendLog("[INFO] Normal speed restored");
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

		// Center: Task Queue
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(createStyledBorder("TASK QUEUE"));

		JList<String> taskQueueList = new JList<>(taskListModel);
		taskQueueList.setFont(new Font("Consolas", Font.PLAIN, 12));
		taskQueueList.setBackground(new Color(13, 17, 23));
		taskQueueList.setForeground(new Color(139, 233, 253));
		taskQueueList.setSelectionBackground(new Color(30, 215, 96));
		taskQueueList.setSelectionForeground(Color.BLACK);
		JScrollPane taskScroll = new JScrollPane(taskQueueList);
		centerPanel.add(taskScroll, BorderLayout.CENTER);

		// Right: Resources
		JPanel rightPanel = createResourcePanel();

		// Bottom: Log + Restock
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(createStyledBorder("BASE TERMINAL"));
		logArea = new JTextArea(10, 50);
		logArea.setEditable(false);
		logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		logArea.setBackground(new Color(13, 17, 23));
		logArea.setForeground(new Color(139, 233, 253));
		logArea.setCaretColor(new Color(30, 215, 96));
		JScrollPane scrollPane = new JScrollPane(logArea);
		bottomPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel restockPanel = new JPanel(new FlowLayout());
		restockPanel.setBorder(createStyledBorder("CARGO REPLICATOR"));
		JComboBox<Resource> resourceCombo = new JComboBox<>(Resource.values());
		JButton restockButton = createStyledButton("[+] Synthesize", new Color(0, 180, 255), Color.BLACK);
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

		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	private JPanel createResourcePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(createStyledBorder("COLONY VITALS"));
		panel.setBackground(new Color(13, 17, 23));
		panel.setPreferredSize(new Dimension(250, 0));

		JPanel resourcesPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		resourcesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Keys must exactly match what updateResourceLabel() uses
		String[] resourceNames = { "Oxygen", "Parts", "Suits", "Crew", "Lab Eq.", "Power", "Supplies", "Credits" };
		for (String res : resourceNames) {
			JLabel label = new JLabel(res + ":");
			label.setFont(new Font("Consolas", Font.PLAIN, 12));
			label.setForeground(new Color(180, 190, 200));
			JLabel valueLabel = new JLabel("0");
			valueLabel.setFont(new Font("Consolas", Font.BOLD, 14));
			valueLabel.setForeground(new Color(30, 215, 96));
			resourceLabels.put(res, valueLabel);
			resourcesPanel.add(label);
			resourcesPanel.add(valueLabel);
		}

		panel.add(resourcesPanel, BorderLayout.NORTH);

		JPanel hpPanel = new JPanel(new BorderLayout(5, 5));
		hpPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		JLabel hpLabel = new JLabel("Base Health:");
		hpLabel.setFont(new Font("Consolas", Font.BOLD, 12));
		hpLabel.setForeground(new Color(200, 210, 220));

		hpBar = new JProgressBar(0, 100);
		hpBar.setBackground(new Color(22, 27, 34));
		hpBar.setFont(new Font("Consolas", Font.BOLD, 11));
		hpBar.setValue(100);
		hpBar.setStringPainted(true);
		hpBar.setString("100 HP");
		hpBar.setForeground(new Color(0, 180, 0));
		hpPanel.add(hpLabel, BorderLayout.NORTH);
		hpPanel.add(hpBar, BorderLayout.CENTER);
		panel.add(hpPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void refreshData() {
		timeLabel.setText("BASE TIME: " + engine.getFormattedGameTime());

		String log = engine.getLastLog();
		if (!log.isEmpty()) {
			appendLog(log);
			engine.clearLastLog();
		}

		ResourceManager rm = engine.getResourceManager();
		updateResourceLabel("Oxygen",   rm.getAmount(Resource.OXYGEN));
		updateResourceLabel("Parts",    rm.getAmount(Resource.SPARE_PARTS));
		updateResourceLabel("Suits",    rm.getAmount(Resource.SPACE_SUITS));
		updateResourceLabel("Crew",     rm.getAmount(Resource.CREW_MEMBERS));
		updateResourceLabel("Lab Eq.",  rm.getAmount(Resource.LAB_EQUIPMENTS));
		updateResourceLabel("Power",    rm.getAmount(Resource.POWER_UNITS));
		updateResourceLabel("Supplies", rm.getAmount(Resource.SUPPLIES));
		updateResourceLabel("Credits",  rm.getCredits());

		taskListModel.clear();
		for (ColonyTask task : engine.getTaskQueue()) {
			taskListModel.addElement("[" + task.getTaskType() + "] " + task.getName() + " | " + task.getResourceSummary());
		}

		int hp = engine.getResourceManager().getHp();
		hpBar.setValue(hp);
		hpBar.setString(hp + " HP");
		if (hp > 60) {
			hpBar.setForeground(new Color(0, 180, 0));
		} else if (hp > 30) {
			hpBar.setForeground(Color.ORANGE);
		} else {
			hpBar.setForeground(Color.RED);
		}

		if (hp <= 0) {
			showGameOver();
		}
	}

	private void showGameOver() {
		engine.pause();

		int choice = JOptionPane.showOptionDialog(this,
				"ARES BASE HAS FALLEN\n\n"
						+ "The colony could not survive the crisis.\n"
						+ "All crew members have been lost.\n\n"
						+ "Time Survived: " + engine.getFormattedGameTime(),
				"GAME OVER", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null,
				new String[] { "Restart", "Quit" }, "Restart");

		if (choice == JOptionPane.YES_OPTION) {
			restartGame();
		} else {
			System.exit(0);
		}
	}

	private void restartGame() {
		engine = new SimulationEngine(() -> refreshData());
		taskListModel.clear();
		logArea.setText("");
		engine.start();
		refreshData();
		appendLog("[INFO] Base restarted. Good luck Commander!");
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
				appendLog("[SAVE] Game saved to " + path);
			} catch (Exception ex) {
				appendLog("[ERROR] Save failed: " + ex.getMessage());
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
				appendLog("[LOAD] Game loaded from " + path);
				refreshData();
			} catch (Exception ex) {
				appendLog("[ERROR] Load failed: " + ex.getMessage());
			}
		}
	}

	private void appendLog(String message) {
		String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
		logArea.append("[" + timestamp + "] " + message + "\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());

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