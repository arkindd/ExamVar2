package RoomModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomPollution {

    private Pollution floorPollution;
    private Pollution wallsPollution;
    private Pollution ceilingPollution;
}