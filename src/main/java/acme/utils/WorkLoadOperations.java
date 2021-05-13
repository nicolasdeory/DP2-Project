package acme.utils;

public final class WorkLoadOperations {
    /*
     * Workload Format: X.YYY where: X is the number of hours YYY is the number of
     * minutes
     *
     * We receive a double number with decimals between 0 and 9 We need to normalize
     * that decimals between 0 and 59
     */

    public static Double formatWorkload(Double workload) {
        Integer hours = workload.intValue();
        Double decimals = (workload - hours);
        Integer minutes = (int) Math.round(Math.floor(decimals * 100) / 100 * 60);
        while (minutes > 59) {
            hours++;
            minutes -= 60;
        }
        return hours + minutes / 100.;
    }

    public static Double unformatWorkload(Double workload) {
        Integer hours = workload.intValue();
        Integer minutes = (int) Math.round((workload - hours) * 100);
        Double decimals = minutes / 60.0;
        return hours + decimals;
    }

    public static boolean isFormatedWorkload(Double workload) {
        Long hours = workload.longValue();
        Double decimals = (workload - hours);
        Long minutes =  Math.round(decimals*100);
        return minutes < 60 && minutes>=0;
    }

    private WorkLoadOperations() { }

}
