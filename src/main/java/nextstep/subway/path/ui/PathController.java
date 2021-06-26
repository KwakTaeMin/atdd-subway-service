package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.excpetion.StationGraphException;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity findShortestDistance(@RequestParam Long source, @RequestParam Long target) {
		PathResponse pathResponse = pathService.findShortestDistance(source, target);
		return ResponseEntity.ok(pathResponse);
	}

	@ExceptionHandler(StationGraphException.class)
	public ResponseEntity handleIllegalArgsException(StationGraphException e) {
		return ResponseEntity.badRequest().build();
	}
}
