#Week 4

##2021-03-15
Another week starting and new content to improve our search engine.
Before that, I was able to change the basic console logger for the log4j.
Then we had our meeting with Ángel where I presented my Steam profile infographic idea.
And I was also able to start some mocking that I'll need in future tests.

Then out meeting came and now we now what will be our new tasks, which will be focused on aggregations.
So I decided to make a few changes to my controller, so it maps all the query options to a bean.
This way I can use it as interface to build the requests on each engine and deprecate concrete methods like searchTitle.

##2021-03-16
Changing the controller was a bit tricky. At first, I was able ti implement the movie item easily, but it was not that easy.
Creating a get method on the movie bean broke everything and did took me a while to know why.
It is fixed now tho, so the search is now more complex.
