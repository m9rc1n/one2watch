package vandy.mooc.model;

import vandy.mooc.common.GenericAsyncTask;
import vandy.mooc.common.GenericAsyncTaskOps;
import vandy.mooc.model.aidl.TrailerType;

public class TrailerAsyncTask<Params, Progress, Result, Ops extends GenericAsyncTaskOps<Params, Progress, Result>>
        extends GenericAsyncTask<Params, Progress, Result, Ops> {

    private final TrailerType type;

    public TrailerAsyncTask(Ops ops, TrailerType type) {
        super(ops);
        this.type = type;
    }

    public TrailerType getType() {
        return type;
    }
}
