
import java.util.ArrayList;
import java.util.List;

public class WithObserverPattern {

    // Interface for Observer
    interface Observer {
        void update(float temperature, float humidity);
    }

    // Interface for Subject
    interface Subject {
        void registerObserver(Observer o);

        void removeObserver(Observer o);

        void notifyObservers();
    }

    // Concrete Subject (WeatherStation)
    static class WeatherStation implements Subject {
        private List<Observer> observers;
        private float temperature;
        private float humidity;

        public WeatherStation() {
            observers = new ArrayList<>();
        }

        @Override
        public void registerObserver(Observer o) {
            observers.add(o);
        }

        @Override
        public void removeObserver(Observer o) {
            observers.remove(o);
        }

        @Override
        public void notifyObservers() {
            for (Observer observer : observers) {
                observer.update(temperature, humidity);
            }
        }

        public void setMeasurements(float temperature, float humidity) {
            this.temperature = temperature;
            this.humidity = humidity;
            measurementsChanged();
        }

        public void measurementsChanged() {
            notifyObservers();
        }
    }

    // Concrete Observer 1
    static class PhoneDisplay implements Observer {
        private float temperature;
        private float humidity;

        @Override
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

    // Concrete Observer 2
    static class WindowDisplay implements Observer {
        private float temperature;
        private float humidity;

        @Override
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

    // Concrete Observer 3
    static class StatisticsDisplay implements Observer {
        private float maxTemp = 0.0f;
        private float minTemp = 200;
        private float tempSum = 0.0f;
        private int numReadings;

        @Override
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

    public static void main(String[] args) {
        System.out.println("========== OBSERVER PATTERN ==========");

        WeatherStation weatherStation = new WeatherStation();

        PhoneDisplay phoneDisplay = new PhoneDisplay();
        WindowDisplay windowDisplay = new WindowDisplay();
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay();

        System.out.println("\n--- Registering Observers ---");
        weatherStation.registerObserver(phoneDisplay);
        weatherStation.registerObserver(windowDisplay);
        weatherStation.registerObserver(statisticsDisplay);

        System.out.println("\n--- First Update: 80F, 65% Humidity ---");
        weatherStation.setMeasurements(80, 65);

        System.out.println("\n--- Second Update: 82F, 70% Humidity ---");
        weatherStation.setMeasurements(82, 70);

        System.out.println("\n--- Removing Phone Display ---");
        weatherStation.removeObserver(phoneDisplay);
        
        System.out.println("\n--- Third Update: 78F, 90% Humidity ---");
        weatherStation.setMeasurements(78, 90);
    }
}
