package pl.javastart.streamsexercise;

import java.util.Comparator;

public class DateComparator implements Comparator<Payment> {
    @Override
    public int compare(Payment p1, Payment p2) {
        return -1 * (p1.getPaymentDate().compareTo(p2.getPaymentDate()));
    }
}
