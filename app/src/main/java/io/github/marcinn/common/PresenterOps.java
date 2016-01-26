package io.github.marcinn.common;

public interface PresenterOps<RequiredViewOps> {
    void onCreate(RequiredViewOps view);
    void onConfigurationChange(RequiredViewOps view);
    void onDestroy(boolean isChangingConfigurations);
}
