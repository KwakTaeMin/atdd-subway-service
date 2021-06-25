package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.StationPathCalculator;

@Service
@Transactional
public class PathService {

	LineRepository lineRepository;
	StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findShortestDistance(Long source, Long target) {
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = findStationById(source);
		Station targetStation = findStationById(target);
		return StationPathCalculator.findShortestDistance(lines, sourceStation, targetStation);
	}

	public Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(RuntimeException::new);
	}
}
