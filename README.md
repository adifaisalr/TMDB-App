# TMDB-App
Simple App to browse Movies / TV Shows using [TMDB](https://www.themoviedb.org/) API

This app was initially written using the following Architecture Components:
- ViewModel
- LiveData
- Fragment + View Binding
- Navigation Component

In progress to migrate to use :
- Jetpack Compose
- Navigation Component for Compose

## Architecture
Clean Architecture with layers :
- Domain : Use Case, Repository Interface, Model
- Data : Repository Implementation, DAO, API Service
- Presentation : Activity, Fragment, ViewModel

## Tech Stacks
- Jetpack Compose (in proccess migration)
- API Call : Retrofit + Gson
- Local DB : Room (newly added)
- Dependency Injection : Hilt
- Image Loader : Coil (for jetpack compose)
- Network logging : Flipper
- Navigation Component
