package pl.xsware.domain.model.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonthName {

    JANUARY(1, "Sty"),
    FEBRUARY(2, "Lut"),
    MARCH(3, "Mar"),
    APRIL(4, "Kwi"),
    MAY(5, "Maj"),
    JUNE(6, "Cze"),
    JULY(7, "Lip"),
    AUGUST(8, "Sie"),
    SEPTEMBER(9, "Wrz"),
    OCTOBER(10, "Paź"),
    NOVEMBER(11, "Lis"),
    DECEMBER(12, "Gru");

    private final int number;
    private final String shortName;

    public static MonthName fromNumber(int number) {
        for (MonthName m : values()) {
            if (m.number == number) {
                return m;
            }
        }
        throw new IllegalArgumentException("Brak miesiąca o numerze: " + number);
    }
}
