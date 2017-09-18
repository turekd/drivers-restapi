package it.dturek.github.drivers.domain;

import javax.persistence.*;

@Entity(name = "car_models")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private CarBrand brand;

    @Column(nullable = false)
    private String name;

    public CarModel() {
    }

    public CarModel(CarBrand brand, String name) {
        this.brand = brand;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public void setBrand(CarBrand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "id=" + id +
                ", brand=" + brand +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarModel carModel = (CarModel) o;

        if (!id.equals(carModel.id)) return false;
        if (!brand.equals(carModel.brand)) return false;
        return name.equals(carModel.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
