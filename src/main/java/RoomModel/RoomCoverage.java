package RoomModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomCoverage {

    private Coverage floorCoverage;
    private Coverage wallsCoverage;
    private Coverage ceilingCoverage;
}
