package com.willwinder.ugs.platform.probe.ui;

import com.willwinder.ugs.platform.probe.ProbeSettings;
import com.willwinder.ugs.platform.probe.actions.ProbePCBZAction;
import com.willwinder.universalgcodesender.model.UnitUtils;
import com.willwinder.universalgcodesender.model.Unit;
import com.willwinder.universalgcodesender.uielements.components.UnitSpinner;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.prefs.PreferenceChangeEvent;

public class ProbePCBZPanel extends JPanel {
    private final UnitSpinner zProbeDistanceSpinner;

    public ProbePCBZPanel() {
        var units = ProbeSettings.getSettingsUnits() == UnitUtils.Units.MM ? Unit.MM : Unit.INCH;        zProbeDistanceSpinner = new UnitSpinner(ProbeSettings.getzDistance(), units);
        createLayout();
        registerListeners();
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (Component component : getComponents()) {
            component.setEnabled(enabled);
        }
    }

    private void createLayout() {
        setLayout(new MigLayout("insets 10, gap 12", "[shrink][120:120, sg1]"));
        add(new JLabel("Plate Thickness: 0mm (PCB)"), "spanx 2, wrap");
        add(new JLabel("Probe Distance:"));
        add(zProbeDistanceSpinner, "growx, wrap");
        add(new JButton(new ProbePCBZAction()), "spanx 2, growx, growy, height 40:40");
    }

    private void registerListeners() {
        zProbeDistanceSpinner.addChangeListener(l -> ProbeSettings.setzDistance(zProbeDistanceSpinner.getDoubleValue()));
        ProbeSettings.addPreferenceChangeListener(this::onSettingsChanged);
    }

    private void onSettingsChanged(PreferenceChangeEvent event) {
        if (event.getKey().equals(ProbeSettings.Z_DISTANCE)) {
            zProbeDistanceSpinner.setValue(ProbeSettings.getzDistance());
        }
    }
}