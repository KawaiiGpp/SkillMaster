package com.akira.skillmaster.func;

import com.akira.skillmaster.utils.BukkitUtils;

@SuppressWarnings("CallToPrintStackTrace")
@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;

    static void runSafely(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            BukkitUtils.err("Exception caught: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
