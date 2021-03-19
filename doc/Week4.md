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
Changing the controller was a bit tricky. 
At first, I was able to implement the movie item easily, but it was really not that easy.
Creating a get method on the movie bean broke everything and took me a while to know why.
It is fixed now tho, so the search is now more complex.

Looking at the documentation I decided to change the filter approach.
I changed the list of musts for one must for the query and filters to the type and genres.
I had to change the index to make those filters as keywords too, that gave me some errors, but I fixed them.

Finally, I dedicated the rest of the day to implement the aggregations.
I had some problems to extract them from the ES type but for now it does the trick and works.
I have some TODOs to improve this later.

##2021-03-17
I had to change the majority of my tests. It was kinda tedious, but no difficult.
I still have a lot more tests to do, they can wait tho. 
First I extracted a SearchResult builder and refactored the methods as the same time I added the aggregations.
It's not a good test as I was unable to mock a class, I'll leave it for later.

I jumped to the range filter task, it looked fascinating to make.
I knew I would need to make some changes to my filters approach to manage different types.
So I used a visitor, which is the pattern I like and understand the less, but I think it's the best approach.
I don't think the filters should know how to convert to each engine, the engine should create the transformations.

It took me a while to complete the refactoring, but the positive effects were discernible from the first second.
Once the range filter and its visitor were in line I only needed to a couple of things to the MovieRequest and everything worked.
Back to test now, I'll leave the range aggregation for tomorrow.

#2021-03-18
The first thing in the morning was changing the error handling, that was a lot easier than I expected and really my code reading.
I also tried to inject the ElasticSearch, but the test started to take A LOT, so I reverted that.

I jumped to the year aggregation, first fixing the response as I knew it would break.
Thankfully I realized how I should handle that, so I was able to send that aggregation nicely.

Next point, the same I did yesterday for range filter: extract a new aggregation class to handle different types.
That also involved creating a visitor to be in charge of the transformations.
This part was pretty easy. I finished fast and was able to move to more testing,

#2021-03-19
Today is testing day. All I might do is create test for everything lacking them.
First I fixed those tests that did break with the changes.
I also applied a fix suggested by Laura to improve the times when using the ElasticSearch injection.