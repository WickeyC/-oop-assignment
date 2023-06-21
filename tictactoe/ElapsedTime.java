package tictactoe;

import java.time.Duration;
import java.time.LocalDateTime;

// The ElapsedTime class is a class used to 
// track the time used when playing the tic-tac-toe game.
public class ElapsedTime {
  private final LocalDateTime startTime;
  private Duration duration;

  public ElapsedTime() {
    this.startTime = LocalDateTime.now();
  }

  public void stop() {
    LocalDateTime endTime = LocalDateTime.now();
    duration = Duration.between(this.startTime, endTime);
  }

  public long getSec() {
    return duration.toSeconds();
  }

  public String getMinSec() {
    long minutes = this.getSec() / 60;
    long seconds = this.getSec() % 60;
    return minutes + (minutes > 1 ? " minutes " : " minute ")
        + seconds + (seconds > 1 ? " seconds" : " second");
  }
}
