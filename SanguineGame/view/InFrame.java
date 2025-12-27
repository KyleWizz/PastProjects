package sanguine.view;

/**
 * The view for our GUI.
 */
public interface InFrame {

  /**
   * Refreshes the games state.
   */
  void refresh();

  /**
   * makes the frame visible.
   *
   * @param visible shows= true and hidden = false.
   */
  void setVisible(boolean visible);

  /**
   * Adds to controller to handle input.
   *
   * @param features that implements.
   */
  void addFeatures(InFeatures features);
}
