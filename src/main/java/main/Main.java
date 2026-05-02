package main;

import ui.AresBaseDashboard;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new AresBaseDashboard().setVisible(true);
		});
	}

}
