package io.github.marcinn.common;

public interface ModelOps<RequiredPresenterOps> {

    void onCreate(RequiredPresenterOps view);
    void onDestroy(boolean isChangingConfigurations);
}
