package src.music;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import src.util.Func2;

public class MusicStreamMerger implements MusicStream {

    MusicStream trackA;
    MusicStream trackB;

    Func2<MusicStream, MusicStream, Byte[]> sampleMergeFunc;

    public MusicStreamMerger(MusicStream trackA, MusicStream trackB,
            Func2<MusicStream, MusicStream, Byte[]> sampleMergeFunc) {
        this.trackA = trackA;
        this.trackB = trackB;
        this.sampleMergeFunc = sampleMergeFunc;
    }

    @Override
    public float getFrequency() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFrequency'");
    }

    @Override
    public boolean allMatch(Predicate<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allMatch'");
    }

    @Override
    public boolean anyMatch(Predicate<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'anyMatch'");
    }

    @Override
    public <R, A> R collect(Collector<? super byte[], A, R> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'collect'");
    }

    @Override
    public <R> R collect(Supplier<R> arg0, BiConsumer<R, ? super byte[]> arg1, BiConsumer<R, R> arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'collect'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public Stream<byte[]> distinct() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'distinct'");
    }

    @Override
    public Stream<byte[]> filter(Predicate<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }

    @Override
    public Optional<byte[]> findAny() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAny'");
    }

    @Override
    public Optional<byte[]> findFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findFirst'");
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super byte[], ? extends Stream<? extends R>> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flatMap'");
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super byte[], ? extends DoubleStream> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flatMapToDouble'");
    }

    @Override
    public IntStream flatMapToInt(Function<? super byte[], ? extends IntStream> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flatMapToInt'");
    }

    @Override
    public LongStream flatMapToLong(Function<? super byte[], ? extends LongStream> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flatMapToLong'");
    }

    @Override
    public void forEach(Consumer<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'forEach'");
    }

    @Override
    public void forEachOrdered(Consumer<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'forEachOrdered'");
    }

    @Override
    public Stream<byte[]> limit(long arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'limit'");
    }

    @Override
    public <R> Stream<R> map(Function<? super byte[], ? extends R> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mapToDouble'");
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mapToInt'");
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mapToLong'");
    }

    @Override
    public Optional<byte[]> max(Comparator<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'max'");
    }

    @Override
    public Optional<byte[]> min(Comparator<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'min'");
    }

    @Override
    public boolean noneMatch(Predicate<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'noneMatch'");
    }

    @Override
    public Stream<byte[]> peek(Consumer<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'peek'");
    }

    @Override
    public Optional<byte[]> reduce(BinaryOperator<byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduce'");
    }

    @Override
    public byte[] reduce(byte[] arg0, BinaryOperator<byte[]> arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduce'");
    }

    @Override
    public <U> U reduce(U arg0, BiFunction<U, ? super byte[], U> arg1, BinaryOperator<U> arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduce'");
    }

    @Override
    public Stream<byte[]> skip(long arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'skip'");
    }

    @Override
    public Stream<byte[]> sorted() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sorted'");
    }

    @Override
    public Stream<byte[]> sorted(Comparator<? super byte[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sorted'");
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    @Override
    public boolean isParallel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isParallel'");
    }

    @Override
    public Iterator<byte[]> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public Stream<byte[]> onClose(Runnable arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    public Stream<byte[]> parallel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parallel'");
    }

    @Override
    public Stream<byte[]> sequential() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sequential'");
    }

    @Override
    public Spliterator<byte[]> spliterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'spliterator'");
    }

    @Override
    public Stream<byte[]> unordered() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unordered'");
    }

}
