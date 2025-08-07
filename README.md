# Gallery App

A modern Android gallery application built to showcase a robust and scalable architecture using 100% Kotlin and Jetpack Compose. This project demonstrates the principles of Clean Architecture, separating concerns into distinct layers to create a maintainable and testable codebase. The app fetches and displays curated photos from the Pexels API.

## Features

*   **Dynamic Start Screen**: The app intelligently checks if an API key is present. If not, it prompts the user to enter one; otherwise, it proceeds directly to the photo gallery.
*   **Paginated Photo List**: Displays an infinite-scrolling grid of photos fetched from the Pexels API, using Jetpack Paging 3.
*   **Pull-to-Refresh**: Users can easily refresh the photo gallery by swiping down, providing an intuitive way to fetch the latest images.
*   **Photo Details**: Users can tap on a photo to view it in higher resolution, along with details like the photographer's name and image dimensions.
*   **Secure API Key Storage**: The user-provided API key is securely encrypted and stored locally using AndroidX Security (Crypto) and Jetpack DataStore.
*   **Offline Caching**: Fetched photos are cached in a local Room database, providing a basic offline experience and faster subsequent loads.
*   **Error Handling**: The UI gracefully handles loading states, network errors, and empty states.

## Architecture

This project strictly follows the principles of **Clean Architecture**, ensuring a clear separation of concerns. The codebase is organized into multiple modules, where each module has a specific responsibility. This modular approach enforces the architectural rules, improves build times, and promotes scalability.

The core principle of this architecture is the **Dependency Rule**: dependencies can only point inwards. The UI layer depends on the Domain layer, and the Data layer depends on the Domain layer, but the Domain layer depends on nothing.

### Layer & Module Breakdown

#### Presentation Layer
This layer is responsible for everything related to the user interface. It is composed of UI elements (Jetpack Compose), state holders (ViewModels), and is divided into feature-specific modules.
*   `:app`: The main application module that integrates all other modules. It handles the top-level navigation graph, dependency injection setup (Hilt), and the `MainActivity`.
*   `:feature_photos` & `:feature_settings`: These are self-contained feature modules. Each handles its own UI, state management (ViewModel), and user interactions. They depend on the Domain layer to execute business logic.
*   `:core_ui`: A shared library module containing common Jetpack Compose components, themes, colors, and typography used across all feature modules to ensure a consistent look and feel.

#### Domain Layer
This is the core of the application and contains the business logic. It is a pure Kotlin module with no Android framework dependencies, making it completely independent and reusable.
*   `:core_domain`: Defines the "what" of the application—the business rules.
    *   **Models**: Plain Kotlin data classes representing the core entities of the app (e.g., `Photo`, `ApiKey`).
    *   **Repository Interfaces**: Defines the contracts (interfaces) for data operations, abstracting away the data sources.
    *   **Use Cases (Interactors)**: Encapsulate specific business rules and operations (e.g., `GetPhotosUseCase`, `SaveApiKeyUseCase`).

#### Data Layer
This layer is responsible for providing data to the application. It implements the repository interfaces defined in the Domain layer.
*   `:core_data`: Implements the "how" of data management. It contains:
    *   **Repository Implementations**: Concrete implementations of the Domain layer's repository interfaces.
    *   **Data Sources**: Manages connections to remote (Ktor API services) and local (Room database, Jetpack DataStore) data sources.
    *   **Mappers**: Transforms data between network models (DTOs), database models (Entities), and domain models.

## Key Technologies

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - A modern declarative UI toolkit for Android.
*   **Architecture**:
    *   [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) (Multi-module)
    *   Model-View-ViewModel (MVVM)
*   **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - For managing dependencies across the app.
*   **Asynchronous Programming**:
    *   [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For managing background threads.
    *   [Flow](https://kotlinlang.org/docs/flow.html) - For reactive streams of data.
*   **Networking**:
    *   [Ktor Client](https://ktor.io/docs/client-create-new-application.html) - A modern, lightweight, and asynchronous HTTP client.
    *   [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - For parsing JSON data.
*   **Database**: [Room](https://developer.android.com/training/data-storage/room) - For robust local data caching.
*   **Preferences**:
    *   [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - For storing key-value pairs, like the API key.
    *   [AndroidX Security (Crypto)](https://developer.android.com/topic/security/data) - For encrypting data in DataStore.
*   **Paging**: [Jetpack Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - For loading and displaying large datasets in the UI.
*   **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/) - An image loading library backed by Kotlin Coroutines.
*   **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) - For navigating between screens.
*   **Testing**:
    *   **Unit Tests**: JUnit4, Mockito, Turbine, Robolectric
    *   **UI Tests**: Espresso, Mockk

## Getting Started

To build and run the project, you will need a Pexels API key.

1.  **Get an API Key**:
    *   Go to the [Pexels API page](https://www.pexels.com/api/) and create a free account.
    *   Request an API key. It will be provided to you immediately.

2.  **Build and Run**:
    *   Open the project in Android Studio and run it on an emulator or a physical device. You can enter the API key in the settings screen within the app.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

*   Pexels API for providing the photo data.
*   Jetpack Compose for enabling modern UI development.
*   Hilt for dependency injection.
*   Ktor for network requests.
*   Coil for image loading.