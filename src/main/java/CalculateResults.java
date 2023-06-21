import lombok.Data;

import java.util.ArrayList;

@Data
public class CalculateResults {

    private double projectCost;
    private double projectTime;
    private double collectiveDose;
    private double personalDose;
    private ArrayList<double[]> doses;
}
