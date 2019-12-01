package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
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
        List<Payment> payments = getPayments();
        List<Payment> paymentsSortedByDate = new ArrayList<>();
        payments.stream()
                .sorted(new DateComparator())
                .forEach(paymentsSortedByDate::add);
        return paymentsSortedByDate;
    }

    List<Payment> findPaymentsForCurrentMonth() {
        List<Payment> payments = getPayments();
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
        List<Payment> payments = getPayments();
        List<Payment> paymentsGivenMonth = new ArrayList<>();
        payments.stream()
                .filter(payment -> payment.getPaymentDate().getYear() == yearMonth.getYear())
                .filter(payment -> payment.getPaymentDate().getMonth().equals(yearMonth.getMonth()))
                .forEach(paymentsGivenMonth::add);
        return paymentsGivenMonth;
    }


    List<Payment> findPaymentsForGivenLastDays(int days) {
        List<Payment> payments = getPayments();
        List<Payment> paymentsForGivenDays = new ArrayList<>();
        payments.stream()
                .filter(payment -> payment.getPaymentDate().getYear() == dateTimeProvider.yearMonthNow().getYear())
                .filter(payment -> dateTimeProvider.zonedDateTimeNow().getDayOfYear()
                        - payment.getPaymentDate().getDayOfYear() <= days)
                .filter(payment -> payment.getPaymentDate().getDayOfYear() -
                        dateTimeProvider.zonedDateTimeNow().getDayOfYear() <= 0)
                .forEach(paymentsForGivenDays::add);
        return paymentsForGivenDays;
    }

    Set<Payment> findPaymentsWithOnePaymentItem() {
        List<Payment> payments = getPayments();
        Set<Payment> paymentsWithOneItem = new HashSet<>();
        payments.stream()
                .filter(payment -> payment.getPaymentItems().size() == 1)
                .forEach(paymentsWithOneItem::add);
        return paymentsWithOneItem;
    }

    Set<String> findProductsSoldInCurrentMonth() {
        Set<String> products = new HashSet<>();
        findPaymentsForCurrentMonth().stream()
                .map(payment -> payment.getPaymentItems())
                .forEach(paymentItems -> paymentItems.stream()
                        .forEach(paymentItem -> products.add(paymentItem.getName())));
        return products;
    }

    BigDecimal sumTotalForGivenMonth(YearMonth yearMonth) {
        Set<BigDecimal> prices = new HashSet<>();
        int sum = 0;
        findPaymentsForGivenMonth(yearMonth).stream()
                .map(payment -> payment.getPaymentItems())
                .forEach(paymentItems -> paymentItems.stream()
                        .forEach(paymentItem -> prices.add(paymentItem.getFinalPrice())));
        for (BigDecimal price : prices) {
            sum += price.intValue();
        }
        return new BigDecimal(sum);
    }

    BigDecimal sumDiscountForGivenMonth(YearMonth yearMonth) {
        List<BigDecimal> prices = new ArrayList<>();
        int sum = 0;
        findPaymentsForGivenMonth(yearMonth).stream()
                .map(payment -> payment.getPaymentItems())
                .forEach(paymentItems -> paymentItems.stream()
                        .forEach(paymentItem -> prices.add(paymentItem.getRegularPrice())));
        for (BigDecimal price : prices) {
            sum += price.intValue();
        }
        sum = sum - sumTotalForGivenMonth(yearMonth).intValue();
        return new BigDecimal(sum);
    }

    List<PaymentItem> getPaymentsForUserWithEmail(String userEmail) {
        List<Payment> payments = getPayments();
        List<PaymentItem> items = new ArrayList<>();
        payments.stream()
                .filter(payment -> payment.getUser().getEmail().equals(userEmail))
                .forEach(payment -> payment.getPaymentItems().stream()
                        .forEach(paymentItem -> items.add(paymentItem)));
        return items;
    }

    Set<Payment> findPaymentsWithValueOver(int value) {
        Set<Payment> paymentsOverValue = new HashSet<>();
        List<Payment> payments = getPayments();
        payments.stream()
                .filter(payment -> getPaymentValue(payment) > value)
                .forEach(payment -> paymentsOverValue.add(payment));
        return paymentsOverValue;
    }

    private List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    private int getPaymentValue(Payment payment) {
        int value = 0;
        List<PaymentItem> items = new ArrayList<>();
        payment.getPaymentItems().stream()
                .forEach(paymentItem -> items.add(paymentItem));
        for (PaymentItem item : items) {
            value += item.getFinalPrice().intValue();
        }
        return value;
    }
}
