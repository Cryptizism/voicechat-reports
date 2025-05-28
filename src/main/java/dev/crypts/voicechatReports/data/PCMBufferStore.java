package dev.crypts.voicechatReports.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PCMBufferStore {

    private static final int BUFFER_SIZE = 7224 * 1024; // 7 MB per user buffer

    private static final PCMBufferStore INSTANCE = new PCMBufferStore();

    private final ConcurrentMap<UUID, CircularPCMBuffer> userBuffers;

    private PCMBufferStore() {
        userBuffers = new ConcurrentHashMap<>();
    }

    public static PCMBufferStore getInstance() {
        return INSTANCE;
    }

    public CircularPCMBuffer getOrCreateBuffer(UUID uuid) {
        return userBuffers.computeIfAbsent(uuid, id -> new CircularPCMBuffer(BUFFER_SIZE));
    }

    public void removeUser(UUID uuid) {
        userBuffers.remove(uuid);
    }
}
