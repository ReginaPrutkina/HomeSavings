package currencyService;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @Column (name = "uid")
    @GeneratedValue
    private long row_id;

    private String  id;

    private String numCode;

    private String name;

    private String charCode;

    private int nominal;

    private double value;

    @Temporal(TemporalType.DATE)
    private Date data;

    Currency(){};

    Currency(String id, String numCode, String charCode, int nominal, String name, double value){
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.name = name;
        this.nominal = nominal;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public void setValue(double value) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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
