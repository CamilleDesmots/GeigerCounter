/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.time.LocalDateTime;

/**
 *
 * @author camilledesmots
 */
public interface GQGMCInterface {

    /**
     *
     * @return Return 7 bytes firmware version, for example "GMC-300".
     */
    public String getModelNumber();

    /**
     *
     * @return Return 7 bytes revision for example "Re 2.11".
     */
    public String getRevision();

    /**
     *
     * @return Return a total of 14 bytes of chars for model number and firmware
     * revsion, for example "GMC-300Re2.11"
     */
    public String getVersion();

    /**
     *
     * @return Return serial number of the device on 14 digit
     */
    public String getSerialNumber();

    /**
     *
     * @return Return value of count per minute
     */
    public int getCPM();

    /**
     *
     * @return Return the count per seconds
     */
    public int getCPS();

    /**
     *
     * @return Return voltage value of battery (X 10V) .g.: return 62(hex) is
     * 9.8V
     */
    public float getVoltage();

    /**
     *
     * @param localDate Date and time to set the hardware
     */
    public void setRealtime(LocalDateTime localDate);

    /**
     *
     * @param vendorId Vendor ID obtained thanks to lsub
     * @param productId Produc ID obtained thanks to lsub
     * @return True if the device is compatible with the implementation
     */
    public boolean checkUSBId(short vendorId, short productId);

    public void setHeartBeatOn();

    public void setHeartBeatOff();

    public boolean getHeartBeatStatus();

    /**
     *
     * @return true is Serial connection is open
     */
    public boolean getSerialStatus();

    public void setSerialOpen();

    public boolean isEmulation();

    public int getIdleTime();

    public void setIdleTime(int newIdleTime);

    public float getTemp();

}
