package org.os890.microreport.demo;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;
import one.microstream.persistence.binary.jdk8.types.BinaryHandlersJDK8;
import one.microstream.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import one.microstream.storage.types.StorageManager;
import org.os890.microreport.demo.storage.MicroStore;
import org.os890.microreport.demo.storage.StorageRoot;

import java.nio.file.Path;

@Specializes
@ApplicationScoped
public class TestMicroStore extends MicroStore {
    private Path storagePath;

    @Override
    protected void initOnCreate() {
        //skip eager init
    }

    public void usePath(Path storagePath) {
        this.storagePath = storagePath;
        init();
    }

    protected StorageManager createStorageManager() {
        return EmbeddedStorageConfiguration.Builder()
                .setChannelCount(Math.max(1, Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1)))
                .setStorageDirectory(storagePath.toFile().getAbsolutePath())
                .createEmbeddedStorageFoundation()
                .onConnectionFoundation(BinaryHandlersJDK8::registerJDK8TypeHandlers)
                .start();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public StorageRoot getStorageRoot() {
        return storageRoot;
    }
}
