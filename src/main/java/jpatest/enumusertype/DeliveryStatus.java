package jpatest.enumusertype;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of enum with explicit string codes
 */
public enum DeliveryStatus {

    SHIPPED("abgesendet"), DELIVERED("geliefert");

    private static Map<String, DeliveryStatus> valuesByCode = new HashMap<String, DeliveryStatus>();

    static {
        for (DeliveryStatus value : DeliveryStatus.values()) {
            valuesByCode.put(value.getCode(), value);
        }
    }

    private DeliveryStatus(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public static DeliveryStatus valueOfCode(String code) {
        if (valuesByCode.containsKey(code)) {
            return valuesByCode.get(code);
        }

        throw new IllegalArgumentException(code + " is not a valid delivery status code");
    }
}
