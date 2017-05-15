Behat-Extensions
================

## ArgumentExtension ##

```yaml
argument: []
```

* Provides services
  * `ArgumentExtension::CONSTRUCTOR_ARGUMENT_ORGANISER_ID` 
    * used in `ContextExtension::loadFactory`
  * `ArgumentExtension::PREG_MATCH_ARGUMENT_ORGANISER_ID` 
    * used in `DefinitionExtension::loadDefaultSearchEngines`
    * Matches step usages to their definitions to extract arguments
  * `ArgumentExtension::MIXED_ARGUMENT_ORGANISER_ID` 
    * never used

## AutoloaderExtension ##

```yaml
autoload: [ '' => '%paths.base%/features/bootstrap' ]
```

* Instantiates `Symfony\Component\ClassLoader\ClassLoader` with given paths, registers it as spl_autoloader

## CallExtension ##

```yaml
calls: 
    error_reporting: E_ALL | E_STRICT #actual Value needs to be calculated as integer
```

* Basically the event handler system
* Executes Environment, Definition, Transformation and Hook Calls

## CliExtension ##

```yaml
cli: []
```

* Handles CLI Parameters
* Calls services tagged with `CliExtension::CONTROLLER_TAG`

## ContextExtension ##

```yaml
contexts: []
```

## DefinitionExtension ##

```yaml
definitions: []
```

## EnvironmentExtension ##

```yaml
environments: []
```

## EventDispatcherExtension ##

```yaml
events: []
```

## ExceptionExtension ##

```yaml
exceptions:
    verbosity: 1
```

## FilesystemExtension ##

```yaml
filesystem: []
```

## GherkinExtension ##

```yaml
gherkin:
  cache: '/tmp/behat_gherkin_cache'
  filters:
    name: ~
```

## GherkinTranslationsExtension ##

```yaml
gherkin_translations: []
```

## HelperContainerExtension ##

```yaml
helper_container: []
```

## HookExtension ##

```yaml
hooks: []
```

## SnippetExtension ##

```yaml
snippets: []
```

## SpecificationExtension ##

```yaml
specifications: []
```

## SuiteExtension ##

```yaml
suites:
  name:
    enabled: true
    type: null
    settings:
      name: ~
    contexts: []
    filters: []
    paths: []
```

## TesterExtension ##

```yaml
testers:
  strict: false
  skip: false
  rerun_cache: /tmp/behat_rerun_cache
```

## TransformationExtension ##

```yaml
transformations: []
```

## TranslatorExtension ##

```yaml
translation:
  locale: en
  fallback_locale: en
```

## OrderingExtension ##

```yaml
ordering: []
```

## OutputExtension ##

```yaml
formatters:
  name:
    name:
```
