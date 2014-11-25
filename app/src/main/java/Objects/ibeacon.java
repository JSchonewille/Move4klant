package Objects;

import android.graphics.Color;

/**
 * Created by Jeff on 22-10-2014.
 */
public class ibeacon {
    private int major;
    private int minor;
    private int beaconID;
    private int productID;
    private int offerID;
    private int kleur = Color.WHITE;
    private int counter = 0;
    private int seconds = 0;
    private boolean shown = false;
   // Qrmaker qmaker;
   // private Bitmap Qr;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getOfferID() {
        return offerID;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public ibeacon(int beaconID, int offerID, int productID, int majorid, int minorid) {
        major = majorid;
        minor = minorid;
        this.beaconID=beaconID;
        this.productID = productID;
        this.offerID=offerID;
       // qmaker = new Qrmaker();
       // try
      //  {
       //     Qr = qmaker.encode(major + " - " + minor );
      //  } catch (WriterException e) {
       //     e.printStackTrace();
      //  }


    }


    public boolean isBeacon(int majorvalue, int minorvalue) {
        if (majorvalue == major && minorvalue == minor) {
            return true;
        } else {
            return false;
        }
    }

    public void setKleur(int kleur) {
        this.kleur = kleur;
    }

    public int getKleur() {
        return kleur;
    }

    public void Counterup() {
        counter++;
        if (counter % 10 == 0) {
            seconds++;
        }

    }

    public int getSeconds() {
        return seconds;
    }
/*
    public Bitmap getQr()
    {
        return Qr;
    }

    */

    public void setShown()
    {
        shown = true;
    }

    public boolean getShown()
    {
        return shown;
    }


}
