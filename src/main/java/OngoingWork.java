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
}
