import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        System.out.println(ExcelReader.readRoomsInfo(new File("src/main/resources/RoomsInformation.xlsx")));
        System.out.println(ExcelReader.readWorksInfo(new File("src/main/resources/WorksInformation.xlsx")));
    }
}