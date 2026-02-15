
public class WithoutObserverPattern {

    static class PhoneDisplay {
        private float temperature;
        private float humidity;

        public void update(float temperature, float humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
            display();
        }

        public void display() {
            System.out.println(
                    "Phone Display: Current conditions: " + temperature + "F degrees and " + humidity + "% humidity");
        }
    }

    static class WindowDisplay {
        private float temperature;
        private float humidity;

        public void update(float temperature, float humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
            display();
        }

        public void display() {
            System.out.println(
                    "Window Display: Current conditions: " + temperature + "F degrees and " + humidity + "% humidity");
        }
    }

    static class StatisticsDisplay {
        private float maxTemp = 0.0f;
        private float minTemp = 200;
        private float tempSum = 0.0f;
        private int numReadings;

        public void update(float temperature, float humidity) {
            tempSum += temperature;
            numReadings++;

            if (temperature > maxTemp) {
                maxTemp = temperature;
            }

            if (temperature < minTemp) {
                minTemp = temperature;
            }

            display();
        }

        public void display() {
            System.out.println("Statistics Display: Avg/Max/Min temperature = " + (tempSum / numReadings)
                    + "/" + maxTemp + "/" + minTemp);
        }
    }

    static class WeatherStation {
        private float temperature;
        private float humidity;

        // Tightly coupled to concrete implementations
        private PhoneDisplay phoneDisplay;
        private WindowDisplay windowDisplay;
        private StatisticsDisplay statisticsDisplay;

        public WeatherStation(PhoneDisplay phoneDisplay, WindowDisplay windowDisplay,
                StatisticsDisplay statisticsDisplay) {
            this.phoneDisplay = phoneDisplay;
            this.windowDisplay = windowDisplay;
            this.statisticsDisplay = statisticsDisplay;
        }

        public void setMeasurements(float temperature, float humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
            measurementsChanged();
        }

        public void measurementsChanged() {
            // Tightly coupled updates
            phoneDisplay.update(temperature, humidity);
            windowDisplay.update(temperature, humidity);
            statisticsDisplay.update(temperature, humidity);
        }
    }

    public static void main(String[] args) {
        System.out.println("========== WITHOUT OBSERVER PATTERN ==========");

        PhoneDisplay phoneDisplay = new PhoneDisplay();
        WindowDisplay windowDisplay = new WindowDisplay();
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay();
        
        WeatherStation weatherStation = new WeatherStation(phoneDisplay, windowDisplay, statisticsDisplay);

        System.out.println("\n--- First Update: 80F, 65% Humidity ---");
        weatherStation.setMeasurements(80, 65);

        System.out.println("\n--- Second Update: 82F, 70% Humidity ---");
        weatherStation.setMeasurements(82, 70);

        System.out.println("\n--- Third Update: 78F, 90% Humidity ---");
        weatherStation.setMeasurements(78, 90);
    }
}
