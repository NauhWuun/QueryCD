package org.Query.Columnar;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class SubColumnTimes
{
    private final AtomicInteger createTimeIndex;
    private final AtomicInteger updateTimeIndex;
    private final Map<Object, TimeKValue<Object, Object>> innerData;

    public SubColumnTimes(long timeStamp) {
        createTimeIndex = new AtomicInteger();
        updateTimeIndex = new AtomicInteger();

        innerData = new IdentityHashMap<>();

        // addition default k-v value
        innerData.put(createTimeIndex.get(), new TimeKValue<>("", ""));
    }

    public void setUpdateTime(int timeStamp, Object key, Object value) {
        updateTimeIndex.getAndSet(timeStamp);
        innerData.put(updateTimeIndex.get(), new TimeKValue<>(key, value));
    }

    public int getUpdateTime() {
        return updateTimeIndex.get();
    }

    public final int getCreateTime() {
        return createTimeIndex.get();
    }

    public Object getInnerValue(Object key) { return innerData.get(key); }

    public Map<Object, Object> getRangeData(int from, int to) {
        if (from < 0 && to < 0)
            from = createTimeIndex.get();

        if (to > updateTimeIndex.get())
            to = updateTimeIndex.get();

        Map<Object, Object> maps = new LinkedHashMap<>();
        for (; from < to; from++) {
            maps.put(from, innerData.get(from));
        }

        maps.put(to, innerData.get(to));
        return maps;
    }

    public void removeInnerData(int timeStamp) {
//        updateTimeIndex.addAndGet(timeStamp);
//        innerData.remove(updateTimeIndex.get());
    }

    public Map<Object, TimeKValue<Object, Object>> getInnerObject() {
        return this.innerData;
    }

    public class TimeKValue<K, V>
    {
        private final K left;
        private final V right;

        public TimeKValue(K left, V right) {
            this.left = left;
            this.right = right;
        }

        public K getLeft() {
            return left;
        }

        public V getRight() {
            return right;
        }
    }
}
