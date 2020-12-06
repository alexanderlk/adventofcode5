package no.alexander.AdventOfCode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdventOfCode5Application implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(AdventOfCode5Application.class);

	private static final String BYR = "byr";
	private static final String IYR = "iyr";
	private static final String EYR = "eyr";
	private static final String HGT = "hgt";
	private static final String HCL = "hcl";
	private static final String ECL = "ecl";
	private static final String PID = "pid";
	private static final String CID = "cid";
	
	public static void main(String[] args) {
		SpringApplication.run(AdventOfCode5Application.class, args);
	}

	@Override
	public void run(String... args) throws URISyntaxException, IOException {
		URL input = getClass().getClassLoader().getResource("input.txt");
		File file = new File(input.toURI());
		
		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		
		List<BoardingPass> boardingPasses = new ArrayList<>();
		for (String line : lines ) {
			boardingPasses.add(new BoardingPass(line));
		}
		
		partOne(boardingPasses);
		partTwo(boardingPasses);
	}
	
	private void partOne(List<BoardingPass> boardingPasses) {
		var maxSeatId = 0;
		
		for (BoardingPass bp : boardingPasses ) {
			var seatId = bp.getSeatId();
			if (seatId > maxSeatId) {
				maxSeatId = seatId;
			}
		}
		
		LOG.info("Part one - " + maxSeatId);
	}
	
	
	
	private void partTwo(List<BoardingPass> boardingPasses) {
		for ( int seatId = 0; seatId < 930; seatId++) {
			var lowSeat = false;
			var highSeat = false;
			var freeSeat = true;
			
			for (BoardingPass bp : boardingPasses) {
				if (bp.getSeatId() == seatId + 1) {
					highSeat = true;
				} else if (bp.getSeatId() == seatId - 1) {
					lowSeat = true;
				} else if (bp.getSeatId() == seatId) {
					freeSeat = false;
				}
			}
			
			if (highSeat && lowSeat && freeSeat) {
				LOG.info("Part two - " + seatId);
			} 
		}
		
	}
	
	private class BoardingPass {
		private static final int MIN_ROW = 0;
		private static final int MAX_ROW = 127;
		private static final int MIN_COL = 0;
		private static final int MAX_COL = 7;
		
		private int row;
		private int column;
		
		public BoardingPass(String code) {
			String rowCode = code.substring(0, 7);
			String columnCode = code.substring(7);
			
			calculateRow(MIN_ROW, MAX_ROW, rowCode);
			calculateColumn(MIN_COL, MAX_COL, columnCode);
		}
		
		private void calculateRow(int minRow, int maxRow, String rowCode) {
			if (rowCode.isEmpty()) {
				row = minRow;
			} else {
				String subCode = rowCode.substring(1);
				if (rowCode.startsWith("F")) {
					calculateRow(minRow, (int) Math.floor((double)(minRow + maxRow) / 2), subCode);
				} else if (rowCode.startsWith("B")) {
					calculateRow((int) Math.ceil((double)(minRow + maxRow) / 2), maxRow, subCode);
				}
			}
		}
		
		private void calculateColumn(int minColumn, int maxColumn, String columnCode) {
			if (columnCode.isEmpty()) {
				column = minColumn;
			} else {
				String subCode = columnCode.substring(1);
				if (columnCode.startsWith("R")) {
					calculateColumn((int)Math.ceil((double)(minColumn + maxColumn) / 2), maxColumn, subCode);
				} else if (columnCode.startsWith("L")) {
					calculateColumn(minColumn, (int)Math.floor((double)(minColumn + maxColumn) / 2), subCode);
				}
			}
		}
		
		public int getSeatId() {
			return row * 8 + column;
		}
	}
	
}
