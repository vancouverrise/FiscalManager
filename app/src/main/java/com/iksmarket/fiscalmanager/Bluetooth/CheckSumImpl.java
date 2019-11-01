package com.iksmarket.fiscalmanager.Bluetooth;

import java.util.Iterator;
import java.util.List;

public final class CheckSumImpl implements CheckSum {
    private final List<Byte> bytes;

    public CheckSumImpl(List<Byte> bytes) {
        this.bytes = bytes;
    }



    public Byte calculate() {
        int sum = 0;

        byte parameter;
        for(Iterator var2 = this.bytes.iterator(); var2.hasNext(); sum += parameter) {
            parameter = (Byte)var2.next();
        }

        byte lowByte = (byte)(sum & 255);
        parameter = (byte)(0 - lowByte);
        return new Byte(parameter);
    }
}