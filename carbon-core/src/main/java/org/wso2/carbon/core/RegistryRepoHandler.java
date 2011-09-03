package org.wso2.carbon.core;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.dump.RegistryToFileSystemHandler;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.utils.CarbonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;


public class RegistryRepoHandler {

    private static Log log = LogFactory.getLog(RegistryRepoHandler.class);

    private static String metaDir = ".repo";

    /**
     * Copies the repository in the given path to the local file system and returns the path.
     * We are using java tmp dir to get the copy of the repository
     * @param registryPath - path of the repository in registry
     * @return local repo path
     */
    public static String prepareRepository(String registryPath) {
        String tmpRepo = System.getProperty("java.io.tmpdir");
        String[] temp = registryPath.split("/");
        String repoDirName = temp[temp.length - 1];

        String repoPath = tmpRepo + File.separator + repoDirName;
        String metaPath = tmpRepo + File.separator + RegistryRepoHandler.metaDir;

        try {
            Registry registry = CarbonCoreServiceComponent
                    .getRegistryService().getConfigSystemRegistry();
            RegistryToFileSystemHandler dumpHandler = new RegistryToFileSystemHandler(registry);

            File repo = new File(repoPath);
            if (repo.exists()) {
                dumpHandler.update(registryPath, repoPath, metaPath);
            } else {
                dumpHandler.dumpToFilesystem(registryPath, repoPath, metaPath);
            }
        } catch (Exception e) {
            log.error("Error dumping repository from registry.", e);
        }

        return repoPath;
    }

    /**
     * Stores an artifact in the repository which is in Registry
     * @param artifactStream - input stream of the artifact
     * @param resourcePath - path to store
     * @throws Exception - error in accessing registry
     */
    public static void storeArtifactInRegistry(InputStream artifactStream,
                                               String resourcePath) throws Exception {
        String registryRepoPath = CarbonUtils.getRegistryRepoPath();
        if (registryRepoPath != null) {
            Registry registry = CarbonCoreServiceComponent
                    .getRegistryService().getConfigSystemRegistry();
            Resource artifactResource = registry.newResource();
            artifactResource.setContentStream(artifactStream);
            registry.put(resourcePath, artifactResource);
        }
    }

    /**
     * When a path to an artifact which is in the file system repository is given, deletes that
     * artifact from the registry based repository.
     * @param fileSystemPath - absolute path of the artifact in the file system
     */
    public static void deleteArtifactFromRegistry(String fileSystemPath) {
        String registryRepoPath = CarbonUtils.getRegistryRepoPath();
        if (registryRepoPath != null && registryRepoPath.lastIndexOf('/') != -1) {
            //get the repo dir from registry repo path
            String repoDir = registryRepoPath.substring(registryRepoPath.lastIndexOf('/') + 1);

            //compute the resource path
            String[] temp = fileSystemPath.split(repoDir);
            String resourcePath = registryRepoPath + temp[temp.length -1];

            //delete the artifact from registry
            try {
                Registry registry = CarbonCoreServiceComponent
                        .getRegistryService().getConfigSystemRegistry();
                if (registry.resourceExists(resourcePath)) {
                    registry.delete(resourcePath);
                }
            } catch (Exception e) {
                log.error("Can't delete artifact from registry.", e);
            }
        }
    }

}
