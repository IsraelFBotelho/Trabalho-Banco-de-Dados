package dto;

public class TemperatureDataDTO {
    private int cityId;
    private String cityName;
    private float maxAvg;
    private float max;
    private float minAvg;
    private float min;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getMaxAvg() {
        return maxAvg;
    }

    public void setMaxAvg(float maxAvg) {
        this.maxAvg = maxAvg;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMinAvg() {
        return minAvg;
    }

    public void setMinAvg(float minAvg) {
        this.minAvg = minAvg;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }
}
