Hilt
====================

https://developer.android.com/training/dependency-injection/hilt-android

Hilt is dependency injection.
Built on top of Dagger (from Google).
Android Studio supports Dagger, provides type-safety, other stuff.

Dagger is generic Java (Kotlin) library for DI, Hilt is Android-specific.

Container: Hilt thing that holds a class to be injected.

Application: class with @HiltAndroidApp that is the top-level holder of everything.

To obtain dependencies from a component, use the @Inject annotation to perform field injection:

    @Inject lateinit var analytics: AnalyticsAdapter



Hilt automatically creates and destroys instances of generated component classes following
the lifecycle of the corresponding Android classes.
https://developer.android.com/training/dependency-injection/hilt-android

For example:
* SingletonComponent destroyed when Application destroyed
* ActivityComponent destroyed when Activity#onDestroy()
* ServiceComponent destroyed when Service#onDestroy()




SCOPES

https://developer.android.com/training/dependency-injection/hilt-android

By default, all bindings in Hilt are unscoped. This means that each time your app requests
the binding, Hilt creates a new instance of the needed type.

However, Hilt also allows a binding to be scoped to a particular component. Hilt only creates
a scoped binding once per instance of the component that the binding is scoped to, and all
requests for that binding share the same instance.

