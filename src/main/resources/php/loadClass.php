<?php

/**
 * Usage: loadClass.php [-p PREFIX:PATH[;PREFIX:PATH...]] [-l PATH[;PATH...] CLASS_NAME [CLASS_NAME ...]
 *      -p  A list of string pairs used to configure symfony autoloader
 *      -l  A list of paths used to find composer autoloaders
 */

require_once("vendor/autoload.php");

function parseArgs($argv) {
    $isSymfony = false;
    $isComposer = false;
    $classNames = [];
    $autoLoaders = [];
    foreach ($argv as $arg) {
        if($arg === '-p') {
            $isSymfony = true;
            $isComposer = false;
            continue;
        }
        if ($arg === '-l') {
            $isSymfony = false;
            $isComposer = true;
            continue;
        }

        if ($isSymfony) {
            $autoLoaders[] = createSymfonyLoader($arg);
            $isSymfony = false;
        } else if ($isComposer) {
            $autoLoaders[] = createComposerLoader($arg);
            $isComposer = false;
        } else {
            $classNames[] = $arg;
        }
    }
    return [
        $autoLoaders,
        $classNames
    ];
}

function createSymfonyLoader($prefixes) {
    $loader = new Symfony\Component\ClassLoader\ClassLoader();
    $splitPrefixes = explode(';', $prefixes);
    foreach ($splitPrefixes as $splitPrefix) {
        list($prefix, $path) = explode(':', $splitPrefix);
        $loader->addPrefix($prefix, $path);
    }
    return $loader;
}

function createComposerLoader($filePath) {
    return include_once($filePath);
}

array_shift($argv);
list($autoLoaders, $classNames) = parseArgs($argv);

foreach ($classNames as $className) {
    $filePath = '';
    foreach ($autoLoaders as $autoLoader) {
        $possiblePath = $autoLoader->findFile($className);
        if (!empty($possiblePath)) {
            $filePath = $possiblePath;
            break;
        }
    }
    echo $filePath . PHP_EOL;
}
