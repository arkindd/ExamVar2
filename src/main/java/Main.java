import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        InitInfo info = new InitInfo();
        info.setRooms(ExcelReader.readRoomsInfo(new File("src/main/resources/RoomsInformation.xlsx")));
        info.setWorks(ExcelReader.readWorksInfo(new File("src/main/resources/WorksInformation.xlsx")));
        System.out.println(Calculate.calculates(info));

    }
}