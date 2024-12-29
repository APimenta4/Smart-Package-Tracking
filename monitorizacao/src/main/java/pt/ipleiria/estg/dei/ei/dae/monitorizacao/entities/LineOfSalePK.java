package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import java.io.Serializable;
import java.util.Objects;


public class LineOfSalePK implements Serializable {

    private long volumeCode;
    private long productCode;

    public LineOfSalePK() {}

    public LineOfSalePK(long volumeCode, long productCode) {
        this.volumeCode = volumeCode;
        this.productCode = productCode;
    }

    public long getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(long volumeCode) {
        this.volumeCode = volumeCode;
    }

    public long getProductCode() {
        return productCode;
    }

    public void setProductCode(long productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineOfSalePK other = (LineOfSalePK) o;
        return volumeCode == other.volumeCode &&
                productCode == other.productCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(volumeCode, productCode);
    }
}
