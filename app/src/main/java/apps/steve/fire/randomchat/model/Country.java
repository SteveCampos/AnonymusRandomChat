package apps.steve.fire.randomchat.model;

import android.content.Context;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import apps.steve.fire.randomchat.R;

/**
 * Created by Steve on 7/10/2016.
 */

public class Country {

    public final static int AFGANISTAN = 1;
    public final static int AKROTIRI = 2;
    public final static int ALBANIA = 3;
    public static final int ALEMANIA = 4;
    public static final int ANDORRA = 5;
    public static final int ANGOLA = 6;
    public static final int ANGUILA = 7;
    public static final int ANTARTIDA= 8;
    public static final int ANTIGUA_Y_BARBUDA = 9;
    public static final int ANTILLAS_NEERLANDESAS = 10;
    public static final int ARABIA_SAUDI = 11;
    public static final int ARCTIC_OCEAN = 12;
    public static final int ARGELIA = 13;
    public static final int ARGENTINA = 14;
    public static final int ARMENIA = 15;
    public static final int ARUBA = 16;
    public static final int ASHMORE = 17;
    public static final int OCEANO_ATLANTICO = 18;
    public static final int AUSTRALIA = 19;
    public static final int AUSTRIA = 20;
    public static final int AZERBAIYAN = 21;
    public static final int BAHAMAS = 22;
    public static final int BAHRAIN = 23;
    public static final int BANGLADESH = 24;
    public static final int BARBADOS = 25;
    public static final int BELGICA = 26;
    public static final int BELICE = 27;
    public static final int BENIN = 28;
    public static final int BERMUDAS = 29;
    public static final int BIELORRUSIA = 30;
    public static final int BIRMANIA= 31;
    public static final int BOLIVIA = 32;
    public static final int BOSNIA_HERCEGOVINA = 33;
    public static final int BOTSUANA = 34;
    public static final int BRASIL = 35;
    public static final int BRUNEI = 36;
    public static final int BULGARIA = 37;
    public static final int BURKINA_FASO = 38;
    public static final int BURUNDI = 39;
    public static final int BUTAN = 40;
    public static final int CABO_VERDE = 41;
    public static final int CAMBOYA = 42;
    public static final int CAMERUN = 43;
    public static final int CANADA = 44;
    public static final int CHAD = 45;
    public static final int CHILE = 46;
    public static final int CHINA = 47;
    public static final int CHIPRE = 48;
    public static final int ISLA_CLIPPERTON = 49;
    public static final int COLOMBIA = 50;
    public static final int COMORAS = 51;
    public static final int CONGO = 52;
    public static final int ISLA_MAR_CORAL = 53;
    public static final int COREA_DEL_NORTE = 54;
    public static final int COREA_DEL_SUR = 55;
    public static final int COSTA_DE_MARFIL = 56;
    public static final int COSTA_RICA = 57;
    public static final int CROACIA = 58;
    public static final int CUBA = 59;
    public static final int DHEKELIA = 60;
    public static final int DINAMARCA = 61;
    public static final int DOMINICA = 62;
    public static final int ECUADOR = 63;
    public static final int EGIPTO = 64;
    public static final int EL_SALVADOR = 65;
    public static final int EL_VATICANO = 66;
    public static final int EMIRATOS_ARABES = 67;
    public static final int ERITREA = 68;
    public static final int ESLOVAQUIA = 69;
    public static final int ESLOVENIA = 70;
    public static final int ESPANA = 71;
    public static final int ESTADOS_UNIDOS = 72;
    public static final int ESTONIA = 73;
    public static final int ETIOPIA = 74;
    public static final int FILIPINAS = 75;
    public static final int FINLANDIA = 76;
    public static final int FIYI = 77;
    public static final int FRANCIA = 78;
    public static final int GABON = 79;
    public static final int GAMBIA = 80;
    public static final int GAZA_STRIP = 81;
    public static final int GEORGIA = 82;
    public static final int GHANA = 83;
    public static final int GIBRALTAR = 84;
    public static final int GRANADA = 85;
    public static final int GRECIA = 86;
    public static final int GROENLANDIA = 87;
    public static final int GUAM = 88;
    public static final int GUATEMALA = 89;
    public static final int GUERNSEY = 90;
    public static final int GUINEA = 91;
    public static final int GUINEA_ECUATORIAL = 92;
    public static final int GUINEA_BISSAU = 93;
    public static final int GUYANA = 94;
    public static final int HAITI = 95;
    public static final int HONDURAS = 96;
    public static final int HONG_KONG = 97;
    public static final int HUNGRIA = 98;
    public static final int INDIA= 99;


    private String nameID;
    private String name;
    private int drawableID;
    private int countryID;

    public Country() {
    }

    public Country(Context context, int countryID) {
        this.countryID = countryID;
        String n = "";
        int d = R.drawable.ic_peru;
        String i = "ALL";
        switch (countryID){
            case AUSTRALIA:
                n = context.getString(R.string.country_australia);
                d = R.drawable.ic_australia;
                i = "AUSTRALIA";
                break;

            default:
                break;
        }
        this.name = n;
        this.drawableID = d;
        this.nameID = i;
    }

    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("countryID", countryID);
        return result;
    }
}
