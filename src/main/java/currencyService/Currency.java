package currencyService;

public class Currency {
    private String  id;
    private String numCode;
    private String name;
    private String charCode;
    private int nominal;
    private double value;
    Currency(String id, String numCode, String charCode, int nominal, String name, double value){
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.name = name;
        this.nominal = nominal;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public int getNominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id='" + id + '\'' +
                ", numCode='" + numCode + '\'' +
                ", name='" + name + '\'' +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", value=" + value +
                '}';
    }
}
