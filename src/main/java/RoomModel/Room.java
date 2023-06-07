package RoomModel;

import lombok.Data;

@Data
public class Room {

    private int roomCode;
    private String roomName;
    private String floor;
    private GeometricSizes sizes;
    private Materials materials;
    private RoomCoverage roomCoverage;
    private RoomPollution roomPollution;
    private double radiationDoseRate;
    private double volumeActivityAlphaBetaRadiation;
}
