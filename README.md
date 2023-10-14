# TMDB-App
Simple App to browse Movies / TV Shows using [TMDB](https://www.themoviedb.org/) API

Features :
- Search Movies / TV Shows (using lazy load pagination)
- Add Movies / TV Shows to favorite (save to local db)
- Movie / TV Show detail


https://github.com/adifaisalr/TMDB-App/assets/1327139/f3264c9e-6aad-4d3a-b792-67f62a097671



Prerequisite :
- Android Studio (Girrafe or later)
- JDK version 17

This app was initially written using the following Architecture Components:
- ViewModel
- LiveData
- Fragment + View Binding
- Navigation Component

In progress to migrate to use :
- Jetpack Compose
- Navigation Component for Compose

TODO :
- modularization
- unit test (broken due to compose migration)

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
