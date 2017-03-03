package com.mooo.ewolvy.uppidown;

class AAState {
    // Constants
    static final int AUTO_MODE = 0;
    static final int COOL_MODE = 1;
    static final int DRY_MODE = 2;
    static final int HEAT_MODE = 3;
    static final int FAN_MODE = 4;

    static final int AUTO_FAN = 0;
    static final int LEVEL1_FAN = 1;
    static final int LEVEL2_FAN = 2;
    static final int LEVEL3_FAN = 3;
    private static final int SPECIAL_FAN = 4;

    static final int TEMP_MIN = 17;
    static final int TEMP_MAX = 30;
    private static final int SPECIAL_TEMP = 31; // DEBE SER TEMP_MAX + 1

    private static final String INIT_CHAIN = "B24D";
    private static final String SWING_CHAIN = "B24D6B94E01F";
    private static final String OFF_CHAIN = "B24D7B84E01F";

    private static final char[] FAN_MODES= {'B', '9', '5', '3', '1'};
    private static final char[] REVERSE_FAN_MODES = {'4', '6', 'A', 'C', 'E'};
    private static final char[] TEMPS = {'0', '1', '3', '2', '6', '7', '5', '4', 'C', 'D', '9', '8', 'A', 'B', 'E'};
    private static final char[] REVERSE_TEMPS = {'F', 'E', 'C', 'D', '9', '8', 'A', 'B', '3', '2', '6', '7', '5', '4', '1'};
    private static final char[] MODES = {'8', '0', '4', 'C', '4'};
    private static final char[] REVERSE_MODES = {'7', 'F', 'B', '3', 'B'};

    // Variables
    private boolean isOn;
    private int currentMode;
    private int currentFan;
    private boolean activeFan;
    private boolean activeTemp;
    private int currentTemp;

    // Constructor
    AAState (int stateMode,
                    int stateFan,
                    int stateTemp){
        if (!setMode(stateMode)){
            setMode (AUTO_MODE);
        }

        isOn = false;
        activeFan = (getMode() != AUTO_MODE) && (getMode() != DRY_MODE);
        activeTemp = getMode() != FAN_MODE;

        if (!setFan (stateFan)){
            setFan (AUTO_FAN);
        }

        if (stateTemp < TEMP_MIN || stateTemp > TEMP_MAX){
            currentTemp = (TEMP_MIN + TEMP_MAX) / 2;
        }else{
            currentTemp = stateTemp;
        }
    }

    // Setters and getters methods for variables //
    boolean getIsOn(){return isOn;}

    int getMode() {
        return currentMode;
    }

    boolean isActiveFan() {return activeFan;}

    boolean isActiveTemp() {return activeTemp;}

    void setOn (boolean on){this.isOn = on;}

    boolean setMode(int mode) {
        if (mode < AUTO_MODE || mode > FAN_MODE){
            return false;
        }else{
            this.activeFan = mode != AUTO_MODE && mode != DRY_MODE;
            this.activeTemp = mode != FAN_MODE;
            this.currentMode = mode;
        }
        return true;
    }

    int getFan() {
        return currentFan;
    }

    boolean setFan(int fan) {
        if (fan < AUTO_FAN || fan > LEVEL3_FAN || !this.activeFan){
            return false;
        }else{
            this.currentFan = fan;
            return true;
        }
    }

    int getCurrentTemp() {
        return currentTemp;
    }
    // End of setters and getters //

    // Plus 1 and minus 1 degrees
    boolean setPlusTemp(){
        if (currentTemp != TEMP_MAX && isActiveTemp()){
            currentTemp++;
            return true;
        }else{
            return false;
        }
    }

    boolean setMinusTemp(){
        if (currentTemp != TEMP_MIN && isActiveTemp()){
            currentTemp--;
            return true;
        }else{
            return false;
        }
    }

    // Commands methods
    String getCommand (){
        String command = INIT_CHAIN;
        if (isActiveFan()) {
            command = command + FAN_MODES[currentFan];
            command = command + "F";
            command = command + REVERSE_FAN_MODES[currentFan];
            command = command + "0";
        }else{
            command = command + FAN_MODES[SPECIAL_FAN];
            command = command + "F";
            command = command + REVERSE_FAN_MODES[SPECIAL_FAN];
            command = command + "0";
        }
        if (isActiveTemp()) {
            command = command + TEMPS[currentTemp - TEMP_MIN];
            command = command + MODES[currentMode];
            command = command + REVERSE_TEMPS[currentTemp - TEMP_MIN];
            command = command + REVERSE_MODES[currentMode];
        }else{
            command = command + TEMPS[SPECIAL_TEMP];
            command = command + MODES[currentMode];
            command = command + REVERSE_TEMPS[SPECIAL_TEMP];
            command = command + REVERSE_MODES[currentMode];
        }
        return command;
    }
    String getSwing(){
        return SWING_CHAIN;
    }
    String getPowerOff(){
        return OFF_CHAIN;
    }
}