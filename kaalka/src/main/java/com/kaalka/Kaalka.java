package main.java.com.kaalka;

import java.util.Calendar;

public class Kaalka {
    private int h = 0;
    private int m = 0;
    private int s = 0;

    protected void setTimeFields(int h, int m, int s) {
        this.h = h;
        this.m = m;
        this.s = s;
    }

    public Kaalka() {
        updateTimestamp();
    }

    private void updateTimestamp() {
        Calendar now = Calendar.getInstance();
        this.h = now.get(Calendar.HOUR_OF_DAY) % 12;
        this.m = now.get(Calendar.MINUTE);
        this.s = now.get(Calendar.SECOND);
    }

    private int[] parseTime(Object timeKey) {
        int h = 0, m = 0, s = 0;
        if (timeKey instanceof Integer) {
            s = (Integer) timeKey;
        } else if (timeKey instanceof String) {
            String[] parts = ((String) timeKey).split(":");
            if (parts.length == 3) {
                h = Integer.parseInt(parts[0]) % 12;
                m = Integer.parseInt(parts[1]);
                s = Integer.parseInt(parts[2]);
            } else if (parts.length == 2) {
                m = Integer.parseInt(parts[0]);
                s = Integer.parseInt(parts[1]);
            } else if (parts.length == 1) {
                s = Integer.parseInt(parts[0]);
            } else {
                throw new IllegalArgumentException("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
            }
        } else {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
        }
        return new int[]{h, m, s};
    }

    public String encrypt(String data) {
        updateTimestamp();
        return encrypt(data, null);
    }

    public String encrypt(String data, Object timeKey) {
        if (timeKey != null) {
            int[] t = parseTime(timeKey);
            this.h = t[0];
            this.m = t[1];
            this.s = t[2];
        } else {
            updateTimestamp();
        }
        return encryptMessage(data);
    }

    public String decrypt(String encryptedMessage) {
        updateTimestamp();
        return decrypt(encryptedMessage, null);
    }

    public String decrypt(String encryptedMessage, Object timeKey) {
        if (timeKey != null) {
            int[] t = parseTime(timeKey);
            this.h = t[0];
            this.m = t[1];
            this.s = t[2];
        } else {
            updateTimestamp();
        }
        return decryptMessage(encryptedMessage);
    }

    private double[] getAngles() {
        double hourAngle = (30 * h) + (0.5 * m) + (0.5 / 60 * s);
        double minuteAngle = (6 * m) + (0.1 * s);
        double secondAngle = 6 * s;
        double angleHm = Math.min(Math.abs(hourAngle - minuteAngle), 360 - Math.abs(hourAngle - minuteAngle));
        double angleMs = Math.min(Math.abs(minuteAngle - secondAngle), 360 - Math.abs(minuteAngle - secondAngle));
        double angleHs = Math.min(Math.abs(hourAngle - secondAngle), 360 - Math.abs(hourAngle - secondAngle));
        return new double[]{angleHm, angleMs, angleHs};
    }

    private double selectTrig(double angle) {
        int quadrant = (int) (angle / 90) + 1;
        double rad = Math.toRadians(angle);
        switch (quadrant) {
            case 1:
                return Math.sin(rad);
            case 2:
                return Math.cos(rad);
            case 3:
                return Math.tan(rad);
            default:
                double tanVal = Math.tan(rad);
                return tanVal != 0 ? 1.0 / tanVal : 0;
        }
    }

    private String encryptMessage(String data) {
        double[] angles = getAngles();
        double angleHm = angles[0];
        double angleMs = angles[1];
        double angleHs = angles[2];
        StringBuilder buffer = new StringBuilder();
        for (int idx = 0; idx < data.length(); idx++) {
            int c = data.charAt(idx);
            int factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
            double offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1);
            buffer.append((char) ((c + Math.round(offset)) % 256));
        }
        return buffer.toString();
    }

    private String decryptMessage(String encryptedMessage) {
        double[] angles = getAngles();
        double angleHm = angles[0];
        double angleMs = angles[1];
        double angleHs = angles[2];
        StringBuilder buffer = new StringBuilder();
        for (int idx = 0; idx < encryptedMessage.length(); idx++) {
            int c = encryptedMessage.charAt(idx);
            int factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
            double offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1);
            buffer.append((char) ((c - Math.round(offset) + 256) % 256));
        }
        return buffer.toString();
    }
}
