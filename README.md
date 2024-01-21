**Movie App**



This application will be very useful for movie lovers.

With this App you will get the detailed information about every film you want. Also this App can help you to make a decision which film to watch based on you previous choice and you favorite films.

***

**-Homepage** : Upon opening the app, users are greeted with a visually appealing homepage, showcasing a variety of movies. 

**-Movie Details**: When a user selects a movie, they are taken to a detailed page showing the movie's poster, rating, title, release year.

**-Favorites Feature**: Users can add movies to a 'Favorites' list for quick access. This list is stored and can be easily accessed from the main menu.

**-Discovery and Recommendations**: The app offers a Discovery section where users receive personalized movie recommendations based on their viewing history and preferences.


***


**Technical Stack**:
Kotlin, Retrofit, Room, Coroutines

***

**API**: https://developer.themoviedb.org/


***


**Architecture**:

https://docs.google.com/document/d/1kWxIVHnqxNQ4tI7KHfJs_4rmv7wT2J5N8yGgmXgMu3w/edit?usp=sharing

App architecture follows _clean_ architecture principles.

There are **Domain**, **Data** and **Presentation** layers.

**-Domain** layer contains application entities such as MoviePreview and Movie itself. UseCases which perform app logic, for example GetDiscoveryUseCase retrieve movies list based on popular movies and user favorites. Also it contains MoviesRepository for data retrieval.

**-Data** layer contains MoviesRepository implementation, Api/Dao and DTO. It is used for data retrieval from different sources. If we decide to move to different api provider, then we will have a smooth transition without  rewriting main app code.

**-Presentation** layer contains UIState, which handles the state of the user interface.
The ViewModel interacts with the UseCases from the Domain layer to perform app logic. For example, a GetMovieDiscoveryUseCase might be called by a ViewModel to fetch a list of movies based on popularity and user favorites.

