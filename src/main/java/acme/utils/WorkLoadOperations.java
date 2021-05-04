package acme.utils;

public class WorkLoadOperations {
    /*
     * Workload Format: X.YYY where: X is the number of hours YYY is the number of
     * minutes
     *
     * We receive a double number with decimals between 0 and 9 We need to normalize
     * that decimals between 0 and 59
     */

    public static Double formatWorkload(Double workload) {
        int hours = workload.intValue();
        double decimals = (workload - hours);
        int minutes = (int) Math.round(Math.floor(decimals * 100) / 100 * 60);
        while (minutes > 59) {
            hours++;
            minutes -= 60;
        }
        return hours + minutes / 100.;
    }

    public static Double unformatWorkload(Double workload) {
        int hours = workload.intValue();
        int minutes = (int) (workload - hours) * 100;
        double decimals = minutes / 60;
        return hours + decimals;
    }

    public static Boolean isFormatedWorkload(Double workload) {
        int hours = workload.intValue();
        double decimals = (workload - hours);
        int minutes = (int) decimals * 100;
        return minutes < 60;
    }

}
