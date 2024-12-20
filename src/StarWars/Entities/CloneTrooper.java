package StarWars.Entities;

public class CloneTrooper extends Entity {
    private String cloneNumber;  // Номер клона

    public CloneTrooper(String name, String fraction, int age, String planet, int powerLevel, String cloneNumber) {
        super(name, "CloneTrooper", fraction, age, planet, powerLevel);
        this.cloneNumber = cloneNumber;
    }

    public String getCloneNumber() {
        return cloneNumber;
    }

    public String toString() {
        return super.toString() + ", Номер клона: " +  cloneNumber;
    }

    @Override
    public String takeCare() {
        return "Клон " + getName() + " выполняет приказ.";
    }

    @Override
    public void setCloneNumber(String cloneNumber) {
        this.cloneNumber = cloneNumber;  // Устанавливаем новый номер клона
    }

    // Эти методы не имеют смысла для клонов, выбрасываем исключение
    @Override
    public void setLightsaberColor(String lightsaberColor) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }

    @Override
    public void setIsMaster(boolean isMaster) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }

    @Override
    public void setModelType(String modelType) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }

    @Override
    public void setBatteryLevel(int batteryLevel) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }

    @Override
    public void setForceLevel(int forceLevel) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }
    @Override
    public void setFunction(String function) {
        throw new UnsupportedOperationException("Метод не поддерживается для клонов.");
    }
}
