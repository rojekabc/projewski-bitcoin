package pl.projewski.cryptogames.gambler;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GamblerStatistics {
    private int win;
    private int loose;

    private int winInSeries;
    private int looseInSeries;

    @Getter
    private int longestWin;
    @Getter
    private int longestLoose;

    private Boolean lastResult;

    public void addResult(final boolean isWin) {
        // calculate win/loose ratio
        if (isWin) {
            win++;
        } else {
            loose++;
        }

        // setup first time value
        if (lastResult == null) {
            lastResult = isWin;
        }

        if (lastResult != isWin) {
            // result changed in comparison to previous
            if (isWin) {
                if (longestLoose < looseInSeries) {
                    longestLoose = looseInSeries;
                }
            } else {
                if (longestWin < winInSeries) {
                    longestWin = winInSeries;
                }
            }
            // reset series calculation
            looseInSeries = 0;
            winInSeries = 0;
        }
        // update calculation
        if (isWin) {
            winInSeries++;
        } else {
            looseInSeries++;
        }
        lastResult = isWin;
    }

    public Double getWinRatio() {
        return (double) win / (double) (loose + win);
    }

    public Double getLooseRatio() {
        return (double) loose / (double) (loose + win);
    }

    public int getRatio() {
        return loose + win;
    }
}
