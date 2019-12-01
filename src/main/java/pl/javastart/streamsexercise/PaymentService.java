package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class PaymentService {

    private PaymentRepository paymentRepository;
    private DateTimeProvider dateTimeProvider;

    PaymentService(PaymentRepository paymentRepository, DateTimeProvider dateTimeProvider) {
        this.paymentRepository = paymentRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    List<Payment> findPaymentsSortedByDateDesc() {
        List<Payment> payments = paymentRepository.findAll();
        List<Payment> paymentsSortedByDate = new ArrayList<>();
        payments.stream()
                .sorted(new DateComparator())
                .forEach(paymentsSortedByDate::add);
        return paymentsSortedByDate;
    }

    List<Payment> findPaymentsForCurrentMonth() {
        List<Payment> payments = paymentRepository.findAll();
        List<Payment> paymentsCurrentMonth = new ArrayList<>();
        payments.stream()
                .filter(payment -> payment.getPaymentDate().getYear()
                        == dateTimeProvider.yearMonthNow().getYear())
                .filter(payment -> payment.getPaymentDate().getMonth()
                        .equals(dateTimeProvider.yearMonthNow().getMonth()))
                .forEach(paymentsCurrentMonth::add);
        return paymentsCurrentMonth;
    }

    List<Payment> findPaymentsForGivenMonth(YearMonth yearMonth) {
        List<Payment> payments = paymentRepository.findAll();
        List<Payment> paymentsGivenMonth = new ArrayList<>();
        payments.stream()
                .filter(payment -> payment.getPaymentDate().getYear() == yearMonth.getYear())
                .filter(payment -> payment.getPaymentDate().getMonth().equals(yearMonth.getMonth()))
                .forEach(paymentsGivenMonth::add);
        return paymentsGivenMonth;
    }

    List<Payment> findPaymentsForGivenLastDays(int days) {
        throw new RuntimeException("Not implemented");
    }

    Set<Payment> findPaymentsWithOnePaymentItem() {
        throw new RuntimeException("Not implemented");
    }

    Set<String> findProductsSoldInCurrentMonth() {
        throw new RuntimeException("Not implemented");
    }

    BigDecimal sumTotalForGivenMonth(YearMonth yearMonth) {
        throw new RuntimeException("Not implemented");
    }

    BigDecimal sumDiscountForGivenMonth(YearMonth yearMonth) {
        throw new RuntimeException("Not implemented");
    }

    List<PaymentItem> getPaymentsForUserWithEmail(String userEmail) {
        throw new RuntimeException("Not implemented");
    }

    Set<Payment> findPaymentsWithValueOver(int value) {
        throw new RuntimeException("Not implemented");
    }

}
