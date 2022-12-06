package dto;

public class DeforestationDataDTO {
    private float deforestation;
    private int year;
    private String cityName;

    public float getDeforestation() {
        return deforestation;
    }

    public void setDeforestation(float deforestation) {
        this.deforestation = deforestation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
