import lombok.Data;

@Data
public class OngoingWork {

    private int number;
    private int code;
    private String room;
    private String part;
    private int elementCode;
    private String workName;
    private String description;
    private String typeOfWork;
    private int cost;
    private int priority;
    private int timeCost;
    private int countOfWorkers;

    @Override
    public String toString() {
        return "Работа №" + number +
                ", код комнаты: " + code +
                ", участок: " + part +
                ", код элемента: " + elementCode +
                ", имя работы: " + workName +
                ", описание работы: " + description+
                ", тип: " + typeOfWork +
                ", стоимость: " + cost +
                ", очередность: " + priority +
                ", норма времени: " + timeCost +
                ", кол-во работников: " + countOfWorkers;
    }
}
