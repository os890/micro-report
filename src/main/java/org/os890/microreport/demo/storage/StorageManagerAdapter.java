/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.microreport.demo.storage;


import jakarta.enterprise.context.ApplicationScoped;
import one.microstream.afs.types.ADirectory;
import one.microstream.afs.types.AFile;
import one.microstream.collections.types.XGettingEnum;
import one.microstream.persistence.binary.types.Binary;
import one.microstream.persistence.types.PersistenceManager;
import one.microstream.persistence.types.PersistenceRootsView;
import one.microstream.persistence.types.PersistenceTypeDictionaryExporter;
import one.microstream.persistence.types.Storer;
import one.microstream.storage.exceptions.StorageExceptionShutdown;
import one.microstream.storage.types.*;

import java.nio.ByteBuffer;
import java.util.function.Predicate;

//workaround needed due to a producer bug in helidon-junit - TODO remove it once a producer works in prod. and test-mode
@ApplicationScoped
public class StorageManagerAdapter implements StorageManager  {
    private StorageManager wrapped;
    public void bind(StorageManager storageManager) {
        this.wrapped = storageManager;
    }

    @Override
    public StorageConfiguration configuration() {
        return wrapped.configuration();
    }

    @Override
    public StorageTypeDictionary typeDictionary() {
        return wrapped.typeDictionary();
    }

    @Override
    public StorageManager start() {
        return wrapped.start();
    }

    @Override
    public boolean shutdown() {
        return wrapped.shutdown();
    }

    @Override
    public StorageConnection createConnection() {
        return wrapped.createConnection();
    }

    @Override
    public Object root() {
        return wrapped.root();
    }

    @Override
    public Object setRoot(Object newRoot) {
        return wrapped.setRoot(newRoot);
    }

    @Override
    public long storeRoot() {
        return wrapped.storeRoot();
    }

    @Override
    public PersistenceRootsView viewRoots() {
        return wrapped.viewRoots();
    }

    @Override
    public Database database() {
        return wrapped.database();
    }

    @Override
    public String databaseName() {
        return wrapped.databaseName();
    }

    @Override
    public boolean isAcceptingTasks() {
        return wrapped.isAcceptingTasks();
    }

    @Override
    public boolean isRunning() {
        return wrapped.isRunning();
    }

    @Override
    public boolean isStartingUp() {
        return wrapped.isStartingUp();
    }

    @Override
    public boolean isShuttingDown() {
        return wrapped.isShuttingDown();
    }

    @Override
    public boolean isShutdown() {
        return wrapped.isShutdown();
    }

    @Override
    public void checkAcceptingTasks() {
        wrapped.checkAcceptingTasks();
    }

    @Override
    public long initializationTime() {
        return wrapped.initializationTime();
    }

    @Override
    public long operationModeTime() {
        return wrapped.operationModeTime();
    }

    @Override
    public long initializationDuration() {
        return wrapped.initializationDuration();
    }

    @Override
    public void close() throws StorageExceptionShutdown {
        wrapped.close();
    }

    @Override
    public boolean isActive() {
        return wrapped.isActive();
    }

    @Override
    public void issueFullGarbageCollection() {
        wrapped.issueFullGarbageCollection();
    }

    @Override
    public boolean issueGarbageCollection(long nanoTimeBudget) {
        return wrapped.issueGarbageCollection(nanoTimeBudget);
    }

    @Override
    public void issueFullFileCheck() {
        wrapped.issueFullFileCheck();
    }

    @Override
    public boolean issueFileCheck(long nanoTimeBudget) {
        return wrapped.issueFileCheck(nanoTimeBudget);
    }

    @Override
    public void issueFullCacheCheck() {
        wrapped.issueFullCacheCheck();
    }

    @Override
    public void issueFullCacheCheck(StorageEntityCacheEvaluator entityEvaluator) {
        wrapped.issueFullCacheCheck(entityEvaluator);
    }

    @Override
    public boolean issueCacheCheck(long nanoTimeBudget) {
        return wrapped.issueCacheCheck(nanoTimeBudget);
    }

    @Override
    public boolean issueCacheCheck(long nanoTimeBudget, StorageEntityCacheEvaluator entityEvaluator) {
        return wrapped.issueCacheCheck(nanoTimeBudget, entityEvaluator);
    }

    @Override
    public void issueFullBackup(ADirectory targetDirectory) {
        wrapped.issueFullBackup(targetDirectory);
    }

    @Override
    public void issueFullBackup(StorageLiveFileProvider targetFileProvider, PersistenceTypeDictionaryExporter typeDictionaryExporter) {
        wrapped.issueFullBackup(targetFileProvider, typeDictionaryExporter);
    }

    @Override
    public StorageRawFileStatistics createStorageStatistics() {
        return wrapped.createStorageStatistics();
    }

    @Override
    public void exportChannels(StorageLiveFileProvider fileProvider, boolean performGarbageCollection) {
        wrapped.exportChannels(fileProvider, performGarbageCollection);
    }

    @Override
    public void exportChannels(StorageLiveFileProvider fileProvider) {
        wrapped.exportChannels(fileProvider);
    }

    @Override
    public StorageEntityTypeExportStatistics exportTypes(StorageEntityTypeExportFileProvider exportFileProvider, Predicate<? super StorageEntityTypeHandler> isExportType) {
        return wrapped.exportTypes(exportFileProvider, isExportType);
    }

    @Override
    public StorageEntityTypeExportStatistics exportTypes(StorageEntityTypeExportFileProvider exportFileProvider) {
        return wrapped.exportTypes(exportFileProvider);
    }

    @Override
    public StorageEntityTypeExportStatistics exportTypes(ADirectory targetDirectory) {
        return wrapped.exportTypes(targetDirectory);
    }

    @Override
    public StorageEntityTypeExportStatistics exportTypes(ADirectory targetDirectory, Predicate<? super StorageEntityTypeHandler> isExportType) {
        return wrapped.exportTypes(targetDirectory, isExportType);
    }

    @Override
    public void importFiles(XGettingEnum<AFile> importFiles) {
        wrapped.importFiles(importFiles);
    }

    @Override
    public void importData(XGettingEnum<ByteBuffer> importData) {
        wrapped.importData(importData);
    }

    @Override
    public PersistenceManager<Binary> persistenceManager() {
        return wrapped.persistenceManager();
    }

    @Override
    public long store(Object instance) {
        return wrapped.store(instance);
    }

    @Override
    public long[] storeAll(Object... instances) {
        return wrapped.storeAll(instances);
    }

    @Override
    public void storeAll(Iterable<?> instances) {
        wrapped.storeAll(instances);
    }

    @Override
    public Storer createLazyStorer() {
        return wrapped.createLazyStorer();
    }

    @Override
    public Storer createStorer() {
        return wrapped.createStorer();
    }

    @Override
    public Storer createEagerStorer() {
        return wrapped.createEagerStorer();
    }

    @Override
    public Object getObject(long objectId) {
        return wrapped.getObject(objectId);
    }

    public static StorageConnection New(PersistenceManager<Binary> persistenceManager, StorageRequestAcceptor connectionRequestAcceptor) {
        return StorageConnection.New(persistenceManager, connectionRequestAcceptor);
    }
}
