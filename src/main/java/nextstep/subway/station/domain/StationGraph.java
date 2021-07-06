package nextstep.subway.station.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.excpetion.StationGraphException;

public class StationGraph {

	private WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;

	public StationGraph(Lines lines) {
		stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		setVertexes(lines.getStations());
		setEdges(lines.getSectionsByLine());
	}

	public StationPath getShortestPath(Station sourceStation, Station targetStation) {
		validateShortestPath(sourceStation, targetStation);
		List<GraphPath<Station, DefaultWeightedEdge>> paths = getPaths(sourceStation, targetStation);
		validatePaths(paths);
		return new StationPath(getMinPath(paths));
	}

	private void validateShortestPath(Station sourceStation, Station targetStation) {
		validateEqualsStations(sourceStation, targetStation);
		validateNotExistStation(sourceStation, targetStation);
	}

	private void validateNotExistStation(Station sourceStation, Station targetStation) {
		if (!stationGraph.containsVertex(sourceStation) || !stationGraph.containsVertex(targetStation)) {
			throw new StationGraphException(
				sourceStation.getName() + ", " + targetStation.getName() + " 둘 중 하나의 역이 존재하지 않습니다.");
		}
	}

	private void validateEqualsStations(Station sourceStation, Station targetStation) {
		if (sourceStation.equals(targetStation)) {
			throw new StationGraphException("출발역과 도착역이 같습니다.");
		}
	}

	private void validatePaths(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
		if (paths.isEmpty()) {
			throw new StationGraphException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private GraphPath<Station, DefaultWeightedEdge> getMinPath(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
		return paths.stream()
			.sorted((path, otherPath) -> (int)(path.getWeight() - otherPath.getWeight()))
			.findFirst()
			.orElseThrow(() -> new StationGraphException("경로들이 존재하지 않습니다."));
	}

	private List<GraphPath<Station, DefaultWeightedEdge>> getPaths(Station sourceStation, Station targetStation) {
		return new KShortestPaths<>(stationGraph, 100).getPaths(sourceStation,
			targetStation);
	}

	private void setVertexes(List<Station> stations) {
		for (Station station : stations) {
			stationGraph.addVertex(station);
		}
	}

	private void setEdges(List<Sections> sectionsByLine) {
		for (Sections sections : sectionsByLine) {
			sections.forEach(section -> stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value()));
		}
	}



}
