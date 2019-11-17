package de.franky.l.capricorn;

/**
 * Created by franky on 06.07.2016.
 * Stores the data for the widget elements to fill them via loop
 */
class Cpc_DispData
{

    private int iIcon;											// Speichert die R-Werte der Icons im Array um per Index zugreifen zu koennen
    private String  sValue;                                     // Speichert den Anzeigewert fürs Widget
    private String	sUnit;                                      // Speichert den Anzeigewert für die Einheit fürs Widget
    private double dValue;                                      // Speichert den Wert als Zahl für weitere Berechnungnen

    Cpc_DispData ()
    {
        iIcon = 12343;
        sValue = "tbd";
        sUnit =  "tbd";
        dValue = 0;
    }

    public void setIcon(int IconId)
    {
        iIcon = IconId;
    }
    public void setValue(String ValueString)
    {
        sValue = ValueString;
    }

    public void setUnit(String UnitString)
    {
        sUnit = UnitString;
    }

    public int getIcon()
    {
        return iIcon;
    }

    public String getValue()
    {
        return sValue;
    }

    public String getUnit()
    {
        return sUnit;
    }

    public void setNumber(double Value)
    {
        dValue = Value;
    }

    public double getNumber()
    {
        return dValue;
    }
}
