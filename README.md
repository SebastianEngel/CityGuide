# CityGuide
Exercise Android app called City guide that shows different places nearby and lets the user select
which type of place they want to see.

## Implementation details
The structure of the app consists of UI, domain and data layer.
The UI layer includes everything that is visible to the user (Activities, Fragments, Views, etc.).
Any (long running) operation (like requesting data that will be shown in the UI) is implemented in the
domain layer. In the service classes (e.g. the PlacesService) all operations are executed asynchronously
using RxJava's Observables which are given back to the calling UI element, which itself subscribes onto it.
Only service classes from within the domain layer are accessing the data layer. That is fetching data from
remote APIs and storing data into a local datastore (not implemented in the app).

The app uses dependency injection with Dagger to provide (singleton) dependencies.

## Possible optimizations

* Store data locally (caching)
* Check internet connection before making an API request
* Build in a Material toolbar
* Provide assets in correct sizes for all densities
* Show a place's details when the user selects a place.
* Add proguard rules and use it (for release builds)
* ...

## Further notes
When having multiple fragments in one Activity, I'd use Otto to post navigation events. The Activity would
subscribe to those and handle the fragment switching.