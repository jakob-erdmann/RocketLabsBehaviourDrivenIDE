package de.rocketlabs.behatide.modules.behat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.rocketlabs.behatide.application.configuration.storage.StorageParameter;
import de.rocketlabs.behatide.application.configuration.storage.state.StateStorageManager;
import de.rocketlabs.behatide.domain.parser.ConfigurationReader;
import de.rocketlabs.behatide.modules.behat.model.BehatProfile;
import de.rocketlabs.behatide.modules.behat.model.Project;
import de.rocketlabs.behatide.modules.behat.model.ProjectConfiguration;
import de.rocketlabs.behatide.modules.behat.parser.BehatConfigurationReader;
import de.rocketlabs.behatide.php.ParseException;
import de.rocketlabs.behatide.php.PhpParser;
import de.rocketlabs.behatide.php.model.PhpClass;
import de.rocketlabs.behatide.php.model.PhpFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectFactory {

    private static final String[] AUTO_LOADER_PATHS = new String[]{
        "../../../autoload.php",
        "vendor/autoload.php",
        "../vendor/autoload.php",
    };

    private static Injector injector = Guice.createInjector(new BehatModule());
    private static BehatConfigurationReader configurationReader =
        (BehatConfigurationReader) injector.getInstance(ConfigurationReader.class);

    public static void main(String... args) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setTitle("test");
        projectConfiguration.setBehatConfigurationFile("/home/jerdmann/source/behat/dummy/behat.yml");
        projectConfiguration.setBehatExecutable("/home/jerdmann/source/behat/dummy/vendor/behat/behat/bin/behat");
        projectConfiguration.setProjectLocation("/home/jerdmann/source/java/RocketLabsBehaviourDrivenIDE/build");

        generateProject(projectConfiguration);
    }

    public static de.rocketlabs.behatide.domain.model.Project generateProject(ProjectConfiguration configuration) {
        Project project = new de.rocketlabs.behatide.modules.behat.model.Project();
        project.setProjectLocation(configuration.getProjectLocation());
        project.setBehatConfigurationFile(configuration.getBehatConfigurationFile());
        project.setBehatExecutablePath(configuration.getBehatExecutable());
        project.setTitle(configuration.getTitle());
        loadConfigurationFile(project);
        loadBehatDefinitions(project, configuration);

        Map<StorageParameter, String> storageParameters = new HashMap<StorageParameter, String>() {{
            put(StorageParameter.STORAGE_DIRECTORY, project.getProjectLocation());
        }};

        StateStorageManager.getInstance().setState(project, storageParameters);
        StateStorageManager.getInstance().save();

        return project;
    }

    private static void loadConfigurationFile(Project project) {
        String filePath = project.getBehatConfigurationFile();
        try (InputStream in = Files.newInputStream(Paths.get(filePath))) {
            project.setConfiguration(configurationReader.read(in));
        } catch (Exception e) {
            throw new RuntimeException("Could not load behat configuration", e);
        }
        setFileHash(project, filePath);
    }

    private static void setFileHash(Project project, String filePath) {
        try (InputStream in = Files.newInputStream(Paths.get(filePath))) {
            project.setFileHash(filePath, DigestUtils.md5(in));
        } catch (Exception e) {
            throw new RuntimeException("Could not load behat configuration", e);
        }
    }

    private static void loadBehatDefinitions(Project project, ProjectConfiguration configuration) {
        Set<String> classPaths = getClassPaths(project, configuration);
        Map<String, PhpFile> parsedFiles = loadPhpClasses(classPaths);
        Map<String, PhpClass> behatDefinitions = getBehatDefinitions(parsedFiles);
        project.setAvailableDefinitions(behatDefinitions);
    }

    private static Set<String> getClassPaths(Project project, ProjectConfiguration configuration) {
        Set<String> classesSet = new HashSet<>();

        project.getConfiguration().getProfiles().forEach(profile -> {
            resolveProfileClasses(project, profile, configuration);
            classesSet.addAll(profile.getClassPaths().values());
        });

        return classesSet;
    }

    private static void resolveProfileClasses(Project project, BehatProfile profile, ProjectConfiguration configuration) {
        Map<String, String> autoLoadPaths = profile.getAutoLoadPaths();
        Set<String> profilesClasses = profile.getSuites().stream()
                                             .flatMap(suite -> suite.getDefinitionContainerIdentifiers().stream())
                                             .collect(Collectors.toSet());

        ProcessBuilder autoLoader = configureProcessBuilder(autoLoadPaths, project, profilesClasses, configuration);

        Map<String, String> classMap = new HashMap<>();
        try {
            Process process = autoLoader.start();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                for (String className : profilesClasses) {
                    String path = in.readLine();
                    classMap.put(className, path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile.setClassPaths(classMap);
    }

    private static Map<String, PhpFile> loadPhpClasses(Set<String> classesSet) {
        Map<String, PhpFile> parsedFiles = new HashMap<>();
        classesSet.forEach(classPath -> {
            try {
                parsedFiles.put(classPath, PhpParser.parse(new FileInputStream(classPath)));
            } catch (ParseException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return parsedFiles;
    }

    @NotNull
    private static ProcessBuilder configureProcessBuilder(final Map<String, String> autoLoadPaths,
                                                          Project project, final Collection<String> classesSet,
                                                          final ProjectConfiguration configuration) {
        String symfonyLoaderString = autoLoadPaths.entrySet().stream()
                                                  .map(e -> e.getKey() + ":" + e.getValue())
                                                  .collect(Collectors.joining(":"));

        for (Map.Entry<String, String> replacement : project.getPathReplacements().entrySet()) {
            symfonyLoaderString = symfonyLoaderString.replace(replacement.getKey(), replacement.getValue());
        }
        List<String> cmdList = new LinkedList<>();
        cmdList.add("php");
        cmdList.add(Project.class.getResource("/php/loadClass.php").getFile());
        cmdList.add("-l");
        cmdList.add(getComposerAutoloadPath(configuration));
        cmdList.add("-p");
        cmdList.add(symfonyLoaderString);
        cmdList.addAll(classesSet);

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmdList);
        return builder;
    }

    @NotNull
    private static String getComposerAutoloadPath(ProjectConfiguration configuration) {
        File f = new File(configuration.getBehatExecutable());
        String parentPath = f.getParent();

        for (String autoLoaderPath : AUTO_LOADER_PATHS) {
            Path path = Paths.get(parentPath, autoLoaderPath);
            if (Files.exists(path)) {
                return path.toAbsolutePath().toString();
            }
        }

        throw new RuntimeException("autoload not found");
    }

    private static Map<String, PhpClass> getBehatDefinitions(Map<String, PhpFile> loadedPhpFiles) {
//        Function<Map.Entry<String, PhpFile>, PhpClass> findClass = entry -> {
//            PhpFile phpFile = entry.getValue();
//            for (PhpClass phpClass : phpFile.getClasses()) {
//                if (entry.getKey().equals('\\' + phpFile.getNamespace() + '\\' + phpClass.getName())) {
//                    return phpClass;
//                }
//            }
//            return null;
//        };

        Map<String, PhpClass> bla = loadedPhpFiles
            .values().stream()
            .flatMap(phpFile -> phpFile.getClasses().stream())
            .collect(Collectors.toMap(phpClass -> phpClass.getName(), phpClass -> phpClass));


        return loadedPhpFiles.values()
                             .stream()
                             .flatMap(phpFile -> {
                                 phpFile.getClasses().stream();
                                 Map<String, PhpClass> classMap = new HashMap<>();
                                 for (PhpClass phpClass : phpFile.getClasses()) {
                                     classMap.put(phpFile.getNamespace() + '\\' + phpClass.getName(), phpClass);
                                 }
                                 return classMap.entrySet().stream();
                             })
                             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
