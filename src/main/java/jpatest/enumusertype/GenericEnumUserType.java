/*
 * Title: GenericEnumUserType
 * Description: 
 * 
 * Copyright: Copyright (c) 2010 ISC-EJPD
 * Company: ISC-EJPD
 * 
 * Last worker: isc-rsa
 * Date : 1 sept. 2010
 * @version : $Revision: 126044 $
 */
package jpatest.enumusertype;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * GenericEnumUserType is a user type allowing to map enums as JPA attribute.
 * GenericEnumUserType allows to use any attribute of the mapped enum (e.g. a
 * integer) as identifier for the enum value.
 * 
 * <pre>
 * Example mapping usage:
 * @Column(name = "GESCHLECHT")
 * @Type(type = "ch.ejpd.armada.business.domain.util.GenericEnumUserType", parameters = {
 *    @Parameter(name = "enumClass", value = "ch.ejpd.armada.business.domain.types.Sex"),
 *    @Parameter(name = "identifierMethod", value = "getCode")})
 * private Sex geschlecht;
 * </pre>
 */
public class GenericEnumUserType implements UserType, ParameterizedType {

    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";

    @SuppressWarnings("rawtypes")
    private Class<? extends Enum> enumClass;
    private Class<?> identifierType;
    private Method identifierMethod;
    private Method valueOfMethod;
    private AbstractSingleColumnStandardBasicType<?> type;
    private int[] sqlTypes;

    /**
     * Sets parameter values for custom type. Following parameters are expected:
     * enumClass: class name of enum to map, mandatory identifierMethod: name of
     * method returning a unique identifier for the enum value, defaults to
     * "name" valueOfMethod: name of static method accepting and identifier and
     * returning an enum value, defaults to "valueOf"
     * 
     * Note that return type of identifier method must match parameter type of
     * valueOf method {@inheritDoc}
     * 
     * @see org.hibernate.usertype.ParameterizedType#setParameterValues(java.util.Properties)
     */
    @Override
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");

        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException cfne) {
            throw new HibernateException("Enum class not found", cfne);
        }

        String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);

        try {
            identifierMethod = enumClass.getMethod(identifierMethodName, new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain identifier method", e);
        }

        TypeResolver tr = new TypeResolver();
        type = (AbstractSingleColumnStandardBasicType<?>) tr.basic(identifierType.getName());
        if (type == null) {
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }
        sqlTypes = new int[] { type.sqlType() };

        if (type == null)
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());

        sqlTypes = new int[] { type.sqlType() };

        String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[] { identifierType });
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object identifier = type.get(rs, names[0], session);
        if (identifier == null) {
            return null;
        }

        try {
            return valueOfMethod.invoke(enumClass, new Object[] { identifier });
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking valueOf method '" + valueOfMethod.getName() + "' of " + "enumeration class '" + enumClass + "'", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, type.sqlType());
            } else {
                Object identifier = identifierMethod.invoke(value, new Object[0]);
                st.setObject(index, identifier);
            }
        } catch (Exception e) {
            throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName() + "' of " + "enumeration class '" + enumClass + "'", e);
        }
    }

    @Override
    public int[] sqlTypes() {
        return sqlTypes;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
