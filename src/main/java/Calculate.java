
import RoomModel.Room;

import java.util.ArrayList;

public class Calculate {

    public static CalculateResults calculates(InitInfo info) {
        ArrayList<CalculateResults> allResults = new ArrayList<>();
        CalculateResults finalResult = new CalculateResults();
        ArrayList<Room> rooms = info.getRooms();
        ArrayList<OngoingWork> works = info.getWorks();
        for (OngoingWork work : works) {
            CalculateResults results = new CalculateResults();
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
                workCost = work.getCost() * workingSquare + time * work.getCountOfWorkers() * 1;
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
                workCost = work.getCost() * workingSquare * workingDepth + time * work.getCountOfWorkers() * 1;
            }
            dose = workingRoom.getRadiationDoseRate() * time;
            results.setPersonalDose(dose);
            results.setCollectiveDose(dose * work.getCountOfWorkers());
            System.out.println(work.getTypeOfWork() + ", время: " + time + " стоимость: " + workCost + " часть: " + work.getPart());
            System.out.println("Персональная доза " + results.getPersonalDose() + ", коллективная: " + results.getCollectiveDose());
            results.setProjectCost(results.getProjectCost() + workCost);
            results.setProjectTime(results.getProjectTime() + time);
            allResults.add(results);
            finalResult.setCollectiveDose(finalResult.getCollectiveDose() + dose * work.getCountOfWorkers());
            finalResult.setPersonalDose(finalResult.getPersonalDose() + dose);
            finalResult.setProjectCost(finalResult.getProjectCost() + results.getProjectCost());
            finalResult.setProjectTime(finalResult.getProjectTime() + results.getProjectTime());
        }
        finalResult.setPersonalDose(finalResult.getPersonalDose() / allResults.size());
        return finalResult;
    }
}