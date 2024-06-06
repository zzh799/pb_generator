package bean;

import com.intellij.protobuf.lang.psi.PbMapField;

public class MapFieldBean extends FieldBean {
    String keyType;
    String valueType;

    public MapFieldBean(PbMapField mapField) {
        super(mapField);
        this.keyType = parseType(mapField.getKeyType());
        this.valueType = parseType(mapField.getValueType());
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}
