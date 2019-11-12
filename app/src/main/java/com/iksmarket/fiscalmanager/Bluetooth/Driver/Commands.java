package com.iksmarket.fiscalmanager.Bluetooth.Driver;

import android.content.Intent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Commands {

    private static short COMMAND_COUNTER = 1;

    public static List<Byte> sendToPrinter(int commandNumber, List<Byte> parameters) {
        List<Byte> commandToSend = new ArrayList<>();
        commandToSend.add(WorkByte.DLE.getValue());
        commandToSend.add(WorkByte.STX.getValue());
        commandToSend.add((byte) COMMAND_COUNTER++);
        commandToSend.add((byte) commandNumber);

        if (parameters != null) {
            commandToSend.addAll(createParametersArray(parameters));
        }
        System.out.println("great!");
        commandToSend.add((byte) 0x00);
        commandToSend.add(WorkByte.DLE.getValue());
        commandToSend.add(WorkByte.ETX.getValue());

        commandToSend.set(commandToSend.size() - 3, getCheckSum(commandToSend));

        int listCurrentSize = commandToSend.size();
        for (int pos = 2; pos < listCurrentSize - 2; pos++) {
            if (commandToSend.get(pos).equals(WorkByte.DLE.getValue())) {
                commandToSend.add(pos + 1, WorkByte.DLE.getValue());
                pos++;
                listCurrentSize++;
            }
        }
        return commandToSend;
    }


    public static List<Byte> printVer() {
        System.out.println(COMMAND_COUNTER);
        return sendToPrinter(32, null);
    }

    public static List<Byte> dayClearReport(long password) {
        List<Byte> param = new ArrayList<>();
        for (byte b : getBytes(password, 2))
            param.add(b);
        return sendToPrinter(13, param);
    }



    public static byte[] getBytes(long v, int b) {
        byte[] writeBuffer = new byte[b];
        if (b == 6) {
            writeBuffer[5] = (byte) ((v >>> 40) & 0xFF);
            writeBuffer[4] = (byte) ((v >>> 32) & 0xFF);
            writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
            writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
            writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
            writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
        }
        if (b == 4) {
            writeBuffer[3] = (byte) ((v >>> 24) & 0xFF);
            writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
            writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
            writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
        }
        if (b == 3) {
            writeBuffer[2] = (byte) ((v >>> 16) & 0xFF);
            writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
            writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
        }
        if (b == 2) {
            writeBuffer[1] = (byte) ((v >>> 8) & 0xFF);
            writeBuffer[0] = (byte) ((v >>> 0) & 0xFF);
        }
        return writeBuffer;
    }

    public static List<Byte> createParametersArray(List<Byte> in) {
        List<Byte> out = new ArrayList<Byte>();

        for (Byte el : in) {
            out.add(el);
        }

        return out;
    }

    public static byte getCheckSum(List<Byte> buf) {
        int i, n;
        byte lobyte, cs;
        long sum, res;

        n = buf.size() - 3;
        sum = 0;
        cs = 0x00;
        lobyte = 0x00;

        for (i = 2; i < n; i++)
            sum += buf.get(i);

        do {
            res = sum + cs;
            cs++;
            lobyte = (byte) (res & 0xFF);
        }
        while (lobyte != 0x00);

        return (byte) (cs - 1);
    }

}
