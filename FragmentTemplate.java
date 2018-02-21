package temp;

public interface FragmentTemplate {
	/*
	 * Starts the sensor/sensors used in the fragment and starts game itself
	 */
	public void start();
	/*
	 * Stops the sensor/sensors used in the fragment and stops the game
	 */
	public void stop();
	/*
	 * Sends the points in the score gathered by the player during the game
	 */
	public int getScore();
	/*
	 * sets the score to 0
	 */
	public void reset();
}
