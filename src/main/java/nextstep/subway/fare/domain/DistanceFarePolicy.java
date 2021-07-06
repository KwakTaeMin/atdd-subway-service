package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public interface DistanceFarePolicy {
	int DEFAULT_FARE = 1250;

	Fare calculate(Distance distance);
	boolean isAccepted(Distance distance);
}
