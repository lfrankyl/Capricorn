package de.franky.l.capricorn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by franky on 16.05.2017.
 */
public class Cpc_Utils_Helper_Test {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void MakeOutString() throws Exception {
        assertEquals(Cpc_Utils_Helper.MakeOutString(4444), "4.33");
    }

    @Test
    public void iCalcDataVal() throws Exception {
        assertEquals(Cpc_Utils_Helper.iCalcDataVal(4444), 4444);
        assertEquals(Cpc_Utils_Helper.iCalcDataVal(10000), 10);
        assertEquals(Cpc_Utils_Helper.iCalcDataVal(22222), 22);
        assertEquals(Cpc_Utils_Helper.iCalcDataVal(44444), 43);
        assertEquals(Cpc_Utils_Helper.iCalcDataVal(555550000), 530);
    }

    @Test
    public void iCalcDataUnitIndex() throws Exception {
        assertEquals(Cpc_Utils_Helper.iCalcDataUnitIndex(1024), 0);
        assertEquals(Cpc_Utils_Helper.iCalcDataUnitIndex(9999), 0);
        assertEquals(Cpc_Utils_Helper.iCalcDataUnitIndex(10000), 1);
        assertEquals(Cpc_Utils_Helper.iCalcDataUnitIndex(10241024), 2);
    }

    @Test
    public void lCalcDataValToByte() throws Exception {
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(1024, 0),       1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(1024, 1),       1024*1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(2431, 1),       2431*1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(  23, 2),         23*1024*1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(6544, 2), (long)6544*1024*1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte(1111, 3), (long)1111*1024*1024*1024);
        assertEquals(Cpc_Utils_Helper.lCalcDataValToByte( 855, 4), (long) 855*1024*1024*1024*1024);

    }

    @Test
    public void CalcUnit() throws Exception {
        assertEquals(Cpc_Utils_Helper.CalcUnit(-1023), "");
        assertEquals(Cpc_Utils_Helper.CalcUnit(1023), " Byte");
        assertEquals(Cpc_Utils_Helper.CalcUnit(1024), " KB");
        assertEquals(Cpc_Utils_Helper.CalcUnit(1000000), " KB");
        assertEquals(Cpc_Utils_Helper.CalcUnit(10000000), " MB");
        assertEquals(Cpc_Utils_Helper.CalcUnit(100000000), " MB");
        assertEquals(Cpc_Utils_Helper.CalcUnit((long) 1000* 1000 * 1000 * 1000), " GB");
        assertEquals(Cpc_Utils_Helper.CalcUnit((long) 1001* 1002 * 1003 * 1004), " GB");
        assertEquals(Cpc_Utils_Helper.CalcUnit((long) 1000* 1000 * 1000 * 1000 * 1000), " TB");
        assertEquals(Cpc_Utils_Helper.CalcUnit((long) 1005* 1006 * 1007 * 1008 * 1009), " TB");
    }

}