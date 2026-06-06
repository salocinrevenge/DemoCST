package support;

import java.util.List;
import java.util.Map;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

public final class LeafletUtils {

    private LeafletUtils() {
    }

    public static boolean hasCompletedLeaflet(List<Leaflet> leaflets) {
        return getCompletedLeaflet(leaflets) != null;
    }

    public static Leaflet getCompletedLeaflet(List<Leaflet> leaflets) {
        if (leaflets == null) {
            return null;
        }
        for (Leaflet leaflet : leaflets) {
            if (leaflet != null && leaflet.isCompleted()) {
                return leaflet;
            }
        }
        return null;
    }

    public static boolean hasOpenLeaflet(List<Leaflet> leaflets) {
        if (leaflets == null) {
            return false;
        }
        for (Leaflet leaflet : leaflets) {
            if (leaflet != null && !leaflet.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    public static boolean needsColor(List<Leaflet> leaflets, String color) {
        if (leaflets == null || color == null) {
            return false;
        }
        for (Leaflet leaflet : leaflets) {
            if (leaflet != null && !leaflet.isCompleted() && leaflet.getMissingNumberOfType(color) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNeededJewel(List<Thing> jewels, List<Leaflet> leaflets) {
        if (jewels == null) {
            return false;
        }
        for (Thing jewel : jewels) {
            if (jewel != null && jewel.getName().contains("Jewel") && needsColor(leaflets, getJewelColor(jewel))) {
                return true;
            }
        }
        return false;
    }

    public static String getJewelColor(Thing jewel) {
        if (jewel == null || jewel.getAttributes() == null) {
            return null;
        }
        return jewel.getAttributes().getColor();
    }

    public static String describeMissing(List<Leaflet> leaflets) {
        if (leaflets == null || leaflets.isEmpty()) {
            return "none";
        }
        StringBuilder out = new StringBuilder();
        for (Leaflet leaflet : leaflets) {
            if (leaflet == null || leaflet.isCompleted()) {
                continue;
            }
            if (out.length() > 0) {
                out.append("; ");
            }
            out.append(leaflet.getID()).append(":");
            for (Object entryObject : leaflet.getWhatToCollect().entrySet()) {
                Map.Entry entry = (Map.Entry) entryObject;
                int missing = (Integer) entry.getValue();
                if (missing > 0) {
                    out.append(" ").append(entry.getKey()).append("=").append(missing);
                }
            }
        }
        return out.length() == 0 ? "completed" : out.toString();
    }
}
