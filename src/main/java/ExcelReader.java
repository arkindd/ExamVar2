import RoomModel.Coverage;
import RoomModel.GeometricSizes;
import RoomModel.Materials;
import RoomModel.Pollution;
import RoomModel.Room;
import RoomModel.RoomCoverage;
import RoomModel.RoomPollution;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelReader {

    public static ArrayList<Room> readRoomsInfo(File file) throws IOException, InvalidFormatException {
        ArrayList<Room> rooms = new ArrayList<>();
        XSSFWorkbook book = new XSSFWorkbook(file);
        XSSFSheet sheet = book.getSheetAt(0);
        for (int i = 7; i <= 16; i++) {
            XSSFRow row = sheet.getRow(i);
            Room room = new Room();
            room.setRoomCode((int) row.getCell(0).getNumericCellValue());
            room.setRoomName(row.getCell(1).getStringCellValue());
            room.setFloor(row.getCell(2).getStringCellValue());
            room.setSizes(new GeometricSizes(
                    row.getCell(3).getNumericCellValue(),
                    row.getCell(4).getNumericCellValue(),
                    row.getCell(5).getNumericCellValue(),
                    row.getCell(6).getNumericCellValue(),
                    row.getCell(7).getNumericCellValue(),
                    row.getCell(8).getNumericCellValue(),
                    row.getCell(9).getNumericCellValue()
            ));
            room.setMaterials(new Materials(
                    row.getCell(10).getStringCellValue(),
                    row.getCell(11).getStringCellValue()
            ));
            room.setRoomCoverage(
                    new RoomCoverage(
                            new Coverage(
                                    row.getCell(12).getStringCellValue(),
                                    row.getCell(13).getNumericCellValue()
                            ),
                            new Coverage(
                                    row.getCell(14).getStringCellValue(),
                                    row.getCell(15).getNumericCellValue()
                            ),
                            new Coverage(
                                    row.getCell(16).getStringCellValue(),
                                    row.getCell(17).getNumericCellValue()
                            )
                    )
            );
            room.setRoomPollution(
                    new RoomPollution(
                            new Pollution(
                                    row.getCell(18).getNumericCellValue(),
                                    row.getCell(19).getNumericCellValue()
                            ),
                            new Pollution(
                                    row.getCell(20).getNumericCellValue(),
                                    row.getCell(21).getNumericCellValue()
                            ),
                            new Pollution(
                                    row.getCell(22).getNumericCellValue(),
                                    row.getCell(23).getNumericCellValue()
                            )
                    )
            );
            room.setRadiationDoseRate(row.getCell(24).getNumericCellValue());
            room.setVolumeActivityAlphaBetaRadiation(row.getCell(25).getNumericCellValue());
            rooms.add(room);
        }
        return rooms;
    }

    public static ArrayList<OngoingWork> readWorksInfo(File file) throws IOException, InvalidFormatException {
        ArrayList<OngoingWork> works = new ArrayList<>();
        XSSFWorkbook book = new XSSFWorkbook(file);
        XSSFSheet sheet = book.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            OngoingWork work = new OngoingWork();
            work.setNumber((int) row.getCell(0).getNumericCellValue());
            work.setCode((int) row.getCell(1).getNumericCellValue());
            work.setRoom(row.getCell(2).getStringCellValue());
            work.setPart(row.getCell(3).getStringCellValue());
            work.setElementCode((int) row.getCell(4).getNumericCellValue());
            work.setWorkName(row.getCell(5).getStringCellValue());
            work.setDescription(row.getCell(6).getStringCellValue());
            work.setTypeOfWork(row.getCell(7).getStringCellValue());
            work.setCost((int) row.getCell(8).getNumericCellValue());
            work.setPriority((int) row.getCell(9).getNumericCellValue());
            work.setTimeCost((int) row.getCell(10).getNumericCellValue());
            work.setCountOfWorkers((int) row.getCell(11).getNumericCellValue());
            works.add(work);
        }
        return works;
    }
}
