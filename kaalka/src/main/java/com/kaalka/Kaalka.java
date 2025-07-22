package com.kaalka;


import java.util.Calendar;

public class Kaalka {
    // Encrypt a file (any type)
    public String encryptFile(String filePath, Object timeKey) throws Exception {
        if (timeKey != null) {
            int[] t = parseTime(timeKey);
            this.h = t[0];
            this.m = t[1];
            this.s = t[2];
        } else {
            updateTimestamp();
        }
        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) throw new IllegalArgumentException("File does not exist: " + filePath);
        String ext = filePath.substring(filePath.lastIndexOf('.'));
        byte[] raw = java.nio.file.Files.readAllBytes(file.toPath());
        byte[] encBytes = proc(raw, true);
        byte[] extBytes = ext.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte extLen = (byte) extBytes.length;
        byte[] finalBytes = new byte[1 + extBytes.length + encBytes.length];
        finalBytes[0] = extLen;
        System.arraycopy(extBytes, 0, finalBytes, 1, extBytes.length);
        System.arraycopy(encBytes, 0, finalBytes, 1 + extBytes.length, encBytes.length);
        String base = filePath.substring(0, filePath.length() - ext.length());
        String outFile = base + ".kaalka";
        java.nio.file.Files.write(java.nio.file.Paths.get(outFile), finalBytes);
        return outFile;
    }

    // Decrypt a file (any type)
    public String decryptFile(String filePath, Object timeKey) throws Exception {
        if (timeKey != null) {
            int[] t = parseTime(timeKey);
            this.h = t[0];
            this.m = t[1];
            this.s = t[2];
        } else {
            updateTimestamp();
        }
        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) throw new IllegalArgumentException("File does not exist: " + filePath);
        byte[] buf = java.nio.file.Files.readAllBytes(file.toPath());
        if (buf.length < 2) throw new IllegalArgumentException("File is too small or corrupted for decryption.");
        int extLen = buf[0];
        String ext = new String(buf, 1, extLen, java.nio.charset.StandardCharsets.UTF_8);
        byte[] encData = java.util.Arrays.copyOfRange(buf, 1 + extLen, buf.length);
        byte[] decBytes = proc(encData, false);
        String base = filePath.substring(0, filePath.length() - ".kaalka".length());
        String outFile = base + ext;
        java.nio.file.Files.write(java.nio.file.Paths.get(outFile), decBytes);
        return outFile;
    }

    // Core byte encryption/decryption logic
    private byte[] proc(byte[] data, boolean encrypt) {
        double[] angles = getAngles();
        byte[] result = new byte[data.length];
        for (int idx = 0; idx < data.length; idx++) {
            int b = data[idx] & 0xFF;
            int factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
            double offset = (selectTrig(angles[0]) + selectTrig(angles[1]) + selectTrig(angles[2])) * factor + (idx + 1);
            int val;
            if (encrypt) {
                val = (b + (int) Math.round(offset)) % 256;
            } else {
                val = (b - (int) Math.round(offset) + 256) % 256;
            }
            result[idx] = (byte) val;
        }
        return result;
    }
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
            buffer.append((char) ((c + (int) Math.round(offset)) % 256));
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
            buffer.append((char) ((c - (int) Math.round(offset) + 256) % 256));
        }
        return buffer.toString();
    }
}
