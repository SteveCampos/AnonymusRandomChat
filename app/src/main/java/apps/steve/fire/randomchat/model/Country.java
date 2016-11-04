package apps.steve.fire.randomchat.model;

import android.content.Context;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.steve.fire.randomchat.R;

/**
 * Created by Steve on 7/10/2016.
 */

public class Country {

    public static final int ABKHAZIA = 1;
    public static final int AFGHANISTAN = 2;
    public static final int ALAND_ISLANDS = 3;
    public static final int ALBANIA = 4;
    public static final int ALGERIA = 5;
    public static final int AMERICAN_SAMOA = 6;
    public static final int ANDORRA = 7;
    public static final int ANGOLA = 8;
    public static final int ANGUILLA = 9;
    public static final int ANTIGUA_AND_BARBUDA = 10;
    public static final int ARGENTINA = 11;
    public static final int ARMENIA = 12;
    public static final int ARUBA = 13;
    public static final int AUSTRALIA = 14;
    public static final int AUSTRIA = 15;
    public static final int AZERBAIJAN = 16;
    public static final int AZORES_ISLANDS = 17;
    public static final int BAHAMAS = 18;
    public static final int BAHRAIN = 19;
    public static final int BALEARIC_ISLANDS = 20;
    public static final int BANGLADESH = 21;
    public static final int BARBADOS = 22;
    public static final int BASQUE_COUNTRY = 23;
    public static final int BELARUS = 24;
    public static final int BELGIUM = 25;
    public static final int BELIZE = 26;
    public static final int BENIN = 27;
    public static final int BERMUDA = 28;
    public static final int BHUTAN = 29;
    public static final int BOLIVIA = 30;
    public static final int BONAIRE = 31;
    public static final int BOSNIA_AND_HERZEGOVINA = 32;
    public static final int BOTSWANA = 33;
    public static final int BRAZIL = 34;
    public static final int BRITISH_COLUMBIA = 35;
    public static final int BRITISH_INDIAN_OCEAN_TERRITORY = 36;
    public static final int BRITISH_VIRGIN_ISLANDS = 37;
    public static final int BRUNEI = 38;
    public static final int BULGARIA = 39;
    public static final int BURKINA_FASO = 40;
    public static final int BURUNDI = 41;
    public static final int CAMBODIA = 42;
    public static final int CAMEROON = 43;
    public static final int CANADA = 44;
    public static final int CANARY_ISLANDS = 45;
    public static final int CAPE_VERDE = 46;
    public static final int CAYMAN_ISLANDS = 47;
    public static final int CENTRAL_AFRICAN_REPUBLIC = 48;
    public static final int CEUTA = 49;
    public static final int CHAD = 50;
    public static final int CHILE = 51;
    public static final int CHINA = 52;
    public static final int CHRISTMAS_ISLAND = 53;
    public static final int COCOS_ISLAND = 54;
    public static final int COLOMBIA = 55;
    public static final int COMOROS = 56;
    public static final int COOK_ISLANDS = 57;
    public static final int CORSICA = 58;
    public static final int COSTA_RICA = 59;
    public static final int CROATIA = 60;
    public static final int CUBA = 61;
    public static final int CURACAO = 62;
    public static final int CYPRUS = 63;
    public static final int CZECH_REPUBLIC = 64;
    public static final int DEMOCRATIC_REPUBLIC_OF_CONGO = 65;
    public static final int DENMARK = 66;
    public static final int DJIBOUTI = 67;
    public static final int DOMINICA = 68;
    public static final int DOMINICAN_REPUBLIC = 69;
    public static final int EAST_TIMOR = 70;
    public static final int ECUADOR = 71;
    public static final int EGYPT = 72;
    public static final int EL_SALVADOR = 73;
    public static final int ENGLAND = 74;
    public static final int EQUATORIAL_GUINEA = 75;
    public static final int ERITREA = 76;
    public static final int ESTONIA = 77;
    public static final int ETHIOPIA = 78;
    public static final int EUROPEAN_UNION = 79;
    public static final int FALKLAND_ISLANDS = 80;
    public static final int FAROE_ISLANDS = 81;
    public static final int FIJI = 82;
    public static final int FINLAND = 83;
    public static final int FRANCE = 84;
    public static final int FRENCH_POLYNESIA = 85;
    public static final int GABON = 86;
    public static final int GALAPAGOS_ISLANDS = 87;
    public static final int GAMBIA = 88;
    public static final int GEORGIA = 89;
    public static final int GERMANY = 90;
    public static final int GHANA = 91;
    public static final int GIBRALTAR = 92;
    public static final int GREECE = 93;
    public static final int GREENLAND = 94;
    public static final int GRENADA = 95;
    public static final int GUAM = 96;
    public static final int GUATEMALA = 97;
    public static final int GUERNSEY = 98;
    public static final int GUINEA_BISSAU = 99;
    public static final int GUINEA = 100;
    public static final int HAITI = 101;
    public static final int HAWAII = 102;
    public static final int HONDURAS = 103;
    public static final int HONG_KONG = 104;
    public static final int HUNGARY = 105;
    public static final int ICELAND = 106;
    public static final int INDIA = 107;
    public static final int INDONESIA = 108;
    public static final int IRAN = 109;
    public static final int IRAQ = 110;
    public static final int IRELAND = 111;
    public static final int ISLE_OF_MAN = 112;
    public static final int ISRAEL = 113;
    public static final int ITALY = 114;
    public static final int IVORY_COAST = 115;
    public static final int JAMAICA = 116;
    public static final int JAPAN = 117;
    public static final int JERSEY = 118;
    public static final int JORDAN = 119;
    public static final int KAZAKHSTAN = 120;
    public static final int KENYA = 121;
    public static final int KIRIBATI = 122;
    public static final int KOSOVO = 123;
    public static final int KWAIT = 124;
    public static final int KYRGYZSTAN = 125;
    public static final int LAOS = 126;
    public static final int LATVIA = 127;
    public static final int LEBANON = 128;
    public static final int LESOTHO = 129;
    public static final int LIBERIA = 130;
    public static final int LIBYA = 131;
    public static final int LIECHTENSTEIN = 132;
    public static final int LITHUANIA = 133;
    public static final int LUXEMBOURG = 134;
    public static final int MACAO = 135;
    public static final int MADAGASCAR = 136;
    public static final int MADEIRA = 137;
    public static final int MALASYA = 138;
    public static final int MALAWI = 139;
    public static final int MALDIVES = 140;
    public static final int MALI = 141;
    public static final int MALTA = 142;
    public static final int MARSHALL_ISLAND = 143;
    public static final int MARTINIQUE = 144;
    public static final int MAURITANIA = 145;
    public static final int MAURITIUS = 146;
    public static final int MELILLA = 147;
    public static final int MEXICO = 148;
    public static final int MICRONESIA = 149;
    public static final int MOLDOVA = 150;
    public static final int MONACO = 151;
    public static final int MONGOLIA = 152;
    public static final int MONTENEGRO = 153;
    public static final int MONTSERRAT = 154;
    public static final int MOROCCO = 155;
    public static final int MOZAMBIQUE = 156;
    public static final int MYANMAR = 157;
    public static final int NAMIBIA = 158;
    public static final int NATO = 159;
    public static final int NAURU = 160;
    public static final int NEPAL = 161;
    public static final int NETHERLANDS = 162;
    public static final int NEW_ZEALAND = 163;
    public static final int NICARAGUA = 164;
    public static final int NIGER = 165;
    public static final int NIGERIA = 166;
    public static final int NIUE = 167;
    public static final int NORFOLK_ISLAND = 168;
    public static final int NORTH_KOREA = 169;
    public static final int NORTHERN_CYPRUS = 170;
    public static final int NORTHERN_MARIANAS_ISLANDS = 171;
    public static final int NORWAY = 172;
    public static final int OMAN = 173;
    public static final int OSSETIA = 174;
    public static final int PAKISTAN = 175;
    public static final int PALAU = 176;
    public static final int PALESTINE = 177;
    public static final int PANAMA = 178;
    public static final int PAPUA_NEW_GUINEA = 179;
    public static final int PARAGUAY = 180;
    public static final int PERU = 181;
    public static final int PHILIPPINES = 182;
    public static final int PITCAIRN_ISLANDS = 183;
    public static final int POLAND = 184;
    public static final int PORTUGAL = 185;
    public static final int PUERTO_RICO = 186;
    public static final int QATAR = 187;
    public static final int RAPA_NUI = 188;
    public static final int REPUBLIC_OF_MACEDONIA = 189;
    public static final int REPUBLIC_OF_THE_CONGO = 190;
    public static final int ROMANIA = 191;
    public static final int RUSSIA = 192;
    public static final int RWANDA = 193;
    public static final int SABA_ISLAND = 194;
    public static final int SAHRAWI_ARAB_DEMOCRATIC_REPUBLIC = 195;
    public static final int SAINT_KITTS_AND_NEVIS = 196;
    public static final int SAMOA = 197;
    public static final int SAN_MARINO = 198;
    public static final int SAO_TOME_AND_PRINCE = 199;
    public static final int SARDINIA = 200;
    public static final int SAUDI_ARABIA = 201;
    public static final int SCOTLAND = 202;
    public static final int SENEGAL = 203;
    public static final int SERBIA = 204;
    public static final int SEYCHELLES = 205;
    public static final int SICILY = 206;
    public static final int SIERRA_LEONE = 207;
    public static final int SINGAPORE = 208;
    public static final int SINT_EUSTATIUS = 209;
    public static final int SINT_MAARTEN = 210;
    public static final int SLOVAKIA = 211;
    public static final int SLOVENIA = 212;
    public static final int SOLOMON_ISLANDS = 213;
    public static final int SOMALIA = 214;
    public static final int SOMALILAND = 215;
    public static final int SOUTH_AFRICA = 216;
    public static final int SOUTH_KOREA = 217;
    public static final int SOUTH_SUDAN = 218;
    public static final int SPAIN = 219;
    public static final int SRI_LANKA = 220;
    public static final int ST_BARTS = 221;
    public static final int ST_LUCIA = 222;
    public static final int ST_VINCENT_AND_THE_GRENADINES = 223;
    public static final int SUDAN = 224;
    public static final int SURINAME = 225;
    public static final int SWAZILAND = 226;
    public static final int SWEDEN = 227;
    public static final int SWITZERLAND = 228;
    public static final int SYRIA = 229;
    public static final int TAIWAN = 230;
    public static final int TAJIKISTAN = 231;
    public static final int TANZANIA = 232;
    public static final int THAILAND = 233;
    public static final int TIBET = 234;
    public static final int TOGO = 235;
    public static final int TOKELAU = 236;
    public static final int TONGA = 237;
    public static final int TRANSNISTRIA = 238;
    public static final int TRINIDAD_AND_TOBAGO = 239;
    public static final int TUNISIA = 240;
    public static final int TURKEY = 241;
    public static final int TURKMENISTAN = 242;
    public static final int TURKS_AND_CAICOS = 243;
    public static final int TUVALU_1 = 244;
    public static final int TUVALU = 245;
    public static final int UGANDA = 246;
    public static final int UKRAINE = 247;
    public static final int UNITED_ARAB_EMIRATES = 248;
    public static final int UNITED_KINGDOM = 249;
    public static final int UNITED_NATIONS = 250;
    public static final int UNITED_STATES = 251;
    public static final int URUGUAY = 252;
    public static final int UZBEKISTN = 253;
    public static final int VANUATU = 254;
    public static final int VATICAN_CITY = 255;
    public static final int VENEZUELA = 256;
    public static final int VIETNAM = 257;
    public static final int VIRGIN_ISLANDS = 258;
    public static final int WALES = 259;
    public static final int YEMEN = 260;
    public static final int ZAMBIA = 261;
    public static final int ZIMBABWE = 262;

    private String nameID;
    private String name;
    private int drawableID;
    private int countryID;

    private Context context;

    public Country() {
    }

    public Country(Context context, int countryID) {
        this.countryID = countryID;
        String n = "";
        int d = R.drawable.ic_peru;
        String i = "ALL";
        switch (countryID){
            case ABKHAZIA:
                n = context.getString(R.string.country_abkhazia);
                d = R.drawable.ic_abkhazia;
                i = "ABKHAZIA";
                break;

            case AFGHANISTAN:
                n = context.getString(R.string.country_afghanistan);
                d = R.drawable.ic_afghanistan;
                i = "AFGHANISTAN";
                break;

            case ALAND_ISLANDS:
                n = context.getString(R.string.country_aland_islands);
                d = R.drawable.ic_aland_islands;
                i = "ALAND_ISLANDS";
                break;

            case ALBANIA:
                n = context.getString(R.string.country_albania);
                d = R.drawable.ic_albania;
                i = "ALBANIA";
                break;

            case ALGERIA:
                n = context.getString(R.string.country_algeria);
                d = R.drawable.ic_algeria;
                i = "ALGERIA";
                break;

            case AMERICAN_SAMOA:
                n = context.getString(R.string.country_american_samoa);
                d = R.drawable.ic_american_samoa;
                i = "AMERICAN_SAMOA";
                break;

            case ANDORRA:
                n = context.getString(R.string.country_andorra);
                d = R.drawable.ic_andorra;
                i = "ANDORRA";
                break;

            case ANGOLA:
                n = context.getString(R.string.country_angola);
                d = R.drawable.ic_angola;
                i = "ANGOLA";
                break;

            case ANGUILLA:
                n = context.getString(R.string.country_anguilla);
                d = R.drawable.ic_anguilla;
                i = "ANGUILLA";
                break;

            case ANTIGUA_AND_BARBUDA:
                n = context.getString(R.string.country_antigua_and_barbuda);
                d = R.drawable.ic_antigua_and_barbuda;
                i = "ANTIGUA_AND_BARBUDA";
                break;

            case ARGENTINA:
                n = context.getString(R.string.country_argentina);
                d = R.drawable.ic_argentina;
                i = "ARGENTINA";
                break;

            case ARMENIA:
                n = context.getString(R.string.country_armenia);
                d = R.drawable.ic_armenia;
                i = "ARMENIA";
                break;

            case ARUBA:
                n = context.getString(R.string.country_aruba);
                d = R.drawable.ic_aruba;
                i = "ARUBA";
                break;

            case AUSTRALIA:
                n = context.getString(R.string.country_australia);
                d = R.drawable.ic_australia;
                i = "AUSTRALIA";
                break;

            case AUSTRIA:
                n = context.getString(R.string.country_austria);
                d = R.drawable.ic_austria;
                i = "AUSTRIA";
                break;

            case AZERBAIJAN:
                n = context.getString(R.string.country_azerbaijan);
                d = R.drawable.ic_azerbaijan;
                i = "AZERBAIJAN";
                break;

            case AZORES_ISLANDS:
                n = context.getString(R.string.country_azores_islands);
                d = R.drawable.ic_azores_islands;
                i = "AZORES_ISLANDS";
                break;

            case BAHAMAS:
                n = context.getString(R.string.country_bahamas);
                d = R.drawable.ic_bahamas;
                i = "BAHAMAS";
                break;

            case BAHRAIN:
                n = context.getString(R.string.country_bahrain);
                d = R.drawable.ic_bahrain;
                i = "BAHRAIN";
                break;

            case BALEARIC_ISLANDS:
                n = context.getString(R.string.country_balearic_islands);
                d = R.drawable.ic_balearic_islands;
                i = "BALEARIC_ISLANDS";
                break;

            case BANGLADESH:
                n = context.getString(R.string.country_bangladesh);
                d = R.drawable.ic_bangladesh;
                i = "BANGLADESH";
                break;

            case BARBADOS:
                n = context.getString(R.string.country_barbados);
                d = R.drawable.ic_barbados;
                i = "BARBADOS";
                break;

            case BASQUE_COUNTRY:
                n = context.getString(R.string.country_basque_country);
                d = R.drawable.ic_basque_country;
                i = "BASQUE_COUNTRY";
                break;

            case BELARUS:
                n = context.getString(R.string.country_belarus);
                d = R.drawable.ic_belarus;
                i = "BELARUS";
                break;

            case BELGIUM:
                n = context.getString(R.string.country_belgium);
                d = R.drawable.ic_belgium;
                i = "BELGIUM";
                break;

            case BELIZE:
                n = context.getString(R.string.country_belize);
                d = R.drawable.ic_belize;
                i = "BELIZE";
                break;

            case BENIN:
                n = context.getString(R.string.country_benin);
                d = R.drawable.ic_benin;
                i = "BENIN";
                break;

            case BERMUDA:
                n = context.getString(R.string.country_bermuda);
                d = R.drawable.ic_bermuda;
                i = "BERMUDA";
                break;

            case BHUTAN:
                n = context.getString(R.string.country_bhutan);
                d = R.drawable.ic_bhutan;
                i = "BHUTAN";
                break;

            case BOLIVIA:
                n = context.getString(R.string.country_bolivia);
                d = R.drawable.ic_bolivia;
                i = "BOLIVIA";
                break;

            case BONAIRE:
                n = context.getString(R.string.country_bonaire);
                d = R.drawable.ic_bonaire;
                i = "BONAIRE";
                break;

            case BOSNIA_AND_HERZEGOVINA:
                n = context.getString(R.string.country_bosnia_and_herzegovina);
                d = R.drawable.ic_bosnia_and_herzegovina;
                i = "BOSNIA_AND_HERZEGOVINA";
                break;

            case BOTSWANA:
                n = context.getString(R.string.country_botswana);
                d = R.drawable.ic_botswana;
                i = "BOTSWANA";
                break;

            case BRAZIL:
                n = context.getString(R.string.country_brazil);
                d = R.drawable.ic_brazil;
                i = "BRAZIL";
                break;

            case BRITISH_COLUMBIA:
                n = context.getString(R.string.country_british_columbia);
                d = R.drawable.ic_british_columbia;
                i = "BRITISH_COLUMBIA";
                break;

            case BRITISH_INDIAN_OCEAN_TERRITORY:
                n = context.getString(R.string.country_british_indian_ocean_territory);
                d = R.drawable.ic_british_indian_ocean_territory;
                i = "BRITISH_INDIAN_OCEAN_TERRITORY";
                break;

            case BRITISH_VIRGIN_ISLANDS:
                n = context.getString(R.string.country_british_virgin_islands);
                d = R.drawable.ic_british_virgin_islands;
                i = "BRITISH_VIRGIN_ISLANDS";
                break;

            case BRUNEI:
                n = context.getString(R.string.country_brunei);
                d = R.drawable.ic_brunei;
                i = "BRUNEI";
                break;

            case BULGARIA:
                n = context.getString(R.string.country_bulgaria);
                d = R.drawable.ic_bulgaria;
                i = "BULGARIA";
                break;

            case BURKINA_FASO:
                n = context.getString(R.string.country_burkina_faso);
                d = R.drawable.ic_burkina_faso;
                i = "BURKINA_FASO";
                break;

            case BURUNDI:
                n = context.getString(R.string.country_burundi);
                d = R.drawable.ic_burundi;
                i = "BURUNDI";
                break;

            case CAMBODIA:
                n = context.getString(R.string.country_cambodia);
                d = R.drawable.ic_cambodia;
                i = "CAMBODIA";
                break;

            case CAMEROON:
                n = context.getString(R.string.country_cameroon);
                d = R.drawable.ic_cameroon;
                i = "CAMEROON";
                break;

            case CANADA:
                n = context.getString(R.string.country_canada);
                d = R.drawable.ic_canada;
                i = "CANADA";
                break;

            case CANARY_ISLANDS:
                n = context.getString(R.string.country_canary_islands);
                d = R.drawable.ic_canary_islands;
                i = "CANARY_ISLANDS";
                break;

            case CAPE_VERDE:
                n = context.getString(R.string.country_cape_verde);
                d = R.drawable.ic_cape_verde;
                i = "CAPE_VERDE";
                break;

            case CAYMAN_ISLANDS:
                n = context.getString(R.string.country_cayman_islands);
                d = R.drawable.ic_cayman_islands;
                i = "CAYMAN_ISLANDS";
                break;

            case CENTRAL_AFRICAN_REPUBLIC:
                n = context.getString(R.string.country_central_african_republic);
                d = R.drawable.ic_central_african_republic;
                i = "CENTRAL_AFRICAN_REPUBLIC";
                break;

            case CEUTA:
                n = context.getString(R.string.country_ceuta);
                d = R.drawable.ic_ceuta;
                i = "CEUTA";
                break;

            case CHAD:
                n = context.getString(R.string.country_chad);
                d = R.drawable.ic_chad;
                i = "CHAD";
                break;

            case CHILE:
                n = context.getString(R.string.country_chile);
                d = R.drawable.ic_chile;
                i = "CHILE";
                break;

            case CHINA:
                n = context.getString(R.string.country_china);
                d = R.drawable.ic_china;
                i = "CHINA";
                break;

            case CHRISTMAS_ISLAND:
                n = context.getString(R.string.country_christmas_island);
                d = R.drawable.ic_christmas_island;
                i = "CHRISTMAS_ISLAND";
                break;

            case COCOS_ISLAND:
                n = context.getString(R.string.country_cocos_island);
                d = R.drawable.ic_cocos_island;
                i = "COCOS_ISLAND";
                break;

            case COLOMBIA:
                n = context.getString(R.string.country_colombia);
                d = R.drawable.ic_colombia;
                i = "COLOMBIA";
                break;

            case COMOROS:
                n = context.getString(R.string.country_comoros);
                d = R.drawable.ic_comoros;
                i = "COMOROS";
                break;

            case COOK_ISLANDS:
                n = context.getString(R.string.country_cook_islands);
                d = R.drawable.ic_cook_islands;
                i = "COOK_ISLANDS";
                break;

            case CORSICA:
                n = context.getString(R.string.country_corsica);
                d = R.drawable.ic_corsica;
                i = "CORSICA";
                break;

            case COSTA_RICA:
                n = context.getString(R.string.country_costa_rica);
                d = R.drawable.ic_costa_rica;
                i = "COSTA_RICA";
                break;

            case CROATIA:
                n = context.getString(R.string.country_croatia);
                d = R.drawable.ic_croatia;
                i = "CROATIA";
                break;

            case CUBA:
                n = context.getString(R.string.country_cuba);
                d = R.drawable.ic_cuba;
                i = "CUBA";
                break;

            case CURACAO:
                n = context.getString(R.string.country_curacao);
                d = R.drawable.ic_curacao;
                i = "CURACAO";
                break;

            case CYPRUS:
                n = context.getString(R.string.country_cyprus);
                d = R.drawable.ic_cyprus;
                i = "CYPRUS";
                break;

            case CZECH_REPUBLIC:
                n = context.getString(R.string.country_czech_republic);
                d = R.drawable.ic_czech_republic;
                i = "CZECH_REPUBLIC";
                break;

            case DEMOCRATIC_REPUBLIC_OF_CONGO:
                n = context.getString(R.string.country_democratic_republic_of_congo);
                d = R.drawable.ic_democratic_republic_of_congo;
                i = "DEMOCRATIC_REPUBLIC_OF_CONGO";
                break;

            case DENMARK:
                n = context.getString(R.string.country_denmark);
                d = R.drawable.ic_denmark;
                i = "DENMARK";
                break;

            case DJIBOUTI:
                n = context.getString(R.string.country_djibouti);
                d = R.drawable.ic_djibouti;
                i = "DJIBOUTI";
                break;

            case DOMINICA:
                n = context.getString(R.string.country_dominica);
                d = R.drawable.ic_dominica;
                i = "DOMINICA";
                break;

            case DOMINICAN_REPUBLIC:
                n = context.getString(R.string.country_dominican_republic);
                d = R.drawable.ic_dominican_republic;
                i = "DOMINICAN_REPUBLIC";
                break;

            case EAST_TIMOR:
                n = context.getString(R.string.country_east_timor);
                d = R.drawable.ic_east_timor;
                i = "EAST_TIMOR";
                break;

            case ECUADOR:
                n = context.getString(R.string.country_ecuador);
                d = R.drawable.ic_ecuador;
                i = "ECUADOR";
                break;

            case EGYPT:
                n = context.getString(R.string.country_egypt);
                d = R.drawable.ic_egypt;
                i = "EGYPT";
                break;

            case EL_SALVADOR:
                n = context.getString(R.string.country_el_salvador);
                d = R.drawable.ic_el_salvador;
                i = "EL_SALVADOR";
                break;

            case ENGLAND:
                n = context.getString(R.string.country_england);
                d = R.drawable.ic_england;
                i = "ENGLAND";
                break;

            case EQUATORIAL_GUINEA:
                n = context.getString(R.string.country_equatorial_guinea);
                d = R.drawable.ic_equatorial_guinea;
                i = "EQUATORIAL_GUINEA";
                break;

            case ERITREA:
                n = context.getString(R.string.country_eritrea);
                d = R.drawable.ic_eritrea;
                i = "ERITREA";
                break;

            case ESTONIA:
                n = context.getString(R.string.country_estonia);
                d = R.drawable.ic_estonia;
                i = "ESTONIA";
                break;

            case ETHIOPIA:
                n = context.getString(R.string.country_ethiopia);
                d = R.drawable.ic_ethiopia;
                i = "ETHIOPIA";
                break;

            case EUROPEAN_UNION:
                n = context.getString(R.string.country_european_union);
                d = R.drawable.ic_european_union;
                i = "EUROPEAN_UNION";
                break;

            case FALKLAND_ISLANDS:
                n = context.getString(R.string.country_falkland_islands);
                d = R.drawable.ic_falkland_islands;
                i = "FALKLAND_ISLANDS";
                break;

            case FAROE_ISLANDS:
                n = context.getString(R.string.country_faroe_islands);
                d = R.drawable.ic_faroe_islands;
                i = "FAROE_ISLANDS";
                break;

            case FIJI:
                n = context.getString(R.string.country_fiji);
                d = R.drawable.ic_fiji;
                i = "FIJI";
                break;

            case FINLAND:
                n = context.getString(R.string.country_finland);
                d = R.drawable.ic_finland;
                i = "FINLAND";
                break;

            case FRANCE:
                n = context.getString(R.string.country_france);
                d = R.drawable.ic_france;
                i = "FRANCE";
                break;

            case FRENCH_POLYNESIA:
                n = context.getString(R.string.country_french_polynesia);
                d = R.drawable.ic_french_polynesia;
                i = "FRENCH_POLYNESIA";
                break;

            case GABON:
                n = context.getString(R.string.country_gabon);
                d = R.drawable.ic_gabon;
                i = "GABON";
                break;

            case GALAPAGOS_ISLANDS:
                n = context.getString(R.string.country_galapagos_islands);
                d = R.drawable.ic_galapagos_islands;
                i = "GALAPAGOS_ISLANDS";
                break;

            case GAMBIA:
                n = context.getString(R.string.country_gambia);
                d = R.drawable.ic_gambia;
                i = "GAMBIA";
                break;

            case GEORGIA:
                n = context.getString(R.string.country_georgia);
                d = R.drawable.ic_georgia;
                i = "GEORGIA";
                break;

            case GERMANY:
                n = context.getString(R.string.country_germany);
                d = R.drawable.ic_germany;
                i = "GERMANY";
                break;

            case GHANA:
                n = context.getString(R.string.country_ghana);
                d = R.drawable.ic_ghana;
                i = "GHANA";
                break;

            case GIBRALTAR:
                n = context.getString(R.string.country_gibraltar);
                d = R.drawable.ic_gibraltar;
                i = "GIBRALTAR";
                break;

            case GREECE:
                n = context.getString(R.string.country_greece);
                d = R.drawable.ic_greece;
                i = "GREECE";
                break;

            case GREENLAND:
                n = context.getString(R.string.country_greenland);
                d = R.drawable.ic_greenland;
                i = "GREENLAND";
                break;

            case GRENADA:
                n = context.getString(R.string.country_grenada);
                d = R.drawable.ic_grenada;
                i = "GRENADA";
                break;

            case GUAM:
                n = context.getString(R.string.country_guam);
                d = R.drawable.ic_guam;
                i = "GUAM";
                break;

            case GUATEMALA:
                n = context.getString(R.string.country_guatemala);
                d = R.drawable.ic_guatemala;
                i = "GUATEMALA";
                break;

            case GUERNSEY:
                n = context.getString(R.string.country_guernsey);
                d = R.drawable.ic_guernsey;
                i = "GUERNSEY";
                break;

            case GUINEA_BISSAU:
                n = context.getString(R.string.country_guinea_bissau);
                d = R.drawable.ic_guinea_bissau;
                i = "GUINEA_BISSAU";
                break;

            case GUINEA:
                n = context.getString(R.string.country_guinea);
                d = R.drawable.ic_guinea;
                i = "GUINEA";
                break;

            case HAITI:
                n = context.getString(R.string.country_haiti);
                d = R.drawable.ic_haiti;
                i = "HAITI";
                break;

            case HAWAII:
                n = context.getString(R.string.country_hawaii);
                d = R.drawable.ic_hawaii;
                i = "HAWAII";
                break;

            case HONDURAS:
                n = context.getString(R.string.country_honduras);
                d = R.drawable.ic_honduras;
                i = "HONDURAS";
                break;

            case HONG_KONG:
                n = context.getString(R.string.country_hong_kong);
                d = R.drawable.ic_hong_kong;
                i = "HONG_KONG";
                break;

            case HUNGARY:
                n = context.getString(R.string.country_hungary);
                d = R.drawable.ic_hungary;
                i = "HUNGARY";
                break;

            case ICELAND:
                n = context.getString(R.string.country_iceland);
                d = R.drawable.ic_iceland;
                i = "ICELAND";
                break;

            case INDIA:
                n = context.getString(R.string.country_india);
                d = R.drawable.ic_india;
                i = "INDIA";
                break;

            case INDONESIA:
                n = context.getString(R.string.country_indonesia);
                d = R.drawable.ic_indonesia;
                i = "INDONESIA";
                break;

            case IRAN:
                n = context.getString(R.string.country_iran);
                d = R.drawable.ic_iran;
                i = "IRAN";
                break;

            case IRAQ:
                n = context.getString(R.string.country_iraq);
                d = R.drawable.ic_iraq;
                i = "IRAQ";
                break;

            case IRELAND:
                n = context.getString(R.string.country_ireland);
                d = R.drawable.ic_ireland;
                i = "IRELAND";
                break;

            case ISLE_OF_MAN:
                n = context.getString(R.string.country_isle_of_man);
                d = R.drawable.ic_isle_of_man;
                i = "ISLE_OF_MAN";
                break;

            case ISRAEL:
                n = context.getString(R.string.country_israel);
                d = R.drawable.ic_israel;
                i = "ISRAEL";
                break;

            case ITALY:
                n = context.getString(R.string.country_italy);
                d = R.drawable.ic_italy;
                i = "ITALY";
                break;

            case IVORY_COAST:
                n = context.getString(R.string.country_ivory_coast);
                d = R.drawable.ic_ivory_coast;
                i = "IVORY_COAST";
                break;

            case JAMAICA:
                n = context.getString(R.string.country_jamaica);
                d = R.drawable.ic_jamaica;
                i = "JAMAICA";
                break;

            case JAPAN:
                n = context.getString(R.string.country_japan);
                d = R.drawable.ic_japan;
                i = "JAPAN";
                break;

            case JERSEY:
                n = context.getString(R.string.country_jersey);
                d = R.drawable.ic_jersey;
                i = "JERSEY";
                break;

            case JORDAN:
                n = context.getString(R.string.country_jordan);
                d = R.drawable.ic_jordan;
                i = "JORDAN";
                break;

            case KAZAKHSTAN:
                n = context.getString(R.string.country_kazakhstan);
                d = R.drawable.ic_kazakhstan;
                i = "KAZAKHSTAN";
                break;

            case KENYA:
                n = context.getString(R.string.country_kenya);
                d = R.drawable.ic_kenya;
                i = "KENYA";
                break;

            case KIRIBATI:
                n = context.getString(R.string.country_kiribati);
                d = R.drawable.ic_kiribati;
                i = "KIRIBATI";
                break;

            case KOSOVO:
                n = context.getString(R.string.country_kosovo);
                d = R.drawable.ic_kosovo;
                i = "KOSOVO";
                break;

            case KWAIT:
                n = context.getString(R.string.country_kwait);
                d = R.drawable.ic_kwait;
                i = "KWAIT";
                break;

            case KYRGYZSTAN:
                n = context.getString(R.string.country_kyrgyzstan);
                d = R.drawable.ic_kyrgyzstan;
                i = "KYRGYZSTAN";
                break;

            case LAOS:
                n = context.getString(R.string.country_laos);
                d = R.drawable.ic_laos;
                i = "LAOS";
                break;

            case LATVIA:
                n = context.getString(R.string.country_latvia);
                d = R.drawable.ic_latvia;
                i = "LATVIA";
                break;

            case LEBANON:
                n = context.getString(R.string.country_lebanon);
                d = R.drawable.ic_lebanon;
                i = "LEBANON";
                break;

            case LESOTHO:
                n = context.getString(R.string.country_lesotho);
                d = R.drawable.ic_lesotho;
                i = "LESOTHO";
                break;

            case LIBERIA:
                n = context.getString(R.string.country_liberia);
                d = R.drawable.ic_liberia;
                i = "LIBERIA";
                break;

            case LIBYA:
                n = context.getString(R.string.country_libya);
                d = R.drawable.ic_libya;
                i = "LIBYA";
                break;

            case LIECHTENSTEIN:
                n = context.getString(R.string.country_liechtenstein);
                d = R.drawable.ic_liechtenstein;
                i = "LIECHTENSTEIN";
                break;

            case LITHUANIA:
                n = context.getString(R.string.country_lithuania);
                d = R.drawable.ic_lithuania;
                i = "LITHUANIA";
                break;

            case LUXEMBOURG:
                n = context.getString(R.string.country_luxembourg);
                d = R.drawable.ic_luxembourg;
                i = "LUXEMBOURG";
                break;

            case MACAO:
                n = context.getString(R.string.country_macao);
                d = R.drawable.ic_macao;
                i = "MACAO";
                break;

            case MADAGASCAR:
                n = context.getString(R.string.country_madagascar);
                d = R.drawable.ic_madagascar;
                i = "MADAGASCAR";
                break;

            case MADEIRA:
                n = context.getString(R.string.country_madeira);
                d = R.drawable.ic_madeira;
                i = "MADEIRA";
                break;

            case MALASYA:
                n = context.getString(R.string.country_malasya);
                d = R.drawable.ic_malasya;
                i = "MALASYA";
                break;

            case MALAWI:
                n = context.getString(R.string.country_malawi);
                d = R.drawable.ic_malawi;
                i = "MALAWI";
                break;

            case MALDIVES:
                n = context.getString(R.string.country_maldives);
                d = R.drawable.ic_maldives;
                i = "MALDIVES";
                break;

            case MALI:
                n = context.getString(R.string.country_mali);
                d = R.drawable.ic_mali;
                i = "MALI";
                break;

            case MALTA:
                n = context.getString(R.string.country_malta);
                d = R.drawable.ic_malta;
                i = "MALTA";
                break;

            case MARSHALL_ISLAND:
                n = context.getString(R.string.country_marshall_island);
                d = R.drawable.ic_marshall_island;
                i = "MARSHALL_ISLAND";
                break;

            case MARTINIQUE:
                n = context.getString(R.string.country_martinique);
                d = R.drawable.ic_martinique;
                i = "MARTINIQUE";
                break;

            case MAURITANIA:
                n = context.getString(R.string.country_mauritania);
                d = R.drawable.ic_mauritania;
                i = "MAURITANIA";
                break;

            case MAURITIUS:
                n = context.getString(R.string.country_mauritius);
                d = R.drawable.ic_mauritius;
                i = "MAURITIUS";
                break;

            case MELILLA:
                n = context.getString(R.string.country_melilla);
                d = R.drawable.ic_melilla;
                i = "MELILLA";
                break;

            case MEXICO:
                n = context.getString(R.string.country_mexico);
                d = R.drawable.ic_mexico;
                i = "MEXICO";
                break;

            case MICRONESIA:
                n = context.getString(R.string.country_micronesia);
                d = R.drawable.ic_micronesia;
                i = "MICRONESIA";
                break;

            case MOLDOVA:
                n = context.getString(R.string.country_moldova);
                d = R.drawable.ic_moldova;
                i = "MOLDOVA";
                break;

            case MONACO:
                n = context.getString(R.string.country_monaco);
                d = R.drawable.ic_monaco;
                i = "MONACO";
                break;

            case MONGOLIA:
                n = context.getString(R.string.country_mongolia);
                d = R.drawable.ic_mongolia;
                i = "MONGOLIA";
                break;

            case MONTENEGRO:
                n = context.getString(R.string.country_montenegro);
                d = R.drawable.ic_montenegro;
                i = "MONTENEGRO";
                break;

            case MONTSERRAT:
                n = context.getString(R.string.country_montserrat);
                d = R.drawable.ic_montserrat;
                i = "MONTSERRAT";
                break;

            case MOROCCO:
                n = context.getString(R.string.country_morocco);
                d = R.drawable.ic_morocco;
                i = "MOROCCO";
                break;

            case MOZAMBIQUE:
                n = context.getString(R.string.country_mozambique);
                d = R.drawable.ic_mozambique;
                i = "MOZAMBIQUE";
                break;

            case MYANMAR:
                n = context.getString(R.string.country_myanmar);
                d = R.drawable.ic_myanmar;
                i = "MYANMAR";
                break;

            case NAMIBIA:
                n = context.getString(R.string.country_namibia);
                d = R.drawable.ic_namibia;
                i = "NAMIBIA";
                break;

            case NATO:
                n = context.getString(R.string.country_nato);
                d = R.drawable.ic_nato;
                i = "NATO";
                break;

            case NAURU:
                n = context.getString(R.string.country_nauru);
                d = R.drawable.ic_nauru;
                i = "NAURU";
                break;

            case NEPAL:
                n = context.getString(R.string.country_nepal);
                d = R.drawable.ic_nepal;
                i = "NEPAL";
                break;

            case NETHERLANDS:
                n = context.getString(R.string.country_netherlands);
                d = R.drawable.ic_netherlands;
                i = "NETHERLANDS";
                break;

            case NEW_ZEALAND:
                n = context.getString(R.string.country_new_zealand);
                d = R.drawable.ic_new_zealand;
                i = "NEW_ZEALAND";
                break;

            case NICARAGUA:
                n = context.getString(R.string.country_nicaragua);
                d = R.drawable.ic_nicaragua;
                i = "NICARAGUA";
                break;

            case NIGER:
                n = context.getString(R.string.country_niger);
                d = R.drawable.ic_niger;
                i = "NIGER";
                break;

            case NIGERIA:
                n = context.getString(R.string.country_nigeria);
                d = R.drawable.ic_nigeria;
                i = "NIGERIA";
                break;

            case NIUE:
                n = context.getString(R.string.country_niue);
                d = R.drawable.ic_niue;
                i = "NIUE";
                break;

            case NORFOLK_ISLAND:
                n = context.getString(R.string.country_norfolk_island);
                d = R.drawable.ic_norfolk_island;
                i = "NORFOLK_ISLAND";
                break;

            case NORTH_KOREA:
                n = context.getString(R.string.country_north_korea);
                d = R.drawable.ic_north_korea;
                i = "NORTH_KOREA";
                break;

            case NORTHERN_CYPRUS:
                n = context.getString(R.string.country_northern_cyprus);
                d = R.drawable.ic_northern_cyprus;
                i = "NORTHERN_CYPRUS";
                break;

            case NORTHERN_MARIANAS_ISLANDS:
                n = context.getString(R.string.country_northern_marianas_islands);
                d = R.drawable.ic_northern_marianas_islands;
                i = "NORTHERN_MARIANAS_ISLANDS";
                break;

            case NORWAY:
                n = context.getString(R.string.country_norway);
                d = R.drawable.ic_norway;
                i = "NORWAY";
                break;

            case OMAN:
                n = context.getString(R.string.country_oman);
                d = R.drawable.ic_oman;
                i = "OMAN";
                break;

            case OSSETIA:
                n = context.getString(R.string.country_ossetia);
                d = R.drawable.ic_ossetia;
                i = "OSSETIA";
                break;

            case PAKISTAN:
                n = context.getString(R.string.country_pakistan);
                d = R.drawable.ic_pakistan;
                i = "PAKISTAN";
                break;

            case PALAU:
                n = context.getString(R.string.country_palau);
                d = R.drawable.ic_palau;
                i = "PALAU";
                break;

            case PALESTINE:
                n = context.getString(R.string.country_palestine);
                d = R.drawable.ic_palestine;
                i = "PALESTINE";
                break;

            case PANAMA:
                n = context.getString(R.string.country_panama);
                d = R.drawable.ic_panama;
                i = "PANAMA";
                break;

            case PAPUA_NEW_GUINEA:
                n = context.getString(R.string.country_papua_new_guinea);
                d = R.drawable.ic_papua_new_guinea;
                i = "PAPUA_NEW_GUINEA";
                break;

            case PARAGUAY:
                n = context.getString(R.string.country_paraguay);
                d = R.drawable.ic_paraguay;
                i = "PARAGUAY";
                break;

            case PERU:
                n = context.getString(R.string.country_peru);
                d = R.drawable.ic_peru;
                i = "PERU";
                break;

            case PHILIPPINES:
                n = context.getString(R.string.country_philippines);
                d = R.drawable.ic_philippines;
                i = "PHILIPPINES";
                break;

            case PITCAIRN_ISLANDS:
                n = context.getString(R.string.country_pitcairn_islands);
                d = R.drawable.ic_pitcairn_islands;
                i = "PITCAIRN_ISLANDS";
                break;

            case POLAND:
                n = context.getString(R.string.country_poland);
                d = R.drawable.ic_poland;
                i = "POLAND";
                break;

            case PORTUGAL:
                n = context.getString(R.string.country_portugal);
                d = R.drawable.ic_portugal;
                i = "PORTUGAL";
                break;

            case PUERTO_RICO:
                n = context.getString(R.string.country_puerto_rico);
                d = R.drawable.ic_puerto_rico;
                i = "PUERTO_RICO";
                break;

            case QATAR:
                n = context.getString(R.string.country_qatar);
                d = R.drawable.ic_qatar;
                i = "QATAR";
                break;

            case RAPA_NUI:
                n = context.getString(R.string.country_rapa_nui);
                d = R.drawable.ic_rapa_nui;
                i = "RAPA_NUI";
                break;

            case REPUBLIC_OF_MACEDONIA:
                n = context.getString(R.string.country_republic_of_macedonia);
                d = R.drawable.ic_republic_of_macedonia;
                i = "REPUBLIC_OF_MACEDONIA";
                break;

            case REPUBLIC_OF_THE_CONGO:
                n = context.getString(R.string.country_republic_of_the_congo);
                d = R.drawable.ic_republic_of_the_congo;
                i = "REPUBLIC_OF_THE_CONGO";
                break;

            case ROMANIA:
                n = context.getString(R.string.country_romania);
                d = R.drawable.ic_romania;
                i = "ROMANIA";
                break;

            case RUSSIA:
                n = context.getString(R.string.country_russia);
                d = R.drawable.ic_russia;
                i = "RUSSIA";
                break;

            case RWANDA:
                n = context.getString(R.string.country_rwanda);
                d = R.drawable.ic_rwanda;
                i = "RWANDA";
                break;

            case SABA_ISLAND:
                n = context.getString(R.string.country_saba_island);
                d = R.drawable.ic_saba_island;
                i = "SABA_ISLAND";
                break;

            case SAHRAWI_ARAB_DEMOCRATIC_REPUBLIC:
                n = context.getString(R.string.country_sahrawi_arab_democratic_republic);
                d = R.drawable.ic_sahrawi_arab_democratic_republic;
                i = "SAHRAWI_ARAB_DEMOCRATIC_REPUBLIC";
                break;

            case SAINT_KITTS_AND_NEVIS:
                n = context.getString(R.string.country_saint_kitts_and_nevis);
                d = R.drawable.ic_saint_kitts_and_nevis;
                i = "SAINT_KITTS_AND_NEVIS";
                break;

            case SAMOA:
                n = context.getString(R.string.country_samoa);
                d = R.drawable.ic_samoa;
                i = "SAMOA";
                break;

            case SAN_MARINO:
                n = context.getString(R.string.country_san_marino);
                d = R.drawable.ic_san_marino;
                i = "SAN_MARINO";
                break;

            case SAO_TOME_AND_PRINCE:
                n = context.getString(R.string.country_sao_tome_and_prince);
                d = R.drawable.ic_sao_tome_and_prince;
                i = "SAO_TOME_AND_PRINCE";
                break;

            case SARDINIA:
                n = context.getString(R.string.country_sardinia);
                d = R.drawable.ic_sardinia;
                i = "SARDINIA";
                break;

            case SAUDI_ARABIA:
                n = context.getString(R.string.country_saudi_arabia);
                d = R.drawable.ic_saudi_arabia;
                i = "SAUDI_ARABIA";
                break;

            case SCOTLAND:
                n = context.getString(R.string.country_scotland);
                d = R.drawable.ic_scotland;
                i = "SCOTLAND";
                break;

            case SENEGAL:
                n = context.getString(R.string.country_senegal);
                d = R.drawable.ic_senegal;
                i = "SENEGAL";
                break;

            case SERBIA:
                n = context.getString(R.string.country_serbia);
                d = R.drawable.ic_serbia;
                i = "SERBIA";
                break;

            case SEYCHELLES:
                n = context.getString(R.string.country_seychelles);
                d = R.drawable.ic_seychelles;
                i = "SEYCHELLES";
                break;

            case SICILY:
                n = context.getString(R.string.country_sicily);
                d = R.drawable.ic_sicily;
                i = "SICILY";
                break;

            case SIERRA_LEONE:
                n = context.getString(R.string.country_sierra_leone);
                d = R.drawable.ic_sierra_leone;
                i = "SIERRA_LEONE";
                break;

            case SINGAPORE:
                n = context.getString(R.string.country_singapore);
                d = R.drawable.ic_singapore;
                i = "SINGAPORE";
                break;

            case SINT_EUSTATIUS:
                n = context.getString(R.string.country_sint_eustatius);
                d = R.drawable.ic_sint_eustatius;
                i = "SINT_EUSTATIUS";
                break;

            case SINT_MAARTEN:
                n = context.getString(R.string.country_sint_maarten);
                d = R.drawable.ic_sint_maarten;
                i = "SINT_MAARTEN";
                break;

            case SLOVAKIA:
                n = context.getString(R.string.country_slovakia);
                d = R.drawable.ic_slovakia;
                i = "SLOVAKIA";
                break;

            case SLOVENIA:
                n = context.getString(R.string.country_slovenia);
                d = R.drawable.ic_slovenia;
                i = "SLOVENIA";
                break;

            case SOLOMON_ISLANDS:
                n = context.getString(R.string.country_solomon_islands);
                d = R.drawable.ic_solomon_islands;
                i = "SOLOMON_ISLANDS";
                break;

            case SOMALIA:
                n = context.getString(R.string.country_somalia);
                d = R.drawable.ic_somalia;
                i = "SOMALIA";
                break;

            case SOMALILAND:
                n = context.getString(R.string.country_somaliland);
                d = R.drawable.ic_somaliland;
                i = "SOMALILAND";
                break;

            case SOUTH_AFRICA:
                n = context.getString(R.string.country_south_africa);
                d = R.drawable.ic_south_africa;
                i = "SOUTH_AFRICA";
                break;

            case SOUTH_KOREA:
                n = context.getString(R.string.country_south_korea);
                d = R.drawable.ic_south_korea;
                i = "SOUTH_KOREA";
                break;

            case SOUTH_SUDAN:
                n = context.getString(R.string.country_south_sudan);
                d = R.drawable.ic_south_sudan;
                i = "SOUTH_SUDAN";
                break;

            case SPAIN:
                n = context.getString(R.string.country_spain);
                d = R.drawable.ic_spain;
                i = "SPAIN";
                break;

            case SRI_LANKA:
                n = context.getString(R.string.country_sri_lanka);
                d = R.drawable.ic_sri_lanka;
                i = "SRI_LANKA";
                break;

            case ST_BARTS:
                n = context.getString(R.string.country_st_barts);
                d = R.drawable.ic_st_barts;
                i = "ST_BARTS";
                break;

            case ST_LUCIA:
                n = context.getString(R.string.country_st_lucia);
                d = R.drawable.ic_st_lucia;
                i = "ST_LUCIA";
                break;

            case ST_VINCENT_AND_THE_GRENADINES:
                n = context.getString(R.string.country_st_vincent_and_the_grenadines);
                d = R.drawable.ic_st_vincent_and_the_grenadines;
                i = "ST_VINCENT_AND_THE_GRENADINES";
                break;

            case SUDAN:
                n = context.getString(R.string.country_sudan);
                d = R.drawable.ic_sudan;
                i = "SUDAN";
                break;

            case SURINAME:
                n = context.getString(R.string.country_suriname);
                d = R.drawable.ic_suriname;
                i = "SURINAME";
                break;

            case SWAZILAND:
                n = context.getString(R.string.country_swaziland);
                d = R.drawable.ic_swaziland;
                i = "SWAZILAND";
                break;

            case SWEDEN:
                n = context.getString(R.string.country_sweden);
                d = R.drawable.ic_sweden;
                i = "SWEDEN";
                break;

            case SWITZERLAND:
                n = context.getString(R.string.country_switzerland);
                d = R.drawable.ic_switzerland;
                i = "SWITZERLAND";
                break;

            case SYRIA:
                n = context.getString(R.string.country_syria);
                d = R.drawable.ic_syria;
                i = "SYRIA";
                break;

            case TAIWAN:
                n = context.getString(R.string.country_taiwan);
                d = R.drawable.ic_taiwan;
                i = "TAIWAN";
                break;

            case TAJIKISTAN:
                n = context.getString(R.string.country_tajikistan);
                d = R.drawable.ic_tajikistan;
                i = "TAJIKISTAN";
                break;

            case TANZANIA:
                n = context.getString(R.string.country_tanzania);
                d = R.drawable.ic_tanzania;
                i = "TANZANIA";
                break;

            case THAILAND:
                n = context.getString(R.string.country_thailand);
                d = R.drawable.ic_thailand;
                i = "THAILAND";
                break;

            case TIBET:
                n = context.getString(R.string.country_tibet);
                d = R.drawable.ic_tibet;
                i = "TIBET";
                break;

            case TOGO:
                n = context.getString(R.string.country_togo);
                d = R.drawable.ic_togo;
                i = "TOGO";
                break;

            case TOKELAU:
                n = context.getString(R.string.country_tokelau);
                d = R.drawable.ic_tokelau;
                i = "TOKELAU";
                break;

            case TONGA:
                n = context.getString(R.string.country_tonga);
                d = R.drawable.ic_tonga;
                i = "TONGA";
                break;

            case TRANSNISTRIA:
                n = context.getString(R.string.country_transnistria);
                d = R.drawable.ic_transnistria;
                i = "TRANSNISTRIA";
                break;

            case TRINIDAD_AND_TOBAGO:
                n = context.getString(R.string.country_trinidad_and_tobago);
                d = R.drawable.ic_trinidad_and_tobago;
                i = "TRINIDAD_AND_TOBAGO";
                break;

            case TUNISIA:
                n = context.getString(R.string.country_tunisia);
                d = R.drawable.ic_tunisia;
                i = "TUNISIA";
                break;

            case TURKEY:
                n = context.getString(R.string.country_turkey);
                d = R.drawable.ic_turkey;
                i = "TURKEY";
                break;

            case TURKMENISTAN:
                n = context.getString(R.string.country_turkmenistan);
                d = R.drawable.ic_turkmenistan;
                i = "TURKMENISTAN";
                break;

            case TURKS_AND_CAICOS:
                n = context.getString(R.string.country_turks_and_caicos);
                d = R.drawable.ic_turks_and_caicos;
                i = "TURKS_AND_CAICOS";
                break;

            case TUVALU_1:
                n = context.getString(R.string.country_tuvalu_1);
                d = R.drawable.ic_tuvalu_1;
                i = "TUVALU_1";
                break;

            case TUVALU:
                n = context.getString(R.string.country_tuvalu);
                d = R.drawable.ic_tuvalu;
                i = "TUVALU";
                break;

            case UGANDA:
                n = context.getString(R.string.country_uganda);
                d = R.drawable.ic_uganda;
                i = "UGANDA";
                break;

            case UKRAINE:
                n = context.getString(R.string.country_ukraine);
                d = R.drawable.ic_ukraine;
                i = "UKRAINE";
                break;

            case UNITED_ARAB_EMIRATES:
                n = context.getString(R.string.country_united_arab_emirates);
                d = R.drawable.ic_united_arab_emirates;
                i = "UNITED_ARAB_EMIRATES";
                break;

            case UNITED_KINGDOM:
                n = context.getString(R.string.country_united_kingdom);
                d = R.drawable.ic_united_kingdom;
                i = "UNITED_KINGDOM";
                break;

            case UNITED_NATIONS:
                n = context.getString(R.string.country_united_nations);
                d = R.drawable.ic_united_nations;
                i = "UNITED_NATIONS";
                break;

            case UNITED_STATES:
                n = context.getString(R.string.country_united_states);
                d = R.drawable.ic_united_states;
                i = "UNITED_STATES";
                break;

            case URUGUAY:
                n = context.getString(R.string.country_uruguay);
                d = R.drawable.ic_uruguay;
                i = "URUGUAY";
                break;

            case UZBEKISTN:
                n = context.getString(R.string.country_uzbekistn);
                d = R.drawable.ic_uzbekistn;
                i = "UZBEKISTN";
                break;

            case VANUATU:
                n = context.getString(R.string.country_vanuatu);
                d = R.drawable.ic_vanuatu;
                i = "VANUATU";
                break;

            case VATICAN_CITY:
                n = context.getString(R.string.country_vatican_city);
                d = R.drawable.ic_vatican_city;
                i = "VATICAN_CITY";
                break;

            case VENEZUELA:
                n = context.getString(R.string.country_venezuela);
                d = R.drawable.ic_venezuela;
                i = "VENEZUELA";
                break;

            case VIETNAM:
                n = context.getString(R.string.country_vietnam);
                d = R.drawable.ic_vietnam;
                i = "VIETNAM";
                break;

            case VIRGIN_ISLANDS:
                n = context.getString(R.string.country_virgin_islands);
                d = R.drawable.ic_virgin_islands;
                i = "VIRGIN_ISLANDS";
                break;

            case WALES:
                n = context.getString(R.string.country_wales);
                d = R.drawable.ic_wales;
                i = "WALES";
                break;

            case YEMEN:
                n = context.getString(R.string.country_yemen);
                d = R.drawable.ic_yemen;
                i = "YEMEN";
                break;

            case ZAMBIA:
                n = context.getString(R.string.country_zambia);
                d = R.drawable.ic_zambia;
                i = "ZAMBIA";
                break;

            case ZIMBABWE:
                n = context.getString(R.string.country_zimbabwe);
                d = R.drawable.ic_zimbabwe;
                i = "ZIMBABWE";
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

    public static List<Country> getAll(Context context){
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(context, Country.ABKHAZIA));
        countries.add(new Country(context, Country.AFGHANISTAN));
        countries.add(new Country(context, Country.ALAND_ISLANDS));
        countries.add(new Country(context, Country.ALBANIA));
        countries.add(new Country(context, Country.ALGERIA));
        countries.add(new Country(context, Country.AMERICAN_SAMOA));
        countries.add(new Country(context, Country.ANDORRA));
        countries.add(new Country(context, Country.ANGOLA));
        countries.add(new Country(context, Country.ANGUILLA));
        countries.add(new Country(context, Country.ANTIGUA_AND_BARBUDA));
        countries.add(new Country(context, Country.ARGENTINA));
        countries.add(new Country(context, Country.ARMENIA));
        countries.add(new Country(context, Country.ARUBA));
        countries.add(new Country(context, Country.AUSTRALIA));
        countries.add(new Country(context, Country.AUSTRIA));
        countries.add(new Country(context, Country.AZERBAIJAN));
        countries.add(new Country(context, Country.AZORES_ISLANDS));
        countries.add(new Country(context, Country.BAHAMAS));
        countries.add(new Country(context, Country.BAHRAIN));
        countries.add(new Country(context, Country.BALEARIC_ISLANDS));
        countries.add(new Country(context, Country.BANGLADESH));
        countries.add(new Country(context, Country.BARBADOS));
        countries.add(new Country(context, Country.BASQUE_COUNTRY));
        countries.add(new Country(context, Country.BELARUS));
        countries.add(new Country(context, Country.BELGIUM));
        countries.add(new Country(context, Country.BELIZE));
        countries.add(new Country(context, Country.BENIN));
        countries.add(new Country(context, Country.BERMUDA));
        countries.add(new Country(context, Country.BHUTAN));
        countries.add(new Country(context, Country.BOLIVIA));
        countries.add(new Country(context, Country.BONAIRE));
        countries.add(new Country(context, Country.BOSNIA_AND_HERZEGOVINA));
        countries.add(new Country(context, Country.BOTSWANA));
        countries.add(new Country(context, Country.BRAZIL));
        countries.add(new Country(context, Country.BRITISH_COLUMBIA));
        countries.add(new Country(context, Country.BRITISH_INDIAN_OCEAN_TERRITORY));
        countries.add(new Country(context, Country.BRITISH_VIRGIN_ISLANDS));
        countries.add(new Country(context, Country.BRUNEI));
        countries.add(new Country(context, Country.BULGARIA));
        countries.add(new Country(context, Country.BURKINA_FASO));
        countries.add(new Country(context, Country.BURUNDI));
        countries.add(new Country(context, Country.CAMBODIA));
        countries.add(new Country(context, Country.CAMEROON));
        countries.add(new Country(context, Country.CANADA));
        countries.add(new Country(context, Country.CANARY_ISLANDS));
        countries.add(new Country(context, Country.CAPE_VERDE));
        countries.add(new Country(context, Country.CAYMAN_ISLANDS));
        countries.add(new Country(context, Country.CENTRAL_AFRICAN_REPUBLIC));
        countries.add(new Country(context, Country.CEUTA));
        countries.add(new Country(context, Country.CHAD));
        countries.add(new Country(context, Country.CHILE));
        countries.add(new Country(context, Country.CHINA));
        countries.add(new Country(context, Country.CHRISTMAS_ISLAND));
        countries.add(new Country(context, Country.COCOS_ISLAND));
        countries.add(new Country(context, Country.COLOMBIA));
        countries.add(new Country(context, Country.COMOROS));
        countries.add(new Country(context, Country.COOK_ISLANDS));
        countries.add(new Country(context, Country.CORSICA));
        countries.add(new Country(context, Country.COSTA_RICA));
        countries.add(new Country(context, Country.CROATIA));
        countries.add(new Country(context, Country.CUBA));
        countries.add(new Country(context, Country.CURACAO));
        countries.add(new Country(context, Country.CYPRUS));
        countries.add(new Country(context, Country.CZECH_REPUBLIC));
        countries.add(new Country(context, Country.DEMOCRATIC_REPUBLIC_OF_CONGO));
        countries.add(new Country(context, Country.DENMARK));
        countries.add(new Country(context, Country.DJIBOUTI));
        countries.add(new Country(context, Country.DOMINICA));
        countries.add(new Country(context, Country.DOMINICAN_REPUBLIC));
        countries.add(new Country(context, Country.EAST_TIMOR));
        countries.add(new Country(context, Country.ECUADOR));
        countries.add(new Country(context, Country.EGYPT));
        countries.add(new Country(context, Country.EL_SALVADOR));
        countries.add(new Country(context, Country.ENGLAND));
        countries.add(new Country(context, Country.EQUATORIAL_GUINEA));
        countries.add(new Country(context, Country.ERITREA));
        countries.add(new Country(context, Country.ESTONIA));
        countries.add(new Country(context, Country.ETHIOPIA));
        countries.add(new Country(context, Country.EUROPEAN_UNION));
        countries.add(new Country(context, Country.FALKLAND_ISLANDS));
        countries.add(new Country(context, Country.FAROE_ISLANDS));
        countries.add(new Country(context, Country.FIJI));
        countries.add(new Country(context, Country.FINLAND));
        countries.add(new Country(context, Country.FRANCE));
        countries.add(new Country(context, Country.FRENCH_POLYNESIA));
        countries.add(new Country(context, Country.GABON));
        countries.add(new Country(context, Country.GALAPAGOS_ISLANDS));
        countries.add(new Country(context, Country.GAMBIA));
        countries.add(new Country(context, Country.GEORGIA));
        countries.add(new Country(context, Country.GERMANY));
        countries.add(new Country(context, Country.GHANA));
        countries.add(new Country(context, Country.GIBRALTAR));
        countries.add(new Country(context, Country.GREECE));
        countries.add(new Country(context, Country.GREENLAND));
        countries.add(new Country(context, Country.GRENADA));
        countries.add(new Country(context, Country.GUAM));
        countries.add(new Country(context, Country.GUATEMALA));
        countries.add(new Country(context, Country.GUERNSEY));
        countries.add(new Country(context, Country.GUINEA_BISSAU));
        countries.add(new Country(context, Country.GUINEA));
        countries.add(new Country(context, Country.HAITI));
        countries.add(new Country(context, Country.HAWAII));
        countries.add(new Country(context, Country.HONDURAS));
        countries.add(new Country(context, Country.HONG_KONG));
        countries.add(new Country(context, Country.HUNGARY));
        countries.add(new Country(context, Country.ICELAND));
        countries.add(new Country(context, Country.INDIA));
        countries.add(new Country(context, Country.INDONESIA));
        countries.add(new Country(context, Country.IRAN));
        countries.add(new Country(context, Country.IRAQ));
        countries.add(new Country(context, Country.IRELAND));
        countries.add(new Country(context, Country.ISLE_OF_MAN));
        countries.add(new Country(context, Country.ISRAEL));
        countries.add(new Country(context, Country.ITALY));
        countries.add(new Country(context, Country.IVORY_COAST));
        countries.add(new Country(context, Country.JAMAICA));
        countries.add(new Country(context, Country.JAPAN));
        countries.add(new Country(context, Country.JERSEY));
        countries.add(new Country(context, Country.JORDAN));
        countries.add(new Country(context, Country.KAZAKHSTAN));
        countries.add(new Country(context, Country.KENYA));
        countries.add(new Country(context, Country.KIRIBATI));
        countries.add(new Country(context, Country.KOSOVO));
        countries.add(new Country(context, Country.KWAIT));
        countries.add(new Country(context, Country.KYRGYZSTAN));
        countries.add(new Country(context, Country.LAOS));
        countries.add(new Country(context, Country.LATVIA));
        countries.add(new Country(context, Country.LEBANON));
        countries.add(new Country(context, Country.LESOTHO));
        countries.add(new Country(context, Country.LIBERIA));
        countries.add(new Country(context, Country.LIBYA));
        countries.add(new Country(context, Country.LIECHTENSTEIN));
        countries.add(new Country(context, Country.LITHUANIA));
        countries.add(new Country(context, Country.LUXEMBOURG));
        countries.add(new Country(context, Country.MACAO));
        countries.add(new Country(context, Country.MADAGASCAR));
        countries.add(new Country(context, Country.MADEIRA));
        countries.add(new Country(context, Country.MALASYA));
        countries.add(new Country(context, Country.MALAWI));
        countries.add(new Country(context, Country.MALDIVES));
        countries.add(new Country(context, Country.MALI));
        countries.add(new Country(context, Country.MALTA));
        countries.add(new Country(context, Country.MARSHALL_ISLAND));
        countries.add(new Country(context, Country.MARTINIQUE));
        countries.add(new Country(context, Country.MAURITANIA));
        countries.add(new Country(context, Country.MAURITIUS));
        countries.add(new Country(context, Country.MELILLA));
        countries.add(new Country(context, Country.MEXICO));
        countries.add(new Country(context, Country.MICRONESIA));
        countries.add(new Country(context, Country.MOLDOVA));
        countries.add(new Country(context, Country.MONACO));
        countries.add(new Country(context, Country.MONGOLIA));
        countries.add(new Country(context, Country.MONTENEGRO));
        countries.add(new Country(context, Country.MONTSERRAT));
        countries.add(new Country(context, Country.MOROCCO));
        countries.add(new Country(context, Country.MOZAMBIQUE));
        countries.add(new Country(context, Country.MYANMAR));
        countries.add(new Country(context, Country.NAMIBIA));
        countries.add(new Country(context, Country.NATO));
        countries.add(new Country(context, Country.NAURU));
        countries.add(new Country(context, Country.NEPAL));
        countries.add(new Country(context, Country.NETHERLANDS));
        countries.add(new Country(context, Country.NEW_ZEALAND));
        countries.add(new Country(context, Country.NICARAGUA));
        countries.add(new Country(context, Country.NIGER));
        countries.add(new Country(context, Country.NIGERIA));
        countries.add(new Country(context, Country.NIUE));
        countries.add(new Country(context, Country.NORFOLK_ISLAND));
        countries.add(new Country(context, Country.NORTH_KOREA));
        countries.add(new Country(context, Country.NORTHERN_CYPRUS));
        countries.add(new Country(context, Country.NORTHERN_MARIANAS_ISLANDS));
        countries.add(new Country(context, Country.NORWAY));
        countries.add(new Country(context, Country.OMAN));
        countries.add(new Country(context, Country.OSSETIA));
        countries.add(new Country(context, Country.PAKISTAN));
        countries.add(new Country(context, Country.PALAU));
        countries.add(new Country(context, Country.PALESTINE));
        countries.add(new Country(context, Country.PANAMA));
        countries.add(new Country(context, Country.PAPUA_NEW_GUINEA));
        countries.add(new Country(context, Country.PARAGUAY));
        countries.add(new Country(context, Country.PERU));
        countries.add(new Country(context, Country.PHILIPPINES));
        countries.add(new Country(context, Country.PITCAIRN_ISLANDS));
        countries.add(new Country(context, Country.POLAND));
        countries.add(new Country(context, Country.PORTUGAL));
        countries.add(new Country(context, Country.PUERTO_RICO));
        countries.add(new Country(context, Country.QATAR));
        countries.add(new Country(context, Country.RAPA_NUI));
        countries.add(new Country(context, Country.REPUBLIC_OF_MACEDONIA));
        countries.add(new Country(context, Country.REPUBLIC_OF_THE_CONGO));
        countries.add(new Country(context, Country.ROMANIA));
        countries.add(new Country(context, Country.RUSSIA));
        countries.add(new Country(context, Country.RWANDA));
        countries.add(new Country(context, Country.SABA_ISLAND));
        countries.add(new Country(context, Country.SAHRAWI_ARAB_DEMOCRATIC_REPUBLIC));
        countries.add(new Country(context, Country.SAINT_KITTS_AND_NEVIS));
        countries.add(new Country(context, Country.SAMOA));
        countries.add(new Country(context, Country.SAN_MARINO));
        countries.add(new Country(context, Country.SAO_TOME_AND_PRINCE));
        countries.add(new Country(context, Country.SARDINIA));
        countries.add(new Country(context, Country.SAUDI_ARABIA));
        countries.add(new Country(context, Country.SCOTLAND));
        countries.add(new Country(context, Country.SENEGAL));
        countries.add(new Country(context, Country.SERBIA));
        countries.add(new Country(context, Country.SEYCHELLES));
        countries.add(new Country(context, Country.SICILY));
        countries.add(new Country(context, Country.SIERRA_LEONE));
        countries.add(new Country(context, Country.SINGAPORE));
        countries.add(new Country(context, Country.SINT_EUSTATIUS));
        countries.add(new Country(context, Country.SINT_MAARTEN));
        countries.add(new Country(context, Country.SLOVAKIA));
        countries.add(new Country(context, Country.SLOVENIA));
        countries.add(new Country(context, Country.SOLOMON_ISLANDS));
        countries.add(new Country(context, Country.SOMALIA));
        countries.add(new Country(context, Country.SOMALILAND));
        countries.add(new Country(context, Country.SOUTH_AFRICA));
        countries.add(new Country(context, Country.SOUTH_KOREA));
        countries.add(new Country(context, Country.SOUTH_SUDAN));
        countries.add(new Country(context, Country.SPAIN));
        countries.add(new Country(context, Country.SRI_LANKA));
        countries.add(new Country(context, Country.ST_BARTS));
        countries.add(new Country(context, Country.ST_LUCIA));
        countries.add(new Country(context, Country.ST_VINCENT_AND_THE_GRENADINES));
        countries.add(new Country(context, Country.SUDAN));
        countries.add(new Country(context, Country.SURINAME));
        countries.add(new Country(context, Country.SWAZILAND));
        countries.add(new Country(context, Country.SWEDEN));
        countries.add(new Country(context, Country.SWITZERLAND));
        countries.add(new Country(context, Country.SYRIA));
        countries.add(new Country(context, Country.TAIWAN));
        countries.add(new Country(context, Country.TAJIKISTAN));
        countries.add(new Country(context, Country.TANZANIA));
        countries.add(new Country(context, Country.THAILAND));
        countries.add(new Country(context, Country.TIBET));
        countries.add(new Country(context, Country.TOGO));
        countries.add(new Country(context, Country.TOKELAU));
        countries.add(new Country(context, Country.TONGA));
        countries.add(new Country(context, Country.TRANSNISTRIA));
        countries.add(new Country(context, Country.TRINIDAD_AND_TOBAGO));
        countries.add(new Country(context, Country.TUNISIA));
        countries.add(new Country(context, Country.TURKEY));
        countries.add(new Country(context, Country.TURKMENISTAN));
        countries.add(new Country(context, Country.TURKS_AND_CAICOS));
        countries.add(new Country(context, Country.TUVALU_1));
        countries.add(new Country(context, Country.TUVALU));
        countries.add(new Country(context, Country.UGANDA));
        countries.add(new Country(context, Country.UKRAINE));
        countries.add(new Country(context, Country.UNITED_ARAB_EMIRATES));
        countries.add(new Country(context, Country.UNITED_KINGDOM));
        countries.add(new Country(context, Country.UNITED_NATIONS));
        countries.add(new Country(context, Country.UNITED_STATES));
        countries.add(new Country(context, Country.URUGUAY));
        countries.add(new Country(context, Country.UZBEKISTN));
        countries.add(new Country(context, Country.VANUATU));
        countries.add(new Country(context, Country.VATICAN_CITY));
        countries.add(new Country(context, Country.VENEZUELA));
        countries.add(new Country(context, Country.VIETNAM));
        countries.add(new Country(context, Country.VIRGIN_ISLANDS));
        countries.add(new Country(context, Country.WALES));
        countries.add(new Country(context, Country.YEMEN));
        countries.add(new Country(context, Country.ZAMBIA));
        countries.add(new Country(context, Country.ZIMBABWE));
        return countries;
    }

}
