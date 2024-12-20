package StarWars.Entities;

public class Droid extends Entity {
    private String modelType;      // Тип модели
    private String function;       // Функция (например, медицинский, боевой)
    private int batteryLevel;      // Уровень батареи

    public Droid(String name, String fraction, int age, String planet, int powerLevel, String modelType, String function, int batteryLevel) {
        super(name, "Droid", fraction, age, planet, powerLevel);
        this.modelType = modelType;
        this.function = function;
        this.batteryLevel = batteryLevel;
    }

    public String getModelType() {
        return modelType;
    }

    public String getFunction() {
        return function;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public String toString() {
        return super.toString() + ", Модуль дроида: " + modelType +
                ", Функция: " + function +
                ", Уровень энергии: " + batteryLevel;
    }

    @Override
    public String takeCare() {
        return "Дроид " + getName() + " выполняет свою задачу.";
    }

    @Override
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    @Override
    public void setFunction(String function) {
        this.function = function;
    }

    // Эти методы не имеют смысла для дроидов, выбрасываем исключение
    @Override
    public void setLightsaberColor(String lightsaberColor) {
        throw new UnsupportedOperationException("Метод не поддерживается для дроидов.");
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        throw new UnsupportedOperationException("Метод не поддерживается для дроидов.");
    }

    @Override
    public void setForceLevel(int forceLevel) {
        throw new UnsupportedOperationException("Метод не поддерживается для дроидов.");
    }

    @Override
    public void setCloneNumber(String cloneNumber) {
        throw new UnsupportedOperationException("Метод не поддерживается для дроидов.");
    }
}
