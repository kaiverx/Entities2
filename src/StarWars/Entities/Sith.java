package StarWars.Entities;

public class Sith extends Entity {
    private String lightsaberColor;  // Цвет светового меча
    private int forceLevel;          // Уровень силы
    private boolean isMaster;        // Мастер ли?

    public Sith(String name, String fraction, int age, String planet, int powerLevel, String lightsaberColor, int forceLevel, boolean isMaster) {
        super(name, "Sith", fraction, age, planet, powerLevel);
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
                ", Мастер: " + isMaster;
    }

    @Override
    public String takeCare() {
        return "Ситх " + getName() + " использует темную сторону силы.";
    }

    public void setForceLevel(int forceLevel) {
        this.forceLevel = forceLevel;
    }

    @Override
    public void setLightsaberColor(String lightsaberColor) {
        this.lightsaberColor = lightsaberColor;
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    @Override
    public void setModelType(String modelType) {
        throw new UnsupportedOperationException("Метод не поддерживается для ситхов.");
    }

    @Override
    public void setBatteryLevel(int batteryLevel) {
        throw new UnsupportedOperationException("Метод не поддерживается для ситхов.");
    }

    @Override
    public void setCloneNumber(String cloneNumber) {
        throw new UnsupportedOperationException("Метод не поддерживается для ситхов.");
    }
    @Override
    public void setFunction(String function) {
        throw new UnsupportedOperationException("Метод не поддерживается для ситхов.");
    }
}
