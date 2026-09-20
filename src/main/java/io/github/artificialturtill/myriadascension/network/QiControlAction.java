package io.github.artificialturtill.myriadascension.network;

public enum QiControlAction {
    GATHER_AND_CIRCULATE,
    SUPPRESS_CIRCULATION,
    TOGGLE_BURST;

    public static QiControlAction fromNetworkId(int id) {
        QiControlAction[] values = values();
        if (id < 0 || id >= values.length) {
            return SUPPRESS_CIRCULATION;
        }
        return values[id];
    }
}
