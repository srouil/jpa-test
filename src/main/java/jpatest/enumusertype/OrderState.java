package jpatest.enumusertype;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of enum with explicit integer codes
 */
public enum OrderState {

    NEW(0), PAID(10), ERROR(-1);

    private static Map<Integer, OrderState> valuesByCode = new HashMap<Integer, OrderState>();

    static {
        for (OrderState value : OrderState.values()) {
            valuesByCode.put(value.getCode(), value);
        }
    }

    private OrderState(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public static OrderState valueOf(int code) {
        if (valuesByCode.containsKey(code)) {
            return valuesByCode.get(code);
        }

        throw new IllegalArgumentException(code + " is not a valid order state code");
    }
}
