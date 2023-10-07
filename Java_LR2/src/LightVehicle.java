public class LightVehicle extends Vehicle {
    private String bodyType; // Тип кузова

    public LightVehicle() {
        this.setName("noName");
        this.setProductionYear(0);
        this.setBodyType("noBodyType");
    }

    public LightVehicle(String name, int productionYear, String bodyType) {
        this.setName(name);
        this.setProductionYear(productionYear);
        this.setBodyType(bodyType);
    }

    public String getBodyType() {
        return "Body type: " + bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public void go() {
        System.out.println("I'm going...");
    }

    public String print() {
        return this.getName() + ",\t\tProduction year: " + this.getProductionYear() + ",\t" + this.getBodyType();
    }
}
