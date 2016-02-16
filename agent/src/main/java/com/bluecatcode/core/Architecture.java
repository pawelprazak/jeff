package com.bluecatcode.core;

import com.bluecatcode.common.base.Environment;
import com.bluecatcode.common.base.RichEnumConstants;
import com.bluecatcode.common.base.RichEnumTrait;

import static com.bluecatcode.common.base.ExceptionSupplier.throwA;
import static java.lang.String.format;

public enum Architecture implements RichEnumTrait<Architecture> {
    X64("amd64"), IA64("ia64"), X32("i386");

    private static final RichEnumConstants<Architecture> constants = RichEnumConstants.richConstants(Architecture.class);

    public static final Architecture CURRENT = getCurrentArchitecture();

    private final String identifier;

    Architecture(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Architecture self() {
        return this;
    }

    public String identifier() {
        return identifier;
    }

    private static Architecture getCurrentArchitecture() {
        String osArch = Environment.getSystemProperty("os.arch").get().toLowerCase();
        return constants.fluent()
                .firstMatch(arch -> arch.identifier().equalsIgnoreCase(osArch))
                .or(throwA(Architecture.class,
                        new UnsupportedOperationException(format("Unknown architecture: '%s'", osArch))));
    }
}
