package com.soundify.server.shared.persistence;

import com.soundify.server.shared.domain.Id;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.BytesHelper;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.java.UUIDJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;

public class IdJavaType extends AbstractClassJavaType<Id> {
    protected IdJavaType() {
        super(Id.class);
    }

    public static final IdJavaType INSTANCE = new IdJavaType();


    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators context) {
        return context.getTypeConfiguration().getJdbcTypeRegistry().getDescriptor(Types.CHAR);
    }

    public String toString(UUID value) {
        return UUIDJavaType.ToStringTransformer.INSTANCE.transform(value);
    }

    public Id fromString(CharSequence string) {
        return ToStringTransformer.INSTANCE.parse(string.toString());
    }

    public long getDefaultSqlLength(Dialect dialect, JdbcType jdbcType) {
        if (jdbcType.isString()) {
            return Id.Id_CHARS;
        } else {
            return jdbcType.isBinary() ? Id.Id_BYTES : super.getDefaultSqlLength(dialect, jdbcType);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(Id value, Class<X> type, WrapperOptions wrapperOptions) {
        if (value == null) {
            return null;
        } else if (Id.class.isAssignableFrom(type)) {
            return (X) PassThroughTransformer.INSTANCE.transform(value);
        } else if (String.class.isAssignableFrom(type)) {
            return (X) ToStringTransformer.INSTANCE.transform(value);
        } else if (byte[].class.isAssignableFrom(type)) {
            return (X) ToBytesTransformer.INSTANCE.transform(value);
        } else {
            throw this.unknownUnwrap(type);
        }
    }

    @Override
    public <X> Id wrap(X value, WrapperOptions wrapperOptions) {
        if (value == null) {
            return null;
        } else if (value instanceof Id) {
            return PassThroughTransformer.INSTANCE.parse(value);
        } else if (value instanceof String) {
            return ToStringTransformer.INSTANCE.parse(value);
        } else if (value instanceof byte[]) {
            return ToBytesTransformer.INSTANCE.parse(value);
        } else {
            throw this.unknownWrap(value.getClass());
        }
    }

    public static class ToBytesTransformer implements ValueTransformer {
        public static final ToBytesTransformer INSTANCE = new ToBytesTransformer();

        public ToBytesTransformer() {
        }

        public byte[] transform(Id value) {
            byte[] bytes = new byte[16];

            BytesHelper.fromLong(value.getMostSignificantBits(), bytes, 0);
            BytesHelper.fromLong(value.getLeastSignificantBits(), bytes, 8);
            return bytes;
        }

        public Id parse(Object value) {
            byte[] bytea = (byte[]) value;
            return new Id(BytesHelper.asLong(bytea, 0), BytesHelper.asLong(bytea, 8));
        }
    }

    public static class NoDashesStringTransformer implements ValueTransformer {
        public static final NoDashesStringTransformer INSTANCE = new NoDashesStringTransformer();

        public NoDashesStringTransformer() {
        }

        public String transform(Id Id) {
            String stringForm = ToStringTransformer.INSTANCE.transform(Id);
            String var10000 = stringForm.substring(0, 8);
            return var10000 + stringForm.substring(9, 13) + stringForm.substring(14, 18) + stringForm.substring(19, 23) + stringForm.substring(24);
        }

        public Id parse(Object value) {
            String stringValue = (String) value;
            String var10000 = stringValue.substring(0, 8);
            String IdString = var10000 + "-" + stringValue.substring(8, 12) + "-" + stringValue.substring(12, 16) + "-" + stringValue.substring(16, 20) + "-" + stringValue.substring(20);
            return Id.from(IdString);
        }
    }

    public static class ToStringTransformer implements ValueTransformer {
        public static final ToStringTransformer INSTANCE = new ToStringTransformer();

        public ToStringTransformer() {
        }

        public String transform(Id Id) {
            return Id.toString();
        }

        public Id parse(Object value) {
            return Id.from((String) value);
        }
    }

    public static class PassThroughTransformer implements ValueTransformer {
        public static final PassThroughTransformer INSTANCE = new PassThroughTransformer();

        public PassThroughTransformer() {
        }

        public Id transform(Id Id) {
            return Id;
        }

        public Id parse(Object value) {
            return (Id) value;
        }
    }

    public interface ValueTransformer {
        Serializable transform(Id var1);

        Id parse(Object var1);
    }
}
