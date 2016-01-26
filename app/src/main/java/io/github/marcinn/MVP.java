package io.github.marcinn;

import java.util.List;

import io.github.marcinn.common.ContextView;
import io.github.marcinn.common.ModelOps;
import io.github.marcinn.common.PresenterOps;
import io.github.marcinn.model.aidl.TrailerData;
import io.github.marcinn.model.aidl.TrailerType;

public interface MVP {

    interface RequiredViewOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorReason, TrailerType type);

        void synchronizeAll();
    }

    interface ProvidedPresenterOps extends PresenterOps<MVP.RequiredViewOps> {
        boolean getTrailersAsync(String query);

        boolean getTrailersAsync(TrailerType type);

        boolean getTrailersSync(String query);

        boolean getTrailersSync(TrailerType type);
    }

    interface RequiredPresenterOps extends ContextView {
        void displayResults(List<TrailerData> trailerData, String errorMessage, TrailerType type);

        void synchronizeAll();
    }

    interface ProvidedModelOps extends ModelOps<MVP.RequiredPresenterOps> {
        boolean getTrailersAsync(String query, TrailerType type);

        boolean getTrailersAsync(TrailerType type);

        List<TrailerData> getTrailersSync(String query, TrailerType type);

        List<TrailerData> getTrailersSync(TrailerType type);
    }
}
