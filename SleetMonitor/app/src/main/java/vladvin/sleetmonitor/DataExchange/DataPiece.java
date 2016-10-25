package vladvin.sleetmonitor.DataExchange;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by Carioca on 07/10/2016.
 */

public class DataPiece implements Comparable<DataPiece>{
    public Object data;
    public UUID clientId;

    @Override
    public int compareTo(DataPiece dataPiece) {
        if(checkDataType(dataPiece.data) == DataType.SENSOR_DATA)
            if(checkDataType(this.data) == DataType.UPDATE_REQUEST)
                return 1;
            else
                return 0;
        else if(checkDataType(this.data) == DataType.UPDATE_REQUEST)
                return 0;
            else
                return -1;
    }

    public enum DataType {
        SENSOR_DATA,
        UPDATE_REQUEST,
        UNKNOWN
    }

    public DataPiece(Object data) {
        this.data = data;
        // FIXME: Call context to get id
        this.clientId = UUID.randomUUID();
    }

    public static DataType checkDataType(Object obj) {
        if (obj instanceof ArrayList) {
            return (((ArrayList) obj).size() > 0 &&
                    ((ArrayList) obj).get(0) instanceof SensorData)
                    ? DataType.SENSOR_DATA
                    : DataType.UNKNOWN;
        }
        // TODO: check MapUpdate type
        return DataType.UNKNOWN;
    }
}
