package StarWars.Entities;

abstract public class Entity {
    private String name;         // Имя
    private String type;         // Тип (Jedi, Sith, Droid, etc.)
    private String fraction;     // Лояльность (например, Республика, Империя)
    private int age;             // Возраст
    private String planet;       // Планета
    private int powerLevel;    // Уровень силы или мощи

    public Entity(String name, String type, String fraction, int age, String planet, int powerLevel) {
        this.name = name;
        this.type = type;
        this.fraction = fraction;
        this.age = age;
        this.planet = planet;
        this.powerLevel = powerLevel;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFraction() {
        return fraction;
    }

    public int getAge() {
        return age;
    }

    public String getPlanet() {
        return planet;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public abstract String takeCare();

    public String toString() {
        return "Имя: " + name + ", Фракция: " + fraction + ", Возраст: " + age +
                ", Планета: " + planet + ", Уровень силы: " + powerLevel;
    }
    public abstract void setBatteryLevel(int batteryLevel);

    public abstract void setIsMaster(boolean isMaster);

    public abstract void setModelType(String modelType);

    public abstract void setLightsaberColor(String lightsaberColor);

    public abstract void setForceLevel(int forceType);

    public abstract void setCloneNumber(String cloneNumber);

    public abstract void setFunction(String function);

    public String getLightsaberColor() {
        throw new UnsupportedOperationException("Метод не поддерживается для данного типа сущности.");
    }

    public int getForceLevel() {
        throw new UnsupportedOperationException("Метод не поддерживается для данного типа сущности.");
    }
}
