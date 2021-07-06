package nextstep.subway.utils;

import java.util.stream.Collectors;

import nextstep.subway.fare.domain.AgeDiscountCalculator;
import nextstep.subway.fare.domain.DistanceFareCalculator;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.StationPath;

public class FareCalculator {

	private static final int DEFAULT_AGE = 20;

	public static Fare getSubwayFare(Distance distance, Fare lineFare, int age) {
		Fare fare = DistanceFareCalculator.getInstance().calculate(distance);
		fare = fare.plus(lineFare);
		return AgeDiscountCalculator.getInstance().discount(fare, age);
	}

	public static Fare getSubwayFare(StationPath path, int age) {
		Distance distance = new Distance((int)path.getDistance());
		Lines lines = new Lines(
			path.getEdgeList().stream().map(SectionEdge::getLine).distinct().collect(Collectors.toList()));
		Fare lineFare = lines.getMaxLineFare();
		return getSubwayFare(distance, lineFare, age);
	}

	public static Fare getSubwayFare(StationPath path) {
		Distance distance = new Distance((int)path.getDistance());
		Lines lines = new Lines(
			path.getEdgeList().stream().map(SectionEdge::getLine).distinct().collect(Collectors.toList()));
		Fare lineFare = lines.getMaxLineFare();
		return getSubwayFare(distance, lineFare, DEFAULT_AGE);
	}
}
