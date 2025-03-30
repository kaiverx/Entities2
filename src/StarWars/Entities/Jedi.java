package StarWars.Entities;

public class Jedi extends Entity {
    private String lightsaberColor;  // Цвет светового меча
    private int forceLevel;          // Уровень силы
    private boolean isMaster;   // Гранд-мастер ли?

    public Jedi(String name, String fraction, int age, String planet, int powerLevel, String lightsaberColor, int forceLevel, boolean isMaster) {
        super(name, "Jedi", fraction, age, planet, powerLevel);
        this.lightsaberColor = lightsaberColor;
        this.forceLevel = forceLevel;
        this.isMaster = isMaster;
    }

    public String getLightsaberColor() {
        return lightsaberColor;
    }

    public int getForceLevel() {
        return forceLevel;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String toString() {
        return super.toString() + ", Цвет светового меча: " + lightsaberColor +
                ", Уровень владения силой: " + forceLevel +
                ", Гранд-мастер: " + isMaster;
    }

    @Override
    public String takeCare() {
        return "Джедай " + getName() + " заботится о благе галактики!";
    }

    @Override
    public void setLightsaberColor(String lightsaberColor) {
        this.lightsaberColor = lightsaberColor;  // Устанавливаем новый цвет меча
    }

    @Override
    public void setForceLevel(int forceLevel) {
        this.forceLevel = forceLevel;
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    @Override
    public void setModelType(String modelType) {
        throw new UnsupportedOperationException("Метод не поддерживается для джедаев.");
    }

    @Override
    public void setBatteryLevel(int batteryLevel) {
        throw new UnsupportedOperationException("Метод не поддерживается для джедаев.");
    }

    @Override
    public void setCloneNumber(String cloneNumber) {
        throw new UnsupportedOperationException("Метод не поддерживается для джедаев.");
    }

    @Override
    public void setFunction(String function) {
        throw new UnsupportedOperationException("Метод не поддерживается для джедаев.");
    }
}
