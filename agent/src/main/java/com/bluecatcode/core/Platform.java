package com.bluecatcode.core;

import com.bluecatcode.common.base.Environment;
import com.bluecatcode.common.base.RichEnumConstants;
import com.bluecatcode.common.base.RichEnumTrait;
import com.google.common.collect.ImmutableSet;

import static com.bluecatcode.common.base.ExceptionSupplier.throwA;
import static com.bluecatcode.common.base.Preconditions.checkNotEmpty;
import static com.google.common.collect.FluentIterable.from;
import static java.lang.String.format;

public enum Platform implements RichEnumTrait<Platform> {
    LINUX("linux", "linux"), WINDOWS("win", "win"), OSX("osx", "mac", "darwin"), UNIX("unix", "freebsd", "sunos", "nix", "aix");

    private static final RichEnumConstants<Platform> constants = RichEnumConstants.richConstants(Platform.class);

    public static final Platform CURRENT = getCurrentOsName();

    private final String identifier;
    private final ImmutableSet<String> identifiers;

    Platform(String identifier, String... identifiers) {
        this.identifier = checkNotEmpty(identifier);
        this.identifiers = ImmutableSet.<String>builder()
                .add(identifier)
                .add(checkNotEmpty(identifiers))
                .build();
    }

    @Override
    public Platform self() {
        return this;
    }

    public String identifier() {
        return identifier;
    }

    public ImmutableSet<String> identifiers() {
        return identifiers;
    }

    private static Platform getCurrentOsName() {
        String osName = Environment.getSystemProperty("os.name").get().toLowerCase();
        return constants.fluent()
                .firstMatch(platform -> from(platform.identifiers).anyMatch(osName::contains))
                .or(throwA(Platform.class,
                        new UnsupportedOperationException(format("Unknown platform: '%s'", osName))));
    }
}
