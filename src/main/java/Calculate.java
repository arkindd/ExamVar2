import RoomModel.Room;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

public class Calculate {

    public static CalculateResults calculates(InitInfo info, int priceL) {
        CalculateResults finalResult = new CalculateResults();
        ArrayList<Room> rooms = info.getRooms();
        ArrayList<OngoingWork> works = info.getWorks();
        for (OngoingWork work : works) {
            Room workingRoom = rooms.stream().filter(room -> room.getRoomCode() == work.getCode()).findFirst().get();
            double time = 0;
            double workCost = 0;
            double workingSquare = 0;
            double workingDepth = 0;
            double dose = 0;
            if (work.getTypeOfWork().equals("Поверхностная")) {
                switch (work.getPart()) {
                    case "Пол" ->
                            workingSquare = workingRoom.getRoomPollution().getFloorPollution().getPollutionSquare();
                    case "Стены" ->
                            workingSquare = workingRoom.getRoomPollution().getWallsPollution().getPollutionSquare();
                    case "Потолок" ->
                            workingSquare = workingRoom.getRoomPollution().getCeilingPollution().getPollutionSquare();
                }
                time = work.getTimeCost() / work.getCountOfWorkers() * workingSquare;
                workCost = work.getCost() * workingSquare + time * work.getCountOfWorkers() * priceL;
            } else if (work.getTypeOfWork().equals("Скол")) {
                switch (work.getPart()) {
                    case "Пол" -> {
                        workingSquare = workingRoom.getRoomPollution().getFloorPollution().getPollutionSquare();
                        workingDepth = Math.ceil(workingRoom.getRoomPollution().getFloorPollution().getPollutionDepth() / 10);
                    }
                    case "Стены" -> {
                        workingSquare = workingRoom.getRoomPollution().getWallsPollution().getPollutionSquare();
                        workingDepth = Math.ceil(workingRoom.getRoomPollution().getWallsPollution().getPollutionDepth() / 10);
                    }
                    case "Потолок" -> {
                        workingSquare = workingRoom.getRoomPollution().getCeilingPollution().getPollutionSquare();
                        workingDepth = Math.ceil(workingRoom.getRoomPollution().getCeilingPollution().getPollutionDepth() / 10);
                    }
                }
                time = (work.getTimeCost() / work.getCountOfWorkers()) * workingSquare * workingDepth;
                workCost = work.getCost() * workingSquare * workingDepth + time * work.getCountOfWorkers() * priceL;
            }
            if (!work.getWorkName().startsWith("Дистанционный")) {
                if (workingRoom.getFloor().endsWith("1")) {
                    dose = calculateMonteCarlo(workingRoom.getRadiationDoseRate(), 5) * time;
                } else dose = calculateMonteCarlo(workingRoom.getRadiationDoseRate(), 10) * time;
            }
            finalResult.setCollectiveDose(finalResult.getCollectiveDose() + dose * work.getCountOfWorkers());
            finalResult.setPersonalDose(finalResult.getPersonalDose() + dose);
            finalResult.setProjectCost(finalResult.getProjectCost() + workCost);
            finalResult.setProjectTime(finalResult.getProjectTime() + time);
        }
        finalResult.setPersonalDose(finalResult.getPersonalDose() / works.size());
        return finalResult;
    }

    public static double calculateMonteCarlo(double m, int q) {
        NormalDistribution distribution = new NormalDistribution(m, q);
        return distribution.sample();
    }
}