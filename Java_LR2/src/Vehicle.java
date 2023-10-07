abstract class Vehicle {
    private String name;
    private int productionYear;

    public String getName() {
        return "Name: " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        if (productionYear < 0) {
            this.productionYear = 0;
        } else {
            this.productionYear = productionYear;
        }
    }

    public abstract void go();

    public abstract String print();
}