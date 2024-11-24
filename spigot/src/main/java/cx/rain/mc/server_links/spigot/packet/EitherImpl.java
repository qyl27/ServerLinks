package cx.rain.mc.server_links.spigot.packet;

import com.comphenix.protocol.wrappers.Either;

import java.util.Optional;
import java.util.function.Function;

public final class EitherImpl<FIRST, SECOND> extends Either<FIRST, SECOND> {
    private final FIRST first;
    private final SECOND second;

    public EitherImpl(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public <T> T map(Function<FIRST, T> left, Function<SECOND, T> right) {
        if (first != null) {
            return left.apply(first);
        } else {
            return right.apply(second);
        }
    }

    @Override
    public Optional<FIRST> left() {
        return Optional.of(first);
    }

    @Override
    public Optional<SECOND> right() {
        return Optional.of(second);
    }
}
