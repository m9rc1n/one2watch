package vandy.mooc.model.aidl;

import vandy.mooc.model.aidl.TrailerData;
import java.util.List;

interface TrailerResults {
    oneway void sendResults(in TrailerData results);
    oneway void sendError(in String reason);
}
