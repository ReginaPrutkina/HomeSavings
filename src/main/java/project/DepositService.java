package project;

public class DepositService {
   public void addTypeOfPercent(Deposit deposit) {
        switch (deposit.getPercentType()) {
            case 1 -> { deposit.setTypeOfPercent(new PercentDaily());     }
            case 30 -> { deposit.setTypeOfPercent(new PercentMonthly());  }
            default -> { deposit.setTypeOfPercent(new PercentAtTheEnd()); }
        }
    }
}
