package io.github.marcinn.model;

import io.github.marcinn.common.GenericAsyncTask;
import io.github.marcinn.common.GenericAsyncTaskOps;
import io.github.marcinn.model.aidl.TrailerType;

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
