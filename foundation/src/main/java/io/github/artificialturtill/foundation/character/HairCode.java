package io.github.artificialturtill.foundation.character;

import java.util.Arrays;

/**
 * The custom hairstyle code (JRMCore 1.3.51 "jrmcDNSH", JBRA ModelBipedDBC.renderHairsV2, ported from Dragonblock C+
 * LegacyHair): 56 hair groups x 14 digits (length, X / Y / Z rotation, bend, bend point, shape change; two digits
 * each) plus a two-digit "G" version = 786 digits. Groups 0-3 front, 4-13 right, 14-23 left, 24-39 back, 40-55 top.
 * Values other than the length are clamped to 18..82 (50 = neutral). "" = bald.
 */
public final class HairCode {
    public static final int GROUPS = 56, LENGTH = 786;
    public static final int L = 0, X = 2, Y = 4, Z = 6, B = 8, P = 10, T = 12;
    /** Page start groups: front, right, left, back, top, end (JRMCoreGuiScreen.hcl). */
    public static final int[] PAGE_START = {0, 4, 14, 24, 40, 56};
    /** JRMCoreH.defHairPrsts as stored (three are 392-digit version 1 codes). */
    private static final String[] RAW_PRESETS = {
        "345050555050801250505550501901505055505080235050455050803450505550508001505055505080015050555050802350505550508034505055505080125050555050800150505550508023505055505080015050555050800150505550506712505045505080015050455050800150504550508023505045505080345050455050801250504550508001505045505080235050455050800150504550508001505045505061015550505050800155505050508001555050505080015550505050800155505050508001555050505080015550505050800155505050508001555050505080015550505050800155505050508001555050505080015550505050630155505050506701555050505061015550505050671170505050503814705050505041147050505050411770505050503817705050505041207050505050412070505050504117705050505041227050505050412570505050504125705050505041207050505050412070505050503225705050505041257050505050412270505050503420",
        "18080801808080180808018080801808080180808018080801808080180808018080801808080180808018080801808080180808018080801808080180808018080801808080180808018080801808080180808018080804808080180808018080801808080180808018080804808080180808018080801808080180808018080801808080180808018080801808080180808028080801808080180808018080801808080180808018080801808080180808018080801808080180808018080801808080",
        "33843554384754461805224154501455050143505014550501435050143505014350501435050050505505050550505055145505014350501455050145505014550501455050145505005050450505045050504515050501505050150505015050501475049147504914750501475050152505015250501525050152505005550501475050147505005550503631847363184726318522631850267194727218502701850270185027019501701850270185027018501505050150505015050501505050",
        "08080180181919019191901819190505055050505505050550505055050505505050550505055050505505050550505055050504505050450505045050504505050450505045050504505050450505045050504505550504785050478505005550500555050474505047450500555050055505047150504715050055505005550500555050055505005550500325050425505042550500345050030505042350504235050038505003450504215050421505003450500695050419505041950500285050",
        "225067556150391150675561502311503245615023205032456150361750505450507117505054505071175050545050711750505450507101505054505071025050545050710150505450507100505054505071005050545050710050505450507122505045505071225050455050722250504550507222505045505072015050455050720150504550507201505045505072005050455050720050504550507200505045505072225445505050712254475050507122545250505071225454505050710154455050507101544950505071015450505050710154545050507100545050505071005450505050710054505050507100545050505071005450505050710054505050507100545050505071005450505050712850505650507465505074801930655050251819303750504550507437505056505074685050748018326850502519183234505045505074345050565050746850507480183868505025181838345050455050743450505650507480505074801843805050251819433450504550507420",
        "025050545050210250505450501801505045505021025050475050180147507467503248505072675043255250726750360150505667501922475071675038255050716750380152507167503202475071675032025250716750300050507167503000505047655036205250276550362250502765503620475027655036225250306550363150503065503622475030655034015250276550250147502765503000505027655036175050505050803150505050508028505050505080225050505050801750505050508022505050505080255050505050801750505050508000505050505080005050505050800050505050508000505050505080005050505050800050505050508000505050505080005050505050803154508067504931545080615028285450766150472854506561506551525080675038655250806150786052507861503451525069615050625050806950528250508061503485505078615030625050696150585149508069506157495080615080624950786150805149506961504920"};
    /** The built-in presets in 786-digit form (renderHairsV2 upgrades version 1 codes the same way). */
    public static final String[] PRESETS = Arrays.stream(RAW_PRESETS).map(HairCode::upgrade).toArray(String[]::new);
    public static final String DEFAULT = PRESETS[0];

    public static boolean valid(String code) {
        if (code == null || code.length() != LENGTH) return false;
        for (int i = 0; i < LENGTH; i++) if (code.charAt(i) < '0' || code.charAt(i) > '9') return false;
        return true;
    }
    /** "" (bald) or a valid code. */
    public static boolean validOrBald(String code) { return code != null && (code.isEmpty() || valid(code)); }
    /** JRMCoreH.dnsHairG1toG2: 392-digit version 1 codes and 784-digit codes without "G" become 786 digits. */
    public static String upgrade(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() == 392) {
            StringBuilder g2 = new StringBuilder();
            for (int face = 0; face < GROUPS; face++) {
                int l = Math.max(0, digit1(s, face * 7) * 11 - 10);
                g2.append(l < 10 ? "0" + l : Integer.toString(l)).append(s, face * 7 + 1, face * 7 + 7).append("505000");
            }
            return g2.append("20").toString();
        }
        return s.length() == 784 ? s + "20" : s;
    }
    public static int get(String code, int group, int field) { return two(code, group * 14 + field); }
    public static String set(String code, int group, int field, int value) {
        int v = Math.clamp(value, 0, 99), at = group * 14 + field;
        return code.substring(0, at) + (v < 10 ? "0" + v : Integer.toString(v)) + code.substring(at + 2);
    }
    /** Rendered / edited value: everything except the length is clamped to 18..82 (50 = neutral). */
    public static int clamped(String code, int group, int field) { return Math.clamp(get(code, group, field), 18, 82); }
    /** dnsHairG of a built-in preset as stored (version 1 presets show G1.0). */
    public static float presetVersion(int index) {
        String raw = RAW_PRESETS[index];
        return raw.length() == LENGTH ? two(raw, 784) * 0.1F : raw.length() == 392 ? 1.0F : 0F;
    }
    public static float version(String code) { return code.length() == LENGTH ? two(code, 784) * 0.1F : 0F; }

    /**
     * One hair group as renderHairsV2 poses it in the base state: root rotation point (model pixels), root angles
     * (radians, applied Z, Y, X), the bend angle of the three child segments (about Z for top / right / left groups,
     * about X otherwise) and the child segment lengths (0..1, hidden when 0).
     */
    public record Strand(float pointX, float pointY, float pointZ, float angleX, float angleY, float angleZ,
                         boolean bendAboutZ, float[] bend, float[] length) {}
    private static final int[] RIGHT_Z = {3, 2, 1, 0, 3, 2, 1, 3, 2, 3}, RIGHT_Y = {0, 0, 0, 0, 1, 1, 1, 2, 2, 3};
    private static final int[] LEFT_Z = {0, 1, 2, 3, 1, 2, 3, 2, 3, 3}, LEFT_Y = {0, 0, 0, 0, 1, 1, 1, 2, 2, 3};
    private static final int[] BACK_X = {0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3}, BACK_Y = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3};
    private static final int[] TOP_X = BACK_X, TOP_Z = BACK_Y;

    /**
     * renderHairsV2 for one group in the base state, null when the group is hidden (length 0). The transformation /
     * aura animation of the original (hair standing up) is not ported yet.
     */
    public static Strand strand(String code, int face) {
        int l = get(code, face, L);
        if (l == 0) return null;
        float x = (clamped(code, face, X) - 50) * 0.1F, y = (clamped(code, face, Y) - 50) * 0.1F, z = (clamped(code, face, Z) - 50) * 0.1F;
        float b = (clamped(code, face, B) - 50) * 0.1F, p = (clamped(code, face, P) - 50) * 0.1F;
        boolean front = face < 4, right = face >= 4 && face < 14, left = face >= 14 && face < 24, back = face >= 24 && face < 40, top = face >= 40;
        float px = -2.999F + (front ? face * 2 : left ? 7 : back ? BACK_X[face - 24] * 2 : top ? TOP_X[face - 40] * 2 : -1);
        float pz = -3.999F + (right ? RIGHT_Z[face - 4] * 2 + 1 : left ? LEFT_Z[face - 14] * 2 + 1 : back ? 8.0F : top ? TOP_Z[face - 40] * 2 + 0.9F : 0.0F);
        float py = -7.0F + (right ? RIGHT_Y[face - 4] * 2 : left ? LEFT_Y[face - 14] * 2 : back ? BACK_Y[face - 24] * 2 : -0.5F);
        boolean aboutZ = top || right || left;
        float sign = aboutZ ? (left ? 1F : -1F) : 1F;
        float first = 0.3F * (p > 0.5F ? 1.0F - p * 0.3F : p < -0.5F ? 1.0F + -p * 0.1F : 1.0F);
        float third = 0.3F * (p > 0.5F ? 1.0F + p * 0.1F : p < -0.5F ? 1.0F - -p * 0.3F : 1.0F);
        float[] bend = {sign * b * first, sign * b * 0.3F, sign * b * third};
        float t1 = l < 33F ? l / 33.0F : 1.0F;
        float t2 = l > 33F && l < 66F ? (l - 33.0F) / 33.0F : l < 33F ? 0.0F : 1.0F;
        float t3 = l > 66F ? (l - 66.0F) / 33.0F : l < 66F ? 0.0F : 1.0F;
        // showModel: segment 2 only above 33, segment 3 only above 66.
        return new Strand(px, py, pz, x, y, z, aboutZ, bend, new float[]{t1, l > 33 ? t2 : 0F, l > 66 ? t3 : 0F});
    }

    private static int two(String s, int n) {
        try { return Integer.parseInt(s.substring(n, n + 2)); } catch (RuntimeException e) { return 0; }
    }
    private static int digit1(String s, int n) {
        try { return Integer.parseInt(s.substring(n, n + 1)); } catch (RuntimeException e) { return 0; }
    }
    private HairCode() {}
}
