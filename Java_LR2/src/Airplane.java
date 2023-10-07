public class Airplane extends Vehicle {
    private int flightDistance; // Дальность полёта

    public Airplane() {
        this.setName("noName");
        this.setProductionYear(0);
        this.setFlightDistance(0);
    }

    public Airplane(String name, int productionYear, int flightDistance) {
        this.setName(name);
        this.setProductionYear(productionYear);
        this.setFlightDistance(flightDistance);
    }

    public String getFlightDistance() {
        return "Flight distance: " + flightDistance + " km";
    }

    public void setFlightDistance(int flightDistance) {
        if (flightDistance < 0) {
            this.flightDistance = 0;
        } else {
            this.flightDistance = flightDistance;
        }
    }

    @Override
    public void go() {
        System.out.println("I'm flying...");
    }

    public String print() {
        return this.getName() + ",\tProduction year: " + this.getProductionYear() + ",\t" + this.getFlightDistance();
    }
}
