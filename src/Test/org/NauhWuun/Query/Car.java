package org.NauhWuun.Query;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.parser.cqn.CQNParser;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.concurrenttrees.common.LazyIterator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

import static java.util.Arrays.asList;

import java.util.List;

public class Car
{
	public static final SimpleAttribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
		public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
	};

	public static final SimpleAttribute<Car, String> MANUFACTURER = new SimpleAttribute<Car, String>("manufacturer") {
		public String getValue(Car car, QueryOptions queryOptions) { return car.manufacturer; }
	};

	public static final SimpleAttribute<Car, String> MODEL = new SimpleAttribute<Car, String>("model") {
		public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
	};

	public static final SimpleAttribute<Car, Color> COLOR = new SimpleAttribute<Car, Color>("color") {
		public Color getValue(Car car, QueryOptions queryOptions) { return car.color; }
	};

	public static final SimpleAttribute<Car, Integer> DOORS = new SimpleAttribute<Car, Integer>("doors") {
		public Integer getValue(Car car, QueryOptions queryOptions) { return car.doors; }
	};

	public static final SimpleAttribute<Car, Double> PRICE = new SimpleAttribute<Car, Double>("price") {
		public Double getValue(Car car, QueryOptions queryOptions) { return car.price; }
	};

	public static final MultiValueAttribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
		public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.features; }
	};

	public static final MultiValueAttribute<Car, String> KEYWORDS = new MultiValueAttribute<Car, String>("keywords") {
		public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.keywords; }
	};

	public enum Color {RED, GREEN, BLUE, BLACK, WHITE}
	final int carId;
	final String manufacturer;
	final String model;
	final Color color;
	final int doors;
	final double price;
	final List<String> features;
	final List<String> keywords;

	public Car(int carId, String manufacturer, String model, Color color, int doors, double price, List<String> features, List<String> keywords) {
		this.carId = carId;
		this.manufacturer = manufacturer;
		this.model = model;
		this.color = color;
		this.doors = doors;
		this.price = price;
		this.features = features;
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "Car{" +
				"carId=" + carId +
				", manufacturer='" + manufacturer + '\'' +
				", model='" + model + '\'' +
				", color=" + color +
				", doors=" + doors +
				", price=" + price +
				", features=" + features +
				", keywords=" + keywords +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Car)) return false;

		Car car = (Car) o;
		return carId == car.carId;
	}

	@Override
	public int hashCode() {
		return carId;
	}

	public static Set<Car> createCollectionOfCars(int numCars) {
		Set<Car> cars = new LinkedHashSet<Car>(numCars);
		for (int carId = 0; carId < numCars; carId++) {
			cars.add(createCar(carId));
		}
		return cars;
	}

	public static Iterable<Car> createIterableOfCars(final int numCars) {
		final AtomicInteger count = new AtomicInteger();
		return new Iterable<Car>() {
			@Override
			public Iterator<Car> iterator() {
				return new LazyIterator<Car>() {
					@Override
					protected Car computeNext() {
						int carId = count.getAndIncrement();
						return carId < numCars ? createCar(carId) : endOfData();
					}
				};
			}
		};
	}

	public static Car createCar(int carId) {
		switch (carId % 10) {
			case 0: return new Car(carId, "Ford",   "Focus",   Car.Color.RED,   5, 5000.00, noFeatures(), noKeywords());
			case 1: return new Car(carId, "Ford",   "Fusion",  Car.Color.RED,   4, 3999.99, asList("hybrid"), asList("zulu"));
			case 2: return new Car(carId, "Ford",   "Taurus",  Car.Color.GREEN, 4, 6000.00, asList("grade a"), asList("alpha"));
			case 3: return new Car(carId, "Honda",  "Civic",   Car.Color.WHITE, 5, 4000.00, asList("grade b"), asList("bravo"));
			case 4: return new Car(carId, "Honda",  "Accord",  Car.Color.BLACK, 5, 3000.00, asList("grade c"), asList("very-good"));
			case 5: return new Car(carId, "Honda",  "Insight", Car.Color.GREEN, 3, 5000.00, noFeatures(), asList("alpha"));
			case 6: return new Car(carId, "Toyota", "Avensis", Car.Color.GREEN, 5, 5999.95, noFeatures(), asList("very-good-car"));
			case 7: return new Car(carId, "Toyota", "Prius",   Car.Color.BLUE,  3, 8500.00, asList("sunroof", "hybrid"), noKeywords());
			case 8: return new Car(carId, "Toyota", "Hilux",   Car.Color.RED,   5, 7800.55, noFeatures(), asList("very-good-car"));
			case 9: return new Car(carId, "BMW",    "M6",      Car.Color.BLUE,  2, 9000.23, asList("coupe"), asList("zulu"));
			default: throw new IllegalStateException();
		}
	}

	static List<String> noFeatures() {
		return Collections.<String>emptyList();
	}

	static List<String> noKeywords() {
		return Collections.<String>emptyList();
	}

	public static void main(String[] args) {
		CQNParser<Car> parser = CQNParser.forPojoWithAttributes(Car.class, createAttributes(Car.class));
		IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
		cars.addAll(Car.createCollectionOfCars(10));

		ResultSet<Car> results = parser.retrieve(cars,
				"and(" +
						"or(equal(\"manufacturer\", \"Ford\"), equal(\"manufacturer\", \"Honda\")), " +
						"lessThanOrEqualTo(\"price\", 5000.0), " +
						"not(in(\"color\", GREEN, WHITE))" +
						")");

		results.forEach(System.out::println); // Prints: Ford Focus, Ford Fusion, Honda Accord
	}
}