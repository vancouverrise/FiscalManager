package com.iksmarket.fiscalmanager.Bluetooth.Driver;

public enum WorkByte {
    DLE((byte) 0x10, 10),
    STX((byte) 0x02, 2),
    ETX((byte) 0x03, 3),
    ACK((byte) 0x06, 6),
    NAK((byte) 0x15, 15),
    SYN((byte) 0x16, 16),
    ENQ((byte) 0x05, 5),
    CS ((byte) 0x00, 0),
    COD((byte) 0x00, 0),
    NOM((byte) 0x00, 0);

    private final byte value;
    private final int decimal;

    WorkByte(byte value, int decimal) {
        this.value = value;
        this.decimal = decimal;
    }

    public byte getValue() {
        return value;
    }

    public int getDecimal() {
        return decimal;
    }
}