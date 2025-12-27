package sanguine.view;

/**
 * Decides what each panel should do.
 * Calls our features listener.
 */
public interface InPanelView {

  /**
   * our add features listener for panel class.
   *
   * @param features our features.
   */
  void addFeaturesListener(InFeatures features);
}
