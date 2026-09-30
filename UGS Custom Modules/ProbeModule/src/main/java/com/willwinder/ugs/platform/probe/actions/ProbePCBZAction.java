package com.willwinder.ugs.platform.probe.actions;

import com.willwinder.ugs.nbp.lib.services.LocalizingService;
import com.willwinder.ugs.platform.probe.ProbeParameters;
import com.willwinder.ugs.platform.probe.ProbeService;
import com.willwinder.ugs.platform.probe.ProbeSettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

import javax.swing.JOptionPane;

@ActionID(
        category = LocalizingService.CATEGORY_MACHINE,
        id = "com.willwinder.ugs.platform.probe.actions.ProbePCBZAction")
@ActionRegistration(
        iconBase = ProbeZAction.BASE_ICON,
        displayName = "Probe and zero PCB Z",
        lazy = false)
@ActionReferences({
        @ActionReference(
                path = LocalizingService.MENU_MACHINE_PROBE,
                position = 15)
})
public class ProbePCBZAction extends ProbeZAction {

    public ProbePCBZAction() {
        putValue("iconBase", BASE_ICON);
        putValue(SMALL_ICON, ImageUtilities.loadImageIcon(BASE_ICON, false));
        putValue(LARGE_ICON_KEY, ImageUtilities.loadImageIcon(BASE_ICON_LARGE, false));
        putValue("menuText", "PCB Z Probe");
        putValue(NAME, "PCB Z Probe");
    }

    @Override
    public void performProbeAction() {
        int confirm = JOptionPane.showConfirmDialog(null,
                "IS YOUR GROUND CONNECTED?",
                "PCB Z Probe",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.OK_OPTION) {
            return;
        }

        double probeDistance = calculateSafeProbeDistance();

        ProbeService probeService = Lookup.getDefault().lookup(ProbeService.class);
        ProbeParameters pc = new ProbeParameters(
                ProbeSettings.getSettingsProbeDiameter(), getBackend().getMachinePosition(),
                0., 0., probeDistance,
                0., 0., 0.0,
                0.0,
                ProbeSettings.getSettingsFastFindRate(), ProbeSettings.getSettingsSlowMeasureRate(),
                ProbeSettings.getSettingsRetractAmount(), ProbeSettings.getSettingsDelayAfterRetract(),
                getBackend().getSettings().getPreferredUnits(),
                ProbeSettings.getSettingsWorkCoordinate());

        probeService.performZProbe(pc);

        new Thread(() -> {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            javax.swing.SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null,
                        "Probing complete!\n\nREMOVE YOUR GROUND CLIP NOW!",
                        "PCB Z Probe",
                        JOptionPane.WARNING_MESSAGE)
            );
        }).start();
    }

    @Override
    public String getProbeConfirmationText() {
        return "IS YOUR GROUND CONNECTED?";
    }
}