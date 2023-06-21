import RoomModel.Room;
import lombok.Data;

import java.util.ArrayList;

@Data
public class InitInfo {

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<OngoingWork> works = new ArrayList<>();
}