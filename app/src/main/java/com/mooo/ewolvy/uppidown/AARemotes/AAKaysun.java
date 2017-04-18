package com.mooo.ewolvy.uppidown.AARemotes;


public class AAKaysun extends AASuper {

    private static final String INIT_CHAIN = "B24D";
    private static final String SWING_CHAIN = "B24D6B94E01F";
    private static final String OFF_CHAIN = "B24D7B84E01F";

    private final char[] FAN_MODES= {'B', '9', '5', '3', '1'};
    private final char[] REVERSE_FAN_MODES = {'4', '6', 'A', 'C', 'E'};
    private final char[] TEMPS = {'0', '1', '3', '2', '6', '7', '5', '4', 'C', 'D', '9', '8', 'A', 'B', 'E'};
    private final char[] REVERSE_TEMPS = {'F', 'E', 'C', 'D', '9', '8', 'A', 'B', '3', '2', '6', '7', '5', '4', '1'};
    private final char[] MODES = {'8', '0', '4', 'C', '4'};
    private final char[] REVERSE_MODES = {'7', 'F', 'B', '3', 'B'};

    public AAKaysun(int stateMode,
                    int stateFan,
                    int stateTemp,
                    boolean stateOn,
                    String statePath){
        super();

        TEMP_MIN = 17;
        TEMP_MAX = 30;

        serverPath = statePath;

        // Indicar que todos los modos están disponibles
        for (int x = 0; x<5; x++){AVAILABLE_MODES[x] = true;}

        if (!setMode(stateMode)){
            setMode (AUTO_MODE);
        }

        isOn = stateOn;
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

    public String getCommand (){
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
            command = command + TEMPS[TEMP_MAX + 1];
            command = command + MODES[currentMode];
            command = command + REVERSE_TEMPS[TEMP_MAX + 1];
            command = command + REVERSE_MODES[currentMode];
        }

        return command;
    }

    @Override
    public String getSwing() {
        return SWING_CHAIN;
    }

    @Override
    public String getPowerOff() {
        return OFF_CHAIN;
    }
}
