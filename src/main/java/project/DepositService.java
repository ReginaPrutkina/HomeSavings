package project;

import org.springframework.beans.factory.annotation.Autowired;

public class DepositService {
    @Autowired
    private PercentDaily percentDaily;
    @Autowired
    private PercentAtTheEnd percentAtTheEnd;
    @Autowired
    private PercentMonthly percentMonthly;

////    public DepositService() {
//     //   percentAtTheEnd = new PercentAtTheEnd();
//     //   percentDaily = new PercentDaily();
//     //   percentMonthly = new PercentMonthly();
//    }

    public PercentDaily getPercentDaily() {
        return percentDaily;
    }

    public void setPercentDaily(PercentDaily percentDaily) {
        this.percentDaily = percentDaily;
    }

    public PercentAtTheEnd getPercentAtTheEnd() {
        return percentAtTheEnd;
    }

    public void setPercentAtTheEnd(PercentAtTheEnd percentAtTheEnd) {
        this.percentAtTheEnd = percentAtTheEnd;
    }

    public PercentMonthly getPercentMonthly() {
        return percentMonthly;
    }

    public void setPercentMonthly(PercentMonthly percentMonthly) {
        this.percentMonthly = percentMonthly;
    }

//    public void addTypeOfPercent(Deposit deposit) {
//       switch (deposit.getPercentType()) {
//            case 1 -> { deposit.setTypeOfPercent(percentDaily);     }
//            case 30 -> { deposit.setTypeOfPercent(percentMonthly);  }
//            default -> { deposit.setTypeOfPercent(percentAtTheEnd); }
//        }
//    }

    public TypeOfPercent getTypeOfPercentObject(Deposit deposit) {
        switch (deposit.getPercentType()) {
            case 1 -> {return percentDaily;   }
            case 30 -> {return percentMonthly;}
            default -> { return percentAtTheEnd; }
        }
    }
}
